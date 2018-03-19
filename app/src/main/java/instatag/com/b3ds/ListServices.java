package instatag.com.b3ds;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by haade on 13/09/2017.
 */

public class ListServices extends Fragment {

    //Constructor default
    public ListServices(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageTwo = inflater.inflate(R.layout.ist_services_fragment, container, false);


        return PageTwo;
    }

}
