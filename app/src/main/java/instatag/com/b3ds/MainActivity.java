package instatag.com.b3ds;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public int p=4;
    Button loginForEnter;
    ContactLogin loginResponse;
    TextView register,login,forgotpass;
    EditText luser,lpass;
    private RadioButton radioButtonDoctor;
    private RadioButton radioButtonPatient;
    private Button continueButton;
    String lusername,lpassword;
    private ProgressBar pb;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private String TAG="MainActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity1);
        register=(TextView)findViewById(R.id.register_here);
        forgotpass=(TextView)findViewById(R.id.forgotpassword);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,VerifyRegEmail.class);
                startActivity(intent);
            }
        });

        luser=(EditText) findViewById(R.id.loginusername);
        lpass=(EditText)findViewById(R.id.loginpassword);
        pb = (ProgressBar) findViewById(R.id.progress);
        pb.setVisibility(View.GONE);

        loginForEnter=(Button)findViewById(R.id.buttonforlogin);
        loginForEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatelogin()) {
                    pb.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    OkHttpClient client = new OkHttpClient();
                    Request request = login_request();
                    Log.i(TAG, "onClick: "+request);
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.i("Activity", "onFailure: Fail");
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {

                            String body=response.body().string();
                            Log.i(TAG, "onResponse: "+body);
                            loginJSON(body);
                            final String message = loginResponse.getMessage();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    if (message.compareTo("success") == 0) {

                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();


                                        //Intent intent2=new Intent(MainActivity.this,DashboardActivity.class);

                                        if((loginResponse.getData().getUserType()).compareTo("4")==0) {

                                            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                                            intent.putExtra("firstname", loginResponse.getData().getFirstName());
                                            intent.putExtra("mobile", loginResponse.getData().getMobile());
                                            intent.putExtra("lastname", loginResponse.getData().getLastName());
                                            intent.putExtra("email", loginResponse.getData().getEmail());
                                            intent.putExtra("userid", loginResponse.getData().getId());
                                            //intent2.putExtras(intent);
                                            startActivity(intent);
                                        }
                                        else{
                                            Intent intent = new Intent(MainActivity.this, DoctorDashboardActivity.class);
                                            //intent.putExtra("mci_number", loginResponse.getData().getMciNumber().toString());
                                            intent.putExtra("firstname", loginResponse.getData().getFirstName());
                                            intent.putExtra("mobile", loginResponse.getData().getMobile());
                                            intent.putExtra("lastname", loginResponse.getData().getLastName());
                                            intent.putExtra("email", loginResponse.getData().getEmail());
                                            intent.putExtra("userid", loginResponse.getData().getId());
                                            //intent2.putExtras(intent);
                                            startActivity(intent);

                                        }
                                    } else if (message.compareTo("Invalid username or password") == 0) {
                                        Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.choice_activity, null);
                onButtonShowPopupWindowClick(popupView.findViewById(R.id.register_here));
            }
        });



    }

    //Build Login Request
    private Request login_request(){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username",lusername);
            postdata.put("password",lpassword);
        } catch(JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON,postdata.toString());
        final Request request = new Request.Builder()
                .addHeader("X-Api-Key","AB5433GMDF657VBB")
                .addHeader("Content-Type", "application/json")
                .url("http://dev.feish.online/apis/login")
                .post(body)
                .build();
        return request;
    }

    public void loginJSON(String response) {
        Gson gson = new GsonBuilder().create();
        loginResponse = gson.fromJson(response,ContactLogin.class);
    }

    private boolean validatelogin(){
        lusername=luser.getText().toString();
        Log.i("validate", "validatepassword: "+lusername);
        lpassword=lpass.getText().toString();
        Log.i("validate", "validatepassword: "+lpassword);
        if(lusername.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Username field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else if (lpassword.compareTo("")==0)
        {
            Toast.makeText(getApplicationContext(),"Password field empty",Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }


    public void onButtonShowPopupWindowClick(View view) {

        // get a reference to the already created main layout
        RelativeLayout mainLayout = (RelativeLayout)
                findViewById(R.id.login_register_activity);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.choice_popup_dialog, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        //popupWindow.setOutsideTouchable(true);
        //popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.xml.animation);
        radioButtonDoctor = (RadioButton) popupView.findViewById(R.id.radioDoctor);
        radioButtonPatient = (RadioButton) popupView.findViewById(R.id.radioPatient);
        continueButton=(Button)popupView.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(radioButtonDoctor.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(MainActivity.this,RegisterDoctor.class);
                    startActivity(intent);

                }
                else if (radioButtonPatient.isChecked())
                {

                    popupWindow.dismiss();
                    Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);

                }
            }
        });
    }

 }

