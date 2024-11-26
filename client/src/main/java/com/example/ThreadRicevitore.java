package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadRicevitore extends Thread {
    Socket socket;
    String dest;
    String mitt;
    boolean threadAttivo;
    boolean entrato;

    public ThreadRicevitore(Socket socket,String mitt, String dest) {
        this.socket = socket;
        this.mitt = mitt;
        this.dest = dest;
    }


    public void threadAttivo(boolean threadAttivo) {
        this.threadAttivo = threadAttivo;
    }


    @Override
    public void run() {
        try 
        {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            threadAttivo = true;


            while (threadAttivo) {
                String messServer;
                if (in.ready()) { // Verifica se ci sono dati disponibili per la lettura
                    messServer = in.readLine();
                    String[] msgSplit = messServer.split(":");
                    entrato=false;
                    if (messServer.equals("KO")) {
                        System.out.println("Destinatario non trovato, scrivi /EXIT per uscrire");
                    }
                    else if (messServer.equals("NO")) {
                        System.out.println("   ---   ");
                    }
                    // far vedere la cronologia una volta entrati in chat
                    if(msgSplit[0].equals("CR")){
                        
                        String[] cronologia = msgSplit[1].split(";");
                        String[] mittenti = msgSplit[2].split(";");
                        System.out.println("Cronologia:");
                        for (int i=0; i<cronologia.length; i++) {
                            System.out.println(mittenti[i] + ": " + cronologia[i]); // si visualizzano tutti i messaggi
                        }
                        System.out.println("  ---  ");
                    }
                    // mentre si è in chat
                    else if (msgSplit[0].equals("PRIV")) { // controlla se il thread è il reale destinatario del messaggio o se ha già fatto vedere la cronologia
                        System.out.println(msgSplit[1] + " (in privato): " + msgSplit[2]); //fa vedere da chi arriva il messaggio e il contenuto
                    } 
                    else if(msgSplit[0].equals("ALL")){
                        System.out.println(msgSplit[2] + "(a tutti): " + msgSplit[1]); //fa
                    }
                }
                    
            }
            
            
         } catch (IOException e) {
            
            e.printStackTrace(); // Stampa il problema se si verifica in circostanze inattese
        }
    }
}