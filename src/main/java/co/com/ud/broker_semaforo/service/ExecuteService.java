package co.com.ud.broker_semaforo.service;

import java.util.Optional;

public interface ExecuteService {

    Optional<Boolean> executeService(Integer idInterseccion);
}
