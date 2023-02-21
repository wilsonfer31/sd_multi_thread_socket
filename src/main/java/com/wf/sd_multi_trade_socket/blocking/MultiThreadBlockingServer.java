package com.wf.sd_multi_trade_socket.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadBlockingServer extends Thread{

    int clientCounter;

    public static void main(String[] args) {
        new MultiThreadBlockingServer().start();
    }

    @Override
    public void run() {
        try {
            System.out.println("The server is started using port 1234");
            ServerSocket serverSocket = new ServerSocket(1234);
            while(true){
                Socket socket = serverSocket.accept();
                ++clientCounter;
                new Conversation(socket, clientCounter).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class Conversation extends Thread{

        private Socket socket;
        private int clientId;


        public Conversation(Socket socket, int clientId){
            this.clientId = clientId;
            this.socket = socket;
        }

        @Override
        public void run() {

            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                System.out.println("New Client Connection -> " + clientId + "IP=" + socket.getRemoteSocketAddress().toString());
                pw.println("Welcome , you are the client : " + clientId);

                while(true){
                    String request = br.readLine();
                    System.out.println("New Request -> " + clientId + "IP=" + socket.getRemoteSocketAddress().toString() +" Request :" + request);
                    String response = "Size = " + request.length();
                    pw.println(response);
                }

            } catch (IOException e) {
              throw new RuntimeException(e);
            }


        }
    }
}
