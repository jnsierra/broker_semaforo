package co.com.ud.broker_semaforo.config;

import co.com.ud.broker_semaforo.service.impl.ManageResponseServiceImpl;
import co.com.ud.broker_semaforo.service.impl.MensajeCentralImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ObjectConfig {

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

    @Bean
    @Scope("singleton")
    public MensajeCentralImpl getMensajeCentral(@Value("${broker.connection.port}")Integer port, ManageResponseServiceImpl manageResponseService){
        return new MensajeCentralImpl(port, manageResponseService);
    }

}
