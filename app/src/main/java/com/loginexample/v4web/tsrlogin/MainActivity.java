package com.loginexample.v4web.tsrlogin;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    ConnectionClass connectionClass;
    EditText edtuserid,edtpass;
    Button btnlogin;
    ProgressBar pbbar;
    String srno, name, username, dob, mobileno;
    SharedPreferences shp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        TextView textViewTitle1 = (TextView)findViewById(R.id.Title1);
        TextView textViewTitle2 = (TextView)findViewById(R.id.Title2);
        Typeface myCustomefont = Typeface.createFromAsset(getAssets(),"fonts/V4WEB.TTF");
        textViewTitle1.setTypeface(myCustomefont);
        textViewTitle2.setTypeface(myCustomefont);
        connectionClass = new ConnectionClass();
        edtuserid = (EditText) findViewById(R.id.edtuserid);
        edtuserid.setTypeface(myCustomefont);
        edtpass = (EditText) findViewById(R.id.edtpass);
        edtpass.setTypeface(myCustomefont);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnlogin.setTypeface(myCustomefont);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        pbbar.setVisibility(View.GONE);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");
            }
        });

        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        String userid = shp.getString("UserId", "none");
        if (userid.trim().equals("none") || userid.trim().equals("")) {
        } else {
            SharedPreferences.Editor edit = shp.edit();
            edit.putString("UserId", srno);
            edit.putString("UserName", username);
            edit.putString("Name", name);
            edit.putString("DOB", dob);
            edit.putString("mobileno", mobileno);
            edit.commit();
            //Intent i = new Intent(MainActivity.this, AddProducts.class);
            //startActivity(i);
            //finish();
        }
    }
    public class DoLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String userid = edtuserid.getText().toString();
        String password = edtpass.getText().toString();
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,r,Toast.LENGTH_SHORT).show();
            if(isSuccess) {
                SharedPreferences.Editor edit = shp.edit();
                edit.putString("UserId", srno);
                edit.putString("UserName", username);
                edit.putString("Name", name);
                edit.putString("DOB", dob);
                edit.putString("mobileno", mobileno);
                edit.commit();
                Intent i = new Intent(MainActivity.this, Profile.class);
                startActivity(i);
                finish();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            if(userid.trim().equals("")|| password.trim().equals(""))
                z = "Please enter User Id and Password";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "select * from login where username='" + userid + "' and password='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            srno = rs.getString("srno");
                            username = rs.getString("username");
                            name = rs.getString("Name");
                            dob = rs.getString("DateofBirth");
                            mobileno = rs.getString("MobileNo");
                            z = "Login successfull";
                            isSuccess=true;
                        }
                        else
                        {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
    
}
