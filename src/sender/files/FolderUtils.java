package sender.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	public static void main(String[] args){
		FolderUtils fus = new FolderUtils();
		fus.listFiles("e:\\ws");
		
	}
}
