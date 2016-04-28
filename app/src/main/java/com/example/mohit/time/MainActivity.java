package com.example.mohit.time;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    Button loginbutt;
    DbHelper dbh;
    EditText uname,passswd;
    List<String> usr,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uname = (EditText) findViewById(R.id.username);
        passswd = (EditText) findViewById(R.id.password);
        loginbutt=(Button)findViewById(R.id.Login);

         usr = new ArrayList<>();
         pass = new ArrayList<>();


        SharedPreferences sharedpreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uname2 =  sharedpreferences.getString("uname", null);

        String pass2 =  sharedpreferences.getString("pass", null);

            if(uname2 == null || pass2 == null) {

                loginbutt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbh = new DbHelper(getApplicationContext());
                        try {
                            dbh.createDataBase();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            dbh.openDataBase();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                        usr = dbh.getUserNameFromDB();
                        pass = dbh.getPassWordFromDB();

                        String uname1 = uname.getText().toString();
                        String passwd = passswd.getText().toString();

                        int flag = 0;

                        for (int i = 0; i < usr.size() ; i++) {


                            if (!uname1.equals("") && !passwd.equals("")) {

                                Log.e("username",uname1);
                                Log.e("password",passwd);

                                if (usr.get(i).equals(uname1)) {
                                    if( pass.get(i).equals(passwd)) {

                                        Log.e("username",uname1);
                                        Log.e("password",passwd);

                                        int id = dbh.getId(usr.get(i), pass.get(i));

                                        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor2 = sharedpreferences.edit();
                                        editor2.putInt("id", id);
                                        editor2.commit();

                                        final SharedPreferences sharedpreferences_details = getSharedPreferences("Details", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor3 = sharedpreferences_details.edit();
                                        editor3.putInt("id", id);
                                        editor3.commit();

                                        final SharedPreferences sharedpreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences1.edit();
                                        editor.putString("uname", uname1);
                                        editor.commit();

                                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                                        editor1.putString("pass", passwd);
                                        editor1.commit();

                                        Intent home = new Intent(getApplicationContext(), userHome.class); // User Home Class
                                        finish();
                                        startActivity(home);
                                        flag = 1;


                                    }
                                } else {
                                    flag = 0;

                                }
                            }
                        }

                        if (flag == 0) {


                            uname.setText("");
                            passswd.setText("");
                        }
                    }
                });


            }
        else {
                Intent home = new Intent(getApplicationContext(), userHome.class);
                finish();
                startActivity(home);
            }
    }

    @Override
    public void onBackPressed() {

        System.exit(0);
    }
}
