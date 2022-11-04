package co.com.ud.broker_semaforo.service;

import co.com.ud.broker_semaforo.dto.PlanSemaforicoDto;

import java.util.Optional;

public interface CargarJsonService {

    Optional<Boolean> cargoJson(String interseccion);

    PlanSemaforicoDto getPlanSemaforicoDto();
}
