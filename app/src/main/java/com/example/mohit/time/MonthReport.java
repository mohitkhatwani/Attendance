package com.example.mohit.time;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MonthReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    int yearSelected,monthSelected;String month;

    int time_hrs,time_min;

    Button b;
    TextView textView;

    DbHelper dbHelper;

    DatePicker dp1;
    DrawerLayout drawerLayout;          //Drawer
    ActionBarDrawerToggle abdt;
    NavigationView nv1;
    Toolbar tb1;

    Spinner spinner;
    List<String> unames;

    int id,desig,id_sel;

    int working_time,break_time,break_time_hrs,break_time_mins;
    private int eff_working_hrs,eff_working_min;

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

        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedpreferences.getInt("id", 0);

        desig = dbHelper.getDesignation(id);
        Log.e("desig", String.valueOf(desig));


        unames = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner_month);
        spinner.setOnItemSelectedListener(MonthReport.this);

        unames = dbHelper.getUserNameFromDesig(desig);

        MyAdapter aa = new MyAdapter(unames);
        spinner.setAdapter(aa);

        b = (Button) findViewById(R.id.month_button);
        dp1 = (DatePicker) findViewById(R.id.date_month);

        textView = (TextView) findViewById(R.id.tool_time);
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
        textView.setText(newtime);

        tb1=(Toolbar)findViewById(R.id.app_bar_month);
        setSupportActionBar(tb1);

        b.setOnClickListener(new View.OnClickListener() {
    @Override
             public void onClick(View v) {


        yearSelected = dp1.getYear();
        monthSelected = dp1.getMonth()+1;

        int show=1;

        Log.e("month selected", String.valueOf(monthSelected));
        if(monthSelected < 10 ) {
            month = "0"+String.valueOf(monthSelected);
        }else if(monthSelected >= 10) {
            month = String.valueOf(monthSelected);
        }


        try {
             working_time = dbHelper.monthReport(id_sel, month);
            break_time = dbHelper.getMonthBreak(id_sel,month);

        }catch (NumberFormatException e) {
            show = 0;
            dialogBuilder
                    .withTitle("NO DATA FOUND")                                  //.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                                  //def
                    .withDividerColor(R.color.accentColor)                              //def
                    .withMessage("INCORRECT DATE SELECTED")               //.withMessage(null)  no Msg
                    .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                    .withDialogColor("#7986CB")                               //def  | withDialogColor(int resid
                    .withDuration(700)                                          //def
                    .withEffect(null)                                         //def Effectstype.Slidetop
                    .withButton1Text("OK")                                      //def gone//def gone
                    .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)//.setCustomView(View or ResId,context)
                    .setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialogBuilder.dismiss();
                        }
                    })
                    .show();
        }

        time_hrs = working_time / 60;

        time_min = working_time % 60;

        working_time = working_time - break_time;

        break_time_hrs = break_time /60;
        break_time_mins = break_time % 60;

        eff_working_hrs = working_time / 60;

        eff_working_min = working_time % 60;

        if(show==1) {
            dialogBuilder
                    .withTitle("REPORT")                                  //.withTitle(null)  no title
                    .withTitleColor("#FFFFFF")                                  //def
                    .withDividerColor(R.color.accentColor)                              //def
                    .withMessage("Working hrs:" + time_hrs + " hrs " + time_min + " min\nBreak " +break_time_hrs+" hours " + break_time_mins + " mins \nEffective Working hrs:" + eff_working_hrs + " hrs " + eff_working_min + " min\n")               //.withMessage(null)  no Msg
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

    }
});

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String uname_sel = unames.get(position);
        int idl = dbHelper.getIdFromUname(uname_sel);
        id_sel = idl;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class MyAdapter extends BaseAdapter {
        private List<String> uname;
        public MyAdapter(List<String> unames) {
            this.uname = unames;
        }

        @Override
        public int getCount() {
            return uname.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView user_name;
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.list_uname, parent, false);
            user_name = (TextView) v.findViewById(R.id.user_name);
            user_name.setText(uname.get(position));
            return v;
        }
    }
}
