package com.example.compass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView mAccelerometerX;
    TextView mAccelerometerY;
    TextView mAccelerometerZ;
    TextView mMagneticX;
    TextView mMagneticY;
    TextView mMagneticZ;
    TextView mProximity;
    TextView mLight;
    SensorManager sensorManager;
    Sensor mAccelerometerSensor;
    Sensor mProximitySensor;
    Sensor mMagneticSensor;
    Sensor mLightSensor;
    float mMaxValue;
    float mValue;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализировать sensorManager перед его использованием
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Проверка наличия магнитного сенсора
        mMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (mMagneticSensor == null) {
            Log.e("MainActivity", "Устройство не имеет магнитного сенсора.");
            // Добавьте соответствующее уведомление или обработку
        }

        // Перенесено из onCreate
        Button buttonShowCompass = findViewById(R.id.buttonShowCompass);
        buttonShowCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Создать Intent для перехода на другую активность (например, CompassActivity)
                Intent intent = new Intent(MainActivity.this, AnotherCompassActivity.class);

                // Добавить необходимые данные в Intent (если нужно)
                // intent.putExtra("key", "value");

                // Запустить активность
                startActivity(intent);
            }
        });

        mAccelerometerX = findViewById(R.id.textView);
        mAccelerometerY = findViewById(R.id.textView2);
        mAccelerometerZ = findViewById(R.id.textView3);
        mMagneticX = findViewById(R.id.textView5);
        mMagneticY = findViewById(R.id.textView6);
        mMagneticZ = findViewById(R.id.textView7);
        mProximity = findViewById(R.id.textView9);
        mLight = findViewById(R.id.textView11);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMaxValue = mLightSensor.getMaximumRange();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            mAccelerometerX.setText(Float.toString(event.values[0]));
            mAccelerometerY.setText(Float.toString(event.values[1]));
            mAccelerometerZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            Log.d("MagneticSensor", "X: " + event.values[0] + ", Y: " + event.values[1] + ", Z: " + event.values[2]);
            mMagneticX.setText(Float.toString(event.values[0]));
            mMagneticY.setText(Float.toString(event.values[1]));
            mMagneticZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            mProximity.setText(Float.toString(event.values[0]));
        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            mLight.setText(Float.toString(event.values[0]));
            mValue =event.values[0];
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = (int)(255f* mValue / mMaxValue);
            getWindow().setAttributes(layout);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mMagneticSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mProximitySensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, mLightSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onStop() {
        super.onStop();
        super.onStop();
        sensorManager.unregisterListener(this, mAccelerometerSensor);
        sensorManager.unregisterListener(this, mMagneticSensor);
        sensorManager.unregisterListener(this, mProximitySensor);
        sensorManager.unregisterListener(this, mLightSensor);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        sensorManager.unregisterListener(this, mLightSensor);
    }
}