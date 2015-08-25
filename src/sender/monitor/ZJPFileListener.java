package sender.monitor;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import sender.files.FileUtils;
import sender.files.ZipCompressorByAnt;

public class ZJPFileListener implements FileAlterationListener{

	
	ZJPFileMonitor monitor = null;
	@Override
	public void onStart(FileAlterationObserver observer) {
		//System.out.println("onStart");
	}
	@Override
	public void onDirectoryCreate(File directory) {
		System.out.println("onDirectoryCreate:" +  directory.getName() + " 路径="+directory.getAbsolutePath());
		//打包
		String zipFolder = directory.getAbsolutePath();
		String zipFile = directory.getAbsolutePath() + ".zip";
		String zipName = directory.getName() + ".zip";
		String targetFile = "E:/filepath/bak/"+zipName;
		ZipCompressorByAnt zba = new ZipCompressorByAnt(zipFile);//压缩后的文件名称
		zba.compressExe(zipFolder);//需要压缩的文件夹
		//复制打包文件到指定路径
		FileUtils.copyFile(zipFile, targetFile);
		//删除打包文件
		FileUtils.deleteFile(zipFile);
	}

	@Override
	public void onDirectoryChange(File directory) {
		System.out.println("onDirectoryChange:" + directory.getName());
	}

	@Override
	public void onDirectoryDelete(File directory) {
		System.out.println("onDirectoryDelete:" + directory.getName());
	}

	@Override
	public void onFileCreate(File file) {
		System.out.println("onFileCreate:" + file.getName());
	}

	@Override
	public void onFileChange(File file) {
		System.out.println("onFileChange : " + file.getName());
	}

	@Override
	public void onFileDelete(File file) {
		System.out.println("onFileDelete :" + file.getName());
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		//System.out.println("onStop");
	}

}

