package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;
import co.com.ud.broker_semaforo.service.ConsultaGrupoSemaforicoService;
import co.com.ud.broker_semaforo.service.ExecuteService;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {

    private MensajeCentralImpl mensajeCentral;
    private ConsultaGrupoSemaforicoService consultaGrupoSemaforicoService;

    private ObjectMapper objectMapper;

    private ManageResponseService manageResponseService;
    private Integer idTrans;

    @Autowired
    public ExecuteServiceImpl(MensajeCentralImpl mensajeCentral
                        , ConsultaGrupoSemaforicoService consultaGrupoSemaforicoService
                        , ObjectMapper objectMapper
                        , ManageResponseService manageResponseService) {
        this.mensajeCentral = mensajeCentral;
        this.consultaGrupoSemaforicoService = consultaGrupoSemaforicoService;
        this.objectMapper = objectMapper;
        this.manageResponseService = manageResponseService;
    }

    @Override
    public Optional<Boolean> executeService(Integer interseccion) {
        try {
            Boolean validar = mensajeCentral.enviaMensaje(interseccion, generateMsnExecuteInterseccion(interseccion));
            if(validar){
                for(int i = 0 ; i < 5 ; i++){
                    dormirHilo();
                    Optional<String> mensajeGrp = manageResponseService.findMessage(this.idTrans);
                    if(mensajeGrp.isPresent()){
                        log.info("Este es el mensaje que debo responder: {} ", mensajeGrp.get());
                        return Optional.of( "true".equalsIgnoreCase(mensajeGrp.get()) ?  Boolean.TRUE : Boolean.FALSE );
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private String generateMsnExecuteInterseccion(Integer interseccion) throws JsonProcessingException {
        this.idTrans = consultaGrupoSemaforicoService.getIdTransaccion() + 1;
        consultaGrupoSemaforicoService.setIdTransaccion(this.idTrans);

        return "MSNEJECUTARGRPSEMAFORICO|"+objectMapper.writeValueAsString(MensajeBroker.builder()
                .idTransaccion(this.idTrans)
                .idInterseccion(interseccion)
                .mensaje("EJECUTARGRPSEMAFORICO")
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
