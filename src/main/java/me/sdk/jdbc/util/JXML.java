package me.sdk.jdbc.util;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.sdk.jdbc.ComponentValueSet;
import me.sdk.jdbc.ValueSet;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

public class JXML
{
  private File f = null;
  private String xmlContent = null;
  private Element rootElement = null;
  
  public JXML(Element e)
  {
    this.rootElement = e;
  }
  
  public JXML(File _f)
    throws Exception
  {
    this.f = _f;
    
    this.xmlContent = FileUtility.readFullContent(_f, "UTF-8");
    try
    {
      Reader r = new StringReader(this.xmlContent);
      
      SAXBuilder builder = new SAXBuilder();
      
      Document doc = builder.build(r);
      
      this.rootElement = doc.getRootElement();
      
      r.close();
    }
    catch (Exception l_ex)
    {
      throw l_ex;
    }
  }
  
  public JXML(String _xmlContent)
    throws Exception
  {
    this.xmlContent = _xmlContent;
    try
    {
      Reader r = new StringReader(_xmlContent);
      
      SAXBuilder builder = new SAXBuilder();
      
      Document doc = builder.build(r);
      
      this.rootElement = doc.getRootElement();
      
      r.close();
    }
    catch (Exception l_ex)
    {
      throw l_ex;
    }
  }
  
  public File getFile()
  {
    return this.f;
  }
  
  public String getContent()
  {
    return this.xmlContent;
  }
  
  public Element getRootElement()
  {
    return this.rootElement;
  }
  
  public String getValue(Element currElement, String xpath, int num)
    throws JDOMException
  {
    XPath l_xpath = null;
    List l_lstNodes = null;
    String l_strRt = null;
    try
    {
      l_xpath = XPath.newInstance(xpath);
      l_lstNodes = l_xpath.selectNodes(currElement);
    }
    catch (JDOMException l_ex)
    {
      throw l_ex;
    }
    if ((l_lstNodes != null) && (!l_lstNodes.isEmpty()))
    {
      if (num >= l_lstNodes.size()) {
        throw new JDOMException("num is not right");
      }
      for (int i = 0; i < l_lstNodes.size(); i++) {
        if (i == num)
        {
          Element l_element = (Element)l_lstNodes.get(i);
          l_strRt = l_element.getValue();
        }
      }
      return l_strRt;
    }
    return null;
  }
  
  public String getValue(String xpath, int num)
  {
    try
    {
      return getValue(getRootElement(), xpath, num);
    }
    catch (JDOMException e) {}
    return null;
  }
  
  public String getValue(String xpath)
  {
    try
    {
      return getValue(getRootElement(), xpath, 0);
    }
    catch (JDOMException e) {}
    return null;
  }
  
  public Element getElement(Element currElement, String xpath, int num)
    throws JDOMException
  {
    XPath l_xpath = null;
    List l_lstNodes = null;
    Element l_element = null;
    try
    {
      l_xpath = XPath.newInstance(xpath);
      l_lstNodes = l_xpath.selectNodes(currElement);
    }
    catch (JDOMException l_ex)
    {
      throw new JDOMException("xpath not right");
    }
    if ((l_lstNodes != null) && (!l_lstNodes.isEmpty()))
    {
      if (num >= l_lstNodes.size()) {
        throw new JDOMException("����������");
      }
      for (int i = 0; i < l_lstNodes.size(); i++) {
        if (i == num)
        {
          l_element = (Element)l_lstNodes.get(i);
          break;
        }
      }
      return l_element;
    }
    return null;
  }
  
  public Element getElement(String xpath, int num)
    throws JDOMException
  {
    return getElement(getRootElement(), xpath, num);
  }
  
  public Element getElement(String xpath)
    throws JDOMException
  {
    return getElement(getRootElement(), xpath, 0);
  }
  
  public List getElementList(Element currElement, String xpath)
    throws JDOMException
  {
    XPath l_xpath = null;
    List l_lstNodes = null;
    List l_elementList = new ArrayList();
    try
    {
      l_xpath = XPath.newInstance(xpath);
      l_lstNodes = l_xpath.selectNodes(currElement);
    }
    catch (JDOMException l_ex)
    {
      throw new JDOMException("xpath not right");
    }
    if ((l_lstNodes != null) && (!l_lstNodes.isEmpty())) {
      for (int i = 0; i < l_lstNodes.size(); i++)
      {
        Element l_element = (Element)l_lstNodes.get(i);
        l_elementList.add(l_element);
      }
    } else {
      throw new JDOMException("node not exist");
    }
    return l_elementList;
  }
  
  public List getElementList(String xpath)
    throws JDOMException
  {
    return getElementList(getRootElement(), xpath);
  }
  
  public List childrenElementList(Element element)
    throws JDOMException
  {
    List l = new ArrayList();
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      if ((l_obj instanceof Element))
      {
        Element l_elmentContent = (Element)l_obj;
        l.add(l_elmentContent);
      }
    }
    return l;
  }
  
  public Element getElement(Element currElement, String xpath, String attributeName, String attributeValue)
    throws JDOMException
  {
    XPath l_xpath = null;
    List l_lstNodes = null;
    Element l_element = null;
    try
    {
      if ((attributeName != null) && (attributeValue != null)) {
        l_xpath = XPath.newInstance(xpath + "[@" + attributeName + "='" + attributeValue + "']");
      } else {
        l_xpath = XPath.newInstance(xpath);
      }
      l_lstNodes = l_xpath.selectNodes(currElement);
    }
    catch (JDOMException l_ex)
    {
      throw new JDOMException("xpath not right");
    }
    if ((l_lstNodes != null) && (!l_lstNodes.isEmpty())) {
      l_element = (Element)l_lstNodes.get(0);
    } else {
      throw new JDOMException("node not exist");
    }
    return l_element;
  }
  
  public Element getElement(String xpath, String attributeName, String attributeValue)
    throws JDOMException
  {
    return getElement(getRootElement(), xpath, attributeName, attributeValue);
  }
  
  public String getAttributeValue(Element element, String attributeName)
    throws JDOMException
  {
    try
    {
      return element.getAttributeValue(attributeName);
    }
    catch (Exception e) {}
    return null;
  }
  
  public String getAttributeValue(String attributeName)
    throws JDOMException
  {
    return getAttributeValue(getRootElement(), attributeName);
  }
  
  public String getValue(Element element, String xpath)
    throws JDOMException
  {
    return getValue(element, xpath, 0);
  }
  
  public Element getElement(Element element, String xpath)
    throws JDOMException
  {
    return getElement(element, xpath, 0);
  }
  
  public ArrayList toList(Element element)
  {
    ArrayList l = new ArrayList();
    
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      
      Element e2 = (Element)l_obj;
      
      l.add(toMap(e2));
    }
    return l;
  }
  
  public HashMap toMap()
  {
    return toMap(getRootElement());
  }
  
  public HashMap toMap(Element element)
  {
    HashMap m = new HashMap();
    
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      if ((l_obj instanceof Element))
      {
        Element l_elementContent = (Element)l_obj;
        if (isList(l_elementContent))
        {
          m.put(l_elementContent.getName(), toList(l_elementContent));
        }
        else
        {
          List subChildren = l_elementContent.getChildren();
          if ((subChildren != null) && (subChildren.size() > 0)) {
            m.put(l_elementContent.getName(), toMap(l_elementContent));
          } else {
            m.put(l_elementContent.getName(), l_elementContent.getValue());
          }
        }
      }
      else if ((l_obj instanceof Text))
      {
        Text l_textContent = (Text)l_obj;
        m.put(element.getName(), l_textContent.getTextTrim());
      }
    }
    return m;
  }
  
  private boolean isList(Element element)
  {
    return element.getName().endsWith("List");
  }
  
  public ValueSet toValueSet(Element element)
  {
    ValueSet vs = new ValueSet();
    
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      if ((l_obj instanceof Element))
      {
        Element l_elementContent = (Element)l_obj;
        if (isList(l_elementContent))
        {
          vs.set(l_elementContent.getName(), toValueSetList(l_elementContent));
        }
        else
        {
          List subChildren = l_elementContent.getChildren();
          if ((subChildren != null) && (subChildren.size() > 0)) {
            vs.set(l_elementContent.getName(), toValueSet(l_elementContent));
          } else {
            vs.set(l_elementContent.getName(), l_elementContent.getValue());
          }
        }
      }
      else if ((l_obj instanceof Text))
      {
        Text l_textContent = (Text)l_obj;
        vs.set(element.getName(), l_textContent.getTextTrim());
      }
    }
    return vs;
  }
  
  public List toValueSetList(Element element)
  {
    List l = new ArrayList();
    
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      
      Element e2 = (Element)l_obj;
      
      l.add(toValueSet(e2));
    }
    return l;
  }
  
  public String toXML()
  {
    return toXML(getRootElement());
  }
  
  public String toXML(Element element)
  {
    StringBuffer l_sb = new StringBuffer("<" + element.getName());
    boolean l_bln = false;
    List l_listAttributes = element.getAttributes();
    if ((l_listAttributes != null) && (!l_listAttributes.isEmpty()))
    {
      for (int i = 0; i < l_listAttributes.size(); i++)
      {
        Attribute attribute = (Attribute)l_listAttributes.get(i);
        l_sb.append(" " + attribute.getName() + "=\"" + attribute.getValue() + "\"");
      }
      l_sb.append(">");
    }
    else
    {
      l_sb.append(">");
    }
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      Element l_elmentContent = (Element)l_obj;
      if (l_elmentContent.getChildren().size() > 0)
      {
        l_sb.append("\n");
        l_sb.append(toXML(l_elmentContent));
        l_bln = true;
      }
      else
      {
        String s = l_elmentContent.getValue();
        
        l_sb.append("<" + l_elmentContent.getName() + ">");
        
        s.indexOf("<![CDATA[");
        
        l_sb.append(s);
        l_sb.append("</" + l_elmentContent.getName() + ">");
      }
    }
    if (l_bln) {
      l_sb.append("\n");
    }
    l_sb.append("</" + element.getName() + ">");
    return l_sb.toString();
  }
  
  public String toHTML()
  {
    return toHTML(getRootElement());
  }
  
  public String toHTML(Element element)
  {
    StringBuffer l_sb = new StringBuffer();
    l_sb.append("<table border=1>");
    l_sb.append("<tr><td>");
    l_sb.append(element.getName());
    l_sb.append("</td><td>");
    boolean l_bln = false;
    
    List l_listAttributes = element.getAttributes();
    if ((l_listAttributes != null) && (!l_listAttributes.isEmpty()))
    {
      l_sb.append("<table border=1>");
      l_bln = true;
      for (int i = 0; i < l_listAttributes.size(); i++)
      {
        l_sb.append("<tr><td>");
        Attribute attribute = (Attribute)l_listAttributes.get(i);
        l_sb.append(attribute.getName());
        l_sb.append("</td><td>");
        l_sb.append(attribute.getValue());
        l_sb.append("</td></tr>");
      }
      l_sb.append("</table>");
    }
    List children = element.getChildren();
    for (int j = 0; (children != null) && (j < children.size()); j++)
    {
      Object l_obj = children.get(j);
      if ((l_obj instanceof Element))
      {
        Element l_elmentContent = (Element)l_obj;
        l_sb.append(toHTML(l_elmentContent));
      }
      else if ((l_obj instanceof Text))
      {
        Text l_textContent = (Text)l_obj;
        l_sb.append(l_textContent.getTextTrim());
      }
    }
    l_sb.append("</td></tr></table>");
    return l_sb.toString();
  }
  
  public static String toXML(Map m)
  {
    return toXML(null, m);
  }
  
  public static String toXML(String mapName, Map m)
  {
    StringBuffer sb = new StringBuffer();
    if ((mapName != null) && (mapName.trim().length() > 0))
    {
      sb.append("<");
      sb.append(mapName.toLowerCase());
      sb.append(">");
    }
    Iterator keys = m.keySet().iterator();
    while ((keys != null) && (keys.hasNext()))
    {
      String k = (String)keys.next();
      Object obj = m.get(k);
      if (obj != null)
      {
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("<");
          sb.append(k.toLowerCase());
          sb.append(">");
        }
        if ((obj instanceof List))
        {
          sb.append("\r\n");
          
          int list_pos = k.indexOf("List");
          if (list_pos == -1) {
            list_pos = k.indexOf("list");
          }
          String childName = k.substring(0, list_pos);
          
          sb.append(convertListToXML(childName, (List)obj));
        }
        else if ((obj instanceof Map))
        {
          sb.append("\r\n");
          
          sb.append(toXML((Map)obj));
        }
        else
        {
          String s = obj.toString();
          if (s.indexOf("<![CDATA[") < 0)
          {
            sb.append("<![CDATA[");
            sb.append(s);
            sb.append("]]>");
          }
          else
          {
            sb.append(s);
          }
        }
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("</");
          sb.append(k.toLowerCase());
          sb.append(">\r\n");
        }
      }
    }
    if ((mapName != null) && (mapName.trim().length() > 0))
    {
      sb.append("</");
      sb.append(mapName.toLowerCase());
      sb.append(">");
    }
    else
    {
      sb.append("<");
      sb.append("null");
      sb.append(">");
    }
    return sb.toString();
  }
  
  private static String convertListToXML(String name, List l)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; (l != null) && (i < l.size()); i++)
    {
      Object o = l.get(i);
      if ((o instanceof Map))
      {
        sb.append(toXML(name, (Map)o));
      }
      else if ((o instanceof List))
      {
        sb.append(convertListToXML(name, (List)o));
      }
      else if ((o instanceof ValueSet))
      {
        sb.append(toXML(name, (ValueSet)o));
      }
      else
      {
        sb.append("<");
        sb.append(name.toLowerCase());
        sb.append(">\r\n");
        
        sb.append(o.toString());
        
        sb.append("</");
        sb.append(name.toLowerCase());
        sb.append(">\r\n");
      }
    }
    return sb.toString();
  }
  
  public static String toXML(ValueSet vs)
  {
    return toXML(null, vs);
  }
  
  public static String toXML(String mapName, ValueSet vs)
  {
    StringBuffer sb = new StringBuffer();
    if ((mapName != null) && (mapName.trim().length() > 0))
    {
      sb.append("<");
      sb.append(mapName.toLowerCase());
      sb.append(">");
      sb.append("\r\n");
    }
    else
    {
      sb.append("<");
      sb.append("null");
      sb.append(">");
    }
    Iterator keys = vs.keyNames();
    while ((keys != null) && (keys.hasNext()))
    {
      String k = (String)keys.next();
      Object obj = vs.get(k);
      if (obj == null)
      {
        sb.append("<");
        sb.append(k.toLowerCase());
        sb.append(">");
        
        String s = "";
        if (s.toUpperCase().indexOf("<![CDATA[") < 0)
        {
          sb.append("<![CDATA[");
          sb.append(s);
          sb.append("]]>");
        }
        else
        {
          sb.append(s);
        }
        sb.append("</");
        sb.append(k);
        sb.append(">\r\n");
      }
      else if ((obj instanceof List))
      {
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("<");
          sb.append(k.toLowerCase());
          sb.append(">");
          sb.append("\r\n");
        }
        sb.append("\r\n");
        
        int list_pos = k.indexOf("List");
        if (list_pos == -1) {
          list_pos = k.indexOf("list");
        }
        String childName;
        if (list_pos == -1) {
          childName = k;
        } else {
          childName = k.substring(0, list_pos);
        }
        sb.append(convertListToXML(childName, (List)obj));
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("</");
          sb.append(k);
          sb.append(">\r\n");
        }
      }
      else if ((obj instanceof Map))
      {
        sb.append(toXML(k, (Map)obj));
      }
      else if ((obj instanceof ValueSet))
      {
        sb.append(toXML(k, (ValueSet)obj));
      }
      else
      {
        sb.append("<");
        sb.append(k.toLowerCase());
        sb.append(">");
        sb.append("\r\n");
        
        String s = obj.toString();
        if (s.indexOf("<![CDATA[") < 0)
        {
          sb.append("<![CDATA[");
          sb.append(s);
          sb.append("]]>");
        }
        else
        {
          sb.append(s);
        }
        sb.append("\r\n");
        sb.append("</");
        sb.append(k);
        sb.append(">\r\n");
      }
    }
    if ((mapName != null) && (mapName.trim().length() > 0))
    {
      sb.append("</");
      sb.append(mapName.toLowerCase());
      sb.append(">");
      sb.append("\r\n");
    }
    else
    {
      sb.append("<");
      sb.append("null");
      sb.append(">");
    }
    return sb.toString();
  }
  
  private static String convertValueSetListToXML(String name, List l)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; (l != null) && (i < l.size()); i++)
    {
      ValueSet vs = (ValueSet)l.get(i);
      
      sb.append(toXML(name, vs));
      sb.append("\r\n");
    }
    return sb.toString();
  }
  
  public static String toXML(ComponentValueSet vs)
  {
    return toXML(null, vs);
  }
  
  public static String toXML(String mapName, ComponentValueSet vs)
  {
    StringBuffer sb = new StringBuffer();
    if ((mapName != null) && (mapName.trim().length() > 0))
    {
      sb.append("<");
      sb.append(mapName);
      sb.append(">");
      sb.append("\r\n");
    }
    Iterator keys = vs.keyNames();
    while ((keys != null) && (keys.hasNext()))
    {
      String k = (String)keys.next();
      Object obj = vs.get(k);
      if (obj == null)
      {
        sb.append("<");
        sb.append(k);
        sb.append(">");
        
        String s = "";
        if (s.toUpperCase().indexOf("<![CDATA[") < 0)
        {
          sb.append("<![CDATA[");
          sb.append(s);
          sb.append("]]>");
        }
        else
        {
          sb.append(s);
        }
        sb.append("</");
        sb.append(k);
        sb.append(">\r\n");
      }
      else if ((obj instanceof List))
      {
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("<");
          sb.append(k);
          sb.append(">");
          sb.append("\r\n");
        }
        sb.append("\r\n");
        
        int list_pos = k.indexOf("List");
        if (list_pos == -1) {
          list_pos = k.indexOf("list");
        }
        String childName;
        if (list_pos == -1) {
          childName = k;
        } else {
          childName = k.substring(0, list_pos);
        }
        sb.append(convertListToXML(childName, (List)obj));
        if (!k.equalsIgnoreCase(mapName))
        {
          sb.append("</");
          sb.append(k);
          sb.append(">\r\n");
        }
      }
      else if ((obj instanceof Map))
      {
        sb.append(toXML(k, (Map)obj));
      }
      else if ((obj instanceof ComponentValueSet))
      {
        sb.append(toXML(k, (ComponentValueSet)obj));
      }
      else if ((obj instanceof ValueSet))
      {
        sb.append(toXML(k, (ValueSet)obj));
      }
      else
      {
        sb.append("<");
        sb.append(k);
        sb.append(">");
        sb.append("\r\n");
        
        String s = obj.toString();
        if (s.indexOf("<![CDATA[") < 0)
        {
          sb.append("<![CDATA[");
          sb.append(s);
          sb.append("]]>");
        }
        else
        {
          sb.append(s);
        }
        sb.append("\r\n");
        sb.append("</");
        sb.append(k);
        sb.append(">\r\n");
      }
    }
    if (mapName != null)
    {
      sb.append("</");
      sb.append(mapName);
      sb.append(">");
      sb.append("\r\n");
    }
    return sb.toString();
  }
}

