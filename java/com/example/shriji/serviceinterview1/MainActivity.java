package com.example.shriji.serviceinterview1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtData)
    TextView mTxtData;

    private SensorManager mSensorManager;
    Sensor light;



    String mMessage;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("", "=== onReceive: ");
            mMessage = intent.getStringExtra(MyIntentService.MESSAGE_KEY);
            mTxtData.append(mMessage);
        }
    };
    @BindView(R.id.btnShow)
    Button mBtnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


      //  mTxtData.setText(mMessage);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 1; i < mList.size(); i++) {
            mTxtData.append("\n" + mList.get(i).getName() +
                    "\n" + mList.get(i).getVendor() +
                    "\n" + mList.get(i).getVersion());
        }
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(new SensorEventListener() {


            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.d("", "=== onSensorChanged: "+event.accuracy);
                Log.d("", "=== onSensorChanged: "+event.values[0]+" - "+event.values[1]+" - "+event.values[2]+" == ");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.d("", "=== onAccuracyChanged: "+accuracy);

            }
        }, light, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mReceiver);

        super.onPause();
    }



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mReceiver, new IntentFilter(MyIntentService.SERVICE_MESSAGE));

    }

    @OnClick(R.id.btnShow)
    public void onViewClicked() {
        MyIntentService.startActionFoo(this, "value1", "value2");

    }
}
