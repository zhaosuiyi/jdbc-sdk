package me.sdk.jdbc.util;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class StringUtility
{
  public static String toLowerCase(String str)
  {
    return str.toLowerCase();
  }
  
  public static String toUpperCase(String str)
  {
    return str.toUpperCase();
  }
  
  public static byte[] replace(byte[] source, byte[] flag, byte[] value)
  {
    int pos = -1;
    for (int i = 0; i < source.length; i++) {
      if (source[i] == flag[0])
      {
        boolean b = true;
        for (int j = 0; j < flag.length; j++) {
          if ((i + j >= source.length) || (flag[j] != source[(i + j)]))
          {
            b = false;
            break;
          }
        }
        if (b)
        {
          pos = i;
          break;
        }
      }
    }
    if (pos > -1)
    {
      byte[] goal = new byte[source.length - flag.length + value.length];
      
      System.arraycopy(source, 0, goal, 0, pos);
      System.arraycopy(value, 0, goal, pos, value.length);
      System.arraycopy(source, pos + flag.length, goal, pos + value.length, source.length - pos - flag.length);
      
      return goal;
    }
    return source;
  }
  
  public static String subString(String str, int startPos, int endPos)
  {
    if (endPos < 0) {
      return str.substring(startPos, str.length());
    }
    return str.substring(startPos, endPos);
  }
  
  public static String subStringBetweenFlag(String str, String flag1, String flag2)
  {
    String subStr = null;
    if (flag1 == null)
    {
      int endPos = str.indexOf(flag2);
      
      subStr = str.substring(0, endPos);
    }
    else if (flag2 == null)
    {
      int startPos = str.indexOf(flag1);
      
      subStr = str.substring(startPos + flag1.length(), str.length());
    }
    else
    {
      int startPos = str.indexOf(flag1);
      int endPos = str.indexOf(flag2);
      
      subStr = str.substring(startPos + flag1.length(), endPos);
    }
    return subStr;
  }
  
  public static String[] parse(String str, String delim)
  {
    if ((str == null) || (str.equals("")) || (str.equalsIgnoreCase("null")) || (delim == null)) {
      return null;
    }
    ArrayList value = new ArrayList();
    
    StringTokenizer st = new StringTokenizer(str, delim, true);
    
    boolean b = false;
    while (st.hasMoreTokens())
    {
      String tempStr = st.nextToken();
      if (tempStr.equals(delim))
      {
        if (b) {
          value.add("NULL");
        }
        b = true;
      }
      else
      {
        value.add(tempStr);
        b = false;
      }
    }
    int size = value.size();
    
    String[] return_value = new String[size];
    for (int i = 0; i < size; i++)
    {
      return_value[i] = ((String)value.get(i));
      if (return_value[i].equals("NULL")) {
        return_value[i] = "";
      }
    }
    return return_value;
  }
  
  public static String convert(String input, String sourceCharSet, String goalCharSet)
  {
    if (input == null) {
      return null;
    }
    if ((sourceCharSet == null) || (goalCharSet == null)) {
      return input;
    }
    try
    {
      byte[] bytes = input.getBytes(sourceCharSet);
      return new String(bytes, goalCharSet);
    }
    catch (Exception e) {}
    return input;
  }
  
  public static String convertForDB(String s)
  {
    if (s == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    
    StringTokenizer st = new StringTokenizer(s, "'\\", true);
    while (st.hasMoreTokens())
    {
      String tempStr = st.nextToken();
      if (tempStr.equals("'")) {
        sb.append("''");
      } else if (tempStr.equals("\\")) {
        sb.append("\\\\");
      } else {
        sb.append(tempStr);
      }
    }
    return sb.toString();
  }
  
  public static String convertForHTMLFlag(String s)
  {
    if (s == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    
    StringTokenizer st = new StringTokenizer(s, "'\"<>& \r\n", true);
    while (st.hasMoreTokens())
    {
      String tempStr = st.nextToken();
      if (tempStr.equals("'")) {
        sb.append("&acute;");
      } else if (tempStr.equals("\"")) {
        sb.append("&quot;");
      } else if (tempStr.equals("\\")) {
        sb.append("\\\\");
      } else if (tempStr.equals("<")) {
        sb.append("&lt;");
      } else if (tempStr.equals(">")) {
        sb.append("&gt;");
      } else if (!tempStr.equals("\r")) {
        if (tempStr.equals("\n")) {
          sb.append("<br/>");
        } else if (tempStr.equals(" ")) {
          sb.append("&nbsp;");
        } else {
          sb.append(tempStr);
        }
      }
    }
    return sb.toString();
  }
  
  public static String convertForXML(String s)
  {
    if (s == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    
    StringTokenizer st = new StringTokenizer(s, "\\<>&", true);
    while (st.hasMoreTokens())
    {
      String tempStr = st.nextToken();
      if (tempStr.equals("\\")) {
        sb.append("\\\\");
      } else if (tempStr.equals("<")) {
        sb.append("&lt;");
      } else if (tempStr.equals(">")) {
        sb.append("&gt;");
      } else if (tempStr.equals("&")) {
        sb.append("&amp;");
      } else {
        sb.append(tempStr);
      }
    }
    return sb.toString();
  }
  
  public static String convertForWMLFlag(String s)
  {
    return convertForHTMLFlag(s);
  }
  
  public static String convertForFlag(String s, String flag)
  {
    if (s == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    
    StringTokenizer st = new StringTokenizer(s, flag, true);
    while (st.hasMoreTokens())
    {
      String tempStr = st.nextToken();
      if (tempStr.equals("'")) {
        sb.append("&acute;");
      } else if (tempStr.equals("\"")) {
        sb.append("&quot;");
      } else if (tempStr.equals("\\")) {
        sb.append("\\\\");
      } else if (tempStr.equals("<")) {
        sb.append("&lt;");
      } else if (tempStr.equals(">")) {
        sb.append("&gt;");
      } else if (tempStr.equals("&")) {
        sb.append("&amp;");
      } else if (!tempStr.equals("\r")) {
        if (tempStr.equals("\n")) {
          sb.append("<br/>");
        } else if (tempStr.equals("\t")) {
          sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        } else if (tempStr.equals(" ")) {
          sb.append("&nbsp;");
        } else if (tempStr.equals(",")) {
          sb.append("&#8218;");
        } else {
          sb.append(tempStr);
        }
      }
    }
    return sb.toString();
  }
  
  public static String unescape(String str)
  {
    if (str == null) {
      return null;
    }
    String regEx = "%u([0-9A-F]{4})";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(str);
    
    StringBuffer sb = new StringBuffer();
    while (m.find())
    {
      String group = m.group().substring(2);
      m.appendReplacement(sb, String.valueOf((char)Integer.parseInt(group, 16)));
    }
    m.appendTail(sb);
    
    return sb.toString();
  }
  
  public static String unescapeEuropeChar(String str)
  {
    if (str == null) {
      return null;
    }
    str = unescape(str);
    
    String regEx = "%([0-9A-F]{2})";
    Pattern p = Pattern.compile(regEx);
    Matcher m = p.matcher(str);
    
    StringBuffer sb = new StringBuffer();
    while (m.find())
    {
      String group = m.group().substring(1);
      m.appendReplacement(sb, String.valueOf((char)Integer.parseInt(group, 16)));
    }
    m.appendTail(sb);
    
    return sb.toString();
  }
  
  private static char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
  
  public static String generateRandomLetter(int codeCount)
  {
    StringBuilder result = new StringBuilder(codeCount);
    
    int n = 0;
    
    int l = chars.length;
    
    Random r = new Random();
    for (int i = 0; i < codeCount; i++)
    {
      n = r.nextInt(l);
      
      result.append(chars[n]);
    }
    return result.toString();
  }
  
  private static char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
  
  public static String generateRandomNumber(int codeCount)
  {
    StringBuilder result = new StringBuilder(codeCount);
    
    int n = 0;
    
    int l = numbers.length;
    
    Random r = new Random();
    for (int i = 0; i < codeCount; i++)
    {
      n = r.nextInt(l);
      
      result.append(numbers[n]);
    }
    return result.toString();
  }
  
  public static int generateRandomInt()
  {
    int codeCount = 8;
    
    StringBuilder result = new StringBuilder(codeCount);
    
    int n = 0;
    
    int l = numbers.length;
    
    Random r = new Random();
    for (int i = 0; i < codeCount; i++)
    {
      n = r.nextInt(l);
      
      result.append(numbers[n]);
    }
    return Integer.parseInt(result.toString());
  }
  
  public static String base64Encrypt(byte[] bytes)
    throws Exception
  {
    BASE64Encoder encoder = new BASE64Encoder();
    
    return encoder.encode(bytes);
  }
  
  public static byte[] base64Decrypt(String str)
    throws Exception
  {
    BASE64Decoder decoder = new BASE64Decoder();
    
    return decoder.decodeBuffer(str);
  }
  
  public static String DESEncrypt(String DES_key, String str)
    throws Exception
  {
    DESPlus des = new DESPlus(DES_key);
    
    return des.encrypt(str);
  }
  
  public static String DESDecrypt(String DES_key, String str)
    throws Exception
  {
    DESPlus des = new DESPlus(DES_key);
    
    return des.decrypt(str);
  }
  
  public static String MD5ForFile(String filePath)
    throws Exception
  {
    File f = new File(filePath);
    
    return MD5(FileUtility.readFile(f));
  }
  
  public static String MD5(String source)
    throws Exception
  {
    return MD5(source.getBytes());
  }
  
  public static String MD5(byte[] source)
    throws Exception
  {
    String s = null;
    
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    MessageDigest md = MessageDigest.getInstance("MD5");
    
    md.update(source);
    
    byte[] tmp = md.digest();
    char[] str = new char[32];
    int k = 0;
    for (int i = 0; i < 16; i++)
    {
      byte byte0 = tmp[i];
      
      str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
      str[(k++)] = hexDigits[(byte0 & 0xF)];
    }
    s = new String(str);
    
    return s;
  }
  
  public static byte[] hex2byte(String hex)
  {
    byte[] bts = new byte[hex.length() / 2];
    for (int i = 0; i < bts.length; i++) {
      bts[i] = ((byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16));
    }
    return bts;
  }
  
  public static String byte2hex(byte[] bytes)
  {
    StringBuffer retString = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      retString.append(Integer.toHexString(256 + (bytes[i] & 0xFF)).substring(1).toUpperCase());
    }
    return retString.toString();
  }
  
  public static String parseVariable(String source, char splitChar)
    throws Exception
  {
    String v = null;
    
    String regEx = "\\" + splitChar + "([\\w|\\W|\\s]+)\\" + splitChar;
    
    Pattern p = Pattern.compile(regEx);
    
    Matcher m = p.matcher(source);
    if ((m != null) && (m.find()))
    {
      String g = m.group();
      
      String name = g.substring(1, g.length() - 1);
      
      v = name;
    }
    return v;
  }
  
  public static String[] parseVariables(String source, char splitChar)
    throws Exception
  {
    ArrayList l = new ArrayList();
    
    String regEx = "\\" + splitChar + "([\\w|\\W|\\s]+)\\" + splitChar;
    
    Pattern p = Pattern.compile(regEx);
    
    Matcher m = p.matcher(source);
    while ((m != null) && (m.find()))
    {
      String g = m.group();
      
      String name = g.substring(1, g.length() - 1);
      
      l.add(name);
    }
    Object[] objs = l.toArray();
    if ((objs == null) || (objs.length == 0)) {
      return null;
    }
    String[] vs = new String[objs.length];
    for (int i = 0; i < objs.length; i++) {
      vs[i] = ((String)objs[i]);
    }
    return vs;
  }
  
  public static String[] uuid_chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
  
  public static String generateShortUuid(int length)
  {
    StringBuffer shortBuffer = new StringBuffer();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    for (int i = 0; i < length; i++)
    {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      shortBuffer.append(uuid_chars[(x % 62)]);
    }
    return shortBuffer.toString();
  }
  
  public static boolean isNullString(Object o)
  {
    return (o == null) || (String.valueOf(o).trim().length() == 0) || (String.valueOf(o).trim().equalsIgnoreCase("null"));
  }
  
  public static String getString(Object o)
  {
    if (!isNullString(o)) {
      return (String)o;
    }
    return "";
  }
}
