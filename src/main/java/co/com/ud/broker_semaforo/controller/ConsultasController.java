package co.com.ud.broker_semaforo.controller;

import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;
import co.com.ud.broker_semaforo.dto.PlanSemaforicoDto;
import co.com.ud.broker_semaforo.service.CargarJsonService;
import co.com.ud.broker_semaforo.service.ConsultaGrupoSemaforicoService;
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
    private ConsultaGrupoSemaforicoService consultaGrupoSemaforicoService;

    @Autowired
    public ConsultasController(CargarJsonService cargarJsonService, ConsultaGrupoSemaforicoService consultaGrupoSemaforicoService) {
        this.cargarJsonService = cargarJsonService;
        this.consultaGrupoSemaforicoService = consultaGrupoSemaforicoService;
    }

    @GetMapping("/infgnral/{interseccion}/")
    public ResponseEntity<PlanSemaforicoDto> getConsultaInterseccion(@PathVariable("interseccion") String interseccion){
        Optional<Boolean> valida = cargarJsonService.cargoJson(interseccion);
        if(valida.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>( cargarJsonService.getPlanSemaforicoDto(), HttpStatus.OK);
    }
    @GetMapping("/estado/{interseccion}/")
    public ResponseEntity<EstadoGrupoSemaforicoEnum> getEstadoGrupoSemaforico(@PathVariable("interseccion") Integer interseccion){
        Optional<EstadoGrupoSemaforicoEnum> response = consultaGrupoSemaforicoService.ejecutaAccion(interseccion,"MSNCONSULTAESTADO");
        if(response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }
    @GetMapping("/conexiones/{interseccion}/")
    public ResponseEntity<Integer> getNumConexiones(@PathVariable("interseccion") Integer interseccion){
        Optional<Integer> response = consultaGrupoSemaforicoService.ejecutaAccion(interseccion,"MSNCONSULTANUMCON");
        if(response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);

    }

    @GetMapping("/tiempo/{interseccion}/")
    public ResponseEntity<String> getTiempoEjecucion(@PathVariable("interseccion") Integer interseccion){
        Optional<String> response = consultaGrupoSemaforicoService.ejecutaAccionReturnInm(interseccion,"MSNCONSULTATIEMEJECUCION");
        if(response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);

    }

}
