package co.com.ud.broker_semaforo.service;

import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;

import java.util.Optional;

public interface ConsultaGrupoSemaforicoService<T> {

    Optional<T> ejecutaAccion(Integer interseccion, String accion);

}