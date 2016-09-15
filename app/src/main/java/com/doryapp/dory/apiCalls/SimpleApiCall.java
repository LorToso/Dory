package com.doryapp.dory.apiCalls;

import java.io.IOException;

public abstract class SimpleApiCall extends ApiCall{

    public SimpleApiCall() {
        call = new Runnable() {
            @Override
            public void run() {
                try {
                    performCall();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        };
    }
    protected abstract void performCall() throws IOException;
}
