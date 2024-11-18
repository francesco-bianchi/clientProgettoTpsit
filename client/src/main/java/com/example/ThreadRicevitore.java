package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadRicevitore extends Thread {
    Socket s0;
    String altroUtente;
    BufferedReader in;
    DataOutputStream out;
    Boolean flag;

    public ThreadRicevitore(Socket s0, String altroUtente) {
        this.s0 = s0;
        this.altroUtente = altroUtente;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }



    @Override
    public void run() {
        try 
        {

            in = new BufferedReader(new InputStreamReader(s0.getInputStream()));
            out = new DataOutputStream(s0.getOutputStream());
            flag = true;


            while (flag) {
                String msg;
                if ((msg= in.readLine())!=null) { // Verifica se ci sono dati disponibili per la lettura
                    
                    if (msg.equals("KO")) {
                        System.out.println("Destinatario non trovato, uscire dalla chat digitando '/EXIT'");
                    }else{
                        if (msg.split(":")[0].equals(altroUtente)) {
                            System.out.println(msg);
                        }
                    }
                } 
            }
            
            
         } catch (IOException e) {
            
            e.printStackTrace(); // Stampa il problema se si verifica in circostanze inattese
        }
    }
}