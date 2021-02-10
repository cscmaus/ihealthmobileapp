package com.example.jayroop.rpm_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.net.http.SslError;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ihealthman extends AppCompatActivity {



    TextView textview;
    WebView wvAuthorise;
    String userID, code, accessToken, refreshToken;
    private static final String TAG = "IHEALTHFragment";
    int c = 0;
    HashMap<String, String> map = new HashMap<String, String>();

    String cookies;

    private RequestQueue queue;


    String myURL="https://api.ihealthlabs.com:8443/OpenApiV2/OAuthv2/userauthorization/?client_id=c496daadd1d147709543cd53e8027047&response_type=code&redirect_uri=http%3a%2f%2ffinished&APIName=OpenApiBP+OpenApiBG+OpenApiSpO2";


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ihealthman);

        wvAuthorise = (WebView) findViewById(R.id.w1);


        wvAuthorise.setWebViewClient(new MyWebViewClient(wvAuthorise) {



            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                if (error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                    handler.proceed();
                } else {
                    handler.proceed();
                }
            }
        });



        wvAuthorise.getSettings().setJavaScriptEnabled(true);


        wvAuthorise.isImportantForAutofill();


        wvAuthorise.loadUrl(myURL);


    }


    public class MyWebViewClient extends WebViewClient {
        WebView wvAuthorise;

        MyWebViewClient(WebView wv) {
            wvAuthorise = wv;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String cookies=CookieManager.getInstance().getCookie(url);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onLoadResource(WebView view, String url) {

            if (url.equals("https://api.ihealthlabs.com:8443/OpenApiV2/OAuthv2/userauthorization/?client_id=c496daadd1d147709543cd53e8027047&response_type=code&redirect_uri=http%3a%2f%2ffinished&APIName=OpenApiBP+OpenApiBG+OpenApiSpO2")) {
                {
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        CookieManager.getInstance().setAcceptThirdPartyCookies(wvAuthorise, true);
                    } else {
                        CookieManager.getInstance().setAcceptCookie(true);
                        CookieManager.getInstance().acceptCookie();
                        CookieManager.getInstance().setCookie(myURL, "cookie");
                    }
                }

            }
        }


        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



            String divStr1 = "code=";
            int first1 = url.indexOf(divStr1);


            if (first1 != -1 && url.startsWith("http://finished")) {

                code = url.substring(first1 + divStr1.length());
                Log.d("CODE", code);
                writeFirestore_rate();
                finish();
                return true;
            }
            return false;
        }

    }
    @Override
    public void onBackPressed() {
        if(wvAuthorise.isFocused() && wvAuthorise.canGoBack()){
            wvAuthorise.goBack();
        }
        else{
            super.onBackPressed();
            finish();

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

        // Create a new user with a first, middle, and last name
//        Map<String, Object> title = new HashMap<>();
//        title.put("gps", [22.4, 22.3]);

        String username;

        username = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Toast.makeText(getApplicationContext(), username+"", Toast.LENGTH_SHORT).show();



        db.collection("Users").document(username)
            .set(title)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot added with ID: ");
                }
            });

    }


}
