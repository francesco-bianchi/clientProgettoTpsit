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
        Socket socketAscolto = new Socket("localhost", 4000);
        String username;
        String serverUsername;
        String[] scelta;
        String[] scelta2;
        String stringaScelta;
        String stringaScelta2;
        String serverRisposta;
        String listaStringa;
        String[] lista;
        String stringM = "";
        String tipoM = "";
        String testoM = "";
        String nomeM = "";
        boolean ascolta= false;
        Scanner scanner = new Scanner(System.in);
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataOutputStream out2 = new DataOutputStream(socketAscolto.getOutputStream());
            
            // Receiver
            ThreadRicevitore ricevitore = new ThreadRicevitore(socket, socketAscolto);

            ricevitore.start();


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
                System.out.println("nuova - avvia una chat");
                System.out.println("EXIT - esci");
                
                stringaScelta = scanner.nextLine();
                scelta = stringaScelta.split(":");
                
                do {
                    ascolta = ricevitore.isAscoltato();
                    if(!ascolta)
                        ascolta = false;
                        else{
                            String messaggio = in.readLine();
                            System.out.println(messaggio);

                        }
                } while (ascolta);
                    


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
                    case "nuova":
                        out.writeBytes("M" + "\n");
                          
                    break;
                
                    default:
                        break;
                }

                // ricezione
                serverRisposta = in.readLine();

                if (serverRisposta.equals("OKG")) {
                    System.out.println("il gruppo è stato creato");
                }
                if (serverRisposta.equals("KOG")) {
                    System.out.println("il gruppo è già esistente");
                }

                if (serverRisposta.equals("OKP")) {
                    System.out.println("ora partecipi al gruppo");
                }
                if (serverRisposta.equals("KOP")) {
                    System.out.println("non puoi partecipare a questo gruppo");
                }

                if (serverRisposta.equals("OKK")) {
                    System.out.println("cosa vuoi fare?");
                    System.out.println("PRIV - invia un messaggio ad un solo utente");
                    System.out.println("GRP - invia un messaggio ad un gruppo");
                    System.out.println("ALL - invia un messaggio a tutti gli utenti");
                    tipoM = scanner.nextLine();

                    if (!tipoM.equals("ALL")) {
                        System.out.println("a chi lo vuoi inviare?");
                        nomeM = scanner.nextLine();
                        ;
                    }

                    System.out.println("Inserisci il messaggio da inviare");
                    testoM = scanner.nextLine();

                    stringM = tipoM + ":" + nomeM + ":" + testoM;
                    out.writeBytes(stringM + "\n");
                    String mess = in.readLine();
                    
                    if(mess.equals("KKO")){
                        System.out.println("nome del contatto inesistente");
                    }
                    else if(username.equals(nomeM))
                        System.out.println(mess);
                }
                if (serverRisposta.equals("KKO")) {
                    System.out.println("Non ci sono altri utenti online");
                }

            } while (!scelta.equals("EXIT"));

        } catch (Exception e) {
        }
    }
}