package com.doryapp.dory.apiCalls;

import java.io.IOException;

public abstract class SimpleApiCall<ReturnType> extends ApiCall<ReturnType>{

    public SimpleApiCall() {
        call = new Runnable() {
            @Override
            public void run() {
                try {
                    ReturnType value = performCall();
                    complete(value);
                } catch (IOException e) {
                    handle(e);
                    //return;
                }
            }
        };
    }
    protected abstract ReturnType performCall() throws IOException;
}
