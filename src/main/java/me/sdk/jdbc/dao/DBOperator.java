package me.sdk.jdbc.dao;

import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOperator
{
  public int count(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    try
    {
      stmt = conn.createStatement();
      
      ResultSet rset = stmt.executeQuery(sql);
      
      rset.next();
      
      int count = rset.getInt(1);
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  public int count(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try
    {
      pstmt = conn.prepareStatement(sql);
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      rset = pstmt.executeQuery();
      
      rset.next();
      
      int count = rset.getInt(1);
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public int delete(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    try
    {
      stmt = conn.createStatement();
      
      int count = stmt.executeUpdate(sql);
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  public int delete(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    try
    {
      pstmt = conn.prepareStatement(sql);
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      int count = pstmt.executeUpdate();
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
    }
  }
  
  public Object insert(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    
    ResultSet rset = null;
    try
    {
      System.out.println("[do insert...]");
      stmt = conn.createStatement();
      
      stmt.executeUpdate(sql, new int[] { 1 });
      
      rset = stmt.getGeneratedKeys();
      
      boolean b = rset.next();
      if (b) {
        return rset.getObject(1);
      }
      return null;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  public Object insert(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    
    ResultSet rset = null;
    try
    {
      pstmt = conn.prepareStatement(sql, new int[] { 1 });
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      pstmt.executeUpdate();
      
      rset = pstmt.getGeneratedKeys();
      
      boolean b = rset.next();
      if (b) {
        return rset.getObject(1);
      }
      return null;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public Map select(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    ResultSet rset = null;
    try
    {
      stmt = conn.createStatement();
      
      rset = stmt.executeQuery(sql);
      
      return convertToMap(rset);
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public Map select(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try
    {
      pstmt = conn.prepareStatement(sql);
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      rset = pstmt.executeQuery();
      
      return convertToMap(rset);
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public List selectList(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    ResultSet rset = null;
    try
    {
      stmt = conn.createStatement();
      
      rset = stmt.executeQuery(sql);
      
      return convertToList(rset);
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public List selectList(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try
    {
      pstmt = conn.prepareStatement(sql);
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      rset = pstmt.executeQuery();
      
      return convertToList(rset);
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public List executeCallable(Connection conn, String callable)
    throws Exception
  {
    CallableStatement proc = null;
    ResultSet rset = null;
    try
    {
      proc = conn.prepareCall(callable);
      
      proc.execute();
      
      rset = proc.getResultSet();
      
      return convertToList(rset);
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (proc != null) {
        proc.close();
      }
      if (rset != null) {
        rset.close();
      }
    }
  }
  
  public boolean truncate(Connection conn, String table)
    throws Exception
  {
    Statement stmt = null;
    
    String sql = "truncate table " + table;
    try
    {
      stmt = conn.createStatement();
      
      stmt.executeUpdate(sql);
      
      return true;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  public int update(Connection conn, String sql)
    throws Exception
  {
    Statement stmt = null;
    try
    {
      stmt = conn.createStatement();
      
      int count = stmt.executeUpdate(sql);
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (stmt != null) {
        stmt.close();
      }
    }
  }
  
  public int update(Connection conn, String sql, Object[] objs)
    throws Exception
  {
    PreparedStatement pstmt = null;
    try
    {
      pstmt = conn.prepareStatement(sql);
      for (int i = 1; (objs != null) && (i < objs.length + 1); i++)
      {
        Object obj = objs[(i - 1)];
        if ((obj != null) && ((obj instanceof String)) && ("NULL".equalsIgnoreCase(obj.toString()))) {
          pstmt.setObject(i, null);
        } else {
          pstmt.setObject(i, obj);
        }
      }
      int count = pstmt.executeUpdate();
      
      return count;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
    }
  }
  
  public boolean isTableExist(Connection conn, String tableName)
    throws Exception
  {
    ResultSet rs = null;
    try
    {
      DatabaseMetaData dbm = conn.getMetaData();
      
      rs = dbm.getTables(null, null, tableName, null);
      if (rs.next()) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (rs != null) {
        rs.close();
      }
    }
  }
  
  public boolean createTable(Connection conn, String sql)
    throws Exception
  {
    Statement pstmt = null;
    
    boolean flag = true;
    try
    {
      pstmt = conn.createStatement();
      
      int k = pstmt.executeUpdate(sql);
      
      return k > 0;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
    }
  }
  
  public boolean createSequence(Connection conn, String sql)
    throws Exception
  {
    Statement pstmt = null;
    
    boolean flag = true;
    try
    {
      pstmt = conn.createStatement();
      
      int k = pstmt.executeUpdate(sql);
      
      return k > 0;
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      if (pstmt != null) {
        pstmt.close();
      }
    }
  }
  
  /* Error */
  public void batch(Connection conn, String[] sqls)
    throws Exception
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_1
    //   3: invokeinterface 206 1 0
    //   8: istore 4
    //   10: aload_1
    //   11: iconst_0
    //   12: invokeinterface 209 2 0
    //   17: aload_1
    //   18: invokeinterface 19 1 0
    //   23: astore_3
    //   24: iconst_0
    //   25: istore 5
    //   27: goto +16 -> 43
    //   30: aload_3
    //   31: aload_2
    //   32: iload 5
    //   34: aaload
    //   35: invokeinterface 213 2 0
    //   40: iinc 5 1
    //   43: aload_2
    //   44: ifnull +10 -> 54
    //   47: iload 5
    //   49: aload_2
    //   50: arraylength
    //   51: if_icmplt -21 -> 30
    //   54: aload_3
    //   55: invokeinterface 216 1 0
    //   60: pop
    //   61: aload_1
    //   62: invokeinterface 220 1 0
    //   67: goto +31 -> 98
    //   70: astore 5
    //   72: aload 5
    //   74: athrow
    //   75: astore 6
    //   77: aload_1
    //   78: iload 4
    //   80: invokeinterface 209 2 0
    //   85: aload_3
    //   86: ifnull +9 -> 95
    //   89: aload_3
    //   90: invokeinterface 41 1 0
    //   95: aload 6
    //   97: athrow
    //   98: aload_1
    //   99: iload 4
    //   101: invokeinterface 209 2 0
    //   106: aload_3
    //   107: ifnull +9 -> 116
    //   110: aload_3
    //   111: invokeinterface 41 1 0
    //   116: return
    // Line number table:
    //   Java source line #478	-> byte code offset #0
    //   Java source line #480	-> byte code offset #2
    //   Java source line #484	-> byte code offset #10
    //   Java source line #486	-> byte code offset #17
    //   Java source line #488	-> byte code offset #24
    //   Java source line #489	-> byte code offset #30
    //   Java source line #488	-> byte code offset #40
    //   Java source line #491	-> byte code offset #54
    //   Java source line #493	-> byte code offset #61
    //   Java source line #494	-> byte code offset #67
    //   Java source line #496	-> byte code offset #72
    //   Java source line #498	-> byte code offset #75
    //   Java source line #499	-> byte code offset #77
    //   Java source line #501	-> byte code offset #85
    //   Java source line #502	-> byte code offset #95
    //   Java source line #499	-> byte code offset #98
    //   Java source line #501	-> byte code offset #106
    //   Java source line #503	-> byte code offset #116
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	117	0	this	DBOperator
    //   0	117	1	conn	Connection
    //   0	117	2	sqls	String[]
    //   1	110	3	stmt	Statement
    //   8	92	4	auto	boolean
    //   25	23	5	i	int
    //   70	3	5	e	Exception
    //   75	21	6	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   10	67	70	java/lang/Exception
    //   10	75	75	finally
  }
  
  /* Error */
  public void batch(Connection conn, String sql, List objArrayList)
    throws Exception
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: aload_1
    //   4: invokeinterface 206 1 0
    //   9: istore 5
    //   11: aload_1
    //   12: iconst_0
    //   13: invokeinterface 209 2 0
    //   18: aload_1
    //   19: aload_2
    //   20: invokeinterface 61 2 0
    //   25: astore 4
    //   27: aload 4
    //   29: invokeinterface 228 1 0
    //   34: iconst_0
    //   35: istore 6
    //   37: goto +112 -> 149
    //   40: aload_3
    //   41: iload 6
    //   43: invokeinterface 231 2 0
    //   48: checkcast 93	[Ljava/lang/Object;
    //   51: astore 7
    //   53: aload 7
    //   55: ifnonnull +6 -> 61
    //   58: goto +88 -> 146
    //   61: iconst_1
    //   62: istore 8
    //   64: goto +65 -> 129
    //   67: aload 7
    //   69: iload 8
    //   71: iconst_1
    //   72: isub
    //   73: aaload
    //   74: astore 9
    //   76: aload 9
    //   78: ifnull +37 -> 115
    //   81: aload 9
    //   83: instanceof 56
    //   86: ifeq +29 -> 115
    //   89: ldc 65
    //   91: aload 9
    //   93: invokevirtual 67	java/lang/Object:toString	()Ljava/lang/String;
    //   96: invokevirtual 71	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   99: ifeq +16 -> 115
    //   102: aload 4
    //   104: iload 8
    //   106: aconst_null
    //   107: invokeinterface 75 3 0
    //   112: goto +14 -> 126
    //   115: aload 4
    //   117: iload 8
    //   119: aload 9
    //   121: invokeinterface 75 3 0
    //   126: iinc 8 1
    //   129: iload 8
    //   131: aload 7
    //   133: arraylength
    //   134: iconst_1
    //   135: iadd
    //   136: if_icmplt -69 -> 67
    //   139: aload 4
    //   141: invokeinterface 234 1 0
    //   146: iinc 6 1
    //   149: aload_3
    //   150: ifnull +14 -> 164
    //   153: iload 6
    //   155: aload_3
    //   156: invokeinterface 236 1 0
    //   161: if_icmplt -121 -> 40
    //   164: aload 4
    //   166: invokeinterface 239 1 0
    //   171: pop
    //   172: aload_1
    //   173: invokeinterface 220 1 0
    //   178: goto +33 -> 211
    //   181: astore 6
    //   183: aload 6
    //   185: athrow
    //   186: astore 10
    //   188: aload_1
    //   189: iload 5
    //   191: invokeinterface 209 2 0
    //   196: aload 4
    //   198: ifnull +10 -> 208
    //   201: aload 4
    //   203: invokeinterface 84 1 0
    //   208: aload 10
    //   210: athrow
    //   211: aload_1
    //   212: iload 5
    //   214: invokeinterface 209 2 0
    //   219: aload 4
    //   221: ifnull +10 -> 231
    //   224: aload 4
    //   226: invokeinterface 84 1 0
    //   231: return
    // Line number table:
    //   Java source line #507	-> byte code offset #0
    //   Java source line #509	-> byte code offset #3
    //   Java source line #513	-> byte code offset #11
    //   Java source line #515	-> byte code offset #18
    //   Java source line #517	-> byte code offset #27
    //   Java source line #519	-> byte code offset #34
    //   Java source line #521	-> byte code offset #40
    //   Java source line #523	-> byte code offset #53
    //   Java source line #525	-> byte code offset #61
    //   Java source line #527	-> byte code offset #67
    //   Java source line #529	-> byte code offset #76
    //   Java source line #531	-> byte code offset #115
    //   Java source line #525	-> byte code offset #126
    //   Java source line #534	-> byte code offset #139
    //   Java source line #519	-> byte code offset #146
    //   Java source line #537	-> byte code offset #164
    //   Java source line #539	-> byte code offset #172
    //   Java source line #540	-> byte code offset #178
    //   Java source line #542	-> byte code offset #183
    //   Java source line #544	-> byte code offset #186
    //   Java source line #545	-> byte code offset #188
    //   Java source line #547	-> byte code offset #196
    //   Java source line #548	-> byte code offset #208
    //   Java source line #545	-> byte code offset #211
    //   Java source line #547	-> byte code offset #219
    //   Java source line #549	-> byte code offset #231
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	232	0	this	DBOperator
    //   0	232	1	conn	Connection
    //   0	232	2	sql	String
    //   0	232	3	objArrayList	List
    //   1	224	4	pstmt	PreparedStatement
    //   9	204	5	auto	boolean
    //   35	119	6	i	int
    //   181	3	6	e	Exception
    //   51	81	7	objs	Object[]
    //   62	68	8	j	int
    //   74	46	9	obj	Object
    //   186	23	10	localObject1	Object
    // Exception table:
    //   from	to	target	type
    //   11	178	181	java/lang/Exception
    //   11	186	186	finally
  }
  
  private Map convertToMap(ResultSet rs)
    throws Exception
  {
    ResultSetMetaData md = rs.getMetaData();
    
    int columnCount = md.getColumnCount();
    
    boolean b = rs.next();
    if (!b) {
      return null;
    }
    Map<String, Object> rowData = new HashMap();
    for (int i = 1; i <= columnCount; i++) {
      if (rs.getObject(i) != null) {
        rowData.put(md.getColumnLabel(i).toUpperCase(), convertObjectToString(rs.getObject(i)));
      }
    }
    return rowData;
  }
  
  private List convertToList(ResultSet rs)
    throws Exception
  {
    ResultSetMetaData md = rs.getMetaData();
    
    int columnCount = md.getColumnCount();
    
    List l = new ArrayList();
    while (rs.next())
    {
      Map<String, Object> rowData = new HashMap();
      for (int i = 1; i <= columnCount; i++) {
        if (rs.getObject(i) != null) {
          rowData.put(md.getColumnLabel(i).toUpperCase(), convertObjectToString(rs.getObject(i)));
        }
      }
      l.add(rowData);
    }
    return l;
  }
  
  private Object convertObjectToString(Object obj)
    throws Exception
  {
    if (obj == null) {
      return null;
    }
    String c = obj.getClass().getName().toLowerCase();
    if ((c.equals("oracle.sql.clob")) || (c.equals("net.sourceforge.jtds.jdbc.clobimpl")))
    {
      Clob clob = (Clob)obj;
      if (clob.length() > 0L) {
        return clob.getSubString(1L, (int)clob.length());
      }
      return null;
    }
    if (c.equals("[b")) {
      return obj;
    }
    if ((c.indexOf("timestamp") >= 0) || (c.indexOf("date") >= 0) || (c.indexOf("time") >= 0)) {
      return obj;
    }
    if (c.indexOf("blob") >= 0) {
      return obj;
    }
    return obj.toString();
  }
}
