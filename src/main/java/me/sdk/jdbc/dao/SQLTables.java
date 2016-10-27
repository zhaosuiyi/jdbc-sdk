package me.sdk.jdbc.dao;

import java.io.File;
import java.util.HashMap;

import me.sdk.jdbc.util.JXML;

public class SQLTables
{
  private HashMap<String, HashMap<String, String>> sqlTable = null;
  
  public SQLTables()
  {
    if (this.sqlTable == null) {
      this.sqlTable = new HashMap();
    }
  }
  
  public void loadSQLConfig(String alias, File sqlFile)
    throws Exception
  {
    if (this.sqlTable.containsKey(alias)) {
      throw new Exception("别名[" + alias + "]的数据库配置已存在，请检查");
    }
    this.sqlTable.put(alias, new JXML(sqlFile).toMap());
  }
  
  public String getSql(String alias, String sqlName)
  {
    HashMap<String, String> m = (HashMap)this.sqlTable.get(alias);
    return (String)m.get(sqlName);
  }
  
  public void loadSQLConfigs(String alias, File sqlFile)throws Exception{
	if (this.sqlTable.containsKey(alias)) {
		HashMap<String, String> m = (HashMap)this.sqlTable.get(alias);
		m.putAll(new JXML(sqlFile).toMap()); 
		this.sqlTable.put(alias, m);
	}else{
		this.sqlTable.put(alias, new JXML(sqlFile).toMap());
	}
  }
  
  public boolean exists(String alias){
	  if (this.sqlTable.containsKey(alias)) return true;
	  return false;
  }
  
}
