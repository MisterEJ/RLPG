package org.misterej.game.CODE.chat;

import org.misterej.engine.Window;
import org.misterej.game.CODE.EventManager;
import org.misterej.game.CODE.Events.MessageEvent;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            sendMessage(username);
        } catch (IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(username + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;
                while (socket.isConnected()) {
                    if(Window.shoudlClose) break;
                    try {
                        message = bufferedReader.readLine();
                        EventManager.notify(new MessageEvent(message));
                    } catch (IOException e) {
                        close(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
