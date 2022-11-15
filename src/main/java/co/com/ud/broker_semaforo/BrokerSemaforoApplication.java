package co.com.ud.broker_semaforo;

import co.com.ud.broker_semaforo.service.impl.MensajeCentralImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrokerSemaforoApplication implements CommandLineRunner {

    @Autowired
    private MensajeCentralImpl mensajeCentral;
    public static void main(String[] args) {
        SpringApplication.run(BrokerSemaforoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mensajeCentral.start();
    }
}
