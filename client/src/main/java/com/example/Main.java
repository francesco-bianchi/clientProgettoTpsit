package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 3000);
        String username;
        String serverUsername;
        String[] scelta;
        String stringaScelta;
        String listaStringa;
        String[] lista;
        String stringM = "";
        String tipoM = "";
        String testoM = "";
        String nomeM = "";
        Scanner scanner = new Scanner(System.in);
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            
            


            do {
                System.out.println("inserire il proprio username:");
                username = scanner.nextLine();
                out.writeBytes(username + "\n");

                serverUsername = in.readLine();
                System.out.println(serverUsername);

                if (serverUsername.equals("KOS")) {
                    System.out.println("username non disponibile");

                } else {
                    System.out.println("username disponibile");
                }
            } while (serverUsername.equals("KOS"));

            do {
                System.out.println("listaC - lista contatti");
                System.out.println("listaG - lista gruppi");
                System.out.println("CREA:NOME - crea un gruppo");
                System.out.println("PART:NOME - entra in un gruppo");
                System.out.println("PRIV - scrivi in una chat privata");
                System.out.println("GRP - scrivi in un gruppo");
                System.out.println("ALL - scrivi a tutti");
                System.out.println("EXIT - esci");
                
                stringaScelta = scanner.nextLine();
                scelta = stringaScelta.split(":");
                

                switch (scelta[0]) {
                    case "listaC":
                        out.writeBytes("C" + "\n");
                        System.out.println("ecco la lista dei contatti:");
                        listaStringa = in.readLine();
                        lista = listaStringa.split(",");

                        for (int i = 0; i < lista.length; i++) {
                            System.out.println(lista[i]);
                        }
                    
                    break;
                    case "listaG":
                        out.writeBytes("G" + "\n");
                        System.out.println("ecco la lista dei gruppi:");
                        listaStringa = in.readLine();
                        lista = listaStringa.split(",");

                        for (int i = 0; i < lista.length; i++) {
                            System.out.println(lista[i]);
                        }
                          
                    break;
                    case "CREA":
                        out.writeBytes(stringaScelta + "\n");
                        
                    break;
                    case "PART":
                       out.writeBytes(stringaScelta + "\n");
                          
                    break;
                    case "PRIV":
                    tipoM = "PRIV";
                    System.out.println("a chi lo vuoi inviare?");
                    nomeM = scanner.nextLine();
                    
                    ThreadRicevitore ricevitore = new ThreadRicevitore(socket, nomeM);
                    ricevitore.start();

                    System.out.println("Inserisci il messaggio da inviare");
                    do{
                        testoM = scanner.nextLine();
                        if(!testoM.equals("!"))
                        {
                            out.writeBytes(tipoM +":"+ nomeM +":"+ testoM +"\n");
                        }
                    }while (!testoM.equals("!"));

                    break;
                
                    default:
                        break;
                }

            } while (!scelta.equals("EXIT"));

        } catch (Exception e) {
        }
    }
}