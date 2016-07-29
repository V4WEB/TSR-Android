package com.loginexample.v4web.tsrlogin;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sanket on 21/07/2016.
 */
public class v4webbiesFragment extends Fragment{
    ConnectionClass connectionClass;
    String Userloggedid;
    ListView lstpro;
    public static v4webbiesFragment newInstance() {
        v4webbiesFragment fragment = new v4webbiesFragment();
        return fragment;
    }
    public v4webbiesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_v4webbies, container, false);
        connectionClass = new ConnectionClass();
        //Typeface myCustomefont = Typeface.createFromAsset(getAssets(),"fonts/V4WEB.TTF");
        //Typeface myCustomefontb = Typeface.createFromAsset(getAssets(),"fonts/V4WEB-BOLD.TTF");

        lstpro = (ListView)rootView.findViewById(R.id.listviewwebbies);

        return rootView;

    }


}

