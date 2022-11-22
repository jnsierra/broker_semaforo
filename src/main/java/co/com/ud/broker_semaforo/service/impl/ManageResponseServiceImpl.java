package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class ManageResponseServiceImpl implements ManageResponseService{

    private List<MensajeBroker> mensajes;
    private ObjectMapper objectMapper;

    public ManageResponseServiceImpl(ObjectMapper objectMapper) {
        this.mensajes = new ArrayList<>();
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> getRtaConsulta(String msn, Integer id) {
        return Optional.empty();
    }

    public void addMensaje(String msn){
        MensajeBroker mensaje = null;
        try {
            mensaje = this.objectMapper.readValue(msn, MensajeBroker.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        this.mensajes.add(mensaje);
    }
}
