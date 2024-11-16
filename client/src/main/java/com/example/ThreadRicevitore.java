package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadRicevitore extends Thread {
    Socket socket;
    Socket socket2;
    boolean ascoltato;

    ThreadRicevitore(Socket socket, Socket socket2) {
        this.socket = socket;
        this.socket2 = socket2;
    }

    public void setAscoltato(boolean ascoltato) {
        this.ascoltato = ascoltato;
    }

    public boolean isAscoltato() {
        return ascoltato;
    }

    public void run() {
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while(ascoltato){

                if((line = in.readLine()) == null){
                    ascoltato = false;
                }
                this.setAscoltato(ascoltato);
                out.writeBytes("MESSAGGIO:" + line);
                System.out.println("MESSAGGIO:" + line);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
