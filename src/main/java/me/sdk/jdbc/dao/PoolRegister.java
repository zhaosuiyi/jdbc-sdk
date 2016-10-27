package me.sdk.jdbc.dao;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

public class PoolRegister
{
  private HashMap<String, ConnectionPool> poolTable = null;
  
  public PoolRegister()
  {
    this.poolTable = new HashMap();
  }
  
  public ConnectionPool getPool(String poolName)
  {
    return (ConnectionPool)this.poolTable.get(poolName);
  }
  
  public ConnectionPool registePool(String proxoolName, String databaseURL, String username, String password, String driverClass, int size)
    throws Exception
  {
    if (this.poolTable.get(proxoolName) != null) {
      throw new Exception("不能重复注册");
    }
    System.out.println("registePool:" + proxoolName + "[" + databaseURL + 
      "]");
    
    ConnectionPool pool = new ConnectionPool(proxoolName, databaseURL, 
      username, password, driverClass, size);
    this.poolTable.put(proxoolName, pool);
    
    return pool;
  }
  
  public Iterator poolNames()
  {
    return this.poolTable.keySet().iterator();
  }
  
  public void removePool(String poolName)
    throws ProxoolException
  {
    ProxoolFacade.removeConnectionPool(poolName);
    this.poolTable.remove(poolName);
  }
  
  public void clear()
  {
    Iterator<String> alias = this.poolTable.keySet().iterator();
    while (alias.hasNext()) {
      try
      {
        String alia = (String)alias.next();
        ProxoolFacade.killAllConnections(alia, "系统关闭", true);
        ProxoolFacade.removeConnectionPool(alia);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    this.poolTable.clear();
  }
}
