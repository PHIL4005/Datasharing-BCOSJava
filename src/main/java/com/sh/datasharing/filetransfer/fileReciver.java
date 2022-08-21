package com.sh.datasharing.filetransfer;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class fileReciver {

    public static void supervise() throws IOException {
        //服务端监听端口
        ServerSocket serverSocket = new ServerSocket(10086);
        Socket socket = serverSocket.accept(); //accept到东西之前会一直阻塞
        FileOutputStream fos = new FileOutputStream("/home/ubuntu/Templates/received/sampleImg.jpg"); //服务端文件保存位置

        //获取socket 的输入流
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        //从socket中获取的数据流，写入到服务端本地文件输出中
        byte[] buffer = new byte[1024];
        int length;
        while ((length = bufferedInputStream.read(buffer)) != -1){
            fos.write(buffer,0,length);
        }

        //给客户端回的话
        OutputStream outputStream = socket.getOutputStream();
        InetAddress sendIp = socket.getInetAddress(); //客户端IP
        int port = socket.getPort();   //客户端端口
        String response = "来自" + sendIp + ":" + port + "的图片上传成功";
        outputStream.write(response.getBytes());

        //各种close
        socket.close();
        serverSocket.close();
        fos.close();
    }
}
