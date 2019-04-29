package com.example.jayroop.rpm_android;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button calButton;
    private Button stepButton;
    private Button heartButton;
    private Button dataButton;
    private Button sessionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calButton = (Button)findViewById(R.id.button);
        stepButton =(Button)findViewById(R.id.button2);
        heartButton = (Button)findViewById(R.id.button3);
        dataButton = (Button)findViewById(R.id.button4);
        sessionButton = (Button)findViewById(R.id.button5);

        calButton.setOnClickListener(this);
        stepButton.setOnClickListener(this);
        heartButton.setOnClickListener(this);
        dataButton.setOnClickListener(this);
        sessionButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.button)
        {

        }
        else if(v.getId() == R.id.button2)
        {
            Intent i = new Intent(MainActivity.this, StepsActivity.class);
            startActivity(i);

        }
        else if(v.getId() == R.id.button3)
        {
            Intent i = new Intent(MainActivity.this, PPGHeart.class);
            startActivity(i);
        }
        else if(v.getId() ==  R.id.button4)
        {

            Intent i = new Intent(MainActivity.this, ihealthman.class);
            startActivity(i);
        }
        else if(v.getId() == R.id.button5)
        {
            Intent i = new Intent(MainActivity.this, History2Activity.class);
            startActivity(i);
        }

    }
}
