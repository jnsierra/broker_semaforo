package co.com.ud.broker_semaforo.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Slf4j
public class ConexionClienteCentral extends Thread{
    private Socket socket;
    private DataInputStream entradaDatos;
    private DataOutputStream salidaDatos;
    @Setter @Getter
    private Integer idCliente;

    private Boolean escuchando;
    private Boolean msnBienvenidaEnviado;
    private ManageResponseServiceImpl manageResponseService;

    public ConexionClienteCentral(Socket socket, ManageResponseServiceImpl manageResponseService) {
        this.msnBienvenidaEnviado = Boolean.FALSE;
        this.manageResponseService = manageResponseService;
        this.socket = socket;
        this.escuchando = Boolean.TRUE;
        try {
            entradaDatos = new DataInputStream(socket.getInputStream());
            salidaDatos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            log.error("Error al crear los stream de entrada y salida : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String mensajeRecibido;
        this.envioMsnBienvenida();
        while(this.escuchando){
            try {
                // Lee un mensaje enviado por el Servidor
                mensajeRecibido = entradaDatos.readUTF();
                log.info("Mensaje enviado con el id {} msn: {}", this.idCliente, mensajeRecibido);
            } catch (IOException ex) {
                log.info("Cliente con la IP " + socket.getInetAddress().getHostName() + " desconectado.");
                this.escuchando = Boolean.FALSE;
                try {
                    entradaDatos.close();
                    salidaDatos.close();
                } catch (IOException ex2) {
                    log.error("Error al cerrar los stream de entrada y salida :" + ex2.getMessage());
                    ex2.printStackTrace();
                }
            }
        }
    }

    public void envioMsnBienvenida(){
        if(!msnBienvenidaEnviado){
            this.msnBienvenidaEnviado = Boolean.TRUE;
            this.enviarMSN("MSNSISTEMA|ID|"+ this.idCliente);
        }
    }

    public void enviarMSN(String mensaje){
        try {
            salidaDatos.writeUTF(mensaje);
        } catch (IOException e) {
            log.error(null, e);
        }
    }
}
