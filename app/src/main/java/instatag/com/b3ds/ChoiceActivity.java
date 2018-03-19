package instatag.com.b3ds;

/**
 * Created by RahulReign on 22-01-2018.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class ChoiceActivity extends AppCompatActivity  {
    private RadioButton radioButtonDoctor;
    private RadioButton radioButtonPatient;
    private Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_activity);


        // onButtonShowPopupWindowClick(findViewById(R.id.activity_main_layout));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    //Display pop up window for doctor and patient
    public void onButtonShowPopupWindowClick(View view) {

        // get a reference to the already created main layout
        RelativeLayout mainLayout = (RelativeLayout)
                findViewById(R.id.activity_main_layout);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.choice_popup_dialog, null);

        // create the popup window
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window

        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        popupWindow.setAnimationStyle(R.xml.animation);

        // dismiss the popup window when touched
        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });*/
        radioButtonDoctor = (RadioButton) popupView.findViewById(R.id.radioDoctor);
        radioButtonPatient = (RadioButton) popupView.findViewById(R.id.radioPatient);
        continueButton=(Button)popupView.findViewById(R.id.continueButton);
        //Log.i("MainActivity", "onButtonShowPopupWindowClick: "+continueButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(radioButtonDoctor.isChecked())
                {
                    popupWindow.dismiss();
                   //setContentView(R.layout.activity_main);
                    Intent intent=new Intent(ChoiceActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                else if (radioButtonPatient.isChecked())
                {
                    popupWindow.dismiss();
                    //setContentView(R.layout.activity_main);
                    //Intent intent=new Intent(ChoiceActivity.this,LoginActivity2.class);
                    //startActivity(intent);
                }
            }
        });

    }

 /*   @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueButton:
                if(radioButtonDoctor.isChecked())
                {
                    setContentView(R.layout.activity2);
                }
                else if (radioButtonPatient.isChecked())
                {
                    setContentView(R.layout.activity3);
                }

                break;

        }
    }*/
}
