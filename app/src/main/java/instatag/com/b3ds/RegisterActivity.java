package instatag.com.b3ds;

/**
 * Created by RahulReign on 31-01-2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static instatag.com.b3ds.MainActivity.JSON;

public class RegisterActivity extends AppCompatActivity {
    final String TAG="RegisterActivity";
    private String mfirst_name,mlast_name,memail,mphone;
    private EditText firstname,lastname,email,phone;
    private TextView register;
    Contact_register registerResponse;
    Spinner salutation;
    private int salutation_val=1;
    private ProgressBar pb;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_register_activity1);
        salutation=(Spinner)findViewById(R.id.salutation_spinner);
        pb=(ProgressBar)findViewById(R.id.patientregprogress);
        salutation.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    salutation_val=pos+1;
                        //Log.i(TAG, "onItemSelected: "+pos);
                        //Log.i(TAG, "onItemSelected: "+id);


                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                        salutation_val=1;
                    }
                });

        firstname=(EditText)findViewById(R.id.firstname);

        lastname=(EditText)findViewById(R.id.lastname);

        email=(EditText)findViewById(R.id.email);

        phone=(EditText)findViewById(R.id.phone);

        register=(TextView)findViewById(R.id.signup);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                if(register_validation()==false) {
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Request register_request=build_request();
                client.newCall(register_request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {

                        Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                        Log.i("Activity", "onFailure: Fail");
                    }
                    @Override
                    public void onResponse(final Response response) throws IOException {
                        register_response(response.body().string());
                        //final String isSuccessful=register_response(response.body().string());
                        final String isSuccessful = registerResponse.getMessage();
                        //Log.i(TAG, "onResponse: "+registerResponse.getData().getId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                if(isSuccessful.compareTo("Saved")==0){
                                    String userid=registerResponse.getData().getId();
                                    Toast.makeText(getApplicationContext(),"Successful registration",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(RegisterActivity.this,VerificationChoiceActivity.class);
                                    intent.putExtra("userid",userid);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"User Already Registered with same Email or Phone",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        });
    }

    private Request build_request(){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("salutation",Integer.toString(salutation_val));
            postdata.put("first_name",mfirst_name);
            postdata.put("last_name",mlast_name);
            postdata.put("email",memail);
            postdata.put("mobile",mphone);
        } catch(JSONException e){
            e.printStackTrace();
        }
        //HttpUrl.Builder urlBuilder = HttpUrl.parse("http://feish.online/apis/login").newBuilder();
        //String url = urlBuilder.build().toString();
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://dev.feish.online/apis/register")
                .post(body)
                .build();
        return request;
    }


    public void register_response(String response) {
        Gson gson = new GsonBuilder().create();
        registerResponse = gson.fromJson(response,Contact_register.class);
        //return registerResponse.getMessage();
    }

    public boolean register_validation(){
        mfirst_name=firstname.getText().toString();
        mlast_name=lastname.getText().toString();
        memail =email.getText().toString();
        mphone=phone.getText().toString();
        if(mfirst_name.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"First name is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mlast_name.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Last name is required",Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(memail.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Email is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(mphone.compareTo("")==0){
            Toast.makeText(getApplicationContext(),"Phone Number is required",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mphone.length()<10){
            Toast.makeText(getApplicationContext(),"Phone Number has less than 10 digits",Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;

    }

}




