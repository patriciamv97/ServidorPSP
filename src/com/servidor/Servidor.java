package com.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {

    Socket socket;
    InputStream entrada;
    OutputStream salida;
    static ArrayList<Servidor> usuarios = new ArrayList();
    static String [] comando= new String[2];
    public Servidor(String name, Socket socket) throws IOException {

        super(name);
        this.socket = socket;
        entrada = socket.getInputStream();
        salida = socket.getOutputStream();

    }

    void enviarMensaje(String mensaje) throws IOException {
        salida.write(mensaje.getBytes());

    }

    @Override
    public void run() {

        String[] comando = new String[2];
        try {

            for (int i = 0; i < usuarios.size(); i++) {
                usuarios.get(i).enviarMensaje(getName() + " acaba de conectarse a este chat");
            }
            usuarios.add(this);

            String mensaje = "";
            while (usuarios.size() != 0) {
                byte[] mensajeRecibido = new byte[140];
                entrada.read(mensajeRecibido);
                mensaje = new String(mensajeRecibido).trim();
                System.out.println(mensaje);
                comando = mensaje.split(": ");
                //System.out.println(comando[1]);
                if (comando[1].equals("/bye")) {
                    System.out.println(usuarios.size());
                    for (int i = 0; i < usuarios.size(); i++) {
                        String desconexion = comando[0] + " deixou este chat";
                        usuarios.get(i).enviarMensaje(desconexion);

                    }
                    this.socket.close();
                    usuarios.remove(this);
                    //System.out.println(usuarios.size() + "\n" + usuarios.get(0));
                } else {
                    for (int i = 0; i < usuarios.size(); i++) {
                        usuarios.get(i).enviarMensaje(mensaje);
                    }
                }
            }
            System.out.println("Ningún cliente conectado");


        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            usuarios.remove(this);
            if (usuarios.size()==0){
                System.out.println("Ningún cliente conectado");
            }else {

                for (int i = 0; i < usuarios.size(); i++) {
                    String desconexion = this.getName() + " deixou este chat";
                    try {
                        usuarios.get(i).enviarMensaje(desconexion);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
            }
            System.out.println("socket cerrado\n");

        }
    }
}
