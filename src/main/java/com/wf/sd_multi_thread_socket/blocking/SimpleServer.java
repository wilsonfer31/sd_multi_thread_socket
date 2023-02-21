package com.wf.sd_multi_thread_socket.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {

            ServerSocket ss = new ServerSocket(1234);
            System.out.println("New Connection !!!");

            Socket socket = ss.accept();
            InputStream is  = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            System.out.println("Wating data.....");

            int nb =is.read();

            System.out.println("Sending response.....");

            int res = nb * 24;

            os.write(res);

            socket.close();

    }

}
