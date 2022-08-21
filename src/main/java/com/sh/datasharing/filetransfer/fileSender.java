package com.sh.datasharing.filetransfer;

import java.io.*;
import java.net.Socket;

public class fileSender {
    public static void send() throws IOException {
        //创建原图片路径的对象
        FileInputStream fileInputStream = new FileInputStream("/home/ubuntu/Templates/sampleImg.jpg");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        //建立一个Socket对象, 10086是服务端监听的端口
        Socket socket = new Socket("127.0.0.1",10086);

        OutputStream socketOut = socket.getOutputStream();
        BufferedOutputStream bufferSocketOut = new BufferedOutputStream(socketOut);

        //把文件数据通过socket传输
        byte[] data = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(data)) != -1){
            bufferSocketOut.write(data,0,len);
        }

        //刷新缓冲流中的缓冲
        bufferSocketOut.flush();
        //单方面关闭 socket输出流
        socket.shutdownOutput();


        //用于接收服务器端的反馈
        byte[] byteBuffer = new byte[1024];
        InputStream is = socket.getInputStream();
        len = is.read(byteBuffer);
        System.out.println(new String(byteBuffer,0,len));

        socket.close();
        bufferedInputStream.close();
    }
}
