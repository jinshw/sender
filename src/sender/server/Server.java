package sender.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
  
/**
 * 服务器
 */
public class Server extends ServerSocket{
  
    private static final int PORT =2013;
      
    private ServerSocket server;
    private Socket client;
    private DataInputStream dis;
    private FileOutputStream fos;
    private String receivePath;
      
    public Server()throws Exception{
    	receivePath = "E:/filepath/server/";
        try {
            
                server =new ServerSocket(PORT);
                  
                while(true){
                	try {
                    client = server.accept();
                      
                    dis =new DataInputStream(client.getInputStream());
                    //文件名和长度
                    String fileName = dis.readUTF();
                    long fileLength = dis.readLong();
                    
                    fos =new FileOutputStream(new File(receivePath + "/" + fileName));//拼接地址时多个“/”可以防止配置文件夹路径后少个"/"
                      
                    byte[] sendBytes =new byte[1024];
                    int transLen =0;
                    System.out.println("----开始接收文件<" + fileName +">,文件大小为<" + fileLength +">----");
                    while(true){
                        int read =0;
                        read = dis.read(sendBytes);
                        if(read == -1)
                            break;
                        transLen += read;
                        System.out.println("接收文件进度" +100 * transLen/fileLength +"%...");
                        fos.write(sendBytes,0, read);
                        fos.flush();
                    }
                    System.out.println("----接收文件<" + fileName +">成功-------");
                    
                    
                	}catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if(dis !=null)
                            dis.close();
                        if(fos !=null)
                            fos.close();
                        client.close();
                    }
                }
            
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
        	server.close();
        }
    }
      
    public static void main(String[] args)throws Exception {
        new Server();
    }
}
