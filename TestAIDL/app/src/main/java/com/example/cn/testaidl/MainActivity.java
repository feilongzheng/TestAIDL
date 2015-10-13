package com.example.cn.testaidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class MainActivity extends Activity {

    IMyAidlInterface myAidlInterface;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                myAidlInterface.DownLoad();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //open service
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        //connect activity and service;
        Intent binderIntent = new Intent(this, MainService.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", true);
        binderIntent.putExtras(bundle);
        //bind service and activity
        this.bindService(binderIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, MainService.class);
        //stopservice
        stopService(intent);
        //unbindservice
        unbindService(serviceConnection);
    }


}
