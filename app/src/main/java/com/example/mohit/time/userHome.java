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

    SharedPreferences sharedpreferences_details;
    SharedPreferences.Editor editor;

    String date;
    String Entrytime,ExitTime;
    Button entry,breakstart;
    TextView textView,en,ex,bre1,bre2,bre3;
    private int flag;

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

                    entry_exit_toggle = sharedpreferences_details.getInt("entry_exit",10);
                    if (entry_exit_toggle%2 ==  0 || entry_exit_toggle==10) {


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

                                        editor.putString("entry", Entrytime);
                                        editor.commit();
                                        en.setText("   Entry Time  :   " + Entrytime);
                                        entry_exit_toggle++;
                                        editor.putInt("entry_exit",entry_exit_toggle);
                                        editor.commit();
                                        breakstart.setEnabled(true);
                                        breakstart.setVisibility(View.VISIBLE);

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

                    if (entry_exit_toggle%2 ==  1) {


                        Log.e("check value", String.valueOf(entry_exit_toggle));

                    if(flag==0) {
                        dialogBuilder
                                .withTitle("Exit")                                  //.withTitle(null)  no title
                                .withTitleColor("#FFFFFF")                                  //def
                                .withDividerColor(R.color.accentColor)                              //def
                                .withMessage("Do you want to Exit?")                     //.withMessage(null)  no Msg
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
                                        //textview4.setVisibility(View.INVISIBLE);

                                        SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                                        ExitTime = sdfDateTime.format(new Date(System.currentTimeMillis()));
                                        entry_exit_toggle++;
                                        editor.putString("exit", ExitTime);
                                        editor.putInt("entry_exit",entry_exit_toggle);
                                        editor.commit();
                                        ex.setText("   Exit Time    :    " + ExitTime);

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
                    }else {
                        Toast.makeText(getApplicationContext(),"Cannot Exit while break is going on",Toast.LENGTH_SHORT).show();
                    }
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

                break_toggle = sharedpreferences_details.getInt("break_toggle",10);

                Log.e("break_toggle", String.valueOf(break_toggle));
                if(break_toggle % 2 ==0) {

                    breakstart.setText("BREAK STOP");

                    flag = 1;
                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                    position = sharedpreferences_details.getInt("break_pos", 0);
                    editor.putInt("break_pos",position).commit();

                    Log.e("pos in break_start", String.valueOf(position));
                    break_start[position] = sdfDateTime.format(new Date(System.currentTimeMillis()));
                    editor.putString("break_start",break_start[position]);
                    editor.commit();


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


                    position = sharedpreferences_details.getInt("break_pos", 0);
                    breakstart.setText("BREAK START");
                    flag =0;
                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("HH:mm", Locale.US);
                    break_stop[position] = sdfDateTime.format(new Date(System.currentTimeMillis()));


                    Log.e("pos in break_stop", String.valueOf(position));
                    switch (position) {
                        case 0: {


                            long break_difference = (long) 0;
                            try {

                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(sharedpreferences_details.getString("break_start","")).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            editor.putLong("break1", break_difference);
                            editor.commit();
                            bre1.setText("   Break 1   :     " + break_difference + " mins");
                            position++;
                            editor.putInt("break_pos", position);

                            editor.commit();
                        }break;

                        case 1: {
                            long break_difference = (long) 0;
                            try {

                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(sharedpreferences_details.getString("break_start","")).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            editor.putLong("break2", break_difference);
                            editor.commit();
                            bre2.setText("   Break 2   :     " + break_difference + " mins");
                            position++;
                            editor.putInt("break_pos", position);

                            editor.commit();
                        }break;

                        case 2: {
                            long break_difference = (long) 0;
                            try {

                                break_difference = sdfDateTime.parse(break_stop[position]).getTime() - sdfDateTime.parse(sharedpreferences_details.getString("break_start","")).getTime();
                                break_difference = break_difference / (60 * 1000) % 60;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            editor.putLong("break3", break_difference);
                            editor.commit();
                            bre3.setText("   Break 3   :     "+break_difference+ " mins");
                            position++;
                            editor.putInt("break_pos", position);

                            editor.commit();
                        }
                    }



                    if( position == 3) {
                        breakstart.setText("Breaks");
                        breakstart.setTextColor(getResources().getColor(R.color.primary));
                        breakstart.setTextSize(20);
                        breakstart.setEnabled(false);
                      //  breakstart.setVisibility(View.INVISIBLE);

                    }

                }

                break_toggle++;
                editor.putInt("break_toggle",break_toggle);
                editor.commit();

            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        sharedpreferences_details = getSharedPreferences("Details", Context.MODE_PRIVATE);


        editor = sharedpreferences_details.edit();

        en=(TextView)findViewById(R.id.Entry);
        ex = (TextView) findViewById(R.id.ExitTime);
        bre1 = (TextView) findViewById(R.id.Break1);
        bre2 = (TextView) findViewById(R.id.Break2);
        bre3 = (TextView) findViewById(R.id.Break3);
        entry = (Button) findViewById(R.id.entry);
        breakstart = (Button) findViewById(R.id.breakStart);
        breakstart.setEnabled(false);
        breakstart.setVisibility(View.INVISIBLE);
        entry.setBackgroundColor(getResources().getColor(R.color.green));



        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);


        int check_id = sharedpreferences_details.getInt("id",0);
        if(check_id != 0) {



            String entry1 = sharedpreferences_details.getString("entry",null);
            String exit1 = sharedpreferences_details.getString("exit",null);
            Long break1 = sharedpreferences_details.getLong("break1", 100);
            Long break2 = sharedpreferences_details.getLong("break2", 100);
            Long break3 = sharedpreferences_details.getLong("break3", 100);
            break_toggle = sharedpreferences_details.getInt("break_toggle",10);
            entry_exit_toggle = sharedpreferences_details.getInt("entry_exit",10);

            Log.e("entry_exit tag", String.valueOf(entry_exit_toggle));
            if(entry_exit_toggle != 10) {
                if(entry_exit_toggle%2 == 1) {
                    entry.setText("EXIT");
                    entry.setBackgroundColor(getResources().getColor(R.color.red));

                    breakstart.setEnabled(true);
                    breakstart.setVisibility(View.VISIBLE);
                   // Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_SHORT).show();
                }else {
                    entry.setText("ENTRY");
                    entry.setBackgroundColor(getResources().getColor(R.color.green));
                }
            }

            position = sharedpreferences_details.getInt("break_pos",10);
            Log.e("position in case", String.valueOf(position));

            if(position==0) {
                bre1.setText("Break 1 is going on");

            }else if(position==1) {
                bre2.setText("Break 2 is going on");
            }else if(position==2){
                bre3.setText("Break 3 is going on");
            }


            if(break_toggle != 10) {
                if(break_toggle %2 == 1) {
                    breakstart.setText("BREAK STOP");
                }else {
                    breakstart.setText("BREAK START");
                }
            }
            if(entry1 != null) {
                en.setText(" Entry Time  :     "+entry1);
            }
            if(exit1 != null) {
                ex.setText(" Exit Time  :      "+exit1);
            }
            if(break1 !=100) {
                bre1.setText("   Break1   :     "+String.valueOf(break1)+" mins");
            }
            if(break2 !=100) {
                bre2.setText("   Break2   :     "+String.valueOf(break2)+" mins");
            }
            if(break3 !=100) {
                bre3.setText("   Break3   :     "+String.valueOf(break3)+" mins");
            }

        }else {

        }
        id =  sharedpreferences.getInt("id",0);
        Log.e("home id", String.valueOf(id));


        dbHelper = new DbHelper(getApplicationContext());

        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        date = sdfDateTime.format(new Date(System.currentTimeMillis()));
        Log.e("userHome check date",date);

        sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("id", id);
        editor.commit();


        //array memory allocation
        break_start = new String[100];
        break_stop = new String[100];














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
                        finish();
                        startActivity(month);
                        break;
                    }
                    case R.id.day:

//                        Intent intent  = new Intent(getApplicationContext(),UserNameList.class);
//                        startActivity(intent);

                        Intent date = new Intent(getApplicationContext(), DayReport.class);
                        finish();
                        startActivity(date);
                        break;
                    case R.id.manual:
                        Intent manual = new Intent(getApplicationContext(), manualEntry.class);
                        finish();
                        startActivity(manual);
                        break;
                    case R.id.logout:
                        final SharedPreferences sharedpreferences1 = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences1.edit();
                        editor.remove("uname");
                        editor.remove("pass");
                        editor.commit();

                        sharedpreferences_details.edit().clear().commit();
                        Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                       finish();
                        startActivity(logout);
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }

        });



    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        onPause();
//        drawerLayout.closeDrawer(GravityCompat.START);
//    }

}
