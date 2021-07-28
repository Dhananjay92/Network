package com.dhanu.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 9/13/17.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "NetworkChangeReceiver";
    private boolean isConnected = false;
    private static Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        //isNetworkAvailable(context);
        this.context=context;
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            checkConnectivity(context);
        }

    }
    public static boolean checkActiveInternetConnection() {
       final boolean isAvailable = false;
        if (isNetworkAvailable()) {
            final Handler handler = new Handler();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                                    urlc.setRequestProperty("User-Agent", "Test");
                                    urlc.setRequestProperty("Connection", "close");
                                    urlc.setConnectTimeout(1500);
                                    urlc.connect();
                                    if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        globals.isConnected=true;
                                    } else {
                                        globals.isConnected=false;
                                    }
                                } catch (IOException e) {
                                    Log.e(LOG_TAG, "Error: ", e);
                                }
                            }
                        });
                    }
                }).start();

        } else {
            Log.d(LOG_TAG, "No network present");
        }
        return globals.isConnected;
    }

    public static boolean checkConnectivity(final Context context) {//
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isConnect = isAbleToConnect(context);//isAbleToConnect("http://www.google.com", 1000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isConnect) {
                            globals.isConnected= true;
                             Toast.makeText(context, "You are ONLINE!", Toast.LENGTH_SHORT).show();
                        }else{
                            globals.isConnected= false;
                             Toast.makeText(context, "You are OFFLINE!", Toast.LENGTH_SHORT).show();
                        }}
                });

            }
        }).start();
        return globals.isConnected;
    }

    private static boolean isAbleToConnect(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ((networkInfo != null && networkInfo.isConnected())
                && ((networkInfo.getType() == ConnectivityManager.TYPE_WIFI) || (networkInfo
                .getType() == ConnectivityManager.TYPE_MOBILE))) {
            HttpURLConnection urlc;
            try {
                urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(15000);
                urlc.connect();
                if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;

            }
        } else {
            return false;
        }
    }

    private static boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }
}
