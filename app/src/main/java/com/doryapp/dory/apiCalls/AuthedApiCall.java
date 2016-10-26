package com.doryapp.dory.apiCalls;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class AuthedApiCall<ReturnType> extends ApiCall<ReturnType>{

    public AuthedApiCall()
    {
        call = new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(auth == null)
                    throw new IllegalStateException("FirebaseAuth is null");

                final FirebaseUser user = auth.getCurrentUser();
                if(user == null)
                    throw new IllegalStateException("No user logged in");


                Task<GetTokenResult> tokenTask = user.getToken(false);

                GetTokenResult tokenResult;

                try {
                    tokenResult = Tasks.await(tokenTask);
                } catch (ExecutionException | InterruptedException e) {
                    handle(e);
                    return;
                }

                if(tokenResult == null)
                    return;
                String token = tokenResult.getToken();
                try {
                    ReturnType value = performCall(token);
                    complete(value);
                } catch (IOException e) {
                    handle(e);
                    return;
                }
            }
        };
    }


    protected abstract ReturnType performCall(String token) throws IOException;
}
