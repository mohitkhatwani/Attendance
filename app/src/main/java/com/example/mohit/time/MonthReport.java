package com.example.mohit.time;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MonthReport extends ActionBarActivity {

    private Calendar mCalendar;

    int mYear,mMonth,mDay,yearSelected,monthSelected;

    int time_hrs,time_min;

    Button b;
    TextView textView,user_drawer;

    DbHelper dbHelper;

    DatePicker dp1;
    DrawerLayout drawerLayout;          //Drawer
    ActionBarDrawerToggle abdt;
    NavigationView nv1;
    Toolbar tb1;
    EditText month_id;



    int id,id_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        final NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(MonthReport.this);

        dbHelper = new DbHelper(getApplicationContext());
        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report);

        month_id = (EditText) findViewById(R.id.month_report_id);

        dp1=(DatePicker)findViewById(R.id.datePicker2);
        b = (Button) findViewById(R.id.month_button);

        textView = (TextView) findViewById(R.id.tool_time);
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
        textView.setText(newtime);

        tb1=(Toolbar)findViewById(R.id.app_bar_month);
        setSupportActionBar(tb1);






b.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedpreferences.getInt("id",0);

        yearSelected = dp1.getYear();
        monthSelected = dp1.getMonth()+1;

        int show=1;
        String temp = month_id.getText().toString();
        if(!temp.equals("")) {
            id_text = Integer.parseInt(temp);
        }

        if(id_text!=0) {

            if (dbHelper.getDesignation(id) > dbHelper.getDesignation(id_text)) {
                id = id_text;
            }
            else {
                Toast.makeText(getApplicationContext(),"Not Authorized",Toast.LENGTH_SHORT).show();
                show=0;
                month_id.setText(null);
            }
        }
        Log.e("month selected", String.valueOf(monthSelected));
        int working_time = dbHelper.monthReport(id, String.valueOf(monthSelected));
        int break_time = dbHelper.getMonthBreak(id, String.valueOf(monthSelected));

        int eff_month_time = working_time - break_time;

        time_hrs = eff_month_time / 60;
        time_min = eff_month_time % 60;

        if(show==1) {
            dialogBuilder
                    .withTitle("REPORT")                                  //.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                                  //def
                    .withDividerColor(R.color.accentColor)                              //def
                    .withMessage("Effective Working hrs:" + time_hrs + "hrs " + time_min + " min \n")               //.withMessage(null)  no Msg
                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                    .withDialogColor("#7986CB")                               //def  | withDialogColor(int resid
                    .withDuration(700)                                          //def
                    .withEffect(null)                                         //def Effectstype.Slidetop
                    .withButton1Text("DISMISS")                                      //def gone//def gone
                    .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)//.setCustomView(View or ResId,context)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogBuilder.dismiss();
                        }
                    })
                    .show();
        }
//
//                    }
//                }, mYear, mMonth, mDay);
//        dpd.show();
    }
});


       //((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day","id","android")).setVisibility(View.GONE);


        drawerLayout=(DrawerLayout)findViewById(R.id.drawlay_month);          /*Drawer*/
        nv1=(NavigationView)findViewById(R.id.draw_month);
        abdt=new ActionBarDrawerToggle(this,drawerLayout,tb1,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(abdt);
        abdt.syncState();
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                abdt.syncState();
            }
        });

//        user_drawer = (TextView) findViewById(R.id.user_drawer);
        String username = dbHelper.getUserDrawer(id);
        user_drawer.setText(username);

        nv1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home: {
                        Intent intent1 = new Intent(getApplicationContext(), userHome.class);

                        finish();
                        startActivity(intent1);
                        break;
                    }
                    case R.id.month: {
                        Toast.makeText(getApplicationContext(), "This is Month Report", Toast.LENGTH_SHORT).show();

                        break;
                    }

                    case R.id.day:

                        Intent month = new Intent(getApplicationContext(), DayReport.class);
                        finish();
                        startActivity(month);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent  = new Intent(MonthReport.this,userHome.class);
        startActivity(intent);


    }


}
