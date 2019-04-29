package com.example.jayroop.rpm_android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static android.hardware.SensorManager.SENSOR_STATUS_NO_CONTACT;
import static android.hardware.SensorManager.SENSOR_STATUS_UNRELIABLE;

public class PPGHeart extends Activity implements SensorEventListener, View.OnClickListener {

        private Sensor mHeartRateSensor;
        private SensorManager mSensorManager;
        private static final String TAG = "HeartRateFragment";
        private static final int SENSOR_PERMISSION_CODE = 123;
        private ArrayList<Float> hrstuff_1 = new ArrayList<>();
        private ArrayList<Long> time_int = new ArrayList<>();

        //Shared references
        private SharedPreferences savedValues;

        private Button start;
        private Button stop;

        long tStart;
        double elapsedSeconds;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ppgheart);

            start = (Button)findViewById(R.id.start);
            stop = (Button)findViewById(R.id.stop);

            start.setOnClickListener(this);
            stop.setOnClickListener(this);


            mSensorManager = ((SensorManager)getApplicationContext().getSystemService(Context.SENSOR_SERVICE));
            mHeartRateSensor = mSensorManager.getDefaultSensor(65572);



        //    List<Sensor> ss;
        //    ss = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //
        //    for (Sensor s: ss)
        //    {
        //      Log.d("SENSORS" ,s+ "");
        //
        //    }
        }
        @Override
        public void onSensorChanged(SensorEvent event) {

       /* if(event.sensor.getType() == Sensor.TYPE_HEART_RATE && event.values.length > 0)
        {
        }*/
            if (event.sensor.getType() == 65572 &&
                event.values.length > 0){


                if((event.accuracy != SENSOR_STATUS_UNRELIABLE) && (event.accuracy != SENSOR_STATUS_NO_CONTACT)) {


                    hrstuff_1.add(event.values[0]);
                    time_int.add(event.timestamp);


                }

            }
        }
        private void writeToFile(String data,Context context) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.start)
            {
                tStart= SystemClock.elapsedRealtime();
                boolean isRegistered = mSensorManager.registerListener(this,
                    this.mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            else
            {
                long tEnd = SystemClock.elapsedRealtime();;
                mSensorManager.unregisterListener(this,  this.mHeartRateSensor);
                Log.d("TAG", hrstuff_1.toString());
                writeToFile(hrstuff_1.toString() +"\n", getApplicationContext());
                long tDelta = tEnd - tStart;
                elapsedSeconds = tDelta / 1000.0;
                writeFirestore_rate();
                Log.d("TAG", elapsedSeconds + "");

            }
        }

        private void writeFirestore_rate() {
            //Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
            db.setFirestoreSettings(settings);
            Log.d(TAG, "got here");
            Date d = new Date();
            // Create a new user with a first, middle, and last name
            Map<String, Object> steps = new HashMap<>();
            steps.put("duration", elapsedSeconds);
            steps.put("PPG_RAW_INDEX_1", hrstuff_1);
            steps.put("Times", time_int);

            // Create a new user with a first, middle, and last name
            Map<String, Object> title = new HashMap<>();
            steps.put("NAME", "NAME");

//            SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(this);
//            String username = sharedPreferences.getString("CurrentUser", "");

            String username =Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Toast.makeText(getApplicationContext(), username+"", Toast.LENGTH_SHORT).show();


            db.collection("Users").document(username)
                .set(title)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                });



            db.collection("Users").document(username).collection("PPG").document(d.toString())
                .set(steps)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                });

        }

    }
