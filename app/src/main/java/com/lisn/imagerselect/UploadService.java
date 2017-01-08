package com.lisn.imagerselect;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class UploadService extends Service {
    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new upLoadThread().start();
    }

    private class upLoadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e("---", "handleMessage: -----" );
                    Toast.makeText(getApplicationContext(), "handler msg", Toast.LENGTH_LONG).show();
                }
            };

            handler.sendEmptyMessage(1);
            Looper.loop();
        }
    }

}
