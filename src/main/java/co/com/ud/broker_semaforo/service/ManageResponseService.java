package co.com.ud.broker_semaforo.service;

import java.util.Optional;

public interface ManageResponseService {

    Optional<String> findMessage(Integer idMsg);

    void addMensaje(String msn);

}
