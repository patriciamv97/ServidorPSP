package com.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ServerSocket serverSocket;
    static InputStream entrada;
    static OutputStream salida;

    static int i =0;
    public static void main(String[] args) {

        try {

            serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress("localhost", 2001);
            serverSocket.bind(addr);
            System.out.println("Ningún cliente conectado");
            while (true) {
                Socket newSocket = serverSocket.accept();
                entrada = newSocket.getInputStream();
                salida = newSocket.getOutputStream();
                salida.write("Conectado á sala de chat".getBytes());
                byte[] nick = new byte[140];
                entrada.read(nick);
                Servidor servidor = new Servidor(new String(nick).trim(),newSocket);
                servidor.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
