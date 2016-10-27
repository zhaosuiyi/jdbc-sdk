package me.sdk.jdbc.dao;

public class SQLStatistic
{
  private int execute_count = 0;
  private int error_count = 0;
  private int max_execute_time = 0;
  private int total_execute_time = 0;
  
  public void log(String sql, int execute_time)
  {
    this.execute_count += 1;
    this.total_execute_time += execute_time;
    if (this.max_execute_time < execute_time) {
      this.max_execute_time = execute_time;
    }
  }
  
  public void logError(String sql)
  {
    this.error_count += 1;
  }
  
  public int getExecuteCount()
  {
    return this.execute_count;
  }
  
  public int getErrorCount()
  {
    return this.error_count;
  }
  
  public int getMaxExecuteTime()
  {
    return this.max_execute_time;
  }
  
  public int getAverageExecuteTime()
  {
    if (this.execute_count == 0) {
      return 0;
    }
    return this.total_execute_time / this.execute_count;
  }
}
