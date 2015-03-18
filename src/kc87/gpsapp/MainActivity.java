package kc87.gpsapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kc87.gpsapp.presenter.MainPresenter;
import kc87.gpsapp.view.GpsViewData;
import kc87.gpsapp.view.MainView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements MainView
{
   private static final String LOG_TAG = "";
   private MainPresenter mPresenter;
   private List<TextView> mValueViewList;

   @InjectView(R.id.sats_value) TextView mSatsValueView;
   @InjectView(R.id.time_value) TextView mTimeValueView;
   @InjectView(R.id.lat_value) TextView mLatValueView;
   @InjectView(R.id.lng_value) TextView mLngValueView;
   @InjectView(R.id.alt_value) TextView mAltValueView;
   @InjectView(R.id.speed_value) TextView mSpeedValueView;
   @InjectView(R.id.course_value) TextView mCourseValueView;


   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      ButterKnife.inject(this);
      setup();
   }

   @Override
   protected void onResume()
   {
      Log.d(LOG_TAG, "onResume()");
      super.onResume();
      mPresenter.onResume();
   }

   @Override
   protected void onPause()
   {
      Log.d(LOG_TAG,"onPause()");
      super.onPause();
      mPresenter.onPause();
   }


   @Override
   protected void onDestroy()
   {
      Log.d(LOG_TAG,"onDestroy()");
      mPresenter.onDestroy();
      super.onDestroy();
   }

   private void setup()
   {
      mPresenter = new MainPresenter(this);

      mValueViewList = new ArrayList<>();

      mValueViewList.add(mSatsValueView);
      mValueViewList.add(mTimeValueView);
      mValueViewList.add(mLatValueView);
      mValueViewList.add(mLngValueView);
      mValueViewList.add(mAltValueView);
      mValueViewList.add(mSpeedValueView);
      mValueViewList.add(mCourseValueView);

      mPresenter.onCreate();
   }

   @Override
   public void updateGpsView(final GpsViewData gpsDataSet)
   {
      mSatsValueView.setText(gpsDataSet.satellites);
      mTimeValueView.setText(gpsDataSet.utcTime);
      mLatValueView.setText(gpsDataSet.latitude);
      mLngValueView.setText(gpsDataSet.longitude);
      mAltValueView.setText(gpsDataSet.altitude);
      mSpeedValueView.setText(gpsDataSet.speedKmh);
      mCourseValueView.setText(gpsDataSet.course);

      for(TextView valueView: mValueViewList){
         valueView.setTextColor(gpsDataSet.hasFix ? Color.GREEN : Color.DKGRAY);
      }
   }
}
