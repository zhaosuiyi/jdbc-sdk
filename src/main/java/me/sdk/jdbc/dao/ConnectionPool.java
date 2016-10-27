package me.sdk.jdbc.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.logicalcobwebs.proxool.admin.SnapshotIF;

public class ConnectionPool
{
  private String poolName = null;
  private String databaseURL = null;
  private String username = null;
  private String password = null;
  private String driverClass = null;
  private int size = 0;
  private long createTime = 0L;
  private int maxConnectionSize = 0;
  private AtomicInteger activeConnectionSize = new AtomicInteger(0);
  private AtomicInteger availableConnectionSize = new AtomicInteger(0);
  private Map sqlTable = null;
  private String[] sqlActions = { "select", "insert", "update", "delete", "count", "callable", "truncate", "batch", "createTable" };
  private HashMap sqlStatisticTable = null;
  private boolean isActive = false;
  
  public ConnectionPool(String _poolName, String _databaseURL, String _username, String _password, String _driverClass, int _size)
  {
    this.poolName = _poolName;
    this.databaseURL = _databaseURL;
    this.username = _username;
    this.password = _password;
    this.driverClass = _driverClass;
    this.size = _size;
    this.createTime = System.currentTimeMillis();
    this.sqlTable = new HashMap();
    
    this.sqlStatisticTable = new HashMap();
    for (int i = 0; i < this.sqlActions.length; i++) {
      this.sqlStatisticTable.put(this.sqlActions[i], new SQLStatistic());
    }
    setActive(true);
  }
  
  public long getCreateTime()
  {
    return this.createTime;
  }
  
  public String getUrl()
  {
    return this.databaseURL;
  }
  
  public String getDriver()
  {
    return this.driverClass;
  }
  
  public String getUser()
  {
    return this.username;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public int getSize()
  {
    return this.size;
  }
  
  public boolean isOracle()
  {
    return this.databaseURL.indexOf("oracle") >= 0;
  }
  
  public boolean isSqlServer()
  {
    return this.databaseURL.indexOf("sqlserver") >= 0;
  }
  
  public boolean isMysql()
  {
    return this.databaseURL.indexOf("mysql") >= 0;
  }
  
  public void loadSQLConfigFile(File sqlXMLFile)
    throws Exception
  {
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(sqlXMLFile);
    Element rootElement = doc.getRootElement();
    
    List children = rootElement.getContent();
    for (int i = 0; (children != null) && (i < children.size()); i++)
    {
      Object obj = children.get(i);
      if ((obj instanceof Element))
      {
        Element el = (Element)obj;
        if ((el.getText() != null) && (!el.getText().trim().equals(""))) {
          this.sqlTable.put(el.getName().toLowerCase(), el.getText().trim());
        }
      }
    }
  }
  
  public int sqlConfigCount()
  {
    return this.sqlTable.size();
  }
  
  public void log(String sqlAction, String sql, int executeTime)
  {
    SQLStatistic statistic = getSQLStatistic(sqlAction);
    
    statistic.log(sql, executeTime);
  }
  
  public void logError(String sqlAction, String sql, Throwable errorInfo)
  {
    SQLStatistic statistic = getSQLStatistic(sqlAction);
    
    statistic.logError(sql);
  }
  
  public Map getConfig()
  {
    Map info = new HashMap();
    
    info.put("alias", this.poolName);
    info.put("driverClass", this.driverClass);
    info.put("url", this.databaseURL);
    info.put("username", this.username);
    info.put("password", this.password);
    info.put("size", Integer.valueOf(this.size));
    info.put("isActive", Boolean.valueOf(this.isActive));
    
    return info;
  }
  
  public String[] sqlActions()
  {
    return this.sqlActions;
  }
  
  public SQLStatistic getSQLStatistic(String sqlAction)
  {
    return (SQLStatistic)this.sqlStatisticTable.get(sqlAction);
  }
  
  public String getName()
  {
    return this.poolName;
  }
  
  public void releaseConnection(Connection conn)
  {
    if (conn == null) {
      return;
    }
    try
    {
      conn.close();
    }
    catch (Exception localException) {}
  }
  
  public Connection getConnection()
    throws Exception
  {
    return DriverManager.getConnection("proxool." + this.poolName);
  }
  
  public void checkSelf()
  {
    try
    {
      SnapshotIF snapshot = ProxoolFacade.getSnapshot(this.poolName, true);
      
      this.activeConnectionSize.set(snapshot.getActiveConnectionCount());
      
      this.availableConnectionSize.set(snapshot.getAvailableConnectionCount());
      
      this.maxConnectionSize = snapshot.getMaximumConnectionCount();
      
      setActive(true);
    }
    catch (Exception e)
    {
      setActive(false);
    }
  }
  
  public int getActiveConnectionCount()
  {
    return this.activeConnectionSize.get();
  }
  
  public int getAvailableConnectionCount()
  {
    return this.availableConnectionSize.get();
  }
  
  public int getMaximumConnectionCount()
  {
    return this.maxConnectionSize;
  }
  
  private void setActive(boolean b)
  {
    this.isActive = b;
  }
  
  public boolean isActive()
  {
    return this.isActive;
  }
}

