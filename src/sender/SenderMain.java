package sender;

import sender.monitor.ZJPFileListener;
import sender.monitor.ZJPFileMonitor;

public class SenderMain {

	public static void main(String[] args) {
		
		
		try {
			//1、监控服务器发布文件夹，如果有新发布的作品，则打包复制到缓存文件夹中
			ZJPFileMonitor m;
			m = new ZJPFileMonitor(1000);
			m.monitor("E:/filepath/code",new ZJPFileListener());
			m.start();
			
			//2、监控缓存文件夹（待传输文件），如有文件进行socket接口传递
			String tmpFolder = "";
			while(true){
				//
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
