package me.sdk.jdbc.dao;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.logicalcobwebs.proxool.ConnectionPoolDefinitionIF;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

public class DAO
{
  public static final String NULL_OBJECT = "NULL";
  private DBOperator dbOperator = null;
  private PoolRegister poolRegister = null;
  private Checker checker = null;
  private SQLTables sqlTable = null;
  private String defaultPoolName=null;
  
  public DAO()
  {
    this.dbOperator = new DBOperator();
    this.poolRegister = new PoolRegister();
    this.checker = new Checker(this);
    this.checker.startup();
    this.sqlTable = new SQLTables();
  }
  
  public void removeConnectionPool(String alia)
    throws ProxoolException
  {
    this.poolRegister.removePool(alia);
  }
  
  public void clear()
  {
    if (this.poolRegister != null) {
      this.poolRegister.clear();
    }
    if (this.checker != null) {
      this.checker.shutdown();
    }
    if (this.dbOperator != null) {
      this.dbOperator = null;
    }
    if (this.poolRegister != null) {
      this.poolRegister = null;
    }
    if (this.checker != null) {
      this.checker = null;
    }
  }
  
  public synchronized ConnectionPool registeConnectionPool(String alias, String driverClass, String databaseURL, String username, String password, int size)
    throws Exception
  {
    if (this.poolRegister == null)
    {
      this.poolRegister = new PoolRegister();
      this.checker = new Checker(this);
      this.checker.startup();
      this.dbOperator = new DBOperator();
    }
    if (this.poolRegister.getPool(alias) != null) {
      throw new Exception("数据库已注册，不能重复注册！");
    }
    Properties ps = new Properties();
    
    ps.put("user", username);
    ps.put("password", password);
    ps.put("proxool.maximum-connection-count", 5 * size);
    ps.put("proxool.minimun-connection-count", size);
    ps.put("proxool.house-keeping-sleep-time", "300000");
    ps.put("proxool.prototype-count", size);
    ps.put("proxool.test-before-use", "true");
    ps.put("proxool.simultaneous-build-throttle", 5 * size);
    if (databaseURL.indexOf("sqlserver") >= 0) {
      ps.put("proxool.house-keeping-test-sql", "select getDate()");
    } else if (databaseURL.indexOf("mysql") >= 0) {
      ps.put("proxool.house-keeping-test-sql", "select now()");
    } else if (databaseURL.indexOf("oracle") >= 0) {
      ps.put("proxool.house-keeping-test-sql", "select sysdate from dual");
    } else if (databaseURL.indexOf("hsql") >= 0) {
      ps.put("proxool.house-keeping-test-sql", "select CURRENT_DATE");
    }
    ps.put("proxool.verbose", "true");
    
    String url = "proxool." + alias + ":" + driverClass + ":" + databaseURL;
    
    ProxoolFacade.registerConnectionPool(url, ps);
    
    return this.poolRegister.registePool(alias, databaseURL, username, password, driverClass, size);
  }
  
  public synchronized ConnectionPool registeConnectionPool(File proxoolConfigFile)
    throws Exception
  {
    if (!proxoolConfigFile.exists()) {
      throw new Exception("没有找到配置文件:" + proxoolConfigFile.toString());
    }
    ConnectionPool pool = null;
    
    FileInputStream is = null;
    try
    {
      if (this.poolRegister == null)
      {
        this.poolRegister = new PoolRegister();
        this.checker = new Checker(this);
        this.checker.startup();
        this.dbOperator = new DBOperator();
      }
      is = new FileInputStream(proxoolConfigFile);
      
      JAXPConfigurator.configure(new InputStreamReader(is), false);
      
      String[] aliases = ProxoolFacade.getAliases();
      for (int i = 0; (aliases != null) && (i < aliases.length); i++)
      {
        pool = this.poolRegister.getPool(aliases[i]);
        if (pool == null)
        {
          ConnectionPoolDefinitionIF poolInfo = ProxoolFacade.getConnectionPoolDefinition(aliases[i]);
          
          String sqlConfigName = aliases[i] + ".sql.xml";
          File sqlConfig = new File(proxoolConfigFile.getParentFile(), sqlConfigName);
          if (sqlConfig.exists())
          {
            this.sqlTable.loadSQLConfig(aliases[i], sqlConfig);
            System.out.println("DAOSQL [" + sqlConfigName + "] loaded");
          }
          String databaseURL = poolInfo.getUrl();
          String user = poolInfo.getUser();
          String password = poolInfo.getPassword();
          String driverClass = poolInfo.getDriver();
          int size = poolInfo.getMaximumConnectionCount();
          
          pool = this.poolRegister.registePool(aliases[i], databaseURL, user, password, driverClass, size);
          System.out.println("DAO [" + aliases[i] + "] build @ " + proxoolConfigFile.getAbsolutePath());
          break;
        }
      }
      return pool;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (is != null) {
        is.close();
      }
    }
  }
  /** 支持 多个sql 配置*/
  public synchronized ConnectionPool registeConnectionPoolExtend(File proxoolConfigFile)
		    throws Exception
		  {
		    if (!proxoolConfigFile.exists()) {
		      throw new Exception("没有找到配置文件:" + proxoolConfigFile.toString());
		    }
		    ConnectionPool pool = null;
		    
		    FileInputStream is = null;
		    try
		    {
		      if (this.poolRegister == null)
		      {
		        this.poolRegister = new PoolRegister();
		        this.checker = new Checker(this);
		        this.checker.startup();
		        this.dbOperator = new DBOperator();
		      }
		      is = new FileInputStream(proxoolConfigFile);
		      
		      JAXPConfigurator.configure(new InputStreamReader(is), false);
		      
		      final String[] aliases = ProxoolFacade.getAliases();
		      for (int i = 0; (aliases != null) && (i < aliases.length); i++)
		      {
		        pool = this.poolRegister.getPool(aliases[i]);
		        if (pool == null)
		        {
		          ConnectionPoolDefinitionIF poolInfo = ProxoolFacade.getConnectionPoolDefinition(aliases[i]);
		          
		          final int index=i;
		          System.out.println(proxoolConfigFile.getParent());
		          System.out.println("aliases="+aliases[index]);
		          File f = new File(proxoolConfigFile.getParent());
		          String[] files = f.list(new FilenameFilter() {
		              @Override
		              public boolean accept(File dir, String name) {
		                  return name.startsWith(aliases[index])&&name.contains(".sql.xml");
		              }
		          });
		          for (int j = 0; j < files.length; j++) {
		        	  String sqlConfigName=files[j];
		        	  File sqlConfig = new File(proxoolConfigFile.getParentFile(), files[j]);
		        	  if (sqlConfig.exists())
			          {
			            this.sqlTable.loadSQLConfigs(aliases[i], sqlConfig);
			            System.out.println("DAOSQL [" + sqlConfigName + "] loaded");
			          }
		          }
		          
		          String databaseURL = poolInfo.getUrl();
		          String user = poolInfo.getUser();
		          String password = poolInfo.getPassword();
		          String driverClass = poolInfo.getDriver();
		          int size = poolInfo.getMaximumConnectionCount();
		          
		          pool = this.poolRegister.registePool(aliases[i], databaseURL, user, password, driverClass, size);
		          System.out.println("DAO [" + aliases[i] + "] build @ " + proxoolConfigFile.getAbsolutePath());
		        }
		      }
		      return pool;
		    }
		    catch (Exception e)
		    {
		      throw e;
		    }
		    finally
		    {
		      if (is != null) {
		        is.close();
		      }
		    }
		  }
  
  public PoolRegister getPoolRegister()
  {
    return this.poolRegister;
  }
  
  public DBOperator getDBOperator()
  {
    return this.dbOperator;
  }
  
  public String getDefaultPoolName()
  {
    Iterator<String> it = this.poolRegister.poolNames();
    String name = (String)it.next();
    return name;
  }
  
  public ConnectionPool getConnectionPool()
  {
    return getConnectionPool(getDefaultPoolName());
  }
  
  public ConnectionPool getConnectionPool(String alias)
  {
    return this.poolRegister.getPool(alias);
  }
  
  public boolean isTableExist(String tableName)
    throws Exception
  {
    return isTableExist(getDefaultPoolName(), tableName);
  }
  
  public boolean isTableExist(String proxoolName, String tableName)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      boolean b = this.dbOperator.isTableExist(conn, tableName);
      
      return b;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public boolean createTable(String sql)
    throws Exception
  {
    return createTable(getDefaultPoolName(), sql);
  }
  
  public boolean createTable(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      boolean b = this.dbOperator.createTable(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("createTable", sql, (int)(time2 - time1));
      
      return b;
    }
    catch (Exception e)
    {
      pool.logError("createTable", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int count(String sql)
    throws Exception
  {
    return count(getDefaultPoolName(), sql);
  }
  
  public int count(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.count(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("count", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("count", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int count(String sql, Object[] objs)
    throws Exception
  {
    return count(getDefaultPoolName(), sql, objs);
  }
  
  public int count(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.count(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("count", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("count", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int count(String tableName, Map dataMap)
    throws Exception
  {
    return count(getDefaultPoolName(), tableName, dataMap);
  }
  
  public int count(String proxoolName, String tableName, Map dataMap)
    throws Exception
  {
    if ((dataMap == null) || (dataMap.size() == 0)) {
      return count(proxoolName, "SELECT count(*) FROM " + tableName);
    }
    int size = dataMap.size();
    
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("SELECT count(*) FROM ");
    sb.append(tableName);
    sb.append(" WHERE ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(" and ");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return count(proxoolName, sb.toString(), objs);
  }
  
  public int delete(String tableName, Map dataMap)
    throws Exception
  {
    return delete(getDefaultPoolName(), tableName, dataMap);
  }
  
  public int delete(String proxoolName, String tableName, Map dataMap)
    throws Exception
  {
    if ((dataMap == null) || (dataMap.size() == 0)) {
      return delete(proxoolName, "DELETE FROM " + tableName);
    }
    int size = dataMap.size();
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("DELETE FROM ");
    sb.append(tableName);
    sb.append(" WHERE ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(" and ");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return delete(proxoolName, sb.toString(), objs);
  }
  
  public int delete(String sql)
    throws Exception
  {
    return delete(getDefaultPoolName(), sql);
  }
  
  public int delete(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.delete(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("delete", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("delete", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int delete(String sql, Object[] objs)
    throws Exception
  {
    return delete(getDefaultPoolName(), sql, objs);
  }
  
  public int delete(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.delete(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("delete", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("delete", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public void batchInsert(String tableName, List dataList)
    throws Exception
  {
    batchInsert(getDefaultPoolName(), tableName, dataList);
  }
  
  public void batchInsert(String proxoolName, String tableName, List dataList)
    throws Exception
  {
    if ((dataList == null) || (dataList.size() == 0)) {
      return;
    }
    Map dataMap = (Map)dataList.get(0);
    
    int size = dataMap.size();
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("INSERT INTO ");
    sb.append(tableName);
    sb.append("(");
    
    boolean b = false;
    
    int n = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(n++)] = columnName;
      if (b) {
        sb.append(",");
      }
      b = true;
      sb.append(columnName);
    }
    sb.append(") VALUES (");
    for (int i = 0; i < size; i++)
    {
      if (i > 0) {
        sb.append(",");
      }
      sb.append("?");
    }
    sb.append(")");
    
    int size2 = dataList.size();
    
    List dataObjectList = new ArrayList(size2);
    for (int k = 0; k < size2; k++)
    {
      Map theDataMap = (Map)dataList.get(k);
      
      Object[] objs = new Object[size];
      for (int i = 0; i < size; i++) {
        objs[i] = theDataMap.get(columnNames[i]);
      }
      dataObjectList.add(objs);
    }
    batch(proxoolName, sb.toString(), dataObjectList);
  }
  
  public Object insert(String tableName, Map dataMap)
    throws Exception
  {
    return insert(getDefaultPoolName(), tableName, dataMap);
  }
  
  public Object insert(String proxoolName, String tableName, Map dataMap)
    throws Exception
  {
    int size = dataMap.size();
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("INSERT INTO ");
    sb.append(tableName);
    sb.append("(");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(",");
      }
      b = true;
      sb.append(columnName);
    }
    sb.append(") VALUES (");
    for (int i = 0; i < size; i++)
    {
      if (i > 0) {
        sb.append(",");
      }
      sb.append("?");
    }
    sb.append(")");
    
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return insert(proxoolName, sb.toString(), objs);
  }
  
  public Object insert(String sql)
    throws Exception
  {
    return insert(getDefaultPoolName(), sql);
  }
  
  public Object insert(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      Object obj = this.dbOperator.insert(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("insert", sql, (int)(time2 - time1));
      
      return obj;
    }
    catch (Exception e)
    {
      pool.logError("insert", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public Object insert(String sql, Object[] objs)
    throws Exception
  {
    return insert(getDefaultPoolName(), sql, objs);
  }
  
  public Object insert(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      Object obj = this.dbOperator.insert(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("insert", sql, (int)(time2 - time1));
      
      return obj;
    }
    catch (Exception e)
    {
      pool.logError("insert", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public Map select(String tableName, int F_ID_VALUE)
    throws Exception
  {
    return select(getDefaultPoolName(), tableName, F_ID_VALUE);
  }
  
  public Map select(String proxoolName, String tableName, int F_ID_VALUE)
    throws Exception
  {
    StringBuffer sb = new StringBuffer();
    
    sb.append("SELECT * FROM ");
    sb.append(tableName);
    sb.append(" WHERE F_ID=");
    sb.append(F_ID_VALUE);
    
    return select(proxoolName, sb.toString());
  }
  
  public Map select(String sql)
    throws Exception
  {
    return select(getDefaultPoolName(), sql);
  }
  
  public Map select(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      Map m = this.dbOperator.select(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("select", sql, (int)(time2 - time1));
      
      return m;
    }
    catch (Exception e)
    {
      pool.logError("select", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public Map select(String sql, Object[] objs)
    throws Exception
  {
    return select(getDefaultPoolName(), sql, objs);
  }
  
  public Map select(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      Map m = this.dbOperator.select(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("select", sql, (int)(time2 - time1));
      
      return m;
    }
    catch (Exception e)
    {
      pool.logError("select", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public Map select(String tableName, Map dataMap)
    throws Exception
  {
    return select(getDefaultPoolName(), tableName, dataMap);
  }
  
  public Map select(String proxoolName, String tableName, Map dataMap)
    throws Exception
  {
    int size = dataMap.size();
    
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("SELECT * FROM ");
    sb.append(tableName);
    sb.append(" WHERE ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(" and ");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return select(proxoolName, sb.toString(), objs);
  }
  
  public List selectList(String tableName, Map dataMap)
    throws Exception
  {
    return selectList(getDefaultPoolName(), tableName, dataMap);
  }
  
  public List selectList(String proxoolName, String tableName, Map dataMap)
    throws Exception
  {
    if ((dataMap == null) || (dataMap.size() == 0)) {
      return selectList(proxoolName, "SELECT * FROM " + tableName);
    }
    int size = dataMap.size();
    
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("SELECT * FROM ");
    sb.append(tableName);
    sb.append(" WHERE ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(" and ");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return selectList(proxoolName, sb.toString(), objs);
  }
  
  public List selectList(String sql, int startRow, int endRow)
    throws Exception
  {
    return selectList(getDefaultPoolName(), sql, startRow, endRow);
  }
  
  public List selectList(String proxoolName, String sql, int startRow, int endRow)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    if (pool.isOracle()) {
      sql = "SELECT * FROM (SELECT A.*, ROWNUM R FROM (" + sql + ") A WHERE ROWNUM <= " + endRow + ") WHERE R >= " + startRow;
    } else if (pool.isSqlServer()) {
      sql = 
        "select * from (select Row_Number() over (order by F_CREATETIME desc) as R ,* from (" + sql + ") r) u where u.R between " + startRow + " and " + endRow;
    }
    return selectList(proxoolName, sql);
  }
  
  public List selectList(String sql)
    throws Exception
  {
    return selectList(getDefaultPoolName(), sql);
  }
  
  public List selectList(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      List l = this.dbOperator.selectList(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("select", sql, (int)(time2 - time1));
      
      return l;
    }
    catch (Exception e)
    {
      pool.logError("select", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public List selectList(String sql, Object[] objs, int startRow, int endRow)
    throws Exception
  {
    return selectList(getDefaultPoolName(), sql, objs, startRow, endRow);
  }
  
  public List selectList(String proxoolName, String sql, Object[] objs, int startRow, int endRow)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    if (pool.isOracle()) {
      sql = "SELECT * FROM (SELECT A.*, ROWNUM R FROM (" + sql + ") A WHERE ROWNUM <= " + endRow + ") WHERE R >= " + startRow;
    } else if (pool.isSqlServer()) {
      sql = 
        "select * from (select Row_Number() over (order by F_CREATETIME desc) as R ,* from (" + sql + ") r) u where u.R between " + startRow + " and " + endRow;
    }
    return selectList(proxoolName, sql, objs);
  }
  
  public List selectList(String sql, Object[] objs)
    throws Exception
  {
    return selectList(getDefaultPoolName(), sql, objs);
  }
  
  public List selectList(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      List l = this.dbOperator.selectList(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("select", sql, (int)(time2 - time1));
      
      return l;
    }
    catch (Exception e)
    {
      pool.logError("select", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public List executeCallable(String callable)
    throws Exception
  {
    return executeCallable(getDefaultPoolName(), callable);
  }
  
  public List executeCallable(String proxoolName, String callable)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      List l = this.dbOperator.executeCallable(conn, callable);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("callable", callable, (int)(time2 - time1));
      
      return l;
    }
    catch (Exception e)
    {
      pool.logError("callable", callable, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public boolean truncate(String tableName)
    throws Exception
  {
    return truncate(getDefaultPoolName(), tableName);
  }
  
  public boolean truncate(String proxoolName, String tableName)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      boolean b = this.dbOperator.truncate(conn, tableName);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("truncate", "truncate " + tableName, (int)(time2 - time1));
      
      return b;
    }
    catch (Exception e)
    {
      pool.logError("truncate", "truncate " + tableName, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int update(String tableName, int F_ID_VALUE, Map dataMap)
    throws Exception
  {
    return update(getDefaultPoolName(), tableName, F_ID_VALUE, dataMap);
  }
  
  public int update(String proxoolName, String tableName, int F_ID_VALUE, Map dataMap)
    throws Exception
  {
    int size = dataMap.size();
    String[] columnNames = new String[size];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("UPDATE ");
    sb.append(tableName);
    sb.append(" SET ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(",");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    sb.append(" WHERE F_ID=" + F_ID_VALUE);
    
    Object[] objs = new Object[size];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    return update(proxoolName, sb.toString(), objs);
  }
  
  public int update(String tableName, Map conditionMap, Map dataMap)
    throws Exception
  {
    return update(getDefaultPoolName(), tableName, conditionMap, dataMap);
  }
  
  public int update(String proxoolName, String tableName, Map conditionMap, Map dataMap)
    throws Exception
  {
    int size = dataMap.size();
    
    int size2 = 0;
    if (conditionMap != null) {
      size2 = conditionMap.size();
    }
    String[] columnNames = new String[size + size2];
    
    StringBuffer sb = new StringBuffer();
    
    sb.append("UPDATE ");
    sb.append(tableName);
    sb.append(" SET ");
    
    boolean b = false;
    
    int k = 0;
    
    Iterator columnIter = dataMap.keySet().iterator();
    while ((columnIter != null) && (columnIter.hasNext()))
    {
      String columnName = (String)columnIter.next();
      
      columnNames[(k++)] = columnName;
      if (b) {
        sb.append(",");
      }
      b = true;
      sb.append(columnName);
      sb.append("=?");
    }
    sb.append(" WHERE ");
    
    b = false;
    k = 0;
    
    Iterator conditionIter = conditionMap.keySet().iterator();
    while ((conditionIter != null) && (conditionIter.hasNext()))
    {
      String conditionName = (String)conditionIter.next();
      
      columnNames[(size + k++)] = conditionName;
      if (b) {
        sb.append(" AND ");
      }
      b = true;
      sb.append(conditionName);
      sb.append("=?");
    }
    Object[] objs = new Object[size + size2];
    for (int i = 0; i < size; i++) {
      objs[i] = dataMap.get(columnNames[i]);
    }
    for (int i = size; i < size + size2; i++) {
      objs[i] = conditionMap.get(columnNames[i]);
    }
    return update(proxoolName, sb.toString(), objs);
  }
  
  public int update(String sql)
    throws Exception
  {
    return update(getDefaultPoolName(), sql);
  }
  
  public int update(String proxoolName, String sql)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.update(conn, sql);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("update", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("update", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public int update(String sql, Object[] objs)
    throws Exception
  {
    return update(getDefaultPoolName(), sql, objs);
  }
  
  public int update(String proxoolName, String sql, Object[] objs)
    throws Exception
  {
    ConnectionPool pool = this.poolRegister.getPool(proxoolName);
    
    long time1 = System.currentTimeMillis();
    
    Connection conn = pool.getConnection();
    if (conn == null) {
      throw new NullConnectionException();
    }
    try
    {
      int count = this.dbOperator.update(conn, sql, objs);
      
      long time2 = System.currentTimeMillis();
      
      pool.log("update", sql, (int)(time2 - time1));
      
      return count;
    }
    catch (Exception e)
    {
      pool.logError("update", sql, e);
      
      throw e;
    }
    finally
    {
      pool.releaseConnection(conn);
    }
  }
  
  public void batch(String[] sqls)
    throws Exception
  {
    batch(getDefaultPoolName(), sqls);
  }
  
  /* Error */
  public void batch(String proxoolName, String[] sqls)
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 25	uniesb/sdk/dao/DAO:poolRegister	Luniesb/sdk/dao/PoolRegister;
    //   4: aload_1
    //   5: invokevirtual 72	uniesb/sdk/dao/PoolRegister:getPool	(Ljava/lang/String;)Luniesb/sdk/dao/ConnectionPool;
    //   8: astore_3
    //   9: invokestatic 316	java/lang/System:currentTimeMillis	()J
    //   12: lstore 4
    //   14: aload_3
    //   15: invokevirtual 320	uniesb/sdk/dao/ConnectionPool:getConnection	()Ljava/sql/Connection;
    //   18: astore 6
    //   20: aload 6
    //   22: ifnonnull +11 -> 33
    //   25: new 324	uniesb/sdk/dao/NullConnectionException
    //   28: dup
    //   29: invokespecial 326	uniesb/sdk/dao/NullConnectionException:<init>	()V
    //   32: athrow
    //   33: aload_0
    //   34: getfield 23	uniesb/sdk/dao/DAO:dbOperator	Luniesb/sdk/dao/DBOperator;
    //   37: aload 6
    //   39: aload_2
    //   40: invokevirtual 624	uniesb/sdk/dao/DBOperator:batch	(Ljava/sql/Connection;[Ljava/lang/String;)V
    //   43: invokestatic 316	java/lang/System:currentTimeMillis	()J
    //   46: lstore 7
    //   48: aload_3
    //   49: ldc_w 627
    //   52: ldc_w 627
    //   55: lload 7
    //   57: lload 4
    //   59: lsub
    //   60: l2i
    //   61: invokevirtual 350	uniesb/sdk/dao/ConnectionPool:log	(Ljava/lang/String;Ljava/lang/String;I)V
    //   64: goto +31 -> 95
    //   67: astore 7
    //   69: aload_3
    //   70: ldc_w 627
    //   73: ldc_w 627
    //   76: aload 7
    //   78: invokevirtual 354	uniesb/sdk/dao/ConnectionPool:logError	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   81: aload 7
    //   83: athrow
    //   84: astore 9
    //   86: aload_3
    //   87: aload 6
    //   89: invokevirtual 330	uniesb/sdk/dao/ConnectionPool:releaseConnection	(Ljava/sql/Connection;)V
    //   92: aload 9
    //   94: athrow
    //   95: aload_3
    //   96: aload 6
    //   98: invokevirtual 330	uniesb/sdk/dao/ConnectionPool:releaseConnection	(Ljava/sql/Connection;)V
    //   101: return
    // Line number table:
    //   Java source line #1267	-> byte code offset #0
    //   Java source line #1269	-> byte code offset #9
    //   Java source line #1271	-> byte code offset #14
    //   Java source line #1273	-> byte code offset #20
    //   Java source line #1277	-> byte code offset #33
    //   Java source line #1279	-> byte code offset #43
    //   Java source line #1281	-> byte code offset #48
    //   Java source line #1282	-> byte code offset #64
    //   Java source line #1284	-> byte code offset #69
    //   Java source line #1286	-> byte code offset #81
    //   Java source line #1288	-> byte code offset #84
    //   Java source line #1289	-> byte code offset #86
    //   Java source line #1290	-> byte code offset #92
    //   Java source line #1289	-> byte code offset #95
    //   Java source line #1291	-> byte code offset #101
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	102	0	this	DAO
    //   0	102	1	proxoolName	String
    //   0	102	2	sqls	String[]
    //   8	88	3	pool	ConnectionPool
    //   12	46	4	time1	long
    //   18	79	6	conn	Connection
    //   46	10	7	time2	long
    //   67	15	7	e	Exception
    //   84	9	9	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   33	64	67	java/lang/Exception
    //   33	84	84	finally
  }
  
  public void batch(String sql, List objArrayList)
    throws Exception
  {
    batch(getDefaultPoolName(), sql, objArrayList);
  }
  
  /* Error */
  public void batch(String proxoolName, String sql, List objArrayList)
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 25	uniesb/sdk/dao/DAO:poolRegister	Luniesb/sdk/dao/PoolRegister;
    //   4: aload_1
    //   5: invokevirtual 72	uniesb/sdk/dao/PoolRegister:getPool	(Ljava/lang/String;)Luniesb/sdk/dao/ConnectionPool;
    //   8: astore 4
    //   10: invokestatic 316	java/lang/System:currentTimeMillis	()J
    //   13: lstore 5
    //   15: aload 4
    //   17: invokevirtual 320	uniesb/sdk/dao/ConnectionPool:getConnection	()Ljava/sql/Connection;
    //   20: astore 7
    //   22: aload 7
    //   24: ifnonnull +11 -> 35
    //   27: new 324	uniesb/sdk/dao/NullConnectionException
    //   30: dup
    //   31: invokespecial 326	uniesb/sdk/dao/NullConnectionException:<init>	()V
    //   34: athrow
    //   35: aload_0
    //   36: getfield 23	uniesb/sdk/dao/DAO:dbOperator	Luniesb/sdk/dao/DBOperator;
    //   39: aload 7
    //   41: aload_2
    //   42: aload_3
    //   43: invokevirtual 629	uniesb/sdk/dao/DBOperator:batch	(Ljava/sql/Connection;Ljava/lang/String;Ljava/util/List;)V
    //   46: invokestatic 316	java/lang/System:currentTimeMillis	()J
    //   49: lstore 8
    //   51: aload 4
    //   53: ldc_w 627
    //   56: aload_2
    //   57: lload 8
    //   59: lload 5
    //   61: lsub
    //   62: l2i
    //   63: invokevirtual 350	uniesb/sdk/dao/ConnectionPool:log	(Ljava/lang/String;Ljava/lang/String;I)V
    //   66: goto +31 -> 97
    //   69: astore 8
    //   71: aload 4
    //   73: ldc_w 627
    //   76: aload_2
    //   77: aload 8
    //   79: invokevirtual 354	uniesb/sdk/dao/ConnectionPool:logError	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   82: aload 8
    //   84: athrow
    //   85: astore 10
    //   87: aload 4
    //   89: aload 7
    //   91: invokevirtual 330	uniesb/sdk/dao/ConnectionPool:releaseConnection	(Ljava/sql/Connection;)V
    //   94: aload 10
    //   96: athrow
    //   97: aload 4
    //   99: aload 7
    //   101: invokevirtual 330	uniesb/sdk/dao/ConnectionPool:releaseConnection	(Ljava/sql/Connection;)V
    //   104: return
    // Line number table:
    //   Java source line #1300	-> byte code offset #0
    //   Java source line #1302	-> byte code offset #10
    //   Java source line #1304	-> byte code offset #15
    //   Java source line #1306	-> byte code offset #22
    //   Java source line #1310	-> byte code offset #35
    //   Java source line #1312	-> byte code offset #46
    //   Java source line #1314	-> byte code offset #51
    //   Java source line #1315	-> byte code offset #66
    //   Java source line #1317	-> byte code offset #71
    //   Java source line #1319	-> byte code offset #82
    //   Java source line #1321	-> byte code offset #85
    //   Java source line #1322	-> byte code offset #87
    //   Java source line #1323	-> byte code offset #94
    //   Java source line #1322	-> byte code offset #97
    //   Java source line #1324	-> byte code offset #104
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	DAO
    //   0	105	1	proxoolName	String
    //   0	105	2	sql	String
    //   0	105	3	objArrayList	List
    //   8	90	4	pool	ConnectionPool
    //   13	47	5	time1	long
    //   20	80	7	conn	Connection
    //   49	9	8	time2	long
    //   69	14	8	e	Exception
    //   85	10	10	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   35	66	69	java/lang/Exception
    //   35	85	85	finally
  }
  
  public String getSQL(String sqlName)
  {
	  if(defaultPoolName==null)defaultPoolName=getDefaultPoolName();
    return this.sqlTable.getSql(defaultPoolName, sqlName);
  }
  
  public String getSQL(String alias, String sqlName)
  {
    return this.sqlTable.getSql(alias, sqlName);
  }
  /** 设置 默认 别名*/
  public boolean setDefaultPoolName(String alias){
	  if(this.sqlTable.exists(alias)){
		  defaultPoolName=alias;
	  }
	  return false;
  }
  /** 手动 添加 sql 配置*/
  public void loadSQLConfig(String sqlConfigName,File sqlConfigFile){
      if (sqlConfigFile.exists())
      {
        try {
			this.sqlTable.loadSQLConfig(sqlConfigName, sqlConfigFile);
			System.out.println("DAOSQL [" + sqlConfigName + ".sql.xml] loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
      }
  }
}
