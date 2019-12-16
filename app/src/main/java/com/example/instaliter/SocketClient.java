package com.example.instaliter;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketClient extends AppCompatActivity
{
    private static Socket mSocket;

    private static void initSocket() {
        try
        {
            mSocket = IO.socket(RegisterActivity.registerurl);

            mSocket.connect();
            try{

                JSONObject json = new JSONObject();

                json.put("id", RegisterActivity.userID); //EZ
                mSocket.emit("setIdentifier", json);
            }
            catch (Exception e)
            {
                Log.d("EXC", e.toString());
            }

        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void disconnectSocket()
    {
        mSocket.disconnect();
    }

    public static Socket getInstance() {
        if (mSocket != null)
        {
            if(mSocket.connected())
            {return mSocket;}
            else
            {
                initSocket();
                return mSocket;
            }
        }
        else
        {
            initSocket();
            return mSocket;
        }
    }

}
