package kc87.gpsapp;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import kc87.gpsapp.model.GpsService;
import kc87.gpsapp.presenter.GpsPresenter;
import kc87.gpsapp.view.GpsView;

import static android.Manifest.permission.*;


public class MainActivity extends Activity implements ServiceConnection {
   private static final String LOG_TAG = "MainActivity";
   private static final int PERMISSION_CODE = 42;
   private GpsService mGpsService;
   private GpsPresenter mGpsPresenter;
   private boolean mIsGpsServiceBound = false;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      mGpsPresenter = ((GpsView) findViewById(R.id.gps_view)).getPresenter();
      setupService();
   }

   @Override
   protected void onResume() {
      Log.d(LOG_TAG, "onResume()");
      super.onResume();
      mGpsPresenter.onResume();
      if (mGpsService != null) {
         mGpsService.start();
      }
   }

   @Override
   protected void onPause() {
      Log.d(LOG_TAG, "onPause()");
      super.onPause();
      mGpsPresenter.onPause();
      if (mGpsService != null) {
         mGpsService.stop();
      }
   }

   @Override
   protected void onDestroy() {
      Log.d(LOG_TAG, "onDestroy()");
      if (mIsGpsServiceBound) {
         unbindService(this);
      }
      super.onDestroy();
   }

   @Override
   public void onServiceConnected(ComponentName name, IBinder service) {
      Log.d(LOG_TAG, "onServiceConnected()");
      mIsGpsServiceBound = true;
      mGpsService = ((GpsService.LocalBinder) service).getService();
      mGpsService.start();
   }

   // Only gets called when service has crashed!!
   @Override
   public void onServiceDisconnected(ComponentName name) {
      Log.d(LOG_TAG, "onServiceDisconnected(): Service has crashed!!");
      mIsGpsServiceBound = false;
      mGpsService = null;
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      Log.d(LOG_TAG, "onRequestPermissionsResult()");
      if (requestCode == PERMISSION_CODE) {
         if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            bindService(new Intent(this, GpsService.class), this, Context.BIND_AUTO_CREATE);
         }
      }
   }

   private void setupService() {
      int hasGpsPermission = checkSelfPermission(ACCESS_FINE_LOCATION);

      if (hasGpsPermission != PackageManager.PERMISSION_GRANTED) {
         requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_CODE);
         return;
      }

      bindService(new Intent(this, GpsService.class), this, Context.BIND_AUTO_CREATE);
   }

}
