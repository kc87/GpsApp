package kc87.gpsapp.view;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import kc87.gpsapp.R;
import kc87.gpsapp.presenter.GpsPresenter;

import java.util.ArrayList;
import java.util.List;

public class GpsView extends LinearLayout {
   private static final String LOG_TAG = "GpsView";
   private GpsPresenter mPresenter;
   private List<TextView> mValueViewList;

   @InjectView(R.id.sats_value)
   TextView mSatsValueView;
   @InjectView(R.id.time_value)
   TextView mTimeValueView;
   @InjectView(R.id.lat_value)
   TextView mLatValueView;
   @InjectView(R.id.lng_value)
   TextView mLngValueView;
   @InjectView(R.id.alt_value)
   TextView mAltValueView;
   @InjectView(R.id.speed_value)
   TextView mSpeedValueView;
   @InjectView(R.id.course_value)
   TextView mCourseValueView;


   public GpsView(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public GpsPresenter getPresenter() {
      return mPresenter;
   }

   @Override
   protected void onFinishInflate() {
      super.onFinishInflate();
      ButterKnife.inject(this);
      setup();
   }

   public void updateGpsView(final GpsViewData gpsDataSet) {
      mSatsValueView.setText(gpsDataSet.satellites);
      mTimeValueView.setText(gpsDataSet.utcTime);
      mLatValueView.setText(gpsDataSet.latitude);
      mLngValueView.setText(gpsDataSet.longitude);
      mAltValueView.setText(gpsDataSet.altitude);
      mSpeedValueView.setText(gpsDataSet.speedKmh);
      mCourseValueView.setText(gpsDataSet.course);

      for (TextView valueView : mValueViewList) {
         valueView.setTextColor(gpsDataSet.hasFix ? Color.GREEN : Color.DKGRAY);
      }
   }

   private void setup() {
      mValueViewList = new ArrayList<>();

      mValueViewList.add(mSatsValueView);
      mValueViewList.add(mTimeValueView);
      mValueViewList.add(mLatValueView);
      mValueViewList.add(mLngValueView);
      mValueViewList.add(mAltValueView);
      mValueViewList.add(mSpeedValueView);
      mValueViewList.add(mCourseValueView);

      mPresenter = new GpsPresenter(this);
   }

}
