package utils.sql;

import java.sql.SQLException;
import java.util.concurrent.Executor;

/**
 * User: And390
 * Date: 05.04.13
 * Time: 17:48
 */
public class ConnectionWrapper implements java.sql.Connection
{
    private final java.sql.Connection inner;
    public ConnectionWrapper (java.sql.Connection inner)  {  this.inner=inner;  }

    public void setReadOnly (boolean _0) throws java.sql.SQLException  {  inner.setReadOnly(_0);  }
    public void close () throws java.sql.SQLException  {  inner.close();  }
    public boolean isReadOnly () throws java.sql.SQLException  {  return inner.isReadOnly();  }
    public boolean isClosed () throws java.sql.SQLException  {  return inner.isClosed();  }
    public boolean isValid (int _0) throws java.sql.SQLException  {  return inner.isValid(_0);  }
    public void clearWarnings () throws java.sql.SQLException  {  inner.clearWarnings();  }
    public void commit () throws java.sql.SQLException  {  inner.commit();  }
    public java.sql.Array createArrayOf (java.lang.String _0, java.lang.Object[] _1) throws java.sql.SQLException  {  return inner.createArrayOf(_0,_1);  }
    public java.sql.Blob createBlob () throws java.sql.SQLException  {  return inner.createBlob();  }
    public java.sql.Clob createClob () throws java.sql.SQLException  {  return inner.createClob();  }
    public java.sql.NClob createNClob () throws java.sql.SQLException  {  return inner.createNClob();  }
    public java.sql.SQLXML createSQLXML () throws java.sql.SQLException  {  return inner.createSQLXML();  }
    public java.sql.Statement createStatement () throws java.sql.SQLException  {  return inner.createStatement();  }
    public java.sql.Statement createStatement (int _0, int _1) throws java.sql.SQLException  {  return inner.createStatement(_0,_1);  }
    public java.sql.Statement createStatement (int _0, int _1, int _2) throws java.sql.SQLException  {  return inner.createStatement(_0,_1,_2);  }
    public java.sql.Struct createStruct (java.lang.String _0, java.lang.Object[] _1) throws java.sql.SQLException  {  return inner.createStruct(_0,_1);  }
    public boolean getAutoCommit () throws java.sql.SQLException  {  return inner.getAutoCommit();  }
    public java.lang.String getCatalog () throws java.sql.SQLException  {  return inner.getCatalog();  }
    public java.util.Properties getClientInfo () throws java.sql.SQLException  {  return inner.getClientInfo();  }
    public java.lang.String getClientInfo (java.lang.String _0) throws java.sql.SQLException  {  return inner.getClientInfo(_0);  }
    public int getHoldability () throws java.sql.SQLException  {  return inner.getHoldability();  }
    public java.sql.DatabaseMetaData getMetaData () throws java.sql.SQLException  {  return inner.getMetaData();  }
    public int getTransactionIsolation () throws java.sql.SQLException  {  return inner.getTransactionIsolation();  }
    public java.util.Map<java.lang.String, java.lang.Class<?>> getTypeMap () throws java.sql.SQLException  {  return inner.getTypeMap();  }
    public java.sql.SQLWarning getWarnings () throws java.sql.SQLException  {  return inner.getWarnings();  }
    public java.lang.String nativeSQL (java.lang.String _0) throws java.sql.SQLException  {  return inner.nativeSQL(_0);  }
    public java.sql.CallableStatement prepareCall (java.lang.String _0) throws java.sql.SQLException  {  return inner.prepareCall(_0);  }
    public java.sql.CallableStatement prepareCall (java.lang.String _0, int _1, int _2) throws java.sql.SQLException  {  return inner.prepareCall(_0,_1,_2);  }
    public java.sql.CallableStatement prepareCall (java.lang.String _0, int _1, int _2, int _3) throws java.sql.SQLException  {  return inner.prepareCall(_0,_1,_2,_3);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0, int _1, int _2) throws java.sql.SQLException  {  return inner.prepareStatement(_0,_1,_2);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0, int _1, int _2, int _3) throws java.sql.SQLException  {  return inner.prepareStatement(_0,_1,_2,_3);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0) throws java.sql.SQLException  {  return inner.prepareStatement(_0);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0, java.lang.String[] _1) throws java.sql.SQLException  {  return inner.prepareStatement(_0,_1);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0, int _1) throws java.sql.SQLException  {  return inner.prepareStatement(_0,_1);  }
    public java.sql.PreparedStatement prepareStatement (java.lang.String _0, int[] _1) throws java.sql.SQLException  {  return inner.prepareStatement(_0,_1);  }
    public void releaseSavepoint (java.sql.Savepoint _0) throws java.sql.SQLException  {  inner.releaseSavepoint(_0);  }
    public void rollback (java.sql.Savepoint _0) throws java.sql.SQLException  {  inner.rollback(_0);  }
    public void rollback () throws java.sql.SQLException  {  inner.rollback();  }
    public void setAutoCommit (boolean _0) throws java.sql.SQLException  {  inner.setAutoCommit(_0);  }
    public void setCatalog (java.lang.String _0) throws java.sql.SQLException  {  inner.setCatalog(_0);  }
    public void setClientInfo (java.util.Properties _0) throws java.sql.SQLClientInfoException  {  inner.setClientInfo(_0);  }
    public void setClientInfo (java.lang.String _0, java.lang.String _1) throws java.sql.SQLClientInfoException  {  inner.setClientInfo(_0,_1);  }
    public void setHoldability (int _0) throws java.sql.SQLException  {  inner.setHoldability(_0);  }
    public java.sql.Savepoint setSavepoint (java.lang.String _0) throws java.sql.SQLException  {  return inner.setSavepoint(_0);  }
    public java.sql.Savepoint setSavepoint () throws java.sql.SQLException  {  return inner.setSavepoint();  }
    public void setTransactionIsolation (int _0) throws java.sql.SQLException  {  inner.setTransactionIsolation(_0);  }
    public void setTypeMap (java.util.Map<java.lang.String, java.lang.Class<?>> _0) throws java.sql.SQLException  {  inner.setTypeMap(_0);  }
    public boolean isWrapperFor (java.lang.Class<?> _0) throws java.sql.SQLException  {  return inner.isWrapperFor(_0);  }
    public <T> T unwrap (java.lang.Class<T> _0) throws java.sql.SQLException  {  return inner.unwrap(_0);  }
    //    java 7
//    public void setSchema(String schema) throws SQLException  {  inner.setSchema(schema);  }
//    public String getSchema() throws SQLException  {  return inner.getSchema();  }
//    public void abort(Executor executor) throws SQLException  {  inner.abort(executor);  }
//    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException  {  inner.setNetworkTimeout(executor, milliseconds);  }
//    public int getNetworkTimeout() throws SQLException  {  return inner.getNetworkTimeout();  }
}