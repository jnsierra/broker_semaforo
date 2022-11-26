package co.com.ud.broker_semaforo.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface ManageTransaccionService {
    String generateMsn(Integer interseccion, String msn,  Integer idTrans);

    Integer solicitarIdTransaccion();

    void dormirHilo();

    Optional<String> buscaMensajeByIdTrans(Integer idTrans);
}
