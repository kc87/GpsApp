package kc87.gpsapp.presenter;


import android.util.Log;

import kc87.gpsapp.events.GpsUpdateEvent;
import kc87.gpsapp.util.NmeaParser;
import kc87.gpsapp.view.GpsView;
import kc87.gpsapp.view.GpsViewData;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

public class GpsPresenter {
   private static final String LOG_TAG = "GpsPresenter";
   private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
   private GpsView mGpsView;

   public GpsPresenter(final GpsView view) {
      mGpsView = view;
   }

   public void onResume() {
      Log.d(LOG_TAG, "onResume()");
      EventBus.getDefault().register(this);
   }

   public void onPause() {
      Log.d(LOG_TAG, "onPause()");
      EventBus.getDefault().unregister(this);
   }

   @SuppressWarnings("unused")
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onGpsUpdateEvent(final GpsUpdateEvent gpsUpdate) {
      final GpsViewData gpsViewDataSet = new GpsViewData();
      final NmeaParser.Data gpsData = gpsUpdate.data;
      final NmeaParser.State gpsState = gpsUpdate.state;

      if (gpsData.utcTime != null) {
         gpsViewDataSet.utcTime = TIME_FORMAT.format(gpsData.utcTime);
      }

      gpsViewDataSet.satellites = String.format("%d", gpsState.satsInView);
      gpsViewDataSet.latitude = String.format("%.6f°", gpsData.latitude);
      gpsViewDataSet.longitude = String.format("%.6f°", gpsData.longitude);
      gpsViewDataSet.altitude = String.format("%.1f m", gpsData.altitude);
      gpsViewDataSet.speedKmh = String.format("%.1f km/h", gpsData.speedKmh);
      gpsViewDataSet.course = String.format("%.1f°", gpsData.course);
      gpsViewDataSet.hasFix = gpsState.hasFix;

      mGpsView.updateGpsView(gpsViewDataSet);
   }
}
