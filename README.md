# TestAIDL
参考地址＝http://www.linuxidc.com/Linux/2015-01/111148.htm


AIDL（Android Interface Definition Language）是Android接口定义语言的意思，它可以用于让某个Service与多个应用程序组件之间进行跨进程通信，从而可以实现多个应用程序共享同一个Service的功能。

言归正传，今天的主题是远程Service建立AIDL进行通信，通过一个小demo来展示AIDL在Android studio中的实现：

1. 搭建了一个简单的Service框架，仅包括startService(intent)，框架在后面代码中展示出来

2. 然后建立AIDL，通过点击建立AIDL文件，如图

建立好之后，出现AIDL文件如图

但是此时并没有AIDL的java文件产生，其实android studio也是带有自动生成的，只不过需要确认一些信息后才能生成。此时，我们可以在目录 build-->generated-->source-->aidl-->test-->debug下面发现还没有任何文件

此时，打开AndroidManifest.xml，确认package的值，如我这个

                                           

关键性的一步，确认aidl文件所在的包名和AndroidMainifest.xml的package名是否一致。如果一致，点击 Build-->Make Project，生成相应的java文件。如果不一致，则改aidl的包名，改成一致，再点击生成，生成效果如图。

则此时就可以在程序中通过AIDL调用远程Service的方法，实现AIDL与远程Service进行通信，代码展示如下。

MainActivity.java

public class MainActivity extends ActionBarActivity {

    private MyServiceAIDL myServiceAIDL;
    private Intent binderIntent;
    private final static boolean create_flag=true;
    private final static boolean destory_flag=false;
    private final static String TAG="MainActivity";

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myServiceAIDL = MyServiceAIDL.Stub.asInterface(service);
            try {
                //通过AIDL远程调用
                Log.d(TAG,"++start download++");
                myServiceAIDL.DownLoad();
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
        Log.d(TAG,"++MainActivity onCreate++");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //开启服务
        Intent intent = new Intent(this, MainService.class);
        startService(intent);

        //连接远程Service和Activity
        binderIntent = new Intent(this,MainService.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag",create_flag);
        binderIntent.putExtras(bundle);
        bindService(binderIntent, sc, BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"++MainActivity onDestroy++");

        boolean flag = false;
        //暂停服务
        Intent intent = new Intent(this, MainService.class);
        stopService(intent);

        //断开与远程Service的连接
        unbindService(sc);
    }
}

MainService.java

public class MainService extends Service {

    boolean flag;
    private final static String TAG = "MainService";

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "++MainService onDestroy++");
        flag = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "++MainService onCreate++");

        Notification no = new Notification(R.drawable.ic_launcher, "有通知到来", System.currentTimeMillis());
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        no.setLatestEventInfo(this, "AIDLDemo", "running", pi);
        startForeground(1, no);
    }

    @Override
    public IBinder onBind(Intent intent) {

        Bundle bundle = intent.getExtras();
        flag = bundle.getBoolean("flag");
        System.out.println(flag);
        return ms;
    }

    MyServiceAIDL.Stub ms = new MyServiceAIDL.Stub() {
        @Override
        public void DownLoad() throws RemoteException {

            new Thread(new Runnable() {
                int i = 0;

                @Override
                public void run() {
                    //未达到线程条件，会一直在后台运行，就算服务已经关闭
                    while (flag) {

                        try {
                            i++;
                            System.out.println("i的值是" + i);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("退出服务");
                }
            }).start();

        }
    };
}

AndroidManifest.xml

<application
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".MainService"
            android:process=":remote"></service>
    </application>

MyServiceAIDL.aidl

// myServiceAIDL.aidl
package com.example.wanghao.aidldemo;

// Declare any non-default types here with import statements

interface MyServiceAIDL {

    void DownLoad();
}


