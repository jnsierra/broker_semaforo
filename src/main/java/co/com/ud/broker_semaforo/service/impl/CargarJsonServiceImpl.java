package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.PlanSemaforicoDto;
import co.com.ud.broker_semaforo.service.CargarJsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Component
public class CargarJsonServiceImpl implements CargarJsonService {

    private ObjectMapper objectMapper;
    @Getter
    private PlanSemaforicoDto planSemaforicoDto;

    @Autowired
    public CargarJsonServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Boolean> cargoJson(String interseccion) {
        try {
            planSemaforicoDto = objectMapper.readValue(new File(interseccionToPathJsonFile(interseccion)), PlanSemaforicoDto.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.of(Boolean.FALSE);
        }
        return Optional.of(Boolean.TRUE);
    }

    private String interseccionToPathJsonFile(String interseccion){
        if("1".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_1.json";
        }
        if("2".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_2.json";
        }
        if("3".equalsIgnoreCase(interseccion)){
            return "src/main/resources/plan_semaforico_3.json";
        }
        return "";
    }

}
