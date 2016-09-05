package com.doryapp.dory.apiCalls;

import android.os.AsyncTask;

import com.doryapp.dory.AsyncExecutor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

/**
 * Created by Lorenzo Toso on 01.09.2016.
 */
public abstract class AsyncApiCall {

    private void getTokenAndPerformCall()
    {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth == null)
                    throw new IllegalStateException("FirebaseAuth is null");

                final FirebaseUser user = auth.getCurrentUser();
                if(user == null)
                    throw new IllegalStateException("No user logged in");


                Task<GetTokenResult> tokenTask = user.getToken(false);

                GetTokenResult tokenResult = tokenTask.getResult();
                if(tokenResult == null)
                    return;
                String token = tokenResult.getToken();
                try {
                    performCall(token);
                } catch (IOException e) {
                    return;
                }
            }
        });
    }
    public void execute()
    {
        getTokenAndPerformCall();
    }
    protected abstract void performCall(String token) throws IOException;

    public interface OnComplete<ParameterType>
    {
        void execute(ParameterType param);
    }
}
