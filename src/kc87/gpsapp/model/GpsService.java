package kc87.gpsapp.model;


import android.app.Service;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import de.greenrobot.event.EventBus;
import kc87.gpsapp.events.GpsUpdateEvent;
import kc87.gpsapp.util.NmeaParser;

public class GpsService extends Service implements LocationListener, GpsStatus.NmeaListener
{
   private static final String LOG_TAG = "GpsService";
   private static final long UPDATE_INTERVAL = 1000;
   private LocalBinder mLocalBinder = new LocalBinder();
   private LocationManager mLocationManager;
   private long lastEventSend = 0;

   @Override
   public void onCreate()
   {
      Log.d(LOG_TAG, "onCreate()");
      super.onCreate();
      mLocationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

      if(mLocationManager != null){
         mLocationManager.addNmeaListener(this);
      }
   }

   @Override
   public IBinder onBind(Intent intent)
   {
      return mLocalBinder;
   }

   @Override
   public void onDestroy()
   {
      Log.d(LOG_TAG, "onDestroy()");
      super.onDestroy();
   }


   public void start()
   {
      if(mLocationManager != null){
         mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.0f, this);
      }
   }

   public void stop()
   {
      if(mLocationManager != null){
         mLocationManager.removeUpdates(this);
      }
   }

   @Override
   public void onNmeaReceived(long timestamp, String nmea)
   {
      NmeaParser.parseSentence(nmea);
      if(System.currentTimeMillis() - lastEventSend > UPDATE_INTERVAL) {
         EventBus.getDefault().post(new GpsUpdateEvent());
         lastEventSend = System.currentTimeMillis();
      }
   }


   public class LocalBinder extends Binder
   {
      public GpsService getService()
      {
         return GpsService.this;
      }
   }


   @Override
   public void onLocationChanged(Location location) {}
   @Override
   public void onStatusChanged(String provider, int status, Bundle extras) {}
   @Override
   public void onProviderEnabled(String provider) {}
   @Override
   public void onProviderDisabled(String provider) {}
}
