package co.com.ud.broker_semaforo.service;

import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;

import java.util.Optional;

public interface ConsultaGrupoSemaforicoService {

    Optional<EstadoGrupoSemaforicoEnum> getEstadoEnum(Integer interseccion);

    Optional<Integer> getNumConexiones(Integer interseccion);

    Optional<Integer> getTiempoEjecucion(Integer interseccion);

    Integer getIdTransaccion();
    void setIdTransaccion(Integer idTransaccion);
}