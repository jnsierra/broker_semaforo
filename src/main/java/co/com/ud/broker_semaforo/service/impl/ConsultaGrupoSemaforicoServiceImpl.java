package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;
import co.com.ud.broker_semaforo.service.ConsultaGrupoSemaforicoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConsultaGrupoSemaforicoServiceImpl implements ConsultaGrupoSemaforicoService {

    private MensajeCentralImpl mensajeCentral;
    private Integer idTransaccion;
    private ObjectMapper objectMapper;

    @Autowired
    public ConsultaGrupoSemaforicoServiceImpl(MensajeCentralImpl mensajeCentral, ObjectMapper objectMapper) {
        this.mensajeCentral = mensajeCentral;
        this.idTransaccion = 0;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<EstadoGrupoSemaforicoEnum> getEstadoEnum(Integer interseccion) {
        try {
            mensajeCentral.enviaMensaje(interseccion, generateMsnConsultaEstado(interseccion));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private String generateMsnConsultaEstado(Integer interseccion) throws JsonProcessingException {
        idTransaccion++;
        return "MSNCONSULTAESTADO|"+objectMapper.writeValueAsString(MensajeBroker.builder()
                        .idTransaccion(idTransaccion)
                        .idInterseccion(interseccion)
                        .mensaje("CONSULTAESTADO")
                .build());
    }
}
