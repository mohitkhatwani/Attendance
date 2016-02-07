package com.example.mohit.time;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class userHome extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle abdt;
    NavigationView nv1;
    Toolbar tb1;
    int entry_exit_toggle,break_toggle;
    int id ;                    //id
    DbHelper dbHelper;      //Database

    String[] break_start,break_stop;
    int position;


    String date="";
    int k=0;
    String Entrytime="",ExitTime="";
    Button entry,breakstart;
    TextView textView,en,ex,bre1,bre2,bre3,textview4;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abdt.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {

        super.onResume();
        final NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(userHome.this);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int check = dbHelper.checkDate(id, date);
                if (check != 1) {               // Already left the company

                    if (entry_exit_toggle == 0) {


                        bre1.setText("");
                        bre2.setText("");
                        bre3.setText("");
                        ex.setText("");


                        dialogBuilder
                                .withTitle("REPORT")                                  //.withTitle(null)  no title
                                .withTitleColor("#FFFFFF")                                  //def
                                .withDividerColor(R.color.accentColor)                              //def
                                .withMessage("Your Day is starting")               //.withMessage(null)  no Msg
                                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#7986CB")                               //def  | withDialogColor(int resid
                                .withDuration(300)                                          //def
                                .withEffect(null)                                         //def Effectstype.Slidetop
                                .withButton1Text("OK")
                                .withButton2Text("CANCEL")//def gone//def gone
                                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)//.setCustomView(View or ResId,context)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        entry.setText("EXIT");
                                        entry.setBackgroundColor(getResources().getColor(R.color.red));
                                        SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                                        Entrytime = sdfDateTime.format(new Date(System.currentTimeMillis()));

                                        en.setText("   Entry Time  :     " + Entrytime);
                                        entry_exit_toggle++;
                                        breakstart.setEnabled(true);
                                        breakstart.setVisibility(View.VISIBLE);
                                        textview4.setVisibility(View.VISIBLE);
                                        dialogBuilder.dismiss();
                                    }
                                })
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                })
                                .show();

                    }

                    if (entry_exit_toggle == 1) {


                        Log.e("check value", String.valueOf(entry_exit_toggle));
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("ExitTime", ExitTime);
//                editor.commit();

                        dialogBuilder
                                .withTitle("Modal Dialog")                                  //.withTitle(null)  no title
                                .withTitleColor("#FFFFFF")                                  //def
                                .withDividerColor(R.color.accentColor)                              //def
                                .withMessage("Do you want to Exit")                     //.withMessage(null)  no Msg
                                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                                .withDialogColor("#7986CB")                               //def  | withDialogColor(int resid)
                                .withDuration(300)                                          //def
                                .withEffect(null)                                         //def Effectstype.Slidetop
                                .withButton1Text("YES")                                      //def gone
                                .withButton2Text("NO")                                  //def gone
                                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)//.setCustomView(View or ResId,context)
                                .setButton1Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        entry.setVisibility(View.INVISIBLE);
                                        textview4.setVisibility(View.INVISIBLE);
                                        SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                                        ExitTime = sdfDateTime.format(new Date(System.currentTimeMillis()));

                                        ex.setText("   Exit Time    :      " + ExitTime);


                                        dbHelper.insertDetails(id, Entrytime, ExitTime, date);
                                        dbHelper.insertBreakDetails(id, break_start, break_stop, date);
                                        dialogBuilder.dismiss();

                                    }
//

//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("Entrytime", Entrytime);
//                editor.commit();


                                })
                                .setButton2Click(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogBuilder.dismiss();
                                    }
                                }).show();
                    }
                } else {
                    dialogBuilder
                            .withTitle("ALERT !")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor(R.color.accentColor)                              //def
                            .withMessage("You Have Already left the Company")                     //.withMessage(null)  no Msg
                            .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#7986CB")                               //def  | withDialogColor(int resid)
                            .withDuration(200)                                          //def
                            .withEffect(null)                                         //def Effectstype.Slidetop
                            .withButton1Text("OK")                                      //def gone
                            .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)//.setCustomView(View or ResId,context)
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogBuilder.dismiss();

                                }
//

//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("Entrytime", Entrytime);
//                editor.commit();


                            }).show();
                }
            }
        });

        breakstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(break_toggle % 2 ==0 ) {

                    breakstart.setText("BREAK STOP");

                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                    break_start[position] = sdfDateTime.format(new Date(System.currentTimeMillis()));

                    switch (position) {
                        case 0:{
                            bre1.setText("Break 1 is going on");
                        }break;
                        case 1: {
                            bre2.setText("Break 2 is going on");
                        }break;
                        case 2: {
                            bre3.setText("Break 3 is going on");
                        }
                    }

                }


                if(break_toggle % 2 == 1) {

                    breakstart.setText("BREAK START");

                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                    break_stop[position] = sdfDateTime.format(new Date(System.currentTimeMillis()));



                    switch (position) {
                        case 0: {


                            long break_difference = (long) 0;
                            try {
                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(break_start[position]).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            bre1.setText("   Break 1   :     "+break_difference+ " mins");
                        }break;

                        case 1: {
                            long break_difference = (long) 0;
                            try {
                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(break_start[position]).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            bre2.setText("   Break 2   :     "+break_difference + " mins");
                        }break;

                        case 2: {
                            long break_difference = (long) 0;
                            try {
                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(break_start[position]).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            bre3.setText("   Break 3   :     "+break_difference+ " mins");
                        }
                    }

                    position++;

                    if( position == 3) {
                        breakstart.setEnabled(false);
                        breakstart.setVisibility(View.INVISIBLE);
                    }

                }

                break_toggle++;

            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
       id =  sharedpreferences.getInt("id",0);
        Log.e("home id", String.valueOf(id));
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_home);

        dbHelper = new DbHelper(getApplicationContext());

        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        en=(TextView)findViewById(R.id.Entry);
        String temp;
        temp=en.getText().toString();
        final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        date =  sdfDateTime.format(new Date(System.currentTimeMillis()));
        Log.e("userHome check date",date);

        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id", id);
        editor.commit();

        if (temp==""){
            String tempEntry,tempexit;
            tempEntry=sharedpreferences.getString("Entrytime",Entrytime);
            en.setText(tempEntry);
            tempexit=sharedpreferences.getString("ExitTime",ExitTime);
            en.setText(tempexit);
        }

        entry = (Button) findViewById(R.id.entry);
        breakstart = (Button) findViewById(R.id.breakStart);
        ex = (TextView) findViewById(R.id.ExitTime);
        bre1 = (TextView) findViewById(R.id.Break1);
        bre2 = (TextView) findViewById(R.id.Break2);
        bre3 = (TextView) findViewById(R.id.Break3);

        textview4 = (TextView) findViewById(R.id.textView4);
        textview4.setVisibility(View.INVISIBLE);
        entry_exit_toggle=0;

        //array memory allocation
        break_start = new String[3];
        break_stop = new String[3];

        breakstart.setEnabled(false);
        breakstart.setVisibility(View.INVISIBLE);
        entry.setBackgroundColor(getResources().getColor(R.color.green));





        break_toggle = 0; position=0;






        textView = (TextView) findViewById(R.id.tool_time);     /*ToolBAr*/
        textView.setText(date);
        tb1=(Toolbar)findViewById(R.id.app_bar_home);
        setSupportActionBar(tb1);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawlay);          /*Drawer*/
        nv1=(NavigationView)findViewById(R.id.draw);
        abdt=new ActionBarDrawerToggle(this,drawerLayout,tb1,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(abdt);

        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                abdt.syncState();
            }
        });




        nv1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home: {
                        Toast.makeText(getApplicationContext(), "This is the home page", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case R.id.month: {
                        Intent month = new Intent(getApplicationContext(), MonthReport.class);
                       // finish();
                        startActivity(month);
                        break;
                    }
                    case R.id.day:

//                        Intent intent  = new Intent(getApplicationContext(),UserNameList.class);
//                        startActivity(intent);

                        Intent date = new Intent(getApplicationContext(), DayReport.class);
                        startActivity(date);
                        break;
                    case R.id.manual:
                        Intent manual = new Intent(getApplicationContext(), manualEntry.class);
                        //  manual.putExtra("id",id);
                       // finish();
                        startActivity(manual);
                        break;
                    case R.id.logout:
                        final SharedPreferences sharedpreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences1.edit();

                        editor.remove("uname");
                        editor.remove("pass");
                        editor.commit();
                        Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                       // finish();
                        startActivity(logout);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }

        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onPause();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

}
