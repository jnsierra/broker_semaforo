package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import co.com.ud.broker_semaforo.service.ManageTransaccionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Scope("singleton")
public class ManageTransaccionServiceImpl implements ManageTransaccionService {

    private ObjectMapper objectMapper;
    private Integer idTransaccion;

    private ManageResponseService manageResponseService;


    @Autowired
    public ManageTransaccionServiceImpl(ObjectMapper objectMapper, ManageResponseService manageResponseService) {
        this.objectMapper = objectMapper;
        this.idTransaccion = 0;
        this.manageResponseService = manageResponseService;
    }

    @Override
    public String generateMsn(Integer interseccion, String msn, Integer idTrans){
        try {
            return msn+ "|" + objectMapper.writeValueAsString(MensajeBroker.builder()
                    .idTransaccion(idTrans)
                    .idInterseccion(interseccion)
                    .mensaje(msn)
                    .build());
        } catch (JsonProcessingException e) {
            log.error("Error generación mensaje Json: {} ", msn);
        }
        return null;
    }
    @Override
    public synchronized Integer solicitarIdTransaccion(){
        idTransaccion++;
        return idTransaccion;
    }
    @Override
    public void dormirHilo(){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> buscaMensajeByIdTrans(Integer idTrans) {
        for(int i = 0 ; i < 5 ; i++){
            dormirHilo();
            Optional<String> mensajeGrp = manageResponseService.findMessage(idTrans);
            if(mensajeGrp.isPresent()){
                return mensajeGrp;
            }
        }
        return Optional.empty();
    }
}