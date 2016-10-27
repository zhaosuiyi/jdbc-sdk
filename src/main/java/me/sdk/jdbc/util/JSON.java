package me.sdk.jdbc.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.sdk.jdbc.ValueSet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSON
{
  public static String string2Json(String s)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i++)
    {
      char c = s.charAt(i);
      switch (c)
      {
      case '"': 
        sb.append("\\\"");
        break;
      case '\\': 
        sb.append("\\\\");
        break;
      case '/': 
        sb.append("\\/");
        break;
      case '\b': 
        sb.append("\\b");
        break;
      case '\f': 
        sb.append("\\f");
        break;
      case '\n': 
        sb.append("\\n");
        break;
      case '\r': 
        sb.append("\\r");
        break;
      case '\t': 
        sb.append("\\t");
        break;
      default: 
        sb.append(c);
      }
    }
    return sb.toString();
  }
  
  public static ValueSet toValueSet(String json)
    throws Exception
  {
    ValueSet vs = new ValueSet();
    JSONObject obj = JSONObject.fromObject(json);
    
    Iterator it = obj.keys();
    while (it.hasNext())
    {
      String key = (String)it.next();
      Object o = obj.get(key);
      vs.set(key, parseObject2(o.toString()));
    }
    return vs;
  }
  
  private static Object parseObject2(Object o)
  {
    Object result = null;
    if ((o instanceof JSONArray))
    {
      List r = new ArrayList();
      
      JSONArray array = (JSONArray)o;
      for (int k = 0; k < array.size(); k++)
      {
        Object childo = array.get(k);
        r.add((ValueSet)parseObject2(childo.toString()));
      }
      result = r;
    }
    else if (((o instanceof Integer)) || ((o instanceof Short)) || ((o instanceof Double)) || ((o instanceof Float)) || ((o instanceof Long)) || ((o instanceof Boolean)) || 
      ((o instanceof Byte)))
    {
      result = o;
    }
    else if ((o instanceof String))
    {
      String s = (String)o;
      if ((s.startsWith("[")) && (s.endsWith("]")))
      {
        List r = new ArrayList();
        
        JSONArray array = JSONArray.fromObject(s);
        for (int k = 0; k < array.size(); k++)
        {
          Object childo = array.get(k);
          
          r.add(parseObject2(childo.toString()));
        }
        result = r;
      }
      else if ((s.startsWith("{")) && (s.endsWith("}")))
      {
        ValueSet vs = new ValueSet();
        
        JSONObject childobj = JSONObject.fromObject(s);
        Iterator it = childobj.keys();
        while (it.hasNext())
        {
          String key = (String)it.next();
          Object childo = childobj.get(key);
          
          vs.set(key, parseObject2(childo.toString()));
        }
        result = vs;
      }
      else
      {
        result = (String)o;
      }
    }
    else
    {
      System.out.println("else o=" + o);
      result = o;
    }
    return result;
  }
  
  public static List toList(String json)
    throws Exception
  {
    JSONArray obj = JSONArray.fromObject(json);
    
    List list = new ArrayList();
    for (int i = 0; i < obj.size(); i++) {
      list.add(parseObject(obj.get(i)));
    }
    return list;
  }
  
  public static HashMap toMap(String json)
    throws Exception
  {
    HashMap m = new HashMap();
    
    JSONObject obj = JSONObject.fromObject(json);
    
    Iterator it = obj.keys();
    while (it.hasNext())
    {
      String key = (String)it.next();
      Object o = obj.get(key);
      m.put(key, parseObject(o.toString()));
    }
    return m;
  }
  
  private static Object parseObject(Object o)
  {
    Object result = null;
    if ((o instanceof JSONArray))
    {
      List r = new ArrayList();
      JSONArray array = (JSONArray)o;
      for (int k = 0; k < array.size(); k++)
      {
        Object childo = array.get(k);
        
        r.add(parseObject(childo.toString()));
      }
      result = r;
    }
    else if ((o instanceof String))
    {
      String s = (String)o;
      if ((s.startsWith("[")) && (s.endsWith("]")))
      {
        List r = new ArrayList();
        
        JSONArray array = JSONArray.fromObject(s);
        for (int k = 0; k < array.size(); k++)
        {
          Object childo = array.get(k);
          
          r.add(parseObject(childo.toString()));
        }
        result = r;
      }
      else if ((s.startsWith("{")) && (s.endsWith("}")))
      {
        HashMap m = new HashMap();
        JSONObject childobj = JSONObject.fromObject(s);
        Iterator it = childobj.keys();
        while (it.hasNext())
        {
          String key = (String)it.next();
          Object childo = childobj.get(key);
          
          m.put(key, parseObject(childo.toString()));
        }
        result = m;
      }
      else
      {
        result = o;
      }
    }
    else if ((o instanceof JSONObject))
    {
      HashMap m = new HashMap();
      JSONObject childobj = (JSONObject)o;
      Iterator it = childobj.keys();
      while (it.hasNext())
      {
        String key = (String)it.next();
        Object childo = childobj.get(key);
        
        m.put(key, parseObject(childo.toString()));
      }
      result = m;
    }
    else
    {
      result = o;
    }
    return result;
  }
  
  public static String toJSON(Object obj)
  {
    String value = null;
    if (obj == null) {
      return value;
    }
    if ((obj instanceof ValueSet)) {
      value = toJSON((ValueSet)obj);
    } else if ((obj instanceof Map)) {
      value = toJSON((Map)obj);
    } else if ((obj instanceof List)) {
      value = convertListToJSON((List)obj);
    } else {
      value = obj.toString();
    }
    return value;
  }
  
  public static String toJSON(Map m)
  {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    
    int i = 0;
    
    Iterator names = m.keySet().iterator();
    while ((names != null) && (names.hasNext()))
    {
      String name = (String)names.next();
      
      Object obj = m.get(name);
      name = name.toLowerCase();
      if (obj != null)
      {
        if (i > 0) {
          sb.append(",");
        }
        i++;
        
        sb.append("\"");
        sb.append(string2Json(name));
        
        sb.append("\":");
        if ((obj instanceof List))
        {
          sb.append(convertListToJSON((List)obj));
        }
        else if ((obj instanceof Map))
        {
          sb.append(toJSON((Map)obj));
        }
        else if ((obj instanceof ValueSet))
        {
          sb.append(toJSON((ValueSet)obj));
        }
        else
        {
          if (!obj.toString().startsWith("\"")) {
            sb.append("\"");
          }
          sb.append(StringUtility.isNullString(obj) ? "" : string2Json(obj.toString()));
          if (!obj.toString().startsWith("\"")) {
            sb.append("\"");
          }
        }
      }
    }
    sb.append("}");
    
    return sb.toString().replaceAll("\r\n", "\\r\\n");
  }
  
  public static String convertListToJSON(List l)
  {
    StringBuffer sb = new StringBuffer();
    
    sb.append("[");
    for (int i = 0; (l != null) && (i < l.size()); i++)
    {
      Object m = l.get(i);
      if (i > 0) {
        sb.append(",");
      }
      if ((m instanceof Map))
      {
        sb.append(toJSON((Map)m));
      }
      else if ((m instanceof ValueSet))
      {
        sb.append(toJSON((ValueSet)m));
      }
      else if ((m instanceof List))
      {
        sb.append(convertListToJSON((List)m));
      }
      else
      {
        sb.append("\"");
        sb.append(string2Json(m.toString()));
        sb.append("\"");
      }
    }
    sb.append("]");
    
    return sb.toString();
  }
  
  public static String toJSON(ValueSet vs)
  {
    return toJSON(vs.getData());
  }
}
