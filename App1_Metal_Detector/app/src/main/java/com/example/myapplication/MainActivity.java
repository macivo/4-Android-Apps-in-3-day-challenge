package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static TextView notification;
    private ProgressBar progressBar;
    private SensorManager sensorManager;

    // ActivityResult with Barcode Scanner
    ActivityResultLauncher<Intent> qrActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    // Call log-Function
                    if (data != null) {
                        log(data.getStringExtra("SCAN_RESULT"));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notification = findViewById(R.id.notificationText);
        progressBar = findViewById(R.id.progressBar);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Button to Scan QR Code, then send the result to LogBuch App
        Button button = findViewById(R.id.summitQr);
        button.setOnClickListener(v -> {
            try {
                qrActivityResultLauncher.launch(new Intent("com.google.zxing.client.android.SCAN"));
            } catch (ActivityNotFoundException e){
                notification.setText(getResources().getString(R.string.error_scan_app));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX)+(magY * magY) +(magZ * magZ));
            progressBar.setProgress((int)magnitude);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Crate a Logbook Format as JSON and send via LogBuch App Activity
     * @param solution - String from QR Barcode (Barcode Scanner)
     */
    private void log(String solution) {
        Intent intent = new Intent("ch.blockwoche.intent.LOG");
        // format depends on app, see logbook format guideline
        JSONObject log = new JSONObject();
        try {
            log.put("task", "Metaldetector");
            log.put("solution", solution);
        } catch (JSONException j){
            notification.setText(R.string.error_log_put);
        }
        intent.putExtra("ch.blockwoche.logmessage", log.toString());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e){
            notification.setText(getResources().getString(R.string.error_log_app));
        }
    }
}
