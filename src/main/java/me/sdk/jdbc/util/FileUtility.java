package me.sdk.jdbc.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class FileUtility
{
  public static List<File> getAllFileList(File rootFile)
  {
    return getAllFileList(rootFile.getAbsolutePath(), null);
  }
  
  public static List<File> getAllFileList(String filePath, List<File> fileList)
  {
    if (fileList == null) {
      fileList = new ArrayList();
    }
    File rootFile = new File(filePath);
    File[] files = rootFile.listFiles();
    if (files != null)
    {
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length;
      for (int i = 0; i < j; i++)
      {
        File file = arrayOfFile1[i];
        if (file.isDirectory()) {
          getAllFileList(file.getAbsolutePath(), fileList);
        } else {
          fileList.add(file);
        }
      }
    }
    return fileList;
  }
  
  public static String[] getFileList(String filePath)
  {
    File path = null;
    String[] fileList = null;
    try
    {
      path = new File(filePath);
      fileList = path.list();
    }
    catch (Exception localException) {}
    return fileList;
  }
  
  /* Error */
  public static boolean copy(File sourceFile, File goalFile)
  {
	return false;
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: new 76	java/io/DataInputStream
    //   10: dup
    //   11: new 78	java/io/BufferedInputStream
    //   14: dup
    //   15: new 80	java/io/FileInputStream
    //   18: dup
    //   19: aload_0
    //   20: invokespecial 82	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   23: invokespecial 85	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   26: invokespecial 88	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   29: astore_3
    //   30: new 89	java/io/DataOutputStream
    //   33: dup
    //   34: new 91	java/io/BufferedOutputStream
    //   37: dup
    //   38: new 93	java/io/FileOutputStream
    //   41: dup
    //   42: aload_1
    //   43: invokespecial 95	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   46: invokespecial 96	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   49: invokespecial 99	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   52: astore 4
    //   54: goto +12 -> 66
    //   57: aload 4
    //   59: aload_3
    //   60: invokevirtual 100	java/io/DataInputStream:readByte	()B
    //   63: invokevirtual 104	java/io/DataOutputStream:write	(I)V
    //   66: aload_3
    //   67: invokevirtual 108	java/io/DataInputStream:available	()I
    //   70: ifne -13 -> 57
    //   73: goto +43 -> 116
    //   76: astore 5
    //   78: iconst_0
    //   79: istore_2
    //   80: aload_3
    //   81: invokevirtual 112	java/io/DataInputStream:close	()V
    //   84: aload 4
    //   86: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   89: goto +41 -> 130
    //   92: astore 7
    //   94: goto +36 -> 130
    //   97: astore 6
    //   99: aload_3
    //   100: invokevirtual 112	java/io/DataInputStream:close	()V
    //   103: aload 4
    //   105: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   108: goto +5 -> 113
    //   111: astore 7
    //   113: aload 6
    //   115: athrow
    //   116: aload_3
    //   117: invokevirtual 112	java/io/DataInputStream:close	()V
    //   120: aload 4
    //   122: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   125: goto +5 -> 130
    //   128: astore 7
    //   130: iload_2
    //   131: ireturn
    // Line number table:
    //   Java source line #101	-> byte code offset #0
    //   Java source line #103	-> byte code offset #2
    //   Java source line #104	-> byte code offset #4
    //   Java source line #108	-> byte code offset #7
    //   Java source line #109	-> byte code offset #30
    //   Java source line #111	-> byte code offset #54
    //   Java source line #112	-> byte code offset #57
    //   Java source line #111	-> byte code offset #66
    //   Java source line #113	-> byte code offset #73
    //   Java source line #115	-> byte code offset #78
    //   Java source line #120	-> byte code offset #80
    //   Java source line #121	-> byte code offset #84
    //   Java source line #122	-> byte code offset #89
    //   Java source line #117	-> byte code offset #97
    //   Java source line #120	-> byte code offset #99
    //   Java source line #121	-> byte code offset #103
    //   Java source line #122	-> byte code offset #108
    //   Java source line #125	-> byte code offset #113
    //   Java source line #120	-> byte code offset #116
    //   Java source line #121	-> byte code offset #120
    //   Java source line #122	-> byte code offset #125
    //   Java source line #127	-> byte code offset #130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	sourceFile	File
    //   0	132	1	goalFile	File
    //   1	130	2	b	boolean
    //   3	114	3	in	java.io.DataInputStream
    //   5	116	4	out	java.io.DataOutputStream
    //   76	3	5	e	Exception
    //   97	17	6	localObject	Object
    //   92	1	7	localException1	Exception
    //   111	1	7	localException2	Exception
    //   128	1	7	localException3	Exception
    // Exception table:
    //   from	to	target	type
    //   7	73	76	java/lang/Exception
    //   80	89	92	java/lang/Exception
    //   7	80	97	finally
    //   99	108	111	java/lang/Exception
    //   116	125	128	java/lang/Exception
  }
  
  public static boolean writeFile(String filePath, String content, String charSet)
    throws Exception
  {
    OutputStream os = new FileOutputStream(filePath);
    InputStream is = new ByteArrayInputStream(content.getBytes(charSet));
    byte[] buf = new byte['?'];
    int length = 0;
    while ((length = is.read(buf)) > 0) {
      os.write(buf, 0, length);
    }
    os.flush();
    os.close();
    is.close();
    return true;
  }
  
  public static void creatDirs(String aParentDir)
  {
    File aFile = new File(aParentDir);
    if (!aFile.exists()) {
      aFile.mkdirs();
    }
  }
  
  public static String formetFileSize(long fileS)
  {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if ((fileS > 0L) && (fileS < 1024L)) {
      fileSizeString = "1KB";
    } else if (fileS < 1048576L) {
      fileSizeString = df.format(fileS / 1024.0D) + "KB";
    } else if (fileS < 1073741824L) {
      fileSizeString = df.format(fileS / 1048576.0D) + "MB";
    } else {
      fileSizeString = df.format(fileS / 1.073741824E9D) + "GB";
    }
    return fileSizeString;
  }
  
  public static File[] subFiles(String filePath)
  {
    File path = null;
    File[] fileList = null;
    try
    {
      path = new File(filePath);
      fileList = path.listFiles();
    }
    catch (Exception localException) {}
    return fileList;
  }
  
  public static String[] subFilePaths(String filePath)
  {
    File path = null;
    String[] fileList = null;
    try
    {
      path = new File(filePath);
      fileList = path.list();
    }
    catch (Exception localException) {}
    return fileList;
  }
  
  /* Error */
  public static byte[] readFile(File file)
  {
	return null;
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_0
    //   5: invokevirtual 169	java/io/File:exists	()Z
    //   8: ifne +18 -> 26
    //   11: aload_2
    //   12: ifnull +12 -> 24
    //   15: aload_2
    //   16: invokevirtual 234	java/io/RandomAccessFile:close	()V
    //   19: goto +5 -> 24
    //   22: astore 5
    //   24: aconst_null
    //   25: areturn
    //   26: aload_0
    //   27: invokevirtual 237	java/io/File:length	()J
    //   30: l2i
    //   31: istore_3
    //   32: new 235	java/io/RandomAccessFile
    //   35: dup
    //   36: aload_0
    //   37: ldc -16
    //   39: invokespecial 242	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   42: astore_2
    //   43: iload_3
    //   44: newarray <illegal type>
    //   46: astore_1
    //   47: aload_2
    //   48: aload_1
    //   49: iconst_0
    //   50: iload_3
    //   51: invokevirtual 245	java/io/RandomAccessFile:readFully	([BII)V
    //   54: goto +63 -> 117
    //   57: astore_3
    //   58: getstatic 248	java/lang/System:err	Ljava/io/PrintStream;
    //   61: new 192	java/lang/StringBuilder
    //   64: dup
    //   65: ldc -2
    //   67: invokespecial 204	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   70: aload_3
    //   71: invokevirtual 256	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   74: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   80: invokevirtual 259	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   83: aload_2
    //   84: ifnull +46 -> 130
    //   87: aload_2
    //   88: invokevirtual 234	java/io/RandomAccessFile:close	()V
    //   91: goto +39 -> 130
    //   94: astore 5
    //   96: goto +34 -> 130
    //   99: astore 4
    //   101: aload_2
    //   102: ifnull +12 -> 114
    //   105: aload_2
    //   106: invokevirtual 234	java/io/RandomAccessFile:close	()V
    //   109: goto +5 -> 114
    //   112: astore 5
    //   114: aload 4
    //   116: athrow
    //   117: aload_2
    //   118: ifnull +12 -> 130
    //   121: aload_2
    //   122: invokevirtual 234	java/io/RandomAccessFile:close	()V
    //   125: goto +5 -> 130
    //   128: astore 5
    //   130: aload_1
    //   131: areturn
    // Line number table:
    //   Java source line #250	-> byte code offset #0
    //   Java source line #251	-> byte code offset #2
    //   Java source line #255	-> byte code offset #4
    //   Java source line #269	-> byte code offset #11
    //   Java source line #270	-> byte code offset #19
    //   Java source line #255	-> byte code offset #24
    //   Java source line #257	-> byte code offset #26
    //   Java source line #259	-> byte code offset #32
    //   Java source line #260	-> byte code offset #43
    //   Java source line #261	-> byte code offset #47
    //   Java source line #262	-> byte code offset #54
    //   Java source line #264	-> byte code offset #58
    //   Java source line #269	-> byte code offset #83
    //   Java source line #270	-> byte code offset #91
    //   Java source line #266	-> byte code offset #99
    //   Java source line #269	-> byte code offset #101
    //   Java source line #270	-> byte code offset #109
    //   Java source line #273	-> byte code offset #114
    //   Java source line #269	-> byte code offset #117
    //   Java source line #270	-> byte code offset #125
    //   Java source line #275	-> byte code offset #130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	file	File
    //   1	130	1	buffer	byte[]
    //   3	119	2	f	RandomAccessFile
    //   31	20	3	size	int
    //   57	14	3	ex	Exception
    //   99	16	4	localObject	Object
    //   22	1	5	localException1	Exception
    //   94	1	5	localException2	Exception
    //   112	1	5	localException3	Exception
    //   128	1	5	localException4	Exception
    // Exception table:
    //   from	to	target	type
    //   11	19	22	java/lang/Exception
    //   4	11	57	java/lang/Exception
    //   26	54	57	java/lang/Exception
    //   83	91	94	java/lang/Exception
    //   4	11	99	finally
    //   26	83	99	finally
    //   101	109	112	java/lang/Exception
    //   117	125	128	java/lang/Exception
  }
  
  public static String readFullContent(File f, String charset)
    throws Exception
  {
    StringBuffer sb = new StringBuffer();
    
    boolean b = false;
    
    Charset cs = Charset.forName(charset);
    
    FileInputStream is = new FileInputStream(f);
    BufferedReader theReader = new BufferedReader(new UnicodeReader(is, cs.name()));
    
    String line = null;
    while ((line = theReader.readLine()) != null)
    {
      sb.append(line);
      if ((!line.endsWith("\r\n")) && (!line.endsWith("\r"))) {
        sb.append("\r\n");
      }
    }
    theReader.close();
    
    return sb.toString();
  }
  
  public static boolean copyDir(File sourceDir, File goalDir)
  {
    boolean b = true;
    
    File[] subFiles = sourceDir.listFiles();
    for (int i = 0; (subFiles != null) && (i < subFiles.length); i++) {
      if (subFiles[i].isFile())
      {
        File goalFile = new File(goalDir, subFiles[i].getName());
        
        b = copyFile(subFiles[i], goalFile);
        if (!b) {
          break;
        }
      }
      else
      {
        File subGoalDir = new File(goalDir, subFiles[i].getName());
        if (!subGoalDir.exists()) {
          subGoalDir.mkdir();
        }
        b = copyDir(subFiles[i], subGoalDir);
        if (!b) {
          break;
        }
      }
    }
    return b;
  }
  
  /* Error */
  public static boolean copyFile(File sourceFile, File goalFile)
  {
	return false;
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: new 76	java/io/DataInputStream
    //   10: dup
    //   11: new 78	java/io/BufferedInputStream
    //   14: dup
    //   15: new 80	java/io/FileInputStream
    //   18: dup
    //   19: aload_0
    //   20: invokespecial 82	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   23: invokespecial 85	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   26: invokespecial 88	java/io/DataInputStream:<init>	(Ljava/io/InputStream;)V
    //   29: astore_3
    //   30: new 89	java/io/DataOutputStream
    //   33: dup
    //   34: new 91	java/io/BufferedOutputStream
    //   37: dup
    //   38: new 93	java/io/FileOutputStream
    //   41: dup
    //   42: aload_1
    //   43: invokespecial 95	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   46: invokespecial 96	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   49: invokespecial 99	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   52: astore 4
    //   54: goto +12 -> 66
    //   57: aload 4
    //   59: aload_3
    //   60: invokevirtual 100	java/io/DataInputStream:readByte	()B
    //   63: invokevirtual 104	java/io/DataOutputStream:write	(I)V
    //   66: aload_3
    //   67: invokevirtual 108	java/io/DataInputStream:available	()I
    //   70: ifne -13 -> 57
    //   73: goto +43 -> 116
    //   76: astore 5
    //   78: iconst_0
    //   79: istore_2
    //   80: aload_3
    //   81: invokevirtual 112	java/io/DataInputStream:close	()V
    //   84: aload 4
    //   86: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   89: goto +41 -> 130
    //   92: astore 7
    //   94: goto +36 -> 130
    //   97: astore 6
    //   99: aload_3
    //   100: invokevirtual 112	java/io/DataInputStream:close	()V
    //   103: aload 4
    //   105: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   108: goto +5 -> 113
    //   111: astore 7
    //   113: aload 6
    //   115: athrow
    //   116: aload_3
    //   117: invokevirtual 112	java/io/DataInputStream:close	()V
    //   120: aload 4
    //   122: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   125: goto +5 -> 130
    //   128: astore 7
    //   130: iload_2
    //   131: ireturn
    // Line number table:
    //   Java source line #365	-> byte code offset #0
    //   Java source line #367	-> byte code offset #2
    //   Java source line #368	-> byte code offset #4
    //   Java source line #372	-> byte code offset #7
    //   Java source line #373	-> byte code offset #30
    //   Java source line #375	-> byte code offset #54
    //   Java source line #376	-> byte code offset #57
    //   Java source line #375	-> byte code offset #66
    //   Java source line #377	-> byte code offset #73
    //   Java source line #379	-> byte code offset #78
    //   Java source line #384	-> byte code offset #80
    //   Java source line #385	-> byte code offset #84
    //   Java source line #386	-> byte code offset #89
    //   Java source line #381	-> byte code offset #97
    //   Java source line #384	-> byte code offset #99
    //   Java source line #385	-> byte code offset #103
    //   Java source line #386	-> byte code offset #108
    //   Java source line #389	-> byte code offset #113
    //   Java source line #384	-> byte code offset #116
    //   Java source line #385	-> byte code offset #120
    //   Java source line #386	-> byte code offset #125
    //   Java source line #391	-> byte code offset #130
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	sourceFile	File
    //   0	132	1	goalFile	File
    //   1	130	2	b	boolean
    //   3	114	3	in	java.io.DataInputStream
    //   5	116	4	out	java.io.DataOutputStream
    //   76	3	5	e	Exception
    //   97	17	6	localObject	Object
    //   92	1	7	localException1	Exception
    //   111	1	7	localException2	Exception
    //   128	1	7	localException3	Exception
    // Exception table:
    //   from	to	target	type
    //   7	73	76	java/lang/Exception
    //   80	89	92	java/lang/Exception
    //   7	80	97	finally
    //   99	108	111	java/lang/Exception
    //   116	125	128	java/lang/Exception
  }
  
  public static boolean cleanDir(File dir)
  {
    File[] files = dir.listFiles();
    for (int i = 0; (files != null) && (i < files.length); i++) {
      if (files[i].isFile())
      {
        boolean b = files[i].delete();
        if (!b) {
          return false;
        }
      }
      else
      {
        boolean b = cleanDir(files[i]);
        if (b) {
          files[i].delete();
        } else {
          return false;
        }
      }
    }
    return true;
  }
  
  /* Error */
  public static boolean write(File f, byte[] bs)
  {
	return false;
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_0
    //   5: invokevirtual 347	java/io/File:getParentFile	()Ljava/io/File;
    //   8: invokevirtual 169	java/io/File:exists	()Z
    //   11: ifne +11 -> 22
    //   14: aload_0
    //   15: invokevirtual 347	java/io/File:getParentFile	()Ljava/io/File;
    //   18: invokevirtual 172	java/io/File:mkdirs	()Z
    //   21: pop
    //   22: aload_0
    //   23: invokevirtual 169	java/io/File:exists	()Z
    //   26: ifne +8 -> 34
    //   29: aload_0
    //   30: invokevirtual 351	java/io/File:createNewFile	()Z
    //   33: pop
    //   34: new 89	java/io/DataOutputStream
    //   37: dup
    //   38: new 91	java/io/BufferedOutputStream
    //   41: dup
    //   42: new 93	java/io/FileOutputStream
    //   45: dup
    //   46: aload_0
    //   47: invokespecial 95	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   50: invokespecial 96	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   53: invokespecial 99	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   56: astore_3
    //   57: aload_3
    //   58: aload_1
    //   59: iconst_0
    //   60: aload_1
    //   61: arraylength
    //   62: invokevirtual 354	java/io/DataOutputStream:write	([BII)V
    //   65: goto +43 -> 108
    //   68: astore 4
    //   70: iconst_0
    //   71: istore_2
    //   72: aload_3
    //   73: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   76: goto +46 -> 122
    //   79: astore 6
    //   81: aload 6
    //   83: invokevirtual 355	java/lang/Exception:printStackTrace	()V
    //   86: goto +36 -> 122
    //   89: astore 5
    //   91: aload_3
    //   92: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   95: goto +10 -> 105
    //   98: astore 6
    //   100: aload 6
    //   102: invokevirtual 355	java/lang/Exception:printStackTrace	()V
    //   105: aload 5
    //   107: athrow
    //   108: aload_3
    //   109: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   112: goto +10 -> 122
    //   115: astore 6
    //   117: aload 6
    //   119: invokevirtual 355	java/lang/Exception:printStackTrace	()V
    //   122: iload_2
    //   123: ireturn
    // Line number table:
    //   Java source line #437	-> byte code offset #0
    //   Java source line #439	-> byte code offset #2
    //   Java source line #443	-> byte code offset #4
    //   Java source line #445	-> byte code offset #14
    //   Java source line #447	-> byte code offset #22
    //   Java source line #449	-> byte code offset #29
    //   Java source line #451	-> byte code offset #34
    //   Java source line #453	-> byte code offset #57
    //   Java source line #454	-> byte code offset #65
    //   Java source line #456	-> byte code offset #70
    //   Java source line #461	-> byte code offset #72
    //   Java source line #462	-> byte code offset #76
    //   Java source line #464	-> byte code offset #81
    //   Java source line #458	-> byte code offset #89
    //   Java source line #461	-> byte code offset #91
    //   Java source line #462	-> byte code offset #95
    //   Java source line #464	-> byte code offset #100
    //   Java source line #466	-> byte code offset #105
    //   Java source line #461	-> byte code offset #108
    //   Java source line #462	-> byte code offset #112
    //   Java source line #464	-> byte code offset #117
    //   Java source line #468	-> byte code offset #122
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	f	File
    //   0	124	1	bs	byte[]
    //   1	122	2	b	boolean
    //   3	106	3	out	java.io.DataOutputStream
    //   68	3	4	e	Exception
    //   89	17	5	localObject	Object
    //   79	3	6	e2	Exception
    //   98	3	6	e2	Exception
    //   115	3	6	e2	Exception
    // Exception table:
    //   from	to	target	type
    //   4	65	68	java/lang/Exception
    //   72	76	79	java/lang/Exception
    //   4	72	89	finally
    //   91	95	98	java/lang/Exception
    //   108	112	115	java/lang/Exception
  }
  
  /* Error */
  public static boolean write(File f, String content)
  {
	return false;
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 89	java/io/DataOutputStream
    //   7: dup
    //   8: new 91	java/io/BufferedOutputStream
    //   11: dup
    //   12: new 93	java/io/FileOutputStream
    //   15: dup
    //   16: aload_0
    //   17: invokespecial 95	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   20: invokespecial 96	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   23: invokespecial 99	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   26: astore_3
    //   27: aload_3
    //   28: aload_1
    //   29: invokevirtual 361	java/io/DataOutputStream:writeBytes	(Ljava/lang/String;)V
    //   32: goto +33 -> 65
    //   35: astore 4
    //   37: iconst_0
    //   38: istore_2
    //   39: aload_3
    //   40: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   43: goto +31 -> 74
    //   46: astore 6
    //   48: goto +26 -> 74
    //   51: astore 5
    //   53: aload_3
    //   54: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   57: goto +5 -> 62
    //   60: astore 6
    //   62: aload 5
    //   64: athrow
    //   65: aload_3
    //   66: invokevirtual 115	java/io/DataOutputStream:close	()V
    //   69: goto +5 -> 74
    //   72: astore 6
    //   74: iload_2
    //   75: ireturn
    // Line number table:
    //   Java source line #480	-> byte code offset #0
    //   Java source line #482	-> byte code offset #2
    //   Java source line #486	-> byte code offset #4
    //   Java source line #488	-> byte code offset #27
    //   Java source line #489	-> byte code offset #32
    //   Java source line #491	-> byte code offset #37
    //   Java source line #496	-> byte code offset #39
    //   Java source line #497	-> byte code offset #43
    //   Java source line #493	-> byte code offset #51
    //   Java source line #496	-> byte code offset #53
    //   Java source line #497	-> byte code offset #57
    //   Java source line #500	-> byte code offset #62
    //   Java source line #496	-> byte code offset #65
    //   Java source line #497	-> byte code offset #69
    //   Java source line #502	-> byte code offset #74
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	76	0	f	File
    //   0	76	1	content	String
    //   1	74	2	b	boolean
    //   3	63	3	out	java.io.DataOutputStream
    //   35	3	4	e	Exception
    //   51	12	5	localObject	Object
    //   46	1	6	localException1	Exception
    //   60	1	6	localException2	Exception
    //   72	1	6	localException3	Exception
    // Exception table:
    //   from	to	target	type
    //   4	32	35	java/lang/Exception
    //   39	43	46	java/lang/Exception
    //   4	39	51	finally
    //   53	57	60	java/lang/Exception
    //   65	69	72	java/lang/Exception
  }
  
  public static boolean zipDir(String src, String dest)
  {
    boolean result = false;
    File srcDir = new File(src);
    File destFile = new File(dest);
    Zip zip = new Zip();
    zip.setBasedir(srcDir);
    
    zip.setDestFile(destFile);
    Project p = new Project();
    p.setBaseDir(srcDir);
    zip.setProject(p);
    zip.execute();
    if (destFile.exists()) {
      result = true;
    }
    System.out.println(new File(dest).getAbsolutePath() + " ziped");
    return result;
  }
  
  public static void unzip(String zipFilePath, File outputFile)
    throws Exception
  {
    ZipFile zipFile = new ZipFile(zipFilePath);
    
    Enumeration e = zipFile.getEntries();
    
    ZipEntry zipEntry = null;
    
    mkdir(outputFile);
    while (e.hasMoreElements())
    {
      zipEntry = (ZipEntry)e.nextElement();
      if (zipEntry.getName().indexOf("META-INF") <= -1) {
        if (zipEntry.isDirectory())
        {
          String name = zipEntry.getName();
          
          name = name.substring(0, name.length() - 1);
          
          File f = new File(outputFile, name);
          
          mkdir(f);
        }
        else
        {
          String fileName = zipEntry.getName();
          fileName = fileName.replace('\\', '/');
          if (fileName.indexOf("/") != -1)
          {
            File subDir = new File(outputFile, fileName.substring(0, fileName.lastIndexOf("/")));
            
            mkdir(subDir);
          }
          InputStream in = null;
          FileOutputStream out = null;
          try
          {
            File f = new File(outputFile, fileName);
            
            f.createNewFile();
            in = zipFile.getInputStream(zipEntry);
            out = new FileOutputStream(f);
            
            byte[] by = new byte['?'];
            int c;
            while ((c = in.read(by)) != -1)
            {
              //int c;
              out.write(by, 0, c);
            }
            out.close();
            in.close();
          }
          catch (Exception e0)
          {
            try
            {
              if (in != null) {
                in.close();
              }
            }
            catch (Exception localException1) {}
            try
            {
              if (out != null) {
                out.close();
              }
            }
            catch (Exception localException2) {}
          }
        }
      }
    }
    zipFile.close();
  }
  
  private static void mkdir(File dir)
  {
    if (!dir.getParentFile().exists()) {
      mkdir(dir.getParentFile());
    }
    if (!dir.exists()) {
      dir.mkdir();
    }
  }
  
  public MappedByteBuffer getFileMapBuffer(String filename)
    throws Exception
  {
    RandomAccessFile RAFile = new RandomAccessFile(filename, "rw");
    
    FileChannel fc = RAFile.getChannel();
    
    int size = (int)fc.size();
    
    MappedByteBuffer mapBuf = fc.map(FileChannel.MapMode.READ_WRITE, 0L, size);
    
    int mode = mapBuf.getInt();
    
    return mapBuf;
  }
  
  public static String getCurrentClassPath()
  {
    Class c = FileUtility.class;
    
    String strClassName = c.getName();
    
    String strClassFileName = strClassName.substring(strClassName.lastIndexOf(".") + 1, strClassName.length()) + ".class";
    
    URL u = c.getResource(strClassFileName);
    
    String strURL = u.toString();
    
    strURL = strURL.replaceAll("%20", " ");
    
    strURL = strURL.substring(strURL.indexOf("file:/") + 6);
    if (strURL.indexOf(":") < 0) {
      strURL = "/" + strURL;
    }
    String s = strClassName.replace('.', '/') + ".class";
    if (strURL.endsWith(s)) {
      strURL = strURL.substring(0, strURL.lastIndexOf(s));
    }
    return strURL;
  }
  
  public static Class convertJavaClass(File classFile)
  {
    return null;
  }
  
  /* Error */
  public static List getClassListFromJar(String zipfilePath, String class_package)
  {
	return null;
    // Byte code:
    //   0: new 30	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 32	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: new 403	org/apache/tools/zip/ZipFile
    //   13: dup
    //   14: aload_0
    //   15: invokespecial 405	org/apache/tools/zip/ZipFile:<init>	(Ljava/lang/String;)V
    //   18: astore_3
    //   19: aload_3
    //   20: invokevirtual 406	org/apache/tools/zip/ZipFile:getEntries	()Ljava/util/Enumeration;
    //   23: astore 4
    //   25: aconst_null
    //   26: astore 5
    //   28: goto +226 -> 254
    //   31: aload 4
    //   33: invokeinterface 412 1 0
    //   38: checkcast 418	org/apache/tools/zip/ZipEntry
    //   41: astore 5
    //   43: aload 5
    //   45: invokevirtual 427	org/apache/tools/zip/ZipEntry:isDirectory	()Z
    //   48: ifne +206 -> 254
    //   51: aload 5
    //   53: invokevirtual 420	org/apache/tools/zip/ZipEntry:getName	()Ljava/lang/String;
    //   56: astore 6
    //   58: aload 6
    //   60: bipush 92
    //   62: bipush 47
    //   64: invokevirtual 434	java/lang/String:replace	(CC)Ljava/lang/String;
    //   67: astore 6
    //   69: aload 6
    //   71: ldc_w 509
    //   74: invokevirtual 298	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   77: ifeq +177 -> 254
    //   80: aload 6
    //   82: iconst_0
    //   83: aload 6
    //   85: ldc_w 509
    //   88: invokevirtual 440	java/lang/String:lastIndexOf	(Ljava/lang/String;)I
    //   91: invokevirtual 430	java/lang/String:substring	(II)Ljava/lang/String;
    //   94: astore 6
    //   96: aload 6
    //   98: ldc_w 438
    //   101: ldc_w 507
    //   104: invokevirtual 522	java/lang/String:replaceAll	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   107: astore 6
    //   109: aload_1
    //   110: ifnull +84 -> 194
    //   113: ldc -72
    //   115: aload_1
    //   116: invokevirtual 545	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   119: ifne +75 -> 194
    //   122: aload 6
    //   124: aload_1
    //   125: invokevirtual 548	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   128: ifeq +126 -> 254
    //   131: new 551	java/util/HashMap
    //   134: dup
    //   135: invokespecial 553	java/util/HashMap:<init>	()V
    //   138: astore 7
    //   140: aload 7
    //   142: ldc_w 554
    //   145: aload 6
    //   147: invokeinterface 556 3 0
    //   152: pop
    //   153: aload 7
    //   155: ldc_w 562
    //   158: new 192	java/lang/StringBuilder
    //   161: dup
    //   162: invokespecial 564	java/lang/StringBuilder:<init>	()V
    //   165: aload 5
    //   167: invokevirtual 565	org/apache/tools/zip/ZipEntry:getCrc	()J
    //   170: invokevirtual 568	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   173: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: invokeinterface 556 3 0
    //   181: pop
    //   182: aload_2
    //   183: aload 7
    //   185: invokeinterface 44 2 0
    //   190: pop
    //   191: goto +63 -> 254
    //   194: new 551	java/util/HashMap
    //   197: dup
    //   198: invokespecial 553	java/util/HashMap:<init>	()V
    //   201: astore 7
    //   203: aload 7
    //   205: ldc_w 554
    //   208: aload 6
    //   210: invokeinterface 556 3 0
    //   215: pop
    //   216: aload 7
    //   218: ldc_w 562
    //   221: new 192	java/lang/StringBuilder
    //   224: dup
    //   225: invokespecial 564	java/lang/StringBuilder:<init>	()V
    //   228: aload 5
    //   230: invokevirtual 565	org/apache/tools/zip/ZipEntry:getCrc	()J
    //   233: invokevirtual 568	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   236: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   239: invokeinterface 556 3 0
    //   244: pop
    //   245: aload_2
    //   246: aload 7
    //   248: invokeinterface 44 2 0
    //   253: pop
    //   254: aload 4
    //   256: ifnull +61 -> 317
    //   259: aload 4
    //   261: invokeinterface 449 1 0
    //   266: ifne -235 -> 31
    //   269: goto +48 -> 317
    //   272: astore 4
    //   274: aload 4
    //   276: invokevirtual 355	java/lang/Exception:printStackTrace	()V
    //   279: aload_3
    //   280: ifnull +52 -> 332
    //   283: aload_3
    //   284: invokevirtual 452	org/apache/tools/zip/ZipFile:close	()V
    //   287: goto +5 -> 292
    //   290: astore 9
    //   292: aconst_null
    //   293: astore_3
    //   294: goto +38 -> 332
    //   297: astore 8
    //   299: aload_3
    //   300: ifnull +14 -> 314
    //   303: aload_3
    //   304: invokevirtual 452	org/apache/tools/zip/ZipFile:close	()V
    //   307: goto +5 -> 312
    //   310: astore 9
    //   312: aconst_null
    //   313: astore_3
    //   314: aload 8
    //   316: athrow
    //   317: aload_3
    //   318: ifnull +14 -> 332
    //   321: aload_3
    //   322: invokevirtual 452	org/apache/tools/zip/ZipFile:close	()V
    //   325: goto +5 -> 330
    //   328: astore 9
    //   330: aconst_null
    //   331: astore_3
    //   332: aload_2
    //   333: areturn
    // Line number table:
    //   Java source line #687	-> byte code offset #0
    //   Java source line #689	-> byte code offset #8
    //   Java source line #693	-> byte code offset #10
    //   Java source line #695	-> byte code offset #19
    //   Java source line #697	-> byte code offset #25
    //   Java source line #699	-> byte code offset #28
    //   Java source line #701	-> byte code offset #31
    //   Java source line #703	-> byte code offset #43
    //   Java source line #705	-> byte code offset #51
    //   Java source line #707	-> byte code offset #58
    //   Java source line #709	-> byte code offset #69
    //   Java source line #711	-> byte code offset #80
    //   Java source line #712	-> byte code offset #96
    //   Java source line #714	-> byte code offset #109
    //   Java source line #716	-> byte code offset #122
    //   Java source line #718	-> byte code offset #131
    //   Java source line #719	-> byte code offset #140
    //   Java source line #720	-> byte code offset #153
    //   Java source line #722	-> byte code offset #182
    //   Java source line #724	-> byte code offset #191
    //   Java source line #726	-> byte code offset #194
    //   Java source line #727	-> byte code offset #203
    //   Java source line #728	-> byte code offset #216
    //   Java source line #730	-> byte code offset #245
    //   Java source line #699	-> byte code offset #254
    //   Java source line #735	-> byte code offset #269
    //   Java source line #737	-> byte code offset #274
    //   Java source line #740	-> byte code offset #279
    //   Java source line #744	-> byte code offset #283
    //   Java source line #745	-> byte code offset #287
    //   Java source line #749	-> byte code offset #292
    //   Java source line #739	-> byte code offset #297
    //   Java source line #740	-> byte code offset #299
    //   Java source line #744	-> byte code offset #303
    //   Java source line #745	-> byte code offset #307
    //   Java source line #749	-> byte code offset #312
    //   Java source line #751	-> byte code offset #314
    //   Java source line #740	-> byte code offset #317
    //   Java source line #744	-> byte code offset #321
    //   Java source line #745	-> byte code offset #325
    //   Java source line #749	-> byte code offset #330
    //   Java source line #753	-> byte code offset #332
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	334	0	zipfilePath	String
    //   0	334	1	class_package	String
    //   7	326	2	l	List
    //   9	323	3	theZipFile	ZipFile
    //   23	237	4	e	Enumeration
    //   272	3	4	ex	Exception
    //   26	203	5	zipEntry	ZipEntry
    //   56	153	6	fileName	String
    //   138	46	7	m	java.util.Map
    //   201	46	7	m	java.util.Map
    //   297	18	8	localObject	Object
    //   290	1	9	localException1	Exception
    //   310	1	9	localException2	Exception
    //   328	1	9	localException3	Exception
    // Exception table:
    //   from	to	target	type
    //   10	269	272	java/lang/Exception
    //   283	287	290	java/lang/Exception
    //   10	279	297	finally
    //   303	307	310	java/lang/Exception
    //   321	325	328	java/lang/Exception
  }
}

