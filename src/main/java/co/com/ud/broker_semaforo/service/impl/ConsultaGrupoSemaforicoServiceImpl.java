package co.com.ud.broker_semaforo.service.impl;

import co.com.ud.broker_semaforo.dto.MensajeBroker;
import co.com.ud.broker_semaforo.enumeration.EstadoGrupoSemaforicoEnum;
import co.com.ud.broker_semaforo.service.ConsultaGrupoSemaforicoService;
import co.com.ud.broker_semaforo.service.ManageResponseService;
import co.com.ud.broker_semaforo.service.ManageTransaccionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
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
    private ManageTransaccionService manageTransaccionService;
    private ManageResponseService manageResponseService;

    @Autowired
    public ConsultaGrupoSemaforicoServiceImpl(MensajeCentralImpl mensajeCentral
            , ManageResponseService manageResponseService
            , ManageTransaccionService manageTransaccionService) {
        this.mensajeCentral = mensajeCentral;
        this.manageResponseService = manageResponseService;
        this.manageTransaccionService = manageTransaccionService;
    }


    @Override
    public synchronized Optional ejecutaAccion(Integer interseccion, String accion) {
        log.info("*****************************");
        int idTranTemporal = manageTransaccionService.solicitarIdTransaccion();
        log.info("Se inicia transacción con el id {} ", idTranTemporal);
        Boolean validar = mensajeCentral.enviaMensaje(interseccion,manageTransaccionService.generateMsn(interseccion, accion, idTranTemporal));
        if(validar){
            log.info("Se envia el mensaje: {}", validar);
            Optional responseMethod = Optional.empty();
            Optional<String> response = manageTransaccionService.buscaMensajeByIdTrans(idTranTemporal);
            if(response.isPresent()){
                switch (accion){
                    case "MSNCONSULTAESTADO":
                        responseMethod = Optional.of(EstadoGrupoSemaforicoEnum.of(response.get()));
                        break;
                    case "MSNCONSULTANUMCON":
                    case "MSNCONSULTATIEMEJECUCION":
                        responseMethod =Optional.of(Integer.valueOf(response.get()));
                        break;
                    case "MSNEJECUTARGRPSEMAFORICO":
                        responseMethod = Optional.of( "true".equalsIgnoreCase(response.get()) ?  Boolean.TRUE : Boolean.FALSE );
                    default:
                        log.info("Mensaje no identificado {} con el id de transaccion: {} ", accion, interseccion);
                        break;
                }
                log.info("Se consiguio la respuesta {} ", response.get());
            }else{
                log.info("No se encontro respuesta de vuelta");
            }
            return  responseMethod;
        }
        return Optional.empty();
    }
}