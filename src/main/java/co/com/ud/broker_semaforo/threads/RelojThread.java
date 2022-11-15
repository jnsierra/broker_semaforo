/**package co.com.ud.broker_semaforo.threads;

import reactor.core.publisher.Mono;

public class RelojThread extends Thread {

    private Integer contador;
    private Mono<Integer>  reloj;

    public RelojThread() {
        this.contador = 0;
    }

    @Override
    public void run() {
        while(true){
            contador++;
        }
    }

    public Mono<Integer> getReloj(){
        return Mono.just(1);
    }
}**/