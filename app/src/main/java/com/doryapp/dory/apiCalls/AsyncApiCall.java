package com.doryapp.dory.apiCalls;

import com.google.android.gms.tasks.OnSuccessListener;
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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth == null)
            throw new IllegalStateException("FirebaseAuth is null");

        FirebaseUser user = auth.getCurrentUser();
        if(user == null)
            throw new IllegalStateException("No user logged in");

        user.getToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String token = getTokenResult.getToken();
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
