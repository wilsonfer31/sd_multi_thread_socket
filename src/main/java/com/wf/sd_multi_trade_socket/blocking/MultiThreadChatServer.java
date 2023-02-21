package com.wf.sd_multi_trade_socket.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiThreadChatServer extends Thread{

    private List<Conversation> conversations = new ArrayList<>();
    int clientCounter;

    public static void main(String[] args) {
        new MultiThreadChatServer().start();
    }

    @Override
    public void run() {
        try {
            System.out.println("The server is started using port 1234");
            ServerSocket serverSocket = new ServerSocket(1234);
            while(true){
                Socket socket = serverSocket.accept();
                ++clientCounter;
              Conversation conversation =  new Conversation(socket, clientCounter);
              conversations.add(conversation);
              conversation.start();
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

                    List<Integer> clientsTo = new ArrayList<>();
                    String message;
                    if(request.contains("=>")){
                        String[] items= request.split("=>");
                        String clients=items[0];
                        message = items[1];
                        if(clients.contains(",")){
                            String[] clientsId = clients.split(",");
                            for (String id : clientsId){
                                clientsTo.add(Integer.parseInt(id));
                            }
                        }else{
                            clientsTo.add(Integer.parseInt(clients));
                        }

                    }else{
                        clientsTo = conversations.stream().map(c -> c.clientId).collect(Collectors.toList());
                        message = request;
                    }


                    broadCastMessage(message, this, clientsTo);
                }

            } catch (IOException e) {
              throw new RuntimeException(e);
            }


        }
    }


    public void broadCastMessage(String message, Conversation from, List<Integer> clients){
        try {
            for (Conversation conversation : conversations) {
                if(conversation != from && clients.contains(conversation.clientId)){
                    Socket socket = conversation.socket;
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(message);
                }

            }
        }catch (IOException e) {
               throw  new RuntimeException(e);
            }


        }

}
