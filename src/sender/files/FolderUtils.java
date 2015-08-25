package sender.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件夹操作工具类
 * @author shw
 *
 */
public class FolderUtils {
	private List listFiles(String folderPath){
		List list = new ArrayList();
		File root = new File(folderPath);
		getAllFiles(root,list);
		
		System.out.println("List length==="+list.size());
		Iterator it=list.iterator();
		File[] fs;
		while(it.hasNext()){
			fs = (File[]) it.next();
			for(int i=0;i<fs.length;i++){
				System.out.println("文件=="+fs[i].getAbsolutePath());
			}
		}
		
		return list;
	}
	
	private void getAllFiles(File dir,List list){
		File[] fs = dir.listFiles();
		for(int i=0;i<fs.length;i++){
			System.out.println("文件=="+fs[i].getAbsolutePath());
			
			if(fs[i].isDirectory()){
				getAllFiles(fs[i],list);
			}
		}
		
		list.add(fs);
	}
	
	/**
	 * 压缩文件夹
	 * @param folderPath 需要压缩的文件夹
	 * @param targetPath 压缩的文件保存的路径
	 */
	public void zipFolder(String folderPath,String targetPath){
		
	}
	
	/**
	 * 删除文件或者文件夹
	 * @param dirFile
	 */
	public void deleteFile(File dirFile){
		if (dirFile.isDirectory()) {
			File[] subFileList = dirFile.listFiles();
			for (int i = 0; i < subFileList.length; i++) {
				deleteFile(subFileList[i]);
			}
			dirFile.delete();
		} else {
			dirFile.delete();
		}
	}
	
	public static void main(String[] args){
		FolderUtils fus = new FolderUtils();
		fus.listFiles("E:\\PKM231218");
		
	}
}
