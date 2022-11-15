package co.com.ud.broker_semaforo.service.impl;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MensajeCentralImpl extends Thread {
    private Integer port;
    private List<ConexionClienteCentral> conexiones;
    private ServerSocket servidor = null;
    private Socket socket = null;


    public MensajeCentralImpl(Integer port) {
        this.port = port;
        conexiones = new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            this.servidor = new ServerSocket(port, 10);
            for(int i = 1; i < 10 ; i++){
                log.info("Servidor a la espera de conexiones.");
                socket = servidor.accept();
                log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " conectado.");
                ConexionClienteCentral ccs = new ConexionClienteCentral(socket);
                ccs.setIdCliente(i);
                ccs.start();
                this.conexiones.add(ccs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean enviaMensaje(Integer id,String mensaje) {
        return null;
    }
}
