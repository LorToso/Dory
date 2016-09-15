package com.doryapp.dory.apiCalls;

import android.os.AsyncTask;

/**
 * Created by Lorenzo Toso on 15.09.2016.
 */
public abstract class ApiCall {

    protected Runnable call;

    public void execute()
    {
        AsyncTask.execute(call);
    }
    public void executeSynchronously()
    {
        call.run();
    }


    public interface OnComplete<ParameterType>
    {
        void execute(ParameterType param);
    }
}
