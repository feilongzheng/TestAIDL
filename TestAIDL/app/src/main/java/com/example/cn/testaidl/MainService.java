package com.example.cn.testaidl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by pengsier on 15/10/13.
 */
public class MainService extends Service {
    boolean flag;

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i("MainService-onDestroy");
        flag = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("MainService-onCreate");

        Notification no = new Notification(R.mipmap.ic_launcher, "有通知到来", System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        no.setLatestEventInfo(this, "AIDLDemo2", "running", pi);
        startForeground(1, no);
//        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(1, no);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle = intent.getExtras();
        flag = bundle.getBoolean("flag");
        LogUtils.i("flag=" + flag);

        return ms;
    }

    IMyAidlInterface.Stub ms = new IMyAidlInterface.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void DownLoad() throws RemoteException {
            new Thread(){
                int i = 0;

                @Override
                public void run() {
                    while (flag){
                        try {
                            i++;
                            LogUtils.i("i="+i);
                            Thread.sleep(1000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    LogUtils.i("run()");
                }
            }.start();
        }
    };
/**
 * 开启两个MainActivity只会有一个MainService存在，调了两次DownLoad（）
 *  I/MainService.onCreate(L:37)﹕ MainService-onCreate
 10-13 18:36:50.993    1639-1639/com.example.cn.testaidl:remote I/MainService.onBind(L:52)﹕ flag=true
 10-13 18:36:51.052    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=1
 10-13 18:36:52.052    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=2
 10-13 18:36:53.052    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=3
 10-13 18:36:54.053    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=4
 10-13 18:36:55.053    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=5
 10-13 18:36:56.053    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=6
 10-13 18:36:57.053    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=7
 10-13 18:36:58.054    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=8
 10-13 18:36:59.055    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=9
 10-13 18:37:00.056    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=10
 10-13 18:37:01.057    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=11
 10-13 18:37:01.514    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=1
 10-13 18:37:02.058    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=12
 10-13 18:37:02.515    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=2
 10-13 18:37:03.058    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=13
 10-13 18:37:03.515    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=3
 10-13 18:37:04.059    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=14
 10-13 18:37:04.516    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=4
 10-13 18:37:05.059    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=15
 10-13 18:37:05.517    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=5
 10-13 18:37:06.060    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=16
 10-13 18:37:06.518    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=6
 10-13 18:37:07.061    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=17
 10-13 18:37:07.519    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=7
 10-13 18:37:08.063    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=18
 10-13 18:37:08.520    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=8
 10-13 18:37:09.063    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=19
 10-13 18:37:09.521    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=9
 10-13 18:37:10.064    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=20
 10-13 18:37:10.522    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=10
 10-13 18:37:11.065    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=21
 10-13 18:37:11.523    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=11
 10-13 18:37:12.067    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=22
 10-13 18:37:12.524    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=12
 10-13 18:37:13.068    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=23
 10-13 18:37:13.526    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=13
 10-13 18:37:14.069    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=24
 10-13 18:37:14.526    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=14
 10-13 18:37:15.070    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=25
 10-13 18:37:15.527    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=15
 10-13 18:37:16.071    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=26
 10-13 18:37:16.528    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=16
 10-13 18:37:17.073    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=27
 10-13 18:37:17.529    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=17
 10-13 18:37:18.074    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=28
 10-13 18:37:18.533    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=18
 10-13 18:37:19.075    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=29
 10-13 18:37:19.534    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=19
 10-13 18:37:20.076    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=30
 10-13 18:37:20.535    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=20
 10-13 18:37:21.077    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=31
 10-13 18:37:21.536    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=21
 10-13 18:37:22.079    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=32
 10-13 18:37:22.537    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=22
 10-13 18:37:23.080    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=33
 10-13 18:37:23.538    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=23
 10-13 18:37:24.081    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=34
 10-13 18:37:24.539    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=24
 10-13 18:37:25.083    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=35
 10-13 18:37:25.541    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=25
 10-13 18:37:26.084    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=36
 10-13 18:37:26.542    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=26
 10-13 18:37:27.085    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=37
 10-13 18:37:27.543    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=27
 10-13 18:37:28.086    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=38
 10-13 18:37:28.544    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=28
 10-13 18:37:29.087    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=39
 10-13 18:37:29.545    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=29
 10-13 18:37:30.087    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=40
 10-13 18:37:30.546    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=30
 10-13 18:37:31.088    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=41
 10-13 18:37:31.546    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=31
 10-13 18:37:32.088    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:74)﹕ i=42
 10-13 18:37:32.448    1639-1639/com.example.cn.testaidl:remote I/MainService.onDestroy(L:25)﹕ MainService-onDestroy
 10-13 18:37:32.547    1639-2408/com.example.cn.testaidl:remote I/MainService$1$1.run(L:81)﹕ run()
 10-13 18:37:33.089    1639-1662/com.example.cn.testaidl:remote I/MainService$1$1.run(L:81)﹕ run()
 */


}
