package co.com.ud.broker_semaforo.enumeration;

public enum EstadoGrupoSemaforicoEnum {

    ESPERA_CONEXIONES, CONEXIONES_COMPLETAS, CORRIENDO;

    public static EstadoGrupoSemaforicoEnum of(String estado){
        if("ESPERA_CONEXIONES".equalsIgnoreCase(estado)){
            return ESPERA_CONEXIONES;
        }
        if("CONEXIONES_COMPLETAS".equalsIgnoreCase(estado)){
            return CONEXIONES_COMPLETAS;
        }
        if("CORRIENDO".equalsIgnoreCase(estado)){
            return CORRIENDO;
        }
        return null;
    }
}
