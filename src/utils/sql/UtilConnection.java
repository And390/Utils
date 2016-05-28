package utils.sql;

import utils.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
* User: And390
* Date: 28.04.14
* Time: 18:08
*/
public class UtilConnection extends ConnectionWrapper
{
    private StatementItem freeStatements;
    private StatementItem busyStatements;

    private HashMap<String, PreparedStatementItem> preparedFreeStatements = null;
    private HashMap<String, PreparedStatementItem> preparedBusyStatements = null;

    public UtilConnection(java.sql.Connection inner) throws SQLException
    {
        super(inner);
    }

    @Override
    public void close() throws SQLException  {
        try  {
            for (StatementItem item=freeStatements; item!=null; item=item.next)  item.close();
            for (StatementItem item=busyStatements, next=(item!=null ? item.next : null);
                 item!=null; item=next, next=(next!=null ? next.next : null))  item.close();
        }
        finally  {  super.close ();  }
    }


    private class StatementItem extends StatementWrapper
    {
        private StatementItem next;
        private StatementItem prev;

        public StatementItem(java.sql.Statement inner)  {  super(inner);  }

        @Override
        public ResultSet executeQuery(String query) throws SQLException
        {
            //    create parent result set
            ResultSet result = super.executeQuery(query);
            //    unlink from free statements
            if (busyStatements==this)  throw new SQLException ("Can not execute query while another is not closed for this statement");
            if (freeStatements==this)  freeStatements = next;
            if (prev!=null)  prev.next = next;
            if (next!=null)  next.prev = prev;
            //    link to busy statements
            next = busyStatements;
            prev = null;
            if (next!=null)  next.prev = this;
            busyStatements = this;
            //    return result set with close handler
            return new ResultSetWrapper(result)
            {
                @Override
                public void close() throws SQLException  {
                    super.close();
                    //    unlink from busy statements
                    if (freeStatements==StatementItem.this)  throw new SQLException ("ResultSet is not open");
                    if (busyStatements==StatementItem.this)  busyStatements = next;
                    if (prev!=null)  prev.next = next;
                    if (next!=null)  next.prev = prev;
                    //    link to free statements
                    next = freeStatements;
                    prev = null;
                    if (next!=null)  next.prev = StatementItem.this;
                    busyStatements = StatementItem.this;
                }
            };
        }
    }

    public Statement statement() throws SQLException
    {
        if (freeStatements==null)  {
            StatementItem statement = new StatementItem(super.createStatement());
            //    link to free statements
            statement.next = null;
            statement.prev = null;
            freeStatements = statement;
        }
        return freeStatements;
    }

    /*
        надо эти методы по-другому назвать или вообще удалить, чтобы они не путались с аналогичными методами
        с PreparedStatement, которые основные для использования

    public ArrayList<Object[]> select(String query) throws SQLException  {
        return SqlUtil.toList(executeQuery(query));
    }

    public ResultSet executeQuery(String query) throws SQLException  {
        return statement().executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException  {
        return statement().executeUpdate(query);
    }

    public boolean execute(String query) throws SQLException  {
        return statement().execute(query);
    }

    */


    private class PreparedStatementItem extends PreparedStatementWrapper
    {
        private String key;
        private PreparedStatementItem next;
        private PreparedStatementItem prev;

        public PreparedStatementItem(java.sql.PreparedStatement inner, String query)  {  super(inner);  key=query;  }

        @Override
        public ResultSet executeQuery() throws SQLException
        {
            //    create parent result set
            ResultSet result = super.executeQuery();
            //    unlink from free statements
            if (preparedBusyStatements.get(key)==this)  throw new SQLException ("Can not execute query while another is not closed for this statement");
            if (preparedFreeStatements.get(key)==this)  preparedFreeStatements.put(key, next);
            if (prev!=null)  prev.next = next;
            if (next!=null)  next.prev = prev;
            //    link to busy statements
            next = preparedBusyStatements.get(key);
            prev = null;
            if (next!=null)  next.prev = this;
            preparedBusyStatements.put(key, this);
            //    return result set with close handler
            return new ResultSetWrapper (result)
            {
                @Override
                public void close() throws SQLException  {
                    super.close();
                    //    unlink from busy statements
                    if (preparedFreeStatements.get(key)==PreparedStatementItem.this)  throw new SQLException ("ResultSet is not open");
                    if (preparedBusyStatements.get(key)==PreparedStatementItem.this)  preparedBusyStatements.put(key, next);
                    if (prev!=null)  prev.next = next;
                    if (next!=null)  next.prev = prev;
                    //    link to free statements
                    next = preparedFreeStatements.get(key);
                    prev = null;
                    if (next!=null)  next.prev = PreparedStatementItem.this;
                    preparedBusyStatements.put(key, PreparedStatementItem.this);
                }
            };
        }
    }

    public PreparedStatement statement(String query) throws SQLException
    {
        if (preparedFreeStatements==null)  {
            preparedFreeStatements = new HashMap<String, PreparedStatementItem> ();
            preparedBusyStatements = new HashMap<String, PreparedStatementItem> ();
        }
        if (preparedFreeStatements.get(query)==null)  {
            PreparedStatementItem statement = new PreparedStatementItem (super.prepareStatement(query), query);
            //    link to free statements
            statement.next = null;
            statement.prev = null;
            preparedFreeStatements.put(query, statement);
        }
        return preparedFreeStatements.get(query);
    }

    public ArrayList<Object[]> select(String query, Object... parameters) throws SQLException  {
        return SqlUtil.toList(executeQuery(query, parameters));
    }

    public ResultSet executeQuery(String query, Object... parameters) throws SQLException  {
        return SqlUtil.setParameters(statement(query), parameters).executeQuery();
    }

    public int executeUpdate(String query, Object... parameters) throws SQLException  {
        return SqlUtil.setParameters(statement(query), parameters).executeUpdate();
    }

    public boolean execute(String query, Object... parameters) throws SQLException  {
        return SqlUtil.setParameters(statement(query), parameters).execute();
    }

    public int executeIntQuery(String query, Object... parameters) throws SQLException  {
        ResultSet resultSet = executeQuery(query, parameters);
        try  {
            if (!resultSet.next())  throw new SQLException ("Query execution return empty result: "+Util.escape(query));
            int result = resultSet.getInt(1);
            if (resultSet.next())  throw new SQLException ("Query execution return more than one row: "+Util.escape(query));
            return result;
        }
        finally  {  resultSet.close();  }
    }

    public String executeStringQuery(String query, String emptyError, String multipleError, Object... parameters) throws Exception  {
        ResultSet resultSet = executeQuery(query, parameters);
        try  {
            if (!resultSet.next())  if (emptyError==null)  return null;  else  throw new Exception (emptyError);
            String result = resultSet.getString(1);
            if (resultSet.next())  throw new SQLException (multipleError!=null ? multipleError :
                    "Query execution return more than one row: "+Util.escape(query));
            return result;
        }
        finally  {  resultSet.close();  }
    }

    public void executeUpdateOne(String query, Object... parameters) throws SQLException  {
        int result = executeUpdate(query, parameters);
        if (result!=1)  throw new SQLException ("Wrong update result: "+result);
    }

    public boolean executeUpdateOneOrZero(String query, Object... parameters) throws SQLException  {
        int result = executeUpdate(query, parameters);
        if (result!=0 && result!=1)  throw new SQLException ("Wrong update result: "+result);
        return result == 1;
    }


    /*
        //other realisation

        private ArrayList<StatementItem> statements = new ArrayList<StatementItem> ();
        private int freeStatements = 0;

        public Connection2(java.sql.Connection inner) throws SQLException  {
            super(inner);
        }

        @Override
        public void close() throws SQLException  {
            try  {  for (int i=0; i<statements.size(); i++)  statements.get(i).close();  }
            finally  {  super.close ();  }
        }

        @Override
        public Statement createStatement() throws SQLException
        {
            return new Statement (super.createStatement());
        }

        private class StatementItem extends Statement
        {
            private int index;

            public StatementItem(java.sql.Statement inner)  {  super(inner);  }

            @Override
            public ResultSet executeQuery(String query) throws SQLException
            {
                //    create parent result set
                ResultSet result = super.executeQuery(query);
                //    swap to end of free statements and mark it busy
                if (index>=freeStatements)  throw new SQLException ("Can not execute query while another is not closed for this statement");
                freeStatements--;
                statements.set(index, statements.get(freeStatements));
                statements.get(freeStatements).index = index;
                statements.set(freeStatements, this);
                index = freeStatements;
                //    return result set with close handler
                return new ResultSetWrapper (result)
                {
                    @Override
                    public void close() throws SQLException  {
                        super.close();
                        //    swap to start of busy statements and mark it free
                        if (index<freeStatements)  throw new SQLException ("ResultSet is not open");
                        statements.set(index, statements.get(freeStatements));
                        statements.get(freeStatements).index = index;
                        statements.set(freeStatements, StatementItem.this);
                        index = freeStatements;
                        freeStatements++;
                    }
                };
            }
        }

        public Statement statement() throws SQLException
        {
            if (freeStatements==0)  {
                StatementItem statement = new StatementItem (createStatement());
                //    add to end and swap with first
                statement.index = 0;
                statements.add(statements.get(0));
                statements.get(0).index = statements.size() - 1;
                statements.set(0, statement);
                freeStatements++;
            }
            return statements.get(0);
        }

        public ArrayList<Object[]> select(String query) throws SQLException  {
            return statement().select(query);
        }

        public boolean execute(String query) throws SQLException  {
            return statement().execute(query);
        }

        public ResultSet executeQuery(String query) throws SQLException  {
            return statement().executeQuery(query);
        }

        public int executeUpdate(String query) throws SQLException  {
            return statement().executeUpdate(query);
        }
     */
}
