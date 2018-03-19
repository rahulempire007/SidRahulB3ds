package instatag.com.b3ds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by haade on 13/09/2017.
 */

public class Page1 extends Fragment {

    //Constructor default
    public Page1(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View PageOne = inflater.inflate(R.layout.page1, container, false);



        return PageOne;
    }}