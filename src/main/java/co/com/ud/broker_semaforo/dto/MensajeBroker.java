package co.com.ud.broker_semaforo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MensajeBroker {

    private Integer idTransaccion;
    private Integer idInterseccion;
    private String mensaje;
}
