package kc87.gpsapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import kc87.gpsapp.model.GpsService;
import kc87.gpsapp.presenter.GpsPresenter;
import kc87.gpsapp.view.GpsView;


public class MainActivity extends Activity implements ServiceConnection
{
   private static final String LOG_TAG = "MainActivity";
   private GpsService mGpsService;
   private GpsPresenter mGpsPresenter;


   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      mGpsPresenter = ((GpsView)findViewById(R.id.gps_view)).getPresenter();
      bindService(new Intent(this, GpsService.class), this, Context.BIND_AUTO_CREATE);
   }

   @Override
   protected void onResume()
   {
      Log.d(LOG_TAG, "onResume()");
      super.onResume();
      mGpsPresenter.onResume();
      if(mGpsService != null) {
         mGpsService.start();
      }
   }

   @Override
   protected void onPause()
   {
      Log.d(LOG_TAG, "onPause()");
      super.onPause();
      mGpsPresenter.onPause();
      if(mGpsService != null) {
         mGpsService.stop();
      }
   }

   @Override
   protected void onDestroy()
   {
      Log.d(LOG_TAG, "onDestroy()");
      unbindService(this);
      super.onDestroy();
   }

   @Override
   public void onServiceConnected(ComponentName name, IBinder service)
   {
      Log.d(LOG_TAG, "onServiceConnected()");
      mGpsService = ((GpsService.LocalBinder) service).getService();
      mGpsService.start();
   }

   // Only gets called when service has crashed!!
   @Override
   public void onServiceDisconnected(ComponentName name)
   {
      Log.d(LOG_TAG, "onServiceDisconnected(): Service has crashed!!");
      mGpsService = null;
   }

}
