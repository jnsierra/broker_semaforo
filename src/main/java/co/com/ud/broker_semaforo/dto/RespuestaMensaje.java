package co.com.ud.broker_semaforo.dto;

import lombok.Data;

@Data
public class RespuestaMensaje<T> {

    private Integer code;
    private String mensaje;
    private T respuesta;

}
