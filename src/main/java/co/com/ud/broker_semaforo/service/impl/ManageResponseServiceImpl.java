package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Scope("singleton")
@Slf4j
public class ManageResponseServiceImpl implements ManageResponseService{

    private List<MensajeBroker> mensajes;
    private ObjectMapper objectMapper;

    public ManageResponseServiceImpl(ObjectMapper objectMapper) {
        this.mensajes = new ArrayList<>();
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

    public Optional<String> findMessage(Integer idMsg){
        if( Objects.nonNull(mensajes) && !mensajes.isEmpty() && Objects.nonNull(idMsg) ){
            Optional<MensajeBroker> response = this.mensajes.stream().parallel()
                    .filter(item -> idMsg == item.getIdTransaccion())
                    .findFirst();
            if(response.isPresent()){
                return Optional.of(response.get().getMensaje());
            }
        }
        return Optional.empty();
    }
}