package instatag.com.b3ds;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static instatag.com.b3ds.MainActivity.JSON;
import static instatag.com.b3ds.R.id.firstname;
import static instatag.com.b3ds.R.id.lastname;
import static instatag.com.b3ds.R.id.login_register_activity;

/**
 * Created by This Pc on 3/1/2018.
 */

public class VerificationChoiceActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG="VerificationChoice";
    private Button email,phone,resendotp;
    protected PinEntryEditText pinEntry;
    private boolean otpsent=false;
    private String userid,choice;
    OkHttpClient client;
    private ProgressBar pb;
    private final int MY_PERMISSIONS_REQUEST_READ_SMS=1;
    private BroadcastReceiver otpReadBroadcastReciever;
    //Request otp_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_choice_activity);
        pb=(ProgressBar)findViewById(R.id.choiceprogress);
        otpReadBroadcastReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final Bundle broadcastbundle = intent.getExtras();
                if (broadcastbundle != null) {
                    final Object[] pdusObj = (Object[]) broadcastbundle.get("pdus");
                    //for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[pdusObj.length - 1]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    String msgotp=message.substring(message.lastIndexOf('.')-4,message.lastIndexOf('.'));
                    Toast.makeText(getApplicationContext(),msgotp,Toast.LENGTH_LONG).show();
                    char[] otp =msgotp.toCharArray();
                    pinEntry.setText(otp, 0, 4);
                    try {
                            /*if (senderNum .equals("TA-DOCOMO"))
                            {
                                Otp Sms = new Otp();
                                Sms.recivedSms(message );
                            }*/
                  } catch (Exception e) {
                    }


                }
            }
        };

        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("userid");
        Log.i(TAG, "onCreate: "+userid);
        email=(Button)findViewById(R.id.verifyemail);
        phone=(Button)findViewById(R.id.verifyphone);
        resendotp=(Button)findViewById(R.id.resendotp);
        resendotp.setEnabled(false);

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendotpemailphone();

            }
        });


        pinEntry = (PinEntryEditText) findViewById(R.id.choice_pin_entry);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    /*if(str.toString().compareTo("1234")==0){
                        Toast.makeText(getApplicationContext(),"Verification successful",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(VerificationChoiceActivity.this,SetPasswordActivity.class);
                        intent.putExtra("userid",userid);
                        startActivity(intent);
                    }*/
                    if(otpsent==false){
                        Toast.makeText(getApplicationContext(),"First send an otp using one of the above options",Toast.LENGTH_LONG).show();
                        return;
                    }
                    OkHttpClient client = new OkHttpClient();
                    Request validation_request=otp_validation_request(str.toString());
                    client.newCall(validation_request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Request request, IOException e) {

                            Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                            Log.i("Activity", "onFailure: Fail");
                        }
                        @Override
                        public void onResponse(final Response response) throws IOException {

                            final boolean isSuccessful=validation_response(response.body().string());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(isSuccessful){
                                        Toast.makeText(getApplicationContext(),"Verification successful",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(VerificationChoiceActivity.this,SetPasswordActivity.class);
                                        intent.putExtra("userid",userid);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"Incorrect verification code",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });

                }
            });

        }

        email.setOnClickListener(this);
        phone.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(VerificationChoiceActivity.this,"android.permission.RECEIVE_SMS")
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: checking for permissions");
            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    "android.permission.RECEIVE_SMS")) {
                Log.i(TAG, "onStart: request rationale");
                //Toast.makeText(getApplicationContext(),"You wont be able to use the autoread OTP feature unless you allow permission",Toast.LENGTH_LONG).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.RECEIVE_SMS"},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

            } else {
                Log.i(TAG, "onStart: requesting permission");
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.RECEIVE_SMS"},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
        else{
            //Toast.makeText(getApplicationContext(),"Permission already granted",Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Toast.makeText(getApplicationContext(),"Auto Read Permission granted",Toast.LENGTH_LONG).show();


                } else {

                    //Toast.makeText(getApplicationContext(),"Auto ReadPermission denied",Toast.LENGTH_LONG).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(otpReadBroadcastReciever,intentFilter);
        Log.i(TAG, "onStart: registered");
    }

    public boolean validation_response(String response) {
        Gson gson = new GsonBuilder().create();
        Contact_request_otp validation_response = gson.fromJson(response,Contact_request_otp.class);
        return validation_response.getStatus();
    }

    private Request otp_validation_request(String otp){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            postdata.put("otp",otp);
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .addHeader("Id", "501")
                .addHeader("UserToken", "939753dec8ff12c0cd2f4a03b761e87f")
                .url("http://dev.feish.online/apis/checkOTP")
                .post(body)
                .build();
        return request;
    }

        @Override
    public void onClick(View view) {
            resendotp.setEnabled(true);
            email.setEnabled(false);
            phone.setEnabled(false);
            switch (view.getId()){

            case R.id.verifyemail:
                Toast.makeText(getApplicationContext(),"you have selected email verification",Toast.LENGTH_LONG).show();
                choice="email";
                sendotpemailphone();
                break;

            case R.id.verifyphone:
                Toast.makeText(getApplicationContext(),"you have selected phone verification",Toast.LENGTH_LONG).show();
                choice="mobile";
                sendotpemailphone();
                break;

        }

        }


        public void sendotpemailphone(){
            pb.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            client = new OkHttpClient();
            Request otp_request=build_request(choice);
            client.newCall(otp_request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                    Log.i("Activity", "onFailure: Fail");
                }
                @Override
                public void onResponse(final Response response) throws IOException {
                    //Log.i("VerificationChoice", "onResponse: we are into response");
                    final boolean statusresponse = otp_response(response.body().string());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if(statusresponse){
                                Toast.makeText(getApplicationContext(),"OTP sent successfully on your "+choice,Toast.LENGTH_LONG).show();
                                otpsent=true;
                                //intent.putExtra("userid",userid);
                                //startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Could not find the user",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

        }
    public boolean otp_response(String response) {
        Gson gson = new GsonBuilder().create();
        Contact_request_otp otpreponse = gson.fromJson(response,Contact_request_otp.class);
        return otpreponse.getStatus();
    }


    private Request build_request(String type){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("user_id",userid);
            postdata.put("type",type);
        } catch(JSONException e){
            e.printStackTrace();
        }
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://feish.online/apis/login").newBuilder();
        //String url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://dev.feish.online/apis/sendOTP")
                .post(body)
                .build();
        return request;
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(otpReadBroadcastReciever);
        Log.i(TAG, "onStop: unregistered");
    }
}
