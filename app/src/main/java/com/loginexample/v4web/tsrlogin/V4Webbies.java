package com.loginexample.v4web.tsrlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class V4Webbies extends AppCompatActivity {
    ConnectionClass connectionClass;
    String Userloggedid;
    ProgressBar pbbar;
    ListView lstpro;
    String proid;
    TextView txtName, txtDaystoBDay, txtYrsCompleted;
    ImageView imgProfile;
    TextView txtErr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionClass = new ConnectionClass();
        Typeface myCustomefont = Typeface.createFromAsset(getAssets(),"fonts/V4WEB.TTF");
        Typeface myCustomefontb = Typeface.createFromAsset(getAssets(),"fonts/V4WEB-BOLD.TTF");
        setContentView(R.layout.activity_v4_webbies);
        lstpro = (ListView) findViewById(R.id.listview);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        txtErr = (TextView) findViewById(R.id.texterr);
        pbbar.setVisibility(View.GONE);

        FillList fillList = new FillList();
        fillList.execute("");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myMenuInflater = getMenuInflater();
        myMenuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case(R.id.menu_Add_TSR):
                Toast.makeText(this, "TSR", Toast.LENGTH_LONG).show();
                AddTSRClick();
                break;
            case(R.id.menu_Profile):
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
                ViewProfile();
                break;
            case(R.id.menu_Webbies):
                Toast.makeText(this, "Already on V4Webbies", Toast.LENGTH_LONG).show();

                break;
            case(R.id.menu_OtherContacts):
                Toast.makeText(this, "Other Contacts", Toast.LENGTH_LONG).show();
                ViewOtherContacts();
                break;
            case(R.id.menu_Logout):
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                logout();
                break;
        }
        return true;
    }

    public void AddTSRClick() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(V4Webbies.this, tsr.class);
        startActivity(i);
        finish();
    }
    public void ViewProfile() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(V4Webbies.this, Profile.class);
        startActivity(i);
        finish();
    }
    public void ViewOtherContacts() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(V4Webbies.this, Othercontacts.class);
        startActivity(i);
        finish();
    }
    public void logout() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(V4Webbies.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";
        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(V4Webbies.this, r, Toast.LENGTH_SHORT).show();
            String[] from = {  "A", "C", "B",  "D" };
            int[] views = { R.id.VName, R.id.VJoin, R.id.VDob, R.id.VMob };
            final SimpleAdapter ADA = new SimpleAdapter(V4Webbies.this,
                    prolist, R.layout.listview_layout, from,
                    views);
            lstpro.setAdapter(ADA);

        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from login where dateofresignation IS NULL and userauth ='user' order by dateofjoining asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("name"));
                        datanum.put("B", getDays(rs.getString("dateofbirth")));
                        datanum.put("C", getCompleted(rs.getString("dateofjoining")));
                        datanum.put("D", rs.getString("mobileno"));
                        //datanum.put("E", rs.getString("profileimage"));
                        prolist.add(datanum);
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table:" + ex.toString();
                
            }
            return z;
        }
    }

    public String getDays(String sendDate)
    {
        String dateString = sendDate.substring(0,10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        DateFormat targetDate = new SimpleDateFormat("d", Locale.getDefault());
        DateFormat targetMonth = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat targetYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date convertedDate = new Date();
        int day=1, month=1, year=1990;
        try {
            convertedDate = dateFormat.parse(dateString);
            sendDate = targetFormat.format(convertedDate);

            day = Integer.parseInt(String.valueOf(targetDate.format(convertedDate)));
            month = Integer.parseInt(String.valueOf(targetMonth.format(convertedDate)));
            year = Integer.parseInt(String.valueOf(targetYear.format(convertedDate)));
            Calendar dateOfYourBirth = new GregorianCalendar(year, month, day);
            Calendar today = Calendar.getInstance();
            int yourAge = today.get(Calendar.YEAR) - dateOfYourBirth.get(Calendar.YEAR);
            dateOfYourBirth.add(Calendar.YEAR, yourAge);
            if (today.before(dateOfYourBirth)) {
                yourAge--;
            }
            sendDate ="You are " + yourAge + " old!";
            //DOB = "Your Bday = " + day +" "+ month + " " + year;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(convertedDate);
        final String strBDay = year +"/"+month+"/"+day;//"1990/04/07"; // Next Birtday date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt = null;
        try
        {
            dt = sdf.parse(strBDay);
        }
        catch (final java.text.ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final Calendar BDay = Calendar.getInstance();
        BDay.setTime(dt);

        final Calendar today = Calendar.getInstance();

        // Take your DOB Month and compare it to current month
        final int BMonth = BDay.get(Calendar.MONTH);
        final int CMonth = today.get(Calendar.MONTH);
        BDay.set(Calendar.YEAR, today.get(Calendar.YEAR));
        if(BMonth <= CMonth)
        {
            BDay.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
        }

        // Result in millis
        final long millis = BDay.getTimeInMillis() - today.getTimeInMillis();

        // Convert to days
        final long days = millis / 86400000; // Precalculated (24 * 60 * 60 * 1000)

        sdf = new SimpleDateFormat("EEEE");
        //final String dayOfTheWeek = sdf.format(BDay.getTime());
        final String dayOfTheWeek = sdf.format(dt);
        String retval = ""+days+ " days to next B'Day";
        if (retval == " days to next B'Day")
        {
            retval = "N/A";
        }
        return retval;
    }

    public String getCompleted(String sendDate)
    {
        String dateString = sendDate.substring(0,10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        DateFormat targetDate = new SimpleDateFormat("d", Locale.getDefault());
        DateFormat targetMonth = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat targetYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date convertedDate = new Date();
        String re="";
        int day=1, month=1, year=1990;
        int yourAge=1;
        try {
            convertedDate = dateFormat.parse(dateString);
            sendDate = targetFormat.format(convertedDate);

            day = Integer.parseInt(String.valueOf(targetDate.format(convertedDate)));
            month = Integer.parseInt(String.valueOf(targetMonth.format(convertedDate)));
            year = Integer.parseInt(String.valueOf(targetYear.format(convertedDate)));
            Calendar dateOfYourBirth = new GregorianCalendar(year, month, day);
            Calendar today = Calendar.getInstance();
            yourAge = today.get(Calendar.YEAR) - dateOfYourBirth.get(Calendar.YEAR);
            dateOfYourBirth.add(Calendar.YEAR, yourAge);
            re = "" + yourAge;
            if (today.before(dateOfYourBirth)) {
                yourAge--;
            }

            sendDate ="You are " + yourAge + " old!";
            //DOB = "Your Bday = " + day +" "+ month + " " + year;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(convertedDate);
        final String strBDay = year +"/"+month+"/"+day;//"1990/04/07"; // Next Birtday date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt = null;
        try
        {
            dt = sdf.parse(strBDay);
        }
        catch (final java.text.ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final Calendar BDay = Calendar.getInstance();
        BDay.setTime(dt);

        final Calendar today = Calendar.getInstance();

        // Take your DOB Month and compare it to current month
        final int BMonth = BDay.get(Calendar.MONTH);
        final int CMonth = today.get(Calendar.MONTH);
        BDay.set(Calendar.YEAR, today.get(Calendar.YEAR));
        if(BMonth <= CMonth)
        {
            BDay.set(Calendar.YEAR, today.get(Calendar.YEAR) + 1);
        }

        // Result in millis
        final long millis = BDay.getTimeInMillis() - today.getTimeInMillis();

        // Convert to days
        final long days = millis / 86400000; // Precalculated (24 * 60 * 60 * 1000)

        sdf = new SimpleDateFormat("EEEE");
        //final String dayOfTheWeek = sdf.format(BDay.getTime());
        final String dayOfTheWeek = sdf.format(dt);

        String retval = ""+re+ " year(s) Completed";
        if (re == "0")
        {
            retval = "N/A";
        }
        if (retval == "1 year(s) Completed")
        {
            retval = "1 year Completed";
        }
        return retval;
    }
}
