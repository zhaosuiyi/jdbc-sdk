package me.sdk.jdbc.dao;

import java.util.Iterator;

public class Checker
  implements Runnable
{
  private boolean isRunning = false;
  private DAO dao = null;
  
  public Checker(DAO _dao)
  {
    this.dao = _dao;
  }
  
  public void startup()
  {
    this.isRunning = true;
    
    new Thread(this).start();
  }
  
  public void shutdown()
  {
    this.isRunning = false;
  }
  
  public boolean isRunning()
  {
    return this.isRunning;
  }
  
  public void run()
  {
    while (this.isRunning)
    {
      Iterator poolNames = this.dao.getPoolRegister().poolNames();
      while ((poolNames != null) && (poolNames.hasNext()))
      {
        String poolName = (String)poolNames.next();
        
        ConnectionPool pool = this.dao.getPoolRegister().getPool(poolName);
        
        pool.checkSelf();
      }
      try
      {
        Thread.sleep(3000L);
      }
      catch (Exception localException) {}
    }
  }
}
