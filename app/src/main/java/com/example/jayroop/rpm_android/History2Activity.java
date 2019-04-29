package com.example.jayroop.rpm_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import static java.text.DateFormat.getDateTimeInstance;


public class History2Activity extends Activity implements View.OnClickListener{


    protected static Day dayObj= new Day();

    public static final String TAG = "BasicHistoryApi";

    // Identifier to identify the sign in activity.
    public static final int REQUEST_OAUTH_REQUEST_CODE = 1;

/*    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    protected static GoogleApiClient mGoogleApiClient;*/

    public static String username;
    private TextView text;
    private Button signoutbutton;

    public static String username_id;

    //Shared references
    private SharedPreferences savedValues;


    GoogleSignInClient mGoogleSignInClient;
    FitnessOptions fitnessOptions;
    //ON CREATE FUNCTION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        // This method sets up our custom logger, which will print all log messages to the device
        // screen, as well as to adb logcat.

        username_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        text=(TextView) findViewById(R.id.textView2);
        signoutbutton=(Button) findViewById(R.id.signoutbutton);

        //==============================================================

        //==============================================================
        FitnessOptions fitnessOptions =
            FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_HEART_POINTS, FitnessOptions.ACCESS_READ )
                .addDataType(DataType.TYPE_HEART_POINTS, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_WEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_HEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_NUTRITION, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_NUTRITION_SUMMARY, FitnessOptions.ACCESS_READ)
                .build();



        GoogleSignInOptions gso = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()//request email id
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                this,
                REQUEST_OAUTH_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this),
                fitnessOptions);

        } /*else {
            Log.i(TAG, GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail() + " ");


        }*/
        //==============================================================

        //==============================================================


        signoutbutton.setOnClickListener(this);




    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if( GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null )
        {
            text.setText("Welcome "+ username +" !");
            dayObj = new Day();

            readHistoryData();
        }
        else {
            text.setText("Loading");
        }

    }


    //GET USER PERMISSIONS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
              readHistoryData();
            }
        }
    }



    private Task<DataReadResponse> readHistoryData() {
        // Begin by creating the query.

        DataReadRequest readRequest = queryFitnessData();

        username = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail() + " ";
        text.setText("Welcome "+ username +" !");
        SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putString("CurrentUser",username);
        editor.commit();
        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(this,  GoogleSignIn.getLastSignedInAccount(getApplicationContext()))
            .readData(readRequest)
            .addOnSuccessListener(
                new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        // For the sake of the sample, we'll print the data so we can see what we just
                        // added. In general, logging fitness information should be avoided for privacy
                        // reasons.
                        printData(dataReadResponse);
                    }
                })
            .addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "There was a problem reading the data.", e);
                    }
                });
    }
   /* public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateTimeInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        final DataSource ds = new DataSource.Builder()
            .setAppPackageName("com.google.heart_minutes.summary")
            .setDataType(DataType.TYPE_HEART_POINTS)
            .setType(DataSource.TYPE_RAW)
            .setStreamName("Heart Minutes")
            .build();

        DataReadRequest readRequest =
            new DataReadRequest.Builder()
                //.read(DataType.TYPE_HEIGHT)
                //.read(DataType.TYPE_NUTRITION)
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                //.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                //.aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(ds, DataType.AGGREGATE_HEART_POINTS)
                //.aggregate(DataType.TYPE_WEIGHT,DataType.AGGREGATE_WEIGHT_SUMMARY)
                //.aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                //.aggregate(DataType.TYPE_MOVE_MINUTES, DataType.AGGREGATE_MOVE_MINUTES)
                //.aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession would allow
                // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }*/

    // Returns a {@link DataReadRequest} for all step count changes in the past week.
    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();

        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateTimeInstance();
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.i(TAG, "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
            new DataReadRequest.Builder()
                .read(DataType.TYPE_HEIGHT)
                .read(DataType.TYPE_NUTRITION)
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(DataType.TYPE_HEART_POINTS, DataType.AGGREGATE_HEART_POINTS)
                .aggregate(DataType.TYPE_WEIGHT,DataType.AGGREGATE_WEIGHT_SUMMARY)
                .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .aggregate(DataType.TYPE_MOVE_MINUTES, DataType.AGGREGATE_MOVE_MINUTES)
                .aggregate(DataType.TYPE_ACTIVITY_SEGMENT, DataType.AGGREGATE_ACTIVITY_SUMMARY)
                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession would allow
                // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        // [END build_read_data_request]

        return readRequest;
    }
    /**
     * Logs a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type, but for demonstrative purposes here's how one would dump
     * all the data. In this sample, logging also prints to the device screen, so we can see what the
     * query returns, but your app should not log fitness information as a privacy consideration. A
     * better option would be to dump the data you receive to a local data directory to avoid exposing
     * it to other applications.
     */
    public static void printData(DataReadResponse dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        dayObj = new Day();
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                TAG, "Number of returned buckets of DataSets is: \n\n" + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }


            }

            Log.d("DAY OBJECT!!!!" , dayObj.toString());
            writeFirestore();

        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }
/*
    // [START parse_dataset]
    private static void dumpDataSet(DataSet dataSet) {
        Log.i(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getDateTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {


            Log.i(TAG, "\n\n________________________________________");
            Log.i(TAG, "Data point:");
            Log.i(TAG, "\tType: " + dp.getDataType().getName());
            Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {

                Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
            Log.i(TAG, "\n\n===========================================");

        }
    }*/
    // [END parse_dataset]

    // [START parse_dataset]
    private static void dumpDataSet(DataSet dataSet){

        DateFormat dateFormat = getDateTimeInstance();
        SimpleDateFormat dateObjFormat = new SimpleDateFormat("yyyy-MM-dd");

        UserBasic ub = new UserBasic();

        for (DataPoint dp : dataSet.getDataPoints()) {

            HeartStatus hm = new HeartStatus();
            MoveStatus ms = new MoveStatus();
            StepsTaken st = new StepsTaken();
            CaloriesBurned cb = new CaloriesBurned();
            Exercises ex = new Exercises();



            //Log.d("timestamp",dateonlyFormat.format(dp.getTimestamp(TimeUnit.MILLISECONDS))+"");
            String dateString= dateObjFormat.format(dp.getTimestamp(TimeUnit.MILLISECONDS));
            Date dayDate= null;
            try {
                dayDate = dateObjFormat.parse(dateString);
                dayObj.setDate(dayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }



            if(dp.getDataType().getName().equals("com.google.heart_minutes.summary"))
            {
                hm.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                hm.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("intensity"))
                    {
                        dayObj.setHeartMinutes(dp.getValue(field) + "");
                    }
                }
            }
            else if(dp.getDataType().getName().equals("com.google.heart_rate.summary"))
            {
                hm.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                hm.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("average"))
                    {
                        dayObj.setHeartRate(dp.getValue(field) + "");
                    }
                }
            }
            else if(dp.getDataType().getName().equals("com.google.active_minutes"))
            {
                ms.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                ms.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("duration"))
                    {
                        dayObj.setMoveMinutes(dp.getValue(field) + "");
                    }
                }
            }
            else if(dp.getDataType().getName().equals("com.google.step_count.delta"))
            {
                st.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                st.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("steps"))
                    {
                        dayObj.setSteps(dp.getValue(field) + "");
                    }
                }
            }
            else if(dp.getDataType().getName().equals("com.google.calories.expended"))
            {
                cb.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                cb.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("calories"))
                    {
                        dayObj.setCalories(dp.getValue(field) + "");
                    }
                }
            }
            else if(dp.getDataType().getName().equals("com.google.activity.summary"))
            {
                ex.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                ex.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("activity"))
                    {
                        ex.setActivity_type(dp.getValue(field) + "");
                    }
                    else   if(field.getName().equals("duration"))
                    {
                        ex.setDuration(dp.getValue(field) + "");
                    }

                }
                dayObj.addEx(ex);
            }
            else if(dp.getDataType().getName().equals("com.google.height") ||dp.getDataType().getName().equals("com.google.weight.summary"))
            {
                ex.setStartDate( dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                ex.setEndDate(dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    //Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    if(field.getName().equals("activity"))
                    {
                        ex.setActivity_type(dp.getValue(field) + "");
                    }
                    else   if(field.getName().equals("duration"))
                    {
                        ex.setDuration(dp.getValue(field) + "");
                    }

                }
            }
            else
            {

                Log.i(TAG, "\n\n________________________________________");
                Log.i(TAG, "Data point:");
                Log.i(TAG, "\tType: " + dp.getDataType().getName());
                Log.i(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                Log.i(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
                for (Field field : dp.getDataType().getFields()) {

                    Log.i(TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                }
                Log.i(TAG, "\n\n===========================================");
            }

            Log.i(TAG, hm.toString());
            Log.i(TAG, ms.toString());
            Log.i(TAG, st.toString());
            Log.i(TAG, cb.toString());
            Log.i(TAG, ex.toString());
        }
    }

    /** Initializes a custom log class that outputs both to in-app targets and logcat. */




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        signOut();

    }

    public void signOut()
    {
        if( GoogleSignIn.getLastSignedInAccount(getApplicationContext()) != null )
        {
            Fitness.getConfigClient(getApplicationContext(), GoogleSignIn.getLastSignedInAccount(getApplicationContext())).disableFit();
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(History2Activity.this, "Google Sign Out done.", Toast.LENGTH_SHORT).show();
                        mGoogleSignInClient.revokeAccess();
                        Intent i = new Intent(History2Activity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

        }
        else
        {
            Toast.makeText(History2Activity.this, "Google Sign Out done.", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(History2Activity.this, MainActivity.class);
            startActivity(i);
        }


    }


    protected static void writeFirestore() {



        if(dayObj.getHeartMinutes() != null && username != null)
        {

            //Firestore instance
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Create a new user with a first, middle, and last name
            Map<String, Object> user_day = new HashMap<>();
            user_day.put("timestamp",  dayObj.getDate());
            user_day.put("calories", Float.parseFloat(dayObj.getCalories()));
            user_day.put("heartMinutes", Float.parseFloat(dayObj.getHeartMinutes()));
            user_day.put("avg_heartrate", Float.parseFloat(dayObj.getHeartRate()));
            user_day.put("steps", Float.parseFloat(dayObj.getSteps()));
            user_day.put("moveMinutes", Float.parseFloat(dayObj.getMoveMinutes()));

            // Add a new document with a generated ID
            db.collection("Users").document(username_id).collection("Daily_Summary").document(dayObj.getDate() +"")
                .set(user_day)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                });


            Map<String, Object> email = new HashMap<>();
            email.put("Current User",  username);

            // Add a new document with a generated ID
            db.collection("Users").document(username_id)
                .set(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                    }
                });

            for(int i =0; i < dayObj.ex_size(); i++)
            {
                // Create a new user with a first, middle, and last name
                Map<String, Object> activity_user = new HashMap<>();
                activity_user.put("timestamp",  dayObj.getDate());
                activity_user.put("startTime", dayObj.getEx().get(i).getStartDate());
                activity_user.put("endTime", dayObj.getEx().get(i).getEndDate());
                activity_user.put("duration", TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(dayObj.getEx().get(i).getDuration())));
                activity_user.put("type", dayObj.getEx().get(i).getActivity_type());
                // Add a new document with a generated ID
                db.collection("Users").document(username_id).collection("Daily_Summary").document(dayObj.getDate() +"").collection("Activity_Summary").document("Activity: " + i )
                    .set(activity_user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot added with ID: ");
                        }
                    });

            }

        }

    }


}
