package kc87.gpsapp.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import de.greenrobot.event.EventBus;
import kc87.gpsapp.events.GpsUpdateEvent;
import kc87.gpsapp.model.GpsService;
import kc87.gpsapp.util.NmeaParser;
import kc87.gpsapp.view.GpsViewData;
import kc87.gpsapp.view.MainView;

import java.text.SimpleDateFormat;

public class MainPresenter implements ServiceConnection
{
   private static final String LOG_TAG = "MainPresenter";
   private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
   private GpsService mGpsService;
   private MainView mMainView;

   public MainPresenter(final MainView view)
   {
      mMainView = view;
   }

   public void onCreate()
   {
      final Context ctx = (Context)mMainView;
      ctx.bindService(new Intent(ctx, GpsService.class), this, Context.BIND_AUTO_CREATE);
   }

   public void onResume()
   {
      Log.d(LOG_TAG, "onResume()");
      if(mGpsService != null) {
         mGpsService.start();
      }

      EventBus.getDefault().register(this);
   }

   public void onPause()
   {
      Log.d(LOG_TAG,"onPause()");
      if(mGpsService != null) {
         mGpsService.stop();
      }

      EventBus.getDefault().unregister(this);
   }

   public void onDestroy()
   {
      final Context ctx = (Context)mMainView;
      ctx.unbindService(this);
   }

   @SuppressWarnings("unused")
   public void onEvent(final GpsUpdateEvent gpsUpdateEvent)
   {
      final GpsViewData gpsViewDataSet = new GpsViewData();
      final NmeaParser.Data gpsData = NmeaParser.getData();
      final NmeaParser.State gpsState = NmeaParser.getState();

      if(gpsData.utcTime != null) {
         gpsViewDataSet.utcTime = TIME_FORMAT.format(gpsData.utcTime);
      }

      gpsViewDataSet.satellites = String.format("%d", gpsState.satsInView);
      gpsViewDataSet.latitude = String.format("%.6f°", gpsData.latitude);
      gpsViewDataSet.longitude = String.format("%.6f°", gpsData.longitude);
      gpsViewDataSet.altitude = String.format("%.1f m", gpsData.altitude);
      gpsViewDataSet.speedKmh = String.format("%.1f km/h", gpsData.speedKmh);
      gpsViewDataSet.course = String.format("%.1f°", gpsData.course);
      gpsViewDataSet.hasFix = gpsState.hasFix;

      mMainView.updateGpsView(gpsViewDataSet);
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
