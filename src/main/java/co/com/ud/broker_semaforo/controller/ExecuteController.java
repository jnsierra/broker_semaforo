package co.com.ud.broker_semaforo.controller;

import co.com.ud.broker_semaforo.dto.PlanSemaforicoDto;
import co.com.ud.broker_semaforo.service.impl.ExecuteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v.1/ejecutarSemaforo")
public class ExecuteController {

    private ExecuteServiceImpl executeService;

    @Autowired
    public ExecuteController(ExecuteServiceImpl executeService) {
        this.executeService = executeService;
    }

    @GetMapping("/ejecutar/{interseccion}/")
    public ResponseEntity<Boolean> getEjecutarInterseccion(@PathVariable("interseccion") Integer interseccion){
        Optional<Boolean> valida = executeService.executeService(interseccion);
        if(valida.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>( valida.get(), HttpStatus.OK);
    }
}
