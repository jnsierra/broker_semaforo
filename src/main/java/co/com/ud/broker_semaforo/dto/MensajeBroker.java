package co.com.ud.broker_semaforo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeBroker {

    private Integer idTransaccion;
    private Integer idInterseccion;
    private String mensaje;
}
