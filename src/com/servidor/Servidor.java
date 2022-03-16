package com.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public  class Servidor extends Thread {

    Socket socket;
    InputStream entrada;
    OutputStream salida;
    static ArrayList<Servidor> usuarios = new ArrayList();

    public Servidor(String name,Socket socket) throws IOException {

        super(name);
        this.socket = socket;
        entrada = socket.getInputStream();
        salida = socket.getOutputStream();
        usuarios.add(this);

    }

    void enviarMensaje(String mensaje) throws IOException {
        salida.write(mensaje.getBytes());
    }

    @Override
    public void run() {


        try {

            String mensaje = "";
            while (!mensaje.equalsIgnoreCase("fin")) {
                byte[] mensajeRecibido  =new byte[140];
                entrada.read(mensajeRecibido);
                mensaje = new String(mensajeRecibido).trim();
                for (int i = 0; i < usuarios.size(); i++) {
                    usuarios.get(i).enviarMensaje(mensaje);
                }
            }

            System.out.println("Cerrando el nuevo socket");

            socket.close();

            System.out.println("Terminado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
