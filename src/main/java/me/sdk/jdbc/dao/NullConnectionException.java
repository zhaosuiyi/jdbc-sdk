package me.sdk.jdbc.dao;


public class NullConnectionException
  extends Exception
{
  public NullConnectionException()
  {
    super("没有可用的数据库连接");
  }
}
