package com.example.instaliter;

import android.app.Application;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ApplicationOwner extends Application implements LifecycleObserver
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {

        if(RegisterActivity.userID > 0)
        {

            SocketClient.getInstance();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground()
    {
        SocketClient.disconnectSocket();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause()
    {
        SocketClient.disconnectSocket();
    }


}