package utils.sql;

/**
 * User: And390
 * Date: 28.04.14
 * Time: 17:49
 */
public class PreparedStatementWrapper implements java.sql.PreparedStatement
{
    private final java.sql.PreparedStatement inner;
    public PreparedStatementWrapper (java.sql.PreparedStatement inner)  {  this.inner=inner;  }

    public void setBoolean (int _0, boolean _1) throws java.sql.SQLException  {  inner.setBoolean(_0,_1);  }
    public void setByte (int _0, byte _1) throws java.sql.SQLException  {  inner.setByte(_0,_1);  }
    public void setDouble (int _0, double _1) throws java.sql.SQLException  {  inner.setDouble(_0,_1);  }
    public void setFloat (int _0, float _1) throws java.sql.SQLException  {  inner.setFloat(_0,_1);  }
    public void setInt (int _0, int _1) throws java.sql.SQLException  {  inner.setInt(_0,_1);  }
    public void setLong (int _0, long _1) throws java.sql.SQLException  {  inner.setLong(_0,_1);  }
    public void setShort (int _0, short _1) throws java.sql.SQLException  {  inner.setShort(_0,_1);  }
    public void setTimestamp (int _0, java.sql.Timestamp _1) throws java.sql.SQLException  {  inner.setTimestamp(_0,_1);  }
    public void setTimestamp (int _0, java.sql.Timestamp _1, java.util.Calendar _2) throws java.sql.SQLException  {  inner.setTimestamp(_0,_1,_2);  }
    public void setURL (int _0, java.net.URL _1) throws java.sql.SQLException  {  inner.setURL(_0,_1);  }
    public void setTime (int _0, java.sql.Time _1, java.util.Calendar _2) throws java.sql.SQLException  {  inner.setTime(_0,_1,_2);  }
    public void setTime (int _0, java.sql.Time _1) throws java.sql.SQLException  {  inner.setTime(_0,_1);  }
    public boolean execute () throws java.sql.SQLException  {  return inner.execute();  }
    public void addBatch () throws java.sql.SQLException  {  inner.addBatch();  }
    public void clearParameters () throws java.sql.SQLException  {  inner.clearParameters();  }
    public java.sql.ResultSet executeQuery () throws java.sql.SQLException  {  return inner.executeQuery();  }
    public int executeUpdate () throws java.sql.SQLException  {  return inner.executeUpdate();  }
    public java.sql.ResultSetMetaData getMetaData () throws java.sql.SQLException  {  return inner.getMetaData();  }
    public java.sql.ParameterMetaData getParameterMetaData () throws java.sql.SQLException  {  return inner.getParameterMetaData();  }
    public void setArray (int _0, java.sql.Array _1) throws java.sql.SQLException  {  inner.setArray(_0,_1);  }
    public void setAsciiStream (int _0, java.io.InputStream _1, long _2) throws java.sql.SQLException  {  inner.setAsciiStream(_0,_1,_2);  }
    public void setAsciiStream (int _0, java.io.InputStream _1, int _2) throws java.sql.SQLException  {  inner.setAsciiStream(_0,_1,_2);  }
    public void setAsciiStream (int _0, java.io.InputStream _1) throws java.sql.SQLException  {  inner.setAsciiStream(_0,_1);  }
    public void setBigDecimal (int _0, java.math.BigDecimal _1) throws java.sql.SQLException  {  inner.setBigDecimal(_0,_1);  }
    public void setBinaryStream (int _0, java.io.InputStream _1) throws java.sql.SQLException  {  inner.setBinaryStream(_0,_1);  }
    public void setBinaryStream (int _0, java.io.InputStream _1, long _2) throws java.sql.SQLException  {  inner.setBinaryStream(_0,_1,_2);  }
    public void setBinaryStream (int _0, java.io.InputStream _1, int _2) throws java.sql.SQLException  {  inner.setBinaryStream(_0,_1,_2);  }
    public void setBlob (int _0, java.io.InputStream _1, long _2) throws java.sql.SQLException  {  inner.setBlob(_0,_1,_2);  }
    public void setBlob (int _0, java.io.InputStream _1) throws java.sql.SQLException  {  inner.setBlob(_0,_1);  }
    public void setBlob (int _0, java.sql.Blob _1) throws java.sql.SQLException  {  inner.setBlob(_0,_1);  }
    public void setBytes (int _0, byte[] _1) throws java.sql.SQLException  {  inner.setBytes(_0,_1);  }
    public void setCharacterStream (int _0, java.io.Reader _1, long _2) throws java.sql.SQLException  {  inner.setCharacterStream(_0,_1,_2);  }
    public void setCharacterStream (int _0, java.io.Reader _1) throws java.sql.SQLException  {  inner.setCharacterStream(_0,_1);  }
    public void setCharacterStream (int _0, java.io.Reader _1, int _2) throws java.sql.SQLException  {  inner.setCharacterStream(_0,_1,_2);  }
    public void setClob (int _0, java.io.Reader _1) throws java.sql.SQLException  {  inner.setClob(_0,_1);  }
    public void setClob (int _0, java.sql.Clob _1) throws java.sql.SQLException  {  inner.setClob(_0,_1);  }
    public void setClob (int _0, java.io.Reader _1, long _2) throws java.sql.SQLException  {  inner.setClob(_0,_1,_2);  }
    public void setDate (int _0, java.sql.Date _1) throws java.sql.SQLException  {  inner.setDate(_0,_1);  }
    public void setDate (int _0, java.sql.Date _1, java.util.Calendar _2) throws java.sql.SQLException  {  inner.setDate(_0,_1,_2);  }
    public void setNCharacterStream (int _0, java.io.Reader _1) throws java.sql.SQLException  {  inner.setNCharacterStream(_0,_1);  }
    public void setNCharacterStream (int _0, java.io.Reader _1, long _2) throws java.sql.SQLException  {  inner.setNCharacterStream(_0,_1,_2);  }
    public void setNClob (int _0, java.io.Reader _1, long _2) throws java.sql.SQLException  {  inner.setNClob(_0,_1,_2);  }
    public void setNClob (int _0, java.sql.NClob _1) throws java.sql.SQLException  {  inner.setNClob(_0,_1);  }
    public void setNClob (int _0, java.io.Reader _1) throws java.sql.SQLException  {  inner.setNClob(_0,_1);  }
    public void setNString (int _0, java.lang.String _1) throws java.sql.SQLException  {  inner.setNString(_0,_1);  }
    public void setNull (int _0, int _1) throws java.sql.SQLException  {  inner.setNull(_0,_1);  }
    public void setNull (int _0, int _1, java.lang.String _2) throws java.sql.SQLException  {  inner.setNull(_0,_1,_2);  }
    public void setObject (int _0, java.lang.Object _1) throws java.sql.SQLException  {  inner.setObject(_0,_1);  }
    public void setObject (int _0, java.lang.Object _1, int _2) throws java.sql.SQLException  {  inner.setObject(_0,_1,_2);  }
    public void setObject (int _0, java.lang.Object _1, int _2, int _3) throws java.sql.SQLException  {  inner.setObject(_0,_1,_2,_3);  }
    public void setRef (int _0, java.sql.Ref _1) throws java.sql.SQLException  {  inner.setRef(_0,_1);  }
    public void setRowId (int _0, java.sql.RowId _1) throws java.sql.SQLException  {  inner.setRowId(_0,_1);  }
    public void setSQLXML (int _0, java.sql.SQLXML _1) throws java.sql.SQLException  {  inner.setSQLXML(_0,_1);  }
    public void setString (int _0, java.lang.String _1) throws java.sql.SQLException  {  inner.setString(_0,_1);  }
    @Deprecated
    public void setUnicodeStream (int _0, java.io.InputStream _1, int _2) throws java.sql.SQLException  {  inner.setUnicodeStream(_0,_1,_2);  }
    public void close () throws java.sql.SQLException  {  inner.close();  }
    public boolean isClosed () throws java.sql.SQLException  {  return inner.isClosed();  }
    public boolean execute (java.lang.String _0, int[] _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public boolean execute (java.lang.String _0) throws java.sql.SQLException  {  return inner.execute(_0);  }
    public boolean execute (java.lang.String _0, java.lang.String[] _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public boolean execute (java.lang.String _0, int _1) throws java.sql.SQLException  {  return inner.execute(_0,_1);  }
    public void addBatch (java.lang.String _0) throws java.sql.SQLException  {  inner.addBatch(_0);  }
    public java.sql.ResultSet executeQuery (java.lang.String _0) throws java.sql.SQLException  {  return inner.executeQuery(_0);  }
    public int executeUpdate (java.lang.String _0, int _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public int executeUpdate (java.lang.String _0) throws java.sql.SQLException  {  return inner.executeUpdate(_0);  }
    public int executeUpdate (java.lang.String _0, java.lang.String[] _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public int executeUpdate (java.lang.String _0, int[] _1) throws java.sql.SQLException  {  return inner.executeUpdate(_0,_1);  }
    public void cancel () throws java.sql.SQLException  {  inner.cancel();  }
    public void clearBatch () throws java.sql.SQLException  {  inner.clearBatch();  }
    public void clearWarnings () throws java.sql.SQLException  {  inner.clearWarnings();  }
    public int[] executeBatch () throws java.sql.SQLException  {  return inner.executeBatch();  }
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
}
