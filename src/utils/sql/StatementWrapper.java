package utils.sql;

import java.sql.SQLException;

/**
 * User: And390
 * Date: 05.04.13
 * Time: 17:48
 */
public class StatementWrapper implements java.sql.Statement
{
    private final java.sql.Statement inner;
    public StatementWrapper (java.sql.Statement inner)  {  this.inner=inner;  }

    public void close () throws java.sql.SQLException  {  inner.close();  }
    public boolean isClosed () throws java.sql.SQLException  {  return inner.isClosed();  }
    public boolean execute (java.lang.String _0, int[] _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public boolean execute (java.lang.String _0, int _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public boolean execute (java.lang.String _0, java.lang.String[] _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public boolean execute (java.lang.String _0) throws java.sql.SQLException  {  return inner.execute(_0);  }
    public void addBatch (java.lang.String _0) throws java.sql.SQLException  {  inner.addBatch(_0);  }
    public void cancel () throws java.sql.SQLException  {  inner.cancel();  }
    public void clearBatch () throws java.sql.SQLException  {  inner.clearBatch();  }
    public void clearWarnings () throws java.sql.SQLException  {  inner.clearWarnings();  }
    public int[] executeBatch () throws java.sql.SQLException  {  return inner.executeBatch();  }
    public java.sql.ResultSet executeQuery (java.lang.String _0) throws java.sql.SQLException  {  return inner.executeQuery(_0);  }
    public int executeUpdate (java.lang.String _0) throws java.sql.SQLException  {  return inner.executeUpdate(_0);  }
    public int executeUpdate (java.lang.String _0, java.lang.String[] _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public int executeUpdate (java.lang.String _0, int _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public int executeUpdate (java.lang.String _0, int[] _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public java.sql.Connection getConnection () throws java.sql.SQLException  {  return inner.getConnection();  }
    public int getFetchDirection () throws java.sql.SQLException  {  return inner.getFetchDirection();  }
    public int getFetchSize () throws java.sql.SQLException  {  return inner.getFetchSize();  }
    public java.sql.ResultSet getGeneratedKeys () throws java.sql.SQLException  {  return inner.getGeneratedKeys();  }
    public int getMaxFieldSize () throws java.sql.SQLException  {  return inner.getMaxFieldSize();  }
    public int getMaxRows () throws java.sql.SQLException  {  return inner.getMaxRows();  }
    public boolean getMoreResults () throws java.sql.SQLException  {  return inner.getMoreResults();  }
    public boolean getMoreResults (int _0) throws java.sql.SQLException  {  return inner.getMoreResults(_0);  }
    public int getQueryTimeout () throws java.sql.SQLException  {  return inner.getQueryTimeout();  }
    public java.sql.ResultSet getResultSet () throws java.sql.SQLException  {  return inner.getResultSet();  }
    public int getResultSetConcurrency () throws java.sql.SQLException  {  return inner.getResultSetConcurrency();  }
    public int getResultSetHoldability () throws java.sql.SQLException  {  return inner.getResultSetHoldability();  }
    public int getResultSetType () throws java.sql.SQLException  {  return inner.getResultSetType();  }
    public int getUpdateCount () throws java.sql.SQLException  {  return inner.getUpdateCount();  }
    public java.sql.SQLWarning getWarnings () throws java.sql.SQLException  {  return inner.getWarnings();  }
    public boolean isPoolable () throws java.sql.SQLException  {  return inner.isPoolable();  }
    public void setCursorName (java.lang.String _0) throws java.sql.SQLException  {  inner.setCursorName(_0);  }
    public void setEscapeProcessing (boolean _0) throws java.sql.SQLException  {  inner.setEscapeProcessing(_0);  }
    public void setFetchDirection (int _0) throws java.sql.SQLException  {  inner.setFetchDirection(_0);  }
    public void setFetchSize (int _0) throws java.sql.SQLException  {  inner.setFetchSize(_0);  }
    public void setMaxFieldSize (int _0) throws java.sql.SQLException  {  inner.setMaxFieldSize(_0);  }
    public void setMaxRows (int _0) throws java.sql.SQLException  {  inner.setMaxRows(_0);  }
    public void setPoolable (boolean _0) throws java.sql.SQLException  {  inner.setPoolable(_0);  }
    public void setQueryTimeout (int _0) throws java.sql.SQLException  {  inner.setQueryTimeout(_0);  }
    public boolean isWrapperFor (java.lang.Class<?> _0) throws java.sql.SQLException  {  return inner.isWrapperFor(_0);  }
    public <T> T unwrap (java.lang.Class<T> _0) throws java.sql.SQLException  {  return inner.unwrap(_0);  }
//    public void closeOnCompletion() throws SQLException  {  inner.closeOnCompletion();  }  //java 7
//    public boolean isCloseOnCompletion() throws SQLException  {  return inner.isCloseOnCompletion();  }  //java 7
}