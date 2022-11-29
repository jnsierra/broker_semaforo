package co.com.ud.broker_semaforo.service.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MensajeCentralImpl extends Thread {
    private Integer port;
    private List<ConexionClienteCentral> conexiones;
    private ServerSocket servidor = null;
    private Socket socket = null;
    private ManageResponseServiceImpl manageResponseService;

    public MensajeCentralImpl(Integer port, ManageResponseServiceImpl manageResponseService) {
        this.port = port;
        this.manageResponseService = manageResponseService;
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
                ConexionClienteCentral ccs = new ConexionClienteCentral(socket, this.manageResponseService);
                ccs.setIdCliente(i);
                if(i == 1){
                    ccs.setIp("localhost");
                    ccs.setPuerto("5001");
                }
                if(i == 2){
                    ccs.setIp("localhost");
                    ccs.setPuerto("5002");
                }
                if(i == 3){
                    ccs.setIp("localhost");
                    ccs.setPuerto("5003");
                }
                ccs.generateUrl();
                ccs.start();
                this.conexiones.add(ccs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean enviaMensaje(Integer id,String mensaje) {
        if(Objects.nonNull(this.conexiones) && !this.conexiones.isEmpty()){
            Optional<ConexionClienteCentral> conexion =this.conexiones.stream()
                    .parallel()
                    .filter( item -> id.equals(item.getIdCliente()) )
                    .findFirst();
            if(conexion.isPresent()){
                conexion.get().enviarMSN(mensaje);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public String enviaMensajeRetornoIn(Integer id, String consulta) {
        if(Objects.nonNull(this.conexiones) && !this.conexiones.isEmpty()){
            Optional<ConexionClienteCentral> conexion =this.conexiones.stream()
                    .parallel()
                    .filter( item -> id.equals(item.getIdCliente()) )
                    .findFirst();
            if(conexion.isPresent()){
                return conexion.get().enviarMSNInm(consulta);
            }
        }
        return null;
    }
}