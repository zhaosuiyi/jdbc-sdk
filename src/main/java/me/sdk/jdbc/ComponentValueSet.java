package me.sdk.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ComponentValueSet
  implements Serializable
{
  private String title = null;
  private LinkedHashMap dataTable = null;
  
  public ComponentValueSet()
  {
    this.dataTable = new LinkedHashMap();
  }
  
  public ComponentValueSet(LinkedHashMap map)
  {
    this.dataTable = map;
  }
  
  public ComponentValueSet clone()
  {
    ComponentValueSet vs = new ComponentValueSet(this.title);
    HashMap tempMap = toFlatMap();
    Iterator<String> it = tempMap.keySet().iterator();
    while (it.hasNext())
    {
      String key = (String)it.next();
      vs.set(key, get(key));
    }
    return vs;
  }
  
  public ComponentValueSet(String _title)
  {
    this.title = _title;
    this.dataTable = new LinkedHashMap();
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public boolean isEmpty()
  {
    return this.dataTable.isEmpty();
  }
  
  public void set(String name, Object obj)
  {
    int index = name.indexOf(".");
    if (index < 0)
    {
      if (obj == null) {
        this.dataTable.remove(name);
      } else {
        this.dataTable.put(name, obj);
      }
    }
    else
    {
      String k = name.substring(0, index);
      
      ValueSet vs = getValueSet(k);
      if (vs == null)
      {
        vs = new ValueSet();
        
        setValueSet(k, vs);
      }
      vs.set(name.substring(index + 1), obj);
    }
  }
  
  public void setValueSet(String name, ValueSet vs)
  {
    set(name, vs);
  }
  
  public void setValueSet(String name, ComponentValueSet vs)
  {
    set(name, vs);
  }
  
  public void setString(String name, String value)
  {
    set(name, value);
  }
  
  public void setByte(String name, byte b)
  {
    set(name, new Byte(b));
  }
  
  public void setInt(String name, int value)
  {
    set(name, new Integer(value));
  }
  
  public void setLong(String name, long value)
  {
    set(name, new Long(value));
  }
  
  public void setFloat(String name, float value)
  {
    set(name, new Float(value));
  }
  
  public void setDouble(String name, double value)
  {
    set(name, new Double(value));
  }
  
  public void setChar(String name, char value)
  {
    set(name, new Character(value));
  }
  
  public void setBoolean(String name, boolean value)
  {
    set(name, new Boolean(value));
  }
  
  public Object get(String name)
  {
    int index = name.indexOf(".");
    if (index < 0)
    {
      Object result = this.dataTable.get(name);
      return StringUtils.isEmpty(String.valueOf(result)) ? null : result;
    }
    ValueSet vs = getValueSet(name.substring(0, index));
    if (vs == null) {
      return null;
    }
    return vs.get(name.substring(index + 1));
  }
  
  public String getString(String name)
  {
    Object obj = get(name);
    
    return obj == null ? null : obj.toString();
  }
  
  public int getInt(String name)
    throws Exception
  {
    Integer value = (Integer)get(name);
    
    return value.intValue();
  }
  
  public long getLong(String name)
    throws Exception
  {
    Long value = (Long)get(name);
    
    return value.longValue();
  }
  
  public float getFloat(String name)
    throws Exception
  {
    Float value = (Float)get(name);
    
    return value.floatValue();
  }
  
  public double getDouble(String name)
    throws Exception
  {
    Double value = (Double)get(name);
    
    return value.doubleValue();
  }
  
  public boolean getBoolean(String name)
    throws Exception
  {
    Boolean value = (Boolean)get(name);
    
    return value.booleanValue();
  }
  
  public char getChar(String name)
    throws Exception
  {
    Character value = (Character)get(name);
    
    return value.charValue();
  }
  
  public ValueSet getValueSet(String name)
  {
    if ((get(name) instanceof String)) {
      return null;
    }
    return (ValueSet)get(name);
  }
  
  public void delete(String name)
  {
    int index = name.indexOf(".");
    if (index < 0)
    {
      this.dataTable.remove(name);
    }
    else
    {
      String k = name.substring(0, index);
      
      ValueSet vs = getValueSet(k);
      if (vs != null) {
        vs.delete(name.substring(index + 1));
      }
    }
  }
  
  public Iterator keyNames()
  {
    return this.dataTable.keySet().iterator();
  }
  
  public HashMap getData()
  {
    return this.dataTable;
  }
  
  public String toString()
  {
    return this.dataTable.toString();
  }
  
  public void clear()
  {
    this.dataTable.clear();
  }
  
  public HashMap toFlatMap()
  {
    List<String> keyList = toKeyList(this, null);
    
    HashMap<String, Object> map = new HashMap();
    for (String string : keyList) {
      map.put(string, get(string));
    }
    return map;
  }
  
  private List toKeyList(ComponentValueSet vs, String pKey)
  {
    List l = new ArrayList();
    Iterator<String> it = vs.keyNames();
    while (it.hasNext())
    {
      String sKey = (String)it.next();
      Object o = vs.get(sKey);
      if ((o instanceof ComponentValueSet))
      {
        if ((pKey != null) && (pKey.length() > 0)) {
          l.addAll(toKeyList((ComponentValueSet)o, pKey + "." + sKey));
        } else {
          l.addAll(toKeyList((ComponentValueSet)o, sKey));
        }
      }
      else if ((pKey != null) && (pKey.length() > 0)) {
        l.add(pKey + "." + sKey);
      } else {
        l.add(sKey);
      }
    }
    return l;
  }
}
