package com.loginexample.v4web.tsrlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//notification imports
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    SharedPreferences shp;
    String Userloggedid, Name, DOB, Mobileno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        connectionClass = new ConnectionClass();
        String z = "";
        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        String UserName = shp.getString("UserName", "none");
        if (shp.contains("Name")) {
            Name= shp.getString("Name", "");
        }
        //Name = shp.getString("Name", "none");
        //DOB = shp.getString("DateofBirth", "none");
        if (shp.contains("DOB")) {
            DOB= shp.getString("DOB", "");

        }
        String dateString = DOB.substring(0,10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        DateFormat targetDate = new SimpleDateFormat("d", Locale.getDefault());
        DateFormat targetMonth = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat targetYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date convertedDate = new Date();
        int day=1, month=1, year=1990;
        try {
            convertedDate = dateFormat.parse(dateString);
            DOB = targetFormat.format(convertedDate);

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
            DOB ="You are " + yourAge + " old!";
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

        DOB = DOB + "\nLeft days to B'day: " + days;
        DOB = DOB + ".\nIt will be: " + dayOfTheWeek;

        Mobileno = shp.getString("mobileno", "0");
        Userloggedid = shp.getString("UserId", "0");
        Typeface myCustomefont = Typeface.createFromAsset(getAssets(),"fonts/V4WEB.TTF");
        Typeface myCustomefontb = Typeface.createFromAsset(getAssets(),"fonts/V4WEB-BOLD.TTF");
        TextView Title1 = (TextView) findViewById(R.id.Title1);
        Title1.setTypeface(myCustomefontb);
        TextView V4webbieName = (TextView) findViewById(R.id.V4webbieName);
        V4webbieName.setText(Name , TextView.BufferType.EDITABLE);
        V4webbieName.setTypeface(myCustomefontb);
        TextView V4webbieAge = (TextView) findViewById(R.id.V4webbieAge);
        V4webbieAge.setText(DOB , TextView.BufferType.EDITABLE);
        V4webbieAge.setTypeface(myCustomefont);
        TextView V4webbieMob = (TextView) findViewById(R.id.V4webbieMob);
        V4webbieMob.setText(Mobileno , TextView.BufferType.EDITABLE);
        V4webbieMob.setTypeface(myCustomefont);
        Button btnTSR = (Button) findViewById(R.id.btnTSR);
        btnTSR.setTypeface(myCustomefontb);
        Button btnV4 = (Button) findViewById(R.id.btnV4);
        btnV4.setTypeface(myCustomefontb);
        NotifyBday();
        NotifyEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //return super.onCreateOptionsMenu(menu);
        return true;
    }
    public void AddTSRClick(View v) {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(ProfileActivity.this, tsr.class);
        startActivity(i);
        finish();
    }

    public void ViewV4(View v) {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(ProfileActivity.this, V4Webbies.class);
        startActivity(i);
        finish();
    }

    public void ViewNotify(View v) {
        Intent i = new Intent(ProfileActivity.this, NotifyActivity.class);
        startActivity(i);
        finish();
    }

    public void NotifyBday() {
        String z = "start";
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                //Toast.makeText(ProfileActivity.this, "Before query", Toast.LENGTH_SHORT).show();


                //String query = "select * from login where dateofresignation IS NULL and userauth ='user' order by dateofjoining asc";
                String query = " SELECT srno, name, username, password, DATEDIFF(hour,dateofbirth,GETDATE())/8766 AS AgeYearsIntTrunc, dateofjoining, userauth, profileimage, dateofresignation, dateofbirth, email, description, createdon, nickname FROM         login WHERE     (DATEPART(dayofyear, dateofbirth) BETWEEN DATEPART(dayofyear, GETDATE()-1) AND DATEPART(dayofyear, DATEADD(day, 30, GETDATE()))) AND (userauth = 'user') order by (DATEPART(dayofyear, dateofbirth))";
                PreparedStatement ps = con.prepareStatement(query);
                String name="", ageshow="", mess1="";
                int age;
                ResultSet rs = ps.executeQuery();
                ArrayList data1 = new ArrayList();
                while (rs.next()) {
                    //Toast.makeText(ProfileActivity.this, "inside while", Toast.LENGTH_SHORT).show();
                    if(name=="") {
                        name=rs.getString("name");
                        age =  Integer.parseInt(rs.getString("AgeYearsIntTrunc")) + 1;
                        ageshow = ""+age;
                        mess1 = name + " will be " + age+ " yrs";
                    }
                    else
                    {
                        name=name + ", "+ rs.getString("name");
                        age =  Integer.parseInt(rs.getString("AgeYearsIntTrunc")) + 1;
                        ageshow = ageshow + ", "+age;
                        mess1 = mess1 + ", " + rs.getString("name") + " will be " + age+ " yrs";
                    }

                }
                int i=1;
                //Toast.makeText(ProfileActivity.this, "before Notification", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.loginexample.v4web.MainActivity");

                PendingIntent pendingIntent = PendingIntent.getActivity(ProfileActivity.this, 1, intent, 0);

                Notification.Builder builder = new Notification.Builder(ProfileActivity.this);


                builder.setAutoCancel(true);
                builder.setTicker(mess1 + " this month");
                builder.setContentTitle("V4WEBBIE B'Day Alert");
                builder.setContentText(mess1 + " this month");
                builder.setSmallIcon(R.mipmap.ic_v4web);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(false);
                builder.setSubText("B'Day Update");   //API level 16

                builder.setNumber(i);
                //Toast.makeText(ProfileActivity.this, "building Notification", Toast.LENGTH_SHORT).show();
                i = i + 1;
                builder.build();
                //Toast.makeText(ProfileActivity.this, "built Notification", Toast.LENGTH_SHORT).show();

                Notification myNotication;

                myNotication = builder.getNotification();
                NotificationManager manager;
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(11, myNotication);
                //Toast.makeText(ProfileActivity.this, "after Notification", Toast.LENGTH_SHORT).show();
                z = "Success";
            }
        } catch (Exception ex) {
            //Toast.makeText(ProfileActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            z = "Error retrieving data from table:" + ex.toString();

        }

    }

    public void NotifyEvent() {
        String z = "start";
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                //Toast.makeText(ProfileActivity.this, "Before query", Toast.LENGTH_SHORT).show();


                //String query = "select * from login where dateofresignation IS NULL and userauth ='user' order by dateofjoining asc";
                String query = "SELECT  srno, name, username, password,  DATEDIFF(hour,dateofjoining,GETDATE())/8766 AS WorkYearsIntTrunc, dateofjoining, userauth, profileimage, dateofresignation, dateofbirth, email, description, createdon, nickname FROM [login] WHERE (DATEPART(dayofyear, dateofjoining) BETWEEN DATEPART(dayofyear, GETDATE()) AND DATEPART(dayofyear, DATEADD(day, 30, GETDATE()))) AND (userauth = 'user') order by (DATEPART(dayofyear, dateofjoining))";
                PreparedStatement ps = con.prepareStatement(query);
                String name="", ageshow="", mess1="";
                int age;
                ResultSet rs = ps.executeQuery();
                ArrayList data1 = new ArrayList();
                while (rs.next()) {
                   // Toast.makeText(ProfileActivity.this, "inside while", Toast.LENGTH_SHORT).show();
                    if(name=="") {
                        name=rs.getString("name");
                        age =  Integer.parseInt(rs.getString("WorkYearsIntTrunc")) + 1;
                        ageshow = ""+age;
                        mess1 = name + " will be completing " + age + " yr(s)";
                    }
                    else
                    {
                        name=name + ", "+ rs.getString("name");
                        age =  Integer.parseInt(rs.getString("WorkYearsIntTrunc")) + 1;
                        ageshow = ageshow + ", "+age;
                        mess1 = mess1 + ", " + rs.getString("name") + " will be " + age + " yr(s)";
                    }

                }
                int i=1;
                //Toast.makeText(ProfileActivity.this, "before Notification", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.loginexample.v4web.MainActivity");

                PendingIntent pendingIntent = PendingIntent.getActivity(ProfileActivity.this, 1, intent, 0);

                Notification.Builder builder = new Notification.Builder(ProfileActivity.this);


                builder.setAutoCancel(true);
                builder.setTicker(mess1 + " this month @V4WEB");
                builder.setContentTitle("V4WEBBIE Work Anniversary Alert");
                builder.setContentText(mess1 + " this month @V4WEB");
                builder.setSmallIcon(R.mipmap.ic_v4web);
                builder.setContentIntent(pendingIntent);
                builder.setOngoing(false);
                builder.setSubText("Work Anniversary Update");   //API level 16

                builder.setNumber(i);
                //Toast.makeText(ProfileActivity.this, "building Notification", Toast.LENGTH_SHORT).show();
                i = i + 1;
                builder.build();
                //Toast.makeText(ProfileActivity.this, "built Notification", Toast.LENGTH_SHORT).show();

                Notification myNotication;

                myNotication = builder.getNotification();
                NotificationManager manager;
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(13, myNotication);
                //Toast.makeText(ProfileActivity.this, "after Notification", Toast.LENGTH_SHORT).show();
                z = "Success";
            }
        } catch (Exception ex) {
            //Toast.makeText(ProfileActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            z = "Error retrieving data from table:" + ex.toString();

        }

    }

}
