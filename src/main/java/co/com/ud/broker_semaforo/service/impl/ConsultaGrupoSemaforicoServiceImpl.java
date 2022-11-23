package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;
import co.com.ud.broker_semaforo.service.ConsultaGrupoSemaforicoService;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@Slf4j
public class ConsultaGrupoSemaforicoServiceImpl implements ConsultaGrupoSemaforicoService {

    private MensajeCentralImpl mensajeCentral;
    private Integer idTransaccion;
    private ObjectMapper objectMapper;

    private ManageResponseService manageResponseService;

    @Autowired
    public ConsultaGrupoSemaforicoServiceImpl(MensajeCentralImpl mensajeCentral
            , ObjectMapper objectMapper
            , ManageResponseService manageResponseService) {
        this.mensajeCentral = mensajeCentral;
        this.idTransaccion = 0;
        this.objectMapper = objectMapper;
        this.manageResponseService = manageResponseService;
    }

    @Override
    @Async
    public Optional<EstadoGrupoSemaforicoEnum> getEstadoEnum(Integer interseccion) {
        try {
            Boolean validar = mensajeCentral.enviaMensaje(interseccion, generateMsnConsultaEstado(interseccion));
            if(validar){
                for(int i = 0 ; i < 5 ; i++){
                    dormirHilo();
                    Optional<String> mensajeGrp = manageResponseService.findMessage(idTransaccion);
                    if(mensajeGrp.isPresent()){
                        log.info("Este es el mensaje que debo responder: {} ", mensajeGrp.get());
                        return Optional.of(EstadoGrupoSemaforicoEnum.of(mensajeGrp.get()));
                    }
                }
            }

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

    private void dormirHilo(){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
