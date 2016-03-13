package com.example.mohit.time;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;


public class DayReport extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {
TextView textView;
    DatePicker dp1;

    int id,desig,id_text;
    Button b1;

    String date_selected;

    List<String> unames,get_unames;

    DrawerLayout drawerLayout;          //Drawer
    ActionBarDrawerToggle abdt;
    NavigationView nv1;
    Toolbar tb1;

   Spinner spinner;

    int working;
    int working_hrs=0,working_min=0,break_time,eff_working_hrs,eff_working_min;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(DayReport.this);
        setContentView(R.layout.activity_day_report);

        unames = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(DayReport.this);

        dbHelper = new DbHelper(getApplicationContext());
        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//         id = getIntent().getExtras().getInt("id_for_report");

        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedpreferences.getInt("id", 0);
//
        desig = dbHelper.getDesignation(id);
        Log.e("desig", String.valueOf(desig));
        //unames = dbHelper.getUserNameFromDesig(desig);
//
        unames = dbHelper.getUserNameFromDesig(desig);


        for(int i=0;i<unames.size();i++) {
            Log.e("check_unames",unames.get(i));
        }


        MyAdapter aa = new MyAdapter(unames);
        spinner.setAdapter(aa);

        dp1=(DatePicker)findViewById(R.id.datePicker);
        b1=(Button)findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.tool_time);

        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final String newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
        textView.setText(newtime);

        tb1=(Toolbar)findViewById(R.id.app_bar_day);
        setSupportActionBar(tb1);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                 id_text=id;

                int day = dp1.getDayOfMonth();
                int month = dp1.getMonth()+1;
                int year = dp1.getYear();

                if(day < 10 && month > 9) {
                    date_selected = year + "-"+month+"-0"+day;
                } else if(day > 9 && month <10 ) {
                    date_selected = year+"-0"+month+"-"+day;
                } else if(day<10 && month<10){
                    date_selected = year+"-0"+month+"-0"+day;
                }else {
                    date_selected = year + "-"+month+"-"+day;
                }
                int show=1;

                Log.e("id while Day report", String.valueOf(id));
                Log.e("check date",date_selected);

                try {
                    working =  dbHelper.getDayReportWorking(id_text, date_selected);
                    break_time = dbHelper.getDayReportBreak(id_text, date_selected);
                }catch (NumberFormatException e) {
                    show =0;
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


                working_hrs = working / 60;

                working_min = working % 60;

                working = working - break_time;

                eff_working_hrs = working / 60;

                eff_working_min = working % 60;

                Log.e("working in hrs", String.valueOf(working_hrs));
                Log.e("working in min", String.valueOf(working_min));


                if(show==1) {
                    dialogBuilder
                            .withTitle("DAY REPORT")                                  //.withTitle(null)  no title
                            .withTitleColor("#FFFFFF")                                  //def
                            .withDividerColor(R.color.accentColor)                              //def
                            .withMessage("Working hrs:" + working_hrs + " hrs " + working_min + " min\nBreak hrs:" + break_time + " min\nEffective Working hrs:" + eff_working_hrs + " hrs " + eff_working_min + " min\n")               //.withMessage(null)  no Msg
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

        drawerLayout=(DrawerLayout)findViewById(R.id.drawlay_day);          /*Drawer*/
        nv1=(NavigationView)findViewById(R.id.draw_day);
        abdt=new ActionBarDrawerToggle(this,drawerLayout,tb1,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(abdt);
        abdt.syncState();
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                abdt.syncState();
            }
        });

        //user_drawer = (TextView) findViewById(R.id.user_drawer);
//        String username = dbHelper.getUserDrawer(id);
//        user_drawer.setText(username);

        nv1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home: {
                        //Intent intent1 = new Intent(getApplicationContext(),userHome.class);
                       //intent1.putExtra("id",id);
                        finish();
                      //  startActivity(intent1);
                        break;
                    }
                    case R.id.month: {
                        Intent month = new Intent(getApplicationContext(),MonthReport.class);
                        finish();
                        startActivity(month);
                        break;
                    }

                    case R.id.day:

                        Toast.makeText(getApplicationContext(),"This is Day Report",Toast.LENGTH_SHORT).show();
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
       // Intent intent  = new Intent(DayReport.this,userHome.class);
        finish();
       // startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        String uname_sel = unames.get(position);
        int idl = dbHelper.getIdFromUname(uname_sel);
       id_text = idl;
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
