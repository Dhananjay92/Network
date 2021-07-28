package com.dhanu.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
private boolean isConnected=false;
   private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();

    }
public void checkInternet(View v)
{
    boolean isConnected = NetworkChangeReceiver.checkConnectivity(context);
    if(isConnected)
        Toast.makeText(this,"You Are Online",Toast.LENGTH_SHORT).show();
    else
        Toast.makeText(this,"You Are Ofline",Toast.LENGTH_SHORT).show();
}
    // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
    public boolean isOnline() {
        new Thread() {
            @Override
            public void run() {
                try {
                    int timeoutMs = 1500;
                    Socket sock = new Socket();
                    SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
                    sock.connect(sockaddr, timeoutMs);
                    sock.close();
                    isConnected= true;
                } catch (IOException e) {
                    isConnected= false;
                }

            }

        }.start();
        return isConnected;
    }

}




