package com.example.jayroop.rpm_android;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoryActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    View.OnClickListener
    {

    private static final int REQUEST_OAUTH = 1;

    /**
     *  Track whether an authorization activity is stacking over the current activity, i.e. when
     *  a known auth error is being resolved, such as showing the account chooser or presenting a
     *  consent dialog. This avoids common duplications as might happen on screen rotations, etc.
     */
      private static final String AUTH_PENDING = "auth_state_pending";
      private boolean authInProgress = false;

        private Button mButtonViewWeek;
        private Button mButtonViewToday;
        private Button mButtonAddSteps;
        private Button mButtonUpdateSteps;
        private Button mButtonDeleteSteps;

        private GoogleApiClient mApiClient;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

            mButtonViewWeek = (Button) findViewById(R.id.btn_view_week);
            mButtonViewToday = (Button) findViewById(R.id.btn_view_today);
            mButtonAddSteps = (Button) findViewById(R.id.btn_add_steps);
            mButtonUpdateSteps = (Button) findViewById(R.id.btn_update_steps);
            mButtonDeleteSteps = (Button) findViewById(R.id.btn_delete_steps);

            mButtonViewWeek.setOnClickListener(this);
            mButtonViewToday.setOnClickListener(this);
            mButtonAddSteps.setOnClickListener(this);
            mButtonUpdateSteps.setOnClickListener(this);
            mButtonDeleteSteps.setOnClickListener(this);

            mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_TEMPERATURE_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ_WRITE))
                .addScope(new Scope(Scopes.EMAIL))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        }


        @Override
        public void onConnectionSuspended(int i) {
            Log.e("HistoryAPI", "onConnectionSuspended");
        }


        protected void onStart() {
            super.onStart();
            mApiClient.connect();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if( requestCode == REQUEST_OAUTH ) {
                authInProgress = false;
                if( resultCode == RESULT_OK ) {
                    if( !mApiClient.isConnecting() && !mApiClient.isConnected() ) {
                        mApiClient.connect();
                    }
                } else if( resultCode == RESULT_CANCELED ) {
                    Log.e( "GoogleFit", "RESULT_CANCELED" );
                }
            } else {
                Log.e("GoogleFit", "requestCode NOT request_oauth");
            }
        }

        @Override
        public void onConnected(Bundle bundle) {

            new ViewWeekStepCountTask().doInBackground();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            if( !authInProgress ) {
                try {
                    authInProgress = true;
                    connectionResult.startResolutionForResult( HistoryActivity.this, REQUEST_OAUTH );
                } catch(IntentSender.SendIntentException e ) {

                }
            } else {
                Log.e( "GoogleFit", "authInProgress" );
            }
        }


        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.btn_view_today)
            {

            }
        }

        private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {
            protected Void doInBackground(Void... params) {
                displayLastWeeksData();
                return null;
            }


        }

        private void displayLastWeeksData() {
            Calendar cal = Calendar.getInstance();
            Date now = new Date();
            cal.setTime(now);
            long endTime = cal.getTimeInMillis();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            long startTime = cal.getTimeInMillis();

            java.text.DateFormat dateFormat = DateFormat.getDateInstance();
            Log.e("History", "Range Start: " + dateFormat.format(startTime));
            Log.e("History", "Range End: " + dateFormat.format(endTime));

            //Check how many steps were walked and recorded in the last 7 days
            DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mApiClient, readRequest).await(1000, TimeUnit.MILLISECONDS);

            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showDataSet(dataSet);
                    }
                }
            }
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showDataSet(dataSet);
                }
            }
        }

        private void showDataSet(DataSet dataSet) {
            Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
            DateFormat dateFormat = DateFormat.getDateInstance();
            DateFormat timeFormat = DateFormat.getTimeInstance();

            for (DataPoint dp : dataSet.getDataPoints()) {
                Log.e("History", "Data point:");
                Log.e("History", "\tType: " + dp.getDataType().getName());
                Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                for(Field field : dp.getDataType().getFields()) {
                    Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                }
            }
        }

        private void displayStepDataForToday() {
            DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mApiClient, DataType.TYPE_STEP_COUNT_DELTA ).await(1, TimeUnit.MINUTES);
            showDataSet(result.getTotal());
        }


    }
