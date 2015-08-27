package sender;

import java.io.File;

import org.apache.log4j.FileAppender;

import sender.files.FileUtils;
import sender.monitor.ZJPFileListener;
import sender.monitor.ZJPFileMonitor;
import sender.server.Client;

public class SenderMain {

	public static void main(String[] args) {
		
		
		try {
			//1、监控服务器发布文件夹，如果有新发布的作品，则打包复制到缓存文件夹中
			ZJPFileMonitor m;
			m = new ZJPFileMonitor(1000);
			m.monitor("E:/filepath/code",new ZJPFileListener());
			m.start();
			
			/**
			 * 2、监控缓存文件夹（待传输文件），如有文件进行socket接口传递
			 */
			
			//缓存文件夹路径
			String tmpFolderPath = "E:/filepath/bak";
			//缓存文件夹
			File tmpFolder = new File(tmpFolderPath);
			//缓存的文件数组
			File[] fileArr = null;
			
			//socket接口客户端
			Client ct = new Client();
			
			//死循环遍历缓存文件夹
			while(true){
				//获取问价中所有文件的列表
				fileArr = tmpFolder.listFiles();
				//循环调用socket接口传递文件
				for(int i=0;i<fileArr.length;i++){
					System.out.println("文件=="+fileArr[i].getAbsolutePath());
					
					//调用socket接口
					if(ct.sendFile(fileArr[i].getAbsolutePath())){
						//如果调用socket接口发送成功，删除缓存文件夹中文件（注：现只实现向单个终端发送，如果向多终端发送时，需要在考虑）
						FileUtils.deleteFile(fileArr[i]);
					}
					System.out.println("文件"+fileArr[i].getAbsolutePath()+"调用socket接口成功!");
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
