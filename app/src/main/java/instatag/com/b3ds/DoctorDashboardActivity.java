package instatag.com.b3ds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

public class DoctorDashboardActivity extends AppCompatActivity {
    LinearLayout setupDoctorProfile,servicelayout,assistantLayout,createplanLayout,patientLayout,notesLayout;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);
        setupDoctorProfile=(LinearLayout)findViewById(R.id.doctordashboardprofileId);
        servicelayout=(LinearLayout)findViewById(R.id.servicesID);
        assistantLayout=(LinearLayout)findViewById(R.id.assistantlayoutId);
        createplanLayout=(LinearLayout)findViewById(R.id.createplanId);
        patientLayout=(LinearLayout)findViewById(R.id.patientid);
        notesLayout=(LinearLayout)findViewById(R.id.makenotesId);
        notesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorDashboardActivity.this,"Purchase plan to use this facility",Toast.LENGTH_SHORT).show();
            }
        });
        patientLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorDashboardActivity.this,"Purchase plan to use this facility",Toast.LENGTH_SHORT).show();
            }
        });
        createplanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorDashboardActivity.this,"Purchase plan to use this facility",Toast.LENGTH_SHORT).show();
            }
        });
        assistantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DoctorDashboardActivity.this,"Purchase plan to use this facility",Toast.LENGTH_SHORT).show();
            }
        });
bundle=getIntent().getExtras();

        servicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DoctorDashboardActivity.this,ServicesActivity.class);
                startActivity(intent);

            }
        });
        setupDoctorProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DoctorDashboardActivity.this,SetupProfile.class);
               // intent.putExtra("mci_number",bundle.getString("mci_number"));
                intent.putExtra("firstname",bundle.getString("firstname"));
                intent.putExtra("userid",bundle.getString("userid"));
                intent.putExtra("mobile",bundle.getString("mobile"));
                intent.putExtra("lastname",bundle.getString("lastname"));
                intent.putExtra("email",bundle.getString("email"));
                startActivity(intent);
            }
        });

    }
}
