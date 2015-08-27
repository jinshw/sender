package sender.entity;

public class Target {
	private String name = "";
	private String protocol = "";
	private String port = "";
	private String ip = "";
	
	//扩展字段
	private String sourePath = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getSourePath() {
		return sourePath;
	}
	public void setSourePath(String sourePath) {
		this.sourePath = sourePath;
	}
	
}
