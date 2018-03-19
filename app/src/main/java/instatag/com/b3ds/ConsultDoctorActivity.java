package instatag.com.b3ds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConsultDoctorActivity extends AppCompatActivity {

    Button backtodashboardbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor);
        backtodashboardbtn=(Button)findViewById(R.id.dashboardbtnid);
        backtodashboardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConsultDoctorActivity.this,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
