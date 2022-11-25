package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Scope("singleton")
@Slf4j
public class ManageResponseServiceImpl implements ManageResponseService{

    private List<MensajeBroker> mensajes;
    private ObjectMapper objectMapper;

    public ManageResponseServiceImpl(ObjectMapper objectMapper) {
        this.mensajes = new LinkedList<>();
        this.objectMapper = objectMapper;
    }

    public void addMensaje(String msn){
        log.info("Intepretar el mensaje: {} ", msn);
        String json = this.obtenerJson(msn);
        MensajeBroker mensaje = null;
        try {
            mensaje = this.objectMapper.readValue(json, MensajeBroker.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.mensajes.add(mensaje);
    }
    public String obtenerJson(String json){
        String[] parts = json.split("\\|");
        return parts[1];
    }

    public synchronized Optional<String> findMessage(Integer idMsg){
        if( Objects.nonNull(mensajes) && !mensajes.isEmpty() && Objects.nonNull(idMsg) ){
            Optional<MensajeBroker> response = this.mensajes.stream().parallel()
                    .filter(item -> idMsg == item.getIdTransaccion())
                    .findFirst();
            log.info("Numero de items antes de remover {} ", this.mensajes.size());
            if(response.isPresent()){
                this.removeItem(idMsg);
                this.mensajes.removeIf(item -> idMsg == item.getIdTransaccion());
                log.info("Numero de items {} ", this.mensajes.size());
                return Optional.of(response.get().getMensaje());
            }
        }
        return Optional.empty();
    }

    public void removeItem(Integer idMsn){
        for(int i = 0; i < mensajes.size() ; i++){
            MensajeBroker item = this.mensajes.get(i);
            if(idMsn == item.getIdTransaccion()){
                this.mensajes.remove(i);
                return ;
            }
        }
    }
}