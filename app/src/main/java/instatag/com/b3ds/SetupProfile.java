package instatag.com.b3ds;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by This Pc on 3/8/2018.
 */

public class SetupProfile extends Activity {

    private EditText firstname,lastname,email,mobile,address,height,weight,bmi;
    private Button submit;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsetupprofileactivity);
        Bundle bundle = getIntent().getExtras();
        firstname=(EditText)findViewById(R.id.setupfirstname);
        lastname=(EditText)findViewById(R.id.setuplastname);
        email=(EditText)findViewById(R.id.setupemail);
        mobile=(EditText)findViewById(R.id.setupmobile);
        submit=(Button)findViewById(R.id.submitprofile);
        address =(EditText)findViewById(R.id.setupaddress);
        height=(EditText)findViewById(R.id.setupheight);
        weight=(EditText)findViewById(R.id.setupweight);
        bmi=(EditText)findViewById(R.id.setupbmi);

        firstname.setText(bundle.getString("firstname"));

        lastname.setText(bundle.getString("lastname"));

        email.setText(bundle.getString("email"));

        mobile.setText(bundle.getString("mobile"));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
