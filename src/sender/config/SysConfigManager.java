/**
 * 系统配置管理类,负责系统整个配置的读取、缓存、修改
 * @author	[ChenTianCao]
 * @version	[cms1.0, 2014-04-03]
 * @since	[cms1.0] 
 */
package sender.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sender.entity.Target;

//import com.rr.h5.entity.system.SysConfig;
//import com.rr.h5.utils.StringValueUtils;


public class SysConfigManager {
	
	private static Logger logger = LoggerFactory.getLogger(SysConfigManager.class);

	private static SysConfigManager instance;

	private SAXReader reader = null;
	private String encode = "UTF-8";
	
	/**
	 * 是否制作工具权限
	 */
	private Boolean licTopicTool=null;
	


	/** 系统整体配置xml对象 */
	private Document document;

	/**
	 * 系统部署路径，其它功能模板调用该属性获取当前部署目录
	 */
	private String SysRootPath;

	public void setSysRootPath(String sysRootPath) {
		SysRootPath = sysRootPath;
	}

	private SysConfigManager() {
			init();
	}

	public static SysConfigManager getInstance() {
		if (instance == null) {
			instance = new SysConfigManager();
		}
		return instance;
	}
	
	/**
	 * 初始化
	 */
	private void init()  {
		FileInputStream fis = null;
		InputStreamReader isr  =null;
		try {
			fis = new FileInputStream(new File(getConfigDir()));
			isr = new InputStreamReader(fis, encode);
			reader = new SAXReader();
//			document = reader.read(isr);
			document = reader.read(new File(getConfigDir()));
		} catch (Exception e) {
			logger.error("初始化系统配置内容时出错", e);
		}finally{
				try {
					isr.close();
					fis.close();
				} catch (IOException e) {
					logger.error("关闭配置文件流出错",e);
				}
		}
	}

	/**
	 * 获取配置文件路径
	 * @return
	 * @throws Exception
	 */
	private String getConfigDir() throws Exception{
//		String configPath = this.getSysRootPath() + "/WEB-INF"
//				+ File.separatorChar + "RRConfig.xml";
		
		File fs = new File("");
		String configPath = fs.getAbsolutePath() + "/config/config.xml";
		return configPath;
	}


	
	/**
	 * 向外公开获取系统配置方法，传入配置路径
	 * 
	 * @param xPath 节点路径
	 * @return
	 * @throws Exception
	 */
	public String getProperty(String xPath) throws Exception {
		String result = null;
		try {
			if (document == null) {
				init();
			}
			Element ele = (Element) document.selectSingleNode(xPath);
			if (ele != null) {
				result = ele.getText();
			}
		} catch (Exception e) {
			logger.error("获取指定配置时出错", e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 向外公开获取系统配置方法，传入配置路径
	 * 
	 * @param xPath 节点路径
	 * @return  返回整型
	 * @throws Exception
	 */
	public int getPropertyToInt(String xPath) throws Exception {
		int result=0;
		String str=getProperty(xPath);
		if(str!=null&&str.length()>0){
			result=Integer.parseInt(str);
		}
		return result;
	}
	
	/**
	 * 获取指定节点下所有节点集合
	 * @param xPath 节点路径
	 * @return
	 * @throws Exception
	 */
	public List getElementList(String xPath) throws Exception{
		List list = new ArrayList();
		try {
			if (document == null) {
				init();
			}
			Element ele = (Element) document.selectSingleNode(xPath);
			list = ele.elements();
		} catch (Exception e) {
			logger.error("获取指定配置时出错", e);
			throw e;
		}
		return list;
	}
	
	
	/**
	 * 获取Send-Mapping集合
	 * @return List 
	 * @throws Exception
	 */
	public List getSenderMappingList() throws Exception{
		List rlist = new ArrayList();
		List sm = getElementList("/rdkl-trans/sender");
		Iterator smi = sm.iterator();
		Element ele = null;
		String targetName = "";
		String sourceName = "";
		Map targetMap = getTargetMap();
		Map sourceMap = getSourceMap();
		
		Target target = null;
		String sourcePath = "";
		while(smi.hasNext()){
			ele = (Element) smi.next();
			if(ele.getName().equals("sender-mapping")){
				
				targetName = ele.element("target-name").getTextTrim();
				sourceName = ele.element("source-name").getTextTrim();
				
				System.out.println("targetName="+targetName+"  sourceName="+sourceName);
				
				
				if( (!"".equals(targetName) && targetName!=null) &&
						(!"".equals(sourceName) && sourceName!=null) ){
					//获取target-name数据对象
					target = (Target) targetMap.get(targetName);
					//获取source-name数据对象
					sourcePath = (String) sourceMap.get(sourceName);
					if(!"".equals(sourcePath) && null != sourcePath){
						if(null != target){
							target.setSourePath(sourcePath);
							rlist.add(target);
						}else{
							logger.info("没有对应<target-name>，请检查配置文件！");
						}
					}else{
						logger.info("没有对应<source-name>，请检查配置文件！");
					}
				}else{
					logger.info("<target-name> 或 <source-name>不能为空！");
				}
				
			}
		}
		
		return rlist;
	}
	
	/**
	 * 获取source集合Map
	 * @return
	 * @throws Exception
	 */
	public Map getSourceMap() throws Exception{
		List sm = getElementList("/rdkl-trans/sender");
		Iterator smi = sm.iterator();
		Element ele = null;
		String sourceName = "";
		String sourcePath = "";
		Map map = new HashMap();
		while(smi.hasNext()){
			ele = (Element) smi.next();
			if(ele.getName().equals("source")){
				
				sourceName = ele.element("name").getTextTrim();
				sourcePath = ele.element("source-path").getTextTrim();
				
				System.out.println("sourceName="+sourceName+"  sourcePath-path="+sourcePath);
				
				
				if( (!"".equals(sourceName) && sourceName!=null) &&
						(!"".equals(sourcePath) && sourcePath!=null) ){
					//获取target-name数据对象
					map.put(sourceName, sourcePath);
				}else{
					logger.info("<target-name> 或 <sourcePath>不能为空！");
				}
				
			}
		}
		
		return map;
	}
	
	/**
	 * 获取TargaetMap集合
	 * @return
	 * @throws Exception
	 */
	public Map getTargetMap() throws Exception{
		List sm = getElementList("/rdkl-trans/sender");
		Iterator smi = sm.iterator();
		Element ele = null;
		String targetName = "";
		String protocol = "";
		String port = "";
		String ip = "";
		
		Map map = new HashMap();
		Target target = null;
		
		while(smi.hasNext()){
			ele = (Element) smi.next();
			if(ele.getName().equals("target")){
				
				targetName = ele.element("name").getTextTrim();
				protocol = ele.element("protocol").getTextTrim();
				port = ele.element("protocol").getTextTrim();
				ip = ele.element("protocol").getTextTrim();
				
				System.out.println("targetName="+targetName+"  protocol="+protocol+"   port="+port+"  ip="+ip);
				
				
				if( (!"".equals(targetName) && targetName!=null) &&
						(!"".equals(protocol) && protocol!=null) &&
						(!"".equals(port) && port!=null) &&
						(!"".equals(ip) && ip!=null) ){
					
					target = new Target();
					target.setName(targetName);
					target.setProtocol(protocol);
					target.setIp(ip);
					target.setPort(port);
					
					//获取target-name数据对象
					map.put(targetName, target);
				}else{
					logger.info("<target-name> 或 <sourcePath>不能为空！");
				}
				
			}
		}
		return map;
	}
	
	
	public static void main(String[] args) throws Exception{
		File fs = new File("");
		System.out.println(fs.getAbsolutePath());
		String cofigPath = fs.getAbsolutePath() + "/config/config.xml";
		
		String value = SysConfigManager.getInstance().getProperty("/rdkl-trans/sender/source/name");
//		System.out.println("value==="+value);
		
		
//		List list = SysConfigManager.getInstance().getElementList("/rdkl-trans/sender");
//		SysConfigManager.getInstance().getSenderMappingList();
		
		List list = SysConfigManager.getInstance().getSenderMappingList();
		
		System.out.println("list size=="+list.size());
	}

}
