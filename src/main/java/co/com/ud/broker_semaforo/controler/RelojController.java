/**package co.com.ud.broker_semaforo.controler;

import co.com.ud.broker_semaforo.threads.RelojThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v.1/reloj")
public class RelojController {

    @Autowired
    private RelojThread relojThread;

    @GetMapping("/")
    public Mono<ResponseEntity<Mono<Integer>>> getTime(){
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(relojThread.getReloj())
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
**/