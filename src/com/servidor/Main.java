package com.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static ServerSocket serverSocket;
    static Socket newSocket;
    static InputStream entrada;
    static OutputStream salida;
    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress("localhost", 2001);
            serverSocket.bind(addr);

            while (true){
                newSocket =serverSocket.accept();
                Servidor servidor = new Servidor();
                servidor.start();


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static class Servidor extends Thread {


        @Override
        public void run() {
            Scanner sc = new Scanner(System.in); //Se crea el lector

            try {
                entrada = newSocket.getInputStream();
                salida = newSocket.getOutputStream();

                byte [] ip = new byte[140];
                entrada.read(ip);
                byte [] porto = new byte[140];
                entrada.read(porto);

                HashMap<String, String> cliente = new HashMap<>();
                cliente.put(new String(ip).trim(), new String(porto).trim());

                ArrayList <HashMap> clientes= new ArrayList();
                clientes.add(cliente);

                byte[] mensaje;
                String ms = "";
                while (!ms.equalsIgnoreCase("fin")) {
                    mensaje = new byte[140];
                    entrada.read(mensaje);
                    System.out.println(new String(mensaje).trim());
                    if (new String(mensaje).trim().equalsIgnoreCase("fin")) {
                        break;
                    }
                    ms = sc.nextLine();
                    salida.write(ms.getBytes());
                    System.out.println("Mensaje enviado");
                }


                System.out.println("Cerrando el nuevo socket");

                newSocket.close();

                System.out.println("Cerrando el socket servidor");

                serverSocket.close();

                System.out.println("Terminado");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
