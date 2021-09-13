package com.johncoimbra.tmdbchallengemvvmandroid.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    //Padrão Singleton
    public static AppExecutors instance;

    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }
    //Serviços de executores programados
    private final ScheduledExecutorService mNetworkIO =
            Executors.newScheduledThreadPool(3); //3 segundos - tempo de espera da resposta

    public ScheduledExecutorService netWorkIO(){
        return mNetworkIO;
    }

}
