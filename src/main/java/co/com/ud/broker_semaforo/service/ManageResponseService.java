package co.com.ud.broker_semaforo.service;

import java.util.Optional;

public interface ManageResponseService {

    Optional<String> getRtaConsulta(String msn, Integer id);

}
