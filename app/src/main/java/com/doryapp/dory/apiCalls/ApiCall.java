package com.doryapp.dory.apiCalls;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo Toso on 15.09.2016.
 */
public abstract class ApiCall {

    protected Runnable call;
    protected List<OnException> exceptionHandler = new ArrayList<>();
    //protected List<OnException> exceptionHandler = new ArrayList<>();

    public void execute()
    {
        AsyncTask.execute(call);
    }
    public void executeSynchronously()
    {
        call.run();
    }
    public ApiCall onException(OnException exceptionHandler){this.exceptionHandler.add(exceptionHandler); return this;}
    protected void handle(Exception ex){
        for (OnException handler : exceptionHandler) {
            handler.handle(ex);
        }
    }

    public interface OnComplete<ParameterType>
    {
        void execute(ParameterType param);
    }
    public interface OnException
    {
        void handle(Exception ex);
    }
}
