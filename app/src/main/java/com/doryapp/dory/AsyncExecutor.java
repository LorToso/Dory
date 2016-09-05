package com.doryapp.dory;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by Lorenzo Toso on 05.09.2016.
 */
public class AsyncExecutor implements Executor{
    @Override
    public void execute(@NonNull final Runnable runnable) {
        AsyncTask.execute(runnable);
    }
}
