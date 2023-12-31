package com.example.durak_game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Server {
    private static ServerSocket server;
    private static HashMap<String, Connection> connections = new HashMap<>();

    public static void main(String[] args) {
        try {
            server = new ServerSocket(1234);
            while (true) {
                Socket socket = server.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    String[] data = in.readLine().split("#");
                    String operation = data[0];
                    if (Objects.equals(operation, "create_session")) {
                        String nickname = data[1];
                        String session_id = data[2];
                        Player2 host = new Player2(nickname, session_id);
                        Connection connection = new Connection(in, out, host);
                        connections.put(host.getId(), connection);
                        Thread con = new Thread(connection);
                        System.out.println("Комната была создана..");
                        con.setDaemon(true);
                        con.start();
                        break;
                    }
                    else if (Objects.equals(operation, "join_session")) {
                        System.out.println("Попытка подключения...");
                        String nickname = data[1];
                        String session_id = data[2];
                        Player2 member = new Player2(nickname);
                        if (connections.containsKey(session_id)) {
                            connections.get(session_id).addStreams(in, out, member);
                        }
                        else {
                            out.println("join_session_failed");
                        }
                        break;
                    }
                    else {
                        socket.close();
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error occurred in main in ServerHandler: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
