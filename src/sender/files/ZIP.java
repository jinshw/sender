package sender.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
/**
 * 功能介绍：
 * 给定一个压缩包，从中找出目标文件，并进行修改
 * 要求是要遍历压缩包中所有的文件，当然就要递归解压缩子文件压缩包
 * 修改完文件之后，要把文件重新打包
 */
public class ZIP {
 
 public static void main(String args[]){
	  ZIP zip = new ZIP();
	  try {
	   zip.release(new File("e:\\项目22.zip"));
	  } catch (IOException e) {
	   e.printStackTrace();
	  }
 }

 /**
  * 循环递归解压缩
  * @param file  带解压缩的文件
  * @throws IOException
  * @throws DocumentException
  */
 @SuppressWarnings("unchecked")
 public  void release(File file) throws IOException{
  //只解析后缀为.rar的包
        if(file.getName().endsWith(".zip")){
      ZipFile zipFile = new ZipFile(file);
      //得到所有的ZipEntry，进行遍历
            Enumeration zEnumeration=zipFile.getEntries();
            ZipEntry zipEntry=null;
            while(zEnumeration.hasMoreElements()){
              zipEntry=(ZipEntry)zEnumeration.nextElement();
              if(zipEntry.isDirectory()){
               //如果是压缩包的子文件是文件夹，代码生成应该的文件夹
               unZipDir(file,zipEntry.getName());
              }else{
                 //如果子文件是压缩包就进行解压缩
               release(file, zipEntry, zipFile);
              }
            }
            zipFile.close();
            //遍历解压后的文件夹，继续解压，递归
            String zipFullName = file.getPath();
            String zipName = zipFullName.substring(0,zipFullName.lastIndexOf("."));
            File zipDirFile = new File(zipName);
            File[] subFile = zipDirFile.listFiles();
            for(int i = 0; i < subFile.length; i++){
             release(subFile[i]);
            }
            //删除旧的压缩文件
            file.delete();  
            //重新压缩文件
//            zipDir(zipDirFile,file);
            //删除解压后的文件夹
//            deleteFile(zipDirFile);
        }else if(file.isDirectory()){
         File[] subFile = file.listFiles();
         for(int i = 0; i < subFile.length; i++){
          release(subFile[i]);
         }
        }else{
         if(file.getName().equalsIgnoreCase("XmlStructure.xml")){
          //TODO:找到目标文件进行修改
         }
        }
 }
 
 /**
  * 
  * @param file
  * @param zipEntry
  * @param zipFile
  * @throws IOException
  */
 public void release(File file,ZipEntry zipEntry,ZipFile zipFile) throws IOException{
     byte[] buf=new byte[1024];
        OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFile(file.getPath(), zipEntry.getName())));
        InputStream is=new BufferedInputStream(zipFile.getInputStream(zipEntry));
        int readLen=0;
        while ((readLen=is.read(buf, 0, 1024))!=-1) {
         os.write(buf, 0, readLen);
        }
        is.close();
        os.close();
 }
 
 /**
  * 如果要解压缩的单元是一个文件夹
  * @param unZipFile
  * @param subDir
  */
 public void unZipDir(File unZipFile,String subDir){
  String dir = subDir.substring(0,subDir.lastIndexOf("/"));
  String unZipFileName = unZipFile.getPath();
  unZipFileName = unZipFileName.substring(0, unZipFileName.lastIndexOf("."));
  File file = new File(unZipFileName, dir);
  if(!file.exists()){
   file.mkdirs();
  }
 }
 
 /**
  * 如果获取的文件名是parentfile/file.txt，就需要先创建文件夹parentfile,然后解压缩出文件file.txt
  * @param baseDir 别解压缩的文件全路径  d:/file/unzipfile.epa
  * @param absFileName 可能是一个文件名 filename,也可能是一个带父文件夹的文件名 subfile/subzip
  * @return
  */
    public File getRealFile(String baseDir, String fileName) {
        String[] dirs = fileName.split("/");
        //
        File ret = new File(baseDir.substring(0,baseDir.lastIndexOf(".")));
        if (dirs.length > 1) {
         for (int i = 0; i < dirs.length - 1; i++) {
          ret = new File(ret, dirs[i]);
         }
        }
        if (!ret.exists()) {
         ret.mkdirs();
        }
        return new File(ret, dirs[dirs.length - 1]);
    }
    
    /** 
     * 压缩文件夹
     * @param sourceFile   被压缩的文件夹
     * @param zipFile  压缩后的文件，默认与被压缩文件同路径
     * @throws IOException 
     */
    public void zipDir(File sourceFile, File zipFile) throws IOException {   
        try {   
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFile));   
            ZipOutputStream zos = new ZipOutputStream(bos);   
            zipFile(zos,sourceFile,""); 
            zos.close();
        } catch (FileNotFoundException e) {   
           throw e;
        } catch (IOException e) {
         throw e;
        }   
    }   
    
    /**
     * 
     * @param out  压缩文件的输出流
     * @param f  被压缩的文件
     * @param base  
     * @throws IOException 
     * @throws Exception
     */
    public void zipFile(ZipOutputStream out, File f, String base) throws IOException{
        if (f.isDirectory()) {
           File[] fl = f.listFiles();
           //直接压缩一个空文件夹
           if(fl.length == 0 && base.length() == 0){
            //加一个反斜线 "/"，表示新增的压缩节点是一个文件夹
            out.putNextEntry(new ZipEntry(f.getName() + "/"));
           }else{
            //增加一个压缩节点
               out.putNextEntry(new ZipEntry(base + "/"));
               base = base.length() == 0 ? "" : base + "/";
               for (int i = 0; i < fl.length; i++) {
                zipFile(out, fl[i], base + fl[i].getName());
               }            
           }
        }else {
           //如果直接压缩一个文件的时候，需要用到下面的一个判断。比如待压缩的文件为->d:/zip/2ewq3.txt
           base = base.length() == 0?f.getName():base;
           //增加一个压缩节点
           out.putNextEntry(new ZipEntry(base));
           BufferedInputStream bin = new BufferedInputStream(new FileInputStream(f));
           byte[] byteArray = new byte[1024];
           int len = 0;
           while ( (len = bin.read(byteArray)) != -1) {
            out.write(byteArray,0,len);
           }
           bin.close();
       }
    } 
 
 /**
  * 删除文件或者文件夹
  * @param dirFile
  */
 public void deleteFile(File dirFile){
	  if(dirFile.isDirectory()){
	   File[] subFileList = dirFile.listFiles();
	   for(int i = 0; i < subFileList.length; i++){
	    deleteFile(subFileList[i]);
	   }
	   dirFile.delete();
	  }else{
	   dirFile.delete();
	  }
 }
}