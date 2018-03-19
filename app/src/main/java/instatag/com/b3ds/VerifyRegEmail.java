package instatag.com.b3ds;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
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

/**
 * Created by This Pc on 3/3/2018.
 */

public class VerifyRegEmail extends AppCompatActivity{

    private static final String TAG = "VerifyRegEmail" ;
    private EditText femail;
    private Button verifysubmit;
    Contact_verify_email veremailreponse;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_emailverify_activity);
        femail=(EditText)findViewById(R.id.verifyemail);
        verifysubmit=(Button)findViewById(R.id.verifysubmit);
        pb=(ProgressBar)findViewById(R.id.verifyemailprogress);
        verifysubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                OkHttpClient client = new OkHttpClient();
                Request veremail_request=verify_email_request();
                client.newCall(veremail_request).enqueue(new Callback() {

                    @Override
                    public void onFailure(Request request, IOException e) {

                        Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
                        Log.i("Activity", "onFailure: Fail");
                    }
                    @Override
                    public void onResponse(final Response response) throws IOException {
                        verify_email_response(response.body().string());
                        final boolean isSuccessful=veremailreponse.getStatus();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                if(isSuccessful){
                                    String userid=veremailreponse.getData().getUserId();
                                    Toast.makeText(getApplicationContext(),"You have entered the registered email",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(VerifyRegEmail.this,SetPasswordActivity.class);
                                    intent.putExtra("userid",userid);
                                    Log.i(TAG, "run: "+userid);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Email is not registered",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        });

    }

    private Request verify_email_request(){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("email",femail.getText().toString());
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://dev.feish.online/apis/forgotPassword")
                .post(body)
                .build();
        return request;
    }

    public void verify_email_response(String response) {
        Gson gson = new GsonBuilder().create();
        veremailreponse = gson.fromJson(response,Contact_verify_email.class);
    }
}
