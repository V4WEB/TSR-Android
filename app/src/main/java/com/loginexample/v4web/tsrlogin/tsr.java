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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class tsr extends AppCompatActivity {
    ConnectionClass connectionClass;
    Spinner spinnerprojects;
    String ip, db, un, passwords;
    Connection connect;
    PreparedStatement stmt;
    ResultSet rs;
    SharedPreferences shp;
    static EditText edtproname, edtprodesc, edttimefrom, edttimeto;
    //Button btnadd,btnupdate,btndelete;
    String prname;
    ProgressBar pbbar;
    ListView lstpro;
    String proid;
    String Userloggedid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tsr);

        connectionClass = new ConnectionClass();
        Typeface myCustomefont = Typeface.createFromAsset(getAssets(),"fonts/V4WEB.TTF");
        Typeface myCustomefontb = Typeface.createFromAsset(getAssets(),"fonts/V4WEB-BOLD.TTF");
        String z = "";
        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        String UserName = shp.getString("UserName", "none");
        Userloggedid = shp.getString("UserId", "0");
        edtproname = (EditText) findViewById(R.id.edtproname);
        edtprodesc = (EditText) findViewById(R.id.edtprodesc);
        edttimefrom = (EditText) findViewById(R.id.edttimefrom);
        edttimefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogfrom(v);
                //showDatePickerDialog(v);
            }
        });
        edttimeto = (EditText) findViewById(R.id.edttimeto);
        edttimeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogto(v);
                //showDatePickerDialog(v);
            }
        });
        Button btnadd = (Button) findViewById(R.id.btnadd);
        btnadd.setTypeface(myCustomefontb);
        TextView lblLoggedID = (TextView) findViewById(R.id.lblLoggedID);

        String login = "Logged in as " + UserName.toUpperCase();
        lblLoggedID.setText(login , TextView.BufferType.EDITABLE);
        lblLoggedID.setTypeface(myCustomefontb);
        Button btnupdate = (Button) findViewById(R.id.btnupdate);
        btnupdate.setTypeface(myCustomefontb);
        Button btndelete = (Button) findViewById(R.id.btndelete);
        btndelete.setTypeface(myCustomefontb);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setTypeface(myCustomefontb);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        lstpro = (ListView) findViewById(R.id.lstproducts);
        proid = "";
        spinnerprojects = (Spinner) findViewById(R.id.spinnerprojects);
        String query = "select ProjectName from projects";
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                z = "Error in connection with SQL server";
            } else {
                // String query = "select * from login where username='" + userid + "' and password='" + password + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ArrayList<String> data = new ArrayList<String>();
                while (rs.next()) {
                    String id = rs.getString("ProjectName");
                    data.add(id);
                }
                String[] array = data.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, data)
                {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/V4WEB.TTF");
                       ((TextView) v).setTypeface(externalFont);
                        return v;
                    }

                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                        View v =super.getDropDownView(position, convertView, parent);
                        Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/V4WEB.TTF");
                       ((TextView) v).setTypeface(externalFont);
                       return v;
                       }


                };

                spinnerprojects.setAdapter(NoCoreAdapter);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        FillList fillList = new FillList();
        fillList.execute("");
        spinnerprojects.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                prname = spinnerprojects.getSelectedItem().toString();
                edtproname.setText(prname);
                Toast.makeText(tsr.this, prname, Toast.LENGTH_SHORT)
                        .show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                Toast.makeText(this, "Already on TSR", Toast.LENGTH_LONG).show();
                ProfileClick();
                break;
            case(R.id.menu_Profile):
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
                ProfileClick();
                break;
            case(R.id.menu_Webbies):
                Toast.makeText(this, "V4Webbies", Toast.LENGTH_LONG).show();
                ViewV4();
                break;
            case(R.id.menu_Logout):
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                logout();
                break;
        }
        return true;
    }

    public void ProfileClick() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(tsr.this, Profile.class);
        startActivity(i);
        finish();
    }
    public void ViewV4() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(tsr.this, V4Webbies.class);
        startActivity(i);
        finish();
    }
    public void logout() {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(tsr.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public static class TimePickerFragmentFrom extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            edttimefrom.setText(hourOfDay + ":" + String.format("%02d", minute));
        }
    }

    public static class TimePickerFragmentTo extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            edttimeto.setText(hourOfDay + ":" + String.format("%02d", minute));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            edttimefrom.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public void showTimePickerDialogfrom(View v) {
        DialogFragment newFragment = new TimePickerFragmentFrom();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialogto(View v) {
        DialogFragment newFragment = new TimePickerFragmentTo();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void AddClick(View v) {
        AddPro addPro = new AddPro();
        addPro.execute("");
    }
    public void UpdateClick(View v) {
        UpdatePro updatePro = new UpdatePro();
        updatePro.execute("");
    }
    public void DelClick(View v) {
        DeletePro deletePro = new DeletePro();
        deletePro.execute("");
    }
    public void LogoutClick(View v) {
        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
        Intent i = new Intent(tsr.this, MainActivity.class);
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
            Toast.makeText(tsr.this, r, Toast.LENGTH_SHORT).show();
            String[] from = { "A", "B", "C", "D", "E" };
            int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc, R.id.lbltimefrom, R.id.lbltimeto };
            final SimpleAdapter ADA = new SimpleAdapter(tsr.this,
                    prolist, R.layout.listview_layout, from,
                    views);
            lstpro.setAdapter(ADA);
            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    proid = (String) obj.get("A");
                    //String proname = (String) obj.get("B");

                    String proname = (String) obj.get("B");
                    spinnerprojects.setSelection(getIndex(spinnerprojects, proname));

                    String prodesc = (String) obj.get("C");
                    String fromtime = (String) obj.get("D");
                    String totime = (String) obj.get("E");
                    edtprodesc.setText(prodesc);
                    edtproname.setText(proname);
                    //     qty.setText(qtys);
                    edttimefrom.setText(fromtime);
                    edttimeto.setText(totime);
                }
            });
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from TSR where name = " + Userloggedid +" and tsrdate>=dateadd(day, datediff(day, 0, getdate()), 0) and tsrdate<dateadd(day, datediff(day, 0, getdate()), 1) order by tsrdate, fromtime asc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("srno"));
                        datanum.put("B", rs.getString("project"));
                        datanum.put("C", rs.getString("description"));
                        datanum.put("D", rs.getString("fromtime"));
                        datanum.put("E", rs.getString("totime"));
                        prolist.add(datanum);
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";
            }
            return z;
        }
    }


    public class AddPro extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        String timefrom = edttimefrom.getText().toString();
        String timeto = edttimeto.getText().toString();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(tsr.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
                edtproname.setText("");
                edtprodesc.setText("");
                edttimefrom.setText("");
                edttimeto.setText("");
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if (prname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please enter details";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        String query = "insert into Tsr (Project,name, fromtime, totime, description,tsrdate,createdon) values ('" + prname + "','" + Userloggedid + "','" + timefrom + "','" + timeto + "','" + prodesc + "','" + dates + "', getdate())";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Added Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }


    public class UpdatePro extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        String timefrom = edttimefrom.getText().toString();
        String timeto = edttimeto.getText().toString();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(tsr.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
                edtproname.setText("");
                edtprodesc.setText("");
                edttimefrom.setText("");
                edttimeto.setText("");
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please enter details";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        String query = "Update tsr set project='"+proname+"',fromtime='"+timefrom+"',totime='"+timeto+"',description='"+prodesc+"' , tsrdate='"+dates+"' where srno="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }


    public class DeletePro extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;
        String proname = edtproname.getText().toString();
        String prodesc = edtprodesc.getText().toString();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(tsr.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute("");
                edtproname.setText("");
                edtprodesc.setText("");
                edttimefrom.setText("");
                edttimeto.setText("");
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals("") || prodesc.trim().equals(""))
                z = "Please select to delete";
            else {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        String query = "delete from tsr where srno="+proid;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Deleted Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
