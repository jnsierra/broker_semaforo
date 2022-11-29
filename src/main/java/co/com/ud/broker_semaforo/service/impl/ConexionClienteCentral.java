package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.dto.RespuestaMensaje;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

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

    private String ROOT_URI;
    @Setter @Getter
    private String ip;
    @Setter @Getter
    private String puerto;

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
                manageResponseService.addMensaje(mensajeRecibido);
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

    public void generateUrl(){
        ROOT_URI = "http://"+this.ip+":"+this.puerto+"/api-business";
    }

    public void envioMsnBienvenida(){
        if(!msnBienvenidaEnviado){
            this.msnBienvenidaEnviado = Boolean.TRUE;
            this.enviarMSN("MSNSISTEMA|ID|"+ this.idCliente);
        }
    }

    public void enviarMSN(String mensaje){
        try {
            if(!validaMensajeTiempo(mensaje)){
                salidaDatos.writeUTF(mensaje);
            }else{
                //Envio el msn por rest template
                this.envioMsnRest(mensaje);
            }
        } catch (IOException e) {
            log.error(null, e);
        }
    }

    public Boolean validaMensajeTiempo(String mensaje){
        if(mensaje.contains("MSNCONSULTATIEMEJECUCION")){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }

    }

    public String enviarMSNInm(String tipoConsulta){
        String rtaString = "";
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        ResponseEntity<RespuestaMensaje> response = null;
        if("MSNCONSULTATIEMEJECUCION".equalsIgnoreCase(tipoConsulta)){
            response = restTemplate.getForEntity(ROOT_URI + "/v.1/consultaSemaforo/tiempo/", RespuestaMensaje.class);
            RespuestaMensaje<Integer> rta = response.getBody();
            if(HttpStatus.OK.equals(response.getStatusCode())){
                rtaString = rta.getRespuesta() + "";
            }
        }
        if("MSNCONSULTAESTADO".equalsIgnoreCase(tipoConsulta)){
            response = restTemplate.getForEntity(ROOT_URI + "/v.1/consultaSemaforo/estado/", RespuestaMensaje.class);
            RespuestaMensaje<String> rta = response.getBody();
            rtaString = rta.getRespuesta();
        }
        log.info("Esta es la respuesta {} ", response);
        return rtaString;
    }

    public void envioMsnRest(String msn){
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        ResponseEntity<RespuestaMensaje> response = restTemplate.getForEntity(ROOT_URI + "/v.1/consultaSemaforo/tiempo/", RespuestaMensaje.class);
        log.info("Esta es la respuesta {} ", response);
        RespuestaMensaje<Integer> rta = response.getBody();
        MensajeBroker mensaje = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String[] aux = msn.split("\\|");
            mensaje = objectMapper.readValue(aux[1], MensajeBroker.class);
            if(HttpStatus.OK.equals(response.getStatusCode())){
                mensaje.setMensaje(rta.getRespuesta()+ "");
                //Convierto a json el objeto
                String json = objectMapper.writeValueAsString(mensaje);
                manageResponseService.addMensaje("MSNRTACONSULTATIEMEJECUCION|"+json);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
