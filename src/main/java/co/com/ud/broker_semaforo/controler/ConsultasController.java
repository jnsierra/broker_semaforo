package co.com.ud.broker_semaforo.controler;

import co.com.ud.broker_semaforo.dto.PlanSemaforicoDto;
import co.com.ud.broker_semaforo.service.CargarJsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/v.1/consultaSemaforo")
public class ConsultasController {

    private final CargarJsonService cargarJsonService;

    @Autowired
    public ConsultasController(CargarJsonService cargarJsonService) {
        this.cargarJsonService = cargarJsonService;
    }

    @GetMapping("/infgnral/{interseccion}/")
    public ResponseEntity<PlanSemaforicoDto> getConsultaInterseccion(@PathVariable("interseccion") String interseccion){
        Optional<Boolean> valida = cargarJsonService.cargoJson(interseccion);
        if(valida.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>( cargarJsonService.getPlanSemaforicoDto(), HttpStatus.OK);
    }

}
