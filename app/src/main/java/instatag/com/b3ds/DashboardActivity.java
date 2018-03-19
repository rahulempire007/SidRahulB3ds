package instatag.com.b3ds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout appoint_list,visitReportlayout;
    Bundle bundle;
    TextView consultdoctorTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        bundle= getIntent().getExtras();
        consultdoctorTV=(TextView)findViewById(R.id.consultdoctorid);
        visitReportlayout=(LinearLayout)findViewById(R.id.visitreportlayoutId);

        appoint_list=(LinearLayout)findViewById(R.id.dashboardprofile);
visitReportlayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(DashboardActivity.this,VisitReportActivity.class);
        startActivity(intent);
    }
});
        appoint_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(DashboardActivity.this,SetupProfile.class);
                intent.putExtra("firstname",bundle.getString("firstname"));
                intent.putExtra("userid",bundle.getString("userid"));
                intent.putExtra("mobile",bundle.getString("mobile"));
                intent.putExtra("lastname",bundle.getString("lastname"));
                intent.putExtra("email",bundle.getString("email"));
            startActivity(intent);
            }
        });
        consultdoctorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DashboardActivity.this,ConsultDoctorActivity.class);
                startActivity(intent);
            }
        });
    }
}
