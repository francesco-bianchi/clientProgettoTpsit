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
        String stringaScelta;
        String listaStringa;
        String[] lista;
        String tipoM = "";
        String testoM = "";
        String nomeM = "";
        String upperString;
        Scanner scanner = new Scanner(System.in);
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            do {
                System.out.println("inserire il proprio username:");
                username = scanner.nextLine();
                out.writeBytes(username + "\n");

                serverUsername = in.readLine();

                if (serverUsername.equals("KOS")) {
                    System.out.println("utente già presente in chat");

                } else {
                    System.out.println("Benevenuto " + username);
                }
            } while (serverUsername.equals("KOS"));

            do {
                System.out.println("");
                System.out.println("--- Menù ---");
                System.out.println("listaC - lista contatti");
                System.out.println("PRIV - scrivi in una chat privata");
                System.out.println("ALL - scrivi a tutti");
                System.out.println("EXIT - esci");
                System.out.println("   ---   ");
                System.out.println("");

                stringaScelta = scanner.nextLine();
                upperString = stringaScelta.toUpperCase(); // si mette tutto maiuscolo per avere un programma case sensitive

                switch (upperString) { // si controlla ciò che ha scritto l'utente
                    case "LISTAC":
                        out.writeBytes("C" + "\n");
                        System.out.println("ecco la lista dei contatti:");
                        listaStringa = in.readLine();
                        lista = listaStringa.split(";"); //lista di tutti i contatti

                        for (int i = 0; i < lista.length; i++) {
                            System.out.println(lista[i]);
                        }

                        break;
                    case "PRIV":
                        tipoM = "PRIV";
                        do {
                            System.out.println("a chi lo vuoi inviare?");
                            nomeM = scanner.nextLine();
                        } while (nomeM.equals(username)); // si controlla che non scriva a se stesso
                        
                        out.writeBytes(tipoM + ":" + nomeM + ":CR\n"); // per ricevere la cronologia
                        ThreadRicevitore ricevitore = new ThreadRicevitore(socket,username, nomeM);
                        ricevitore.start();
                        
                        System.out.println("Sei nella chat con "+ nomeM + " (scrivere '/exit' per uscire)");
                        do {
                            testoM = scanner.nextLine();
                            if (!testoM.toUpperCase().equals("/EXIT")) {
                                out.writeBytes(tipoM + ":" + nomeM + ":" + testoM + "\n"); //si sta nella chat con il destinatario fino a che non si scrive /EXIT
                            }
                        } while (!testoM.toUpperCase().equals("/EXIT"));
                        ricevitore.threadAttivo(false);
                        ricevitore.interrupt();

                        break;
                    case "ALL":
                        tipoM = "ALL";
                        ThreadRicevitore ricevitore2 = new ThreadRicevitore(socket, username, "ALL");
                        ricevitore2.start();
                        System.out.println("Sei nella chat con tutti (scrivere '/exit' per uscire)");
                        do {
                            testoM = scanner.nextLine();
                            if (!testoM.toUpperCase().equals("/EXIT")) {
                                out.writeBytes(tipoM + ":" + testoM + "\n");
                            }
                        } while (!testoM.toUpperCase().equals("/EXIT"));
                        
                        ricevitore2.threadAttivo(false);
                        ricevitore2.interrupt();

                        break;

                    default:
                        out.writeBytes("EXT\n");
                        break;
                }
            } while (!upperString.equals("EXIT"));
            socket.close();

        } catch (Exception e) {
        }
    }
}