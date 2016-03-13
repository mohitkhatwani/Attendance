package com.example.mohit.time;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;

public class manualEntry extends AppCompatActivity {
   Toolbar tb1;
    DrawerLayout drawerLayout;          //Drawer
    ActionBarDrawerToggle abdt;
    NavigationView nv1;

    DbHelper dbHelper;

    String[] details;

    int id,flag;
    String dates;
    TextView date;
    EditText start1,exit1,break1_start1,break1_stop1,break2_start1,break2_stop1,break3_start1,break3_stop1;
    Button edit;

    private int mYear, mMonth, mDay,mHour,mMinute;

    private  int yearSelected,monthSelected,daySelected,entryhourSelected,entryminuteSelected,exithourSelected,exitminSelected
            ,break1_hrs_start, break2_hrs_start,break3_hrs_start,break1_hrs_stop, break2_hrs_stop,break3_hrs_stop,
            break1_min_start,break2_min_start,break3_min_start,break1_min_stop,break2_min_stop,break3_min_stop;

    private String entry, exit, break1_start, break2_start, break3_start,break1_stop,break2_stop,break3_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();
//        id = intent.getIntExtra("id",0);
        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedpreferences.getInt("id",0);
        Log.e("manual id", String.valueOf(id));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);


        tb1=(Toolbar)findViewById(R.id.app_bar_manual);
        setSupportActionBar(tb1);

        date = (TextView) findViewById(R.id.date_manual);
        start1 = (EditText) findViewById(R.id.entry_time);
        exit1 = (EditText) findViewById(R.id.exit_time);
        break1_start1 = (EditText) findViewById(R.id.b1_start);
        break2_start1 = (EditText) findViewById(R.id.b2_start);
        break3_start1 = (EditText) findViewById(R.id.b3_start);

        break1_stop1 = (EditText) findViewById(R.id.b1_stop);
        break2_stop1 = (EditText) findViewById(R.id.b2_stop);
        break3_stop1 = (EditText) findViewById(R.id.b3_stop);

        edit = (Button) findViewById(R.id.edit);

        dbHelper = new DbHelper(getApplicationContext());
        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

         details = new String[8];


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);



        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        flag = 1;
                        yearSelected = year;
                        monthSelected = monthOfYear+1;
                        daySelected = dayOfMonth;

                        if(daySelected == mDay) {
                            Toast.makeText(getApplication(),getString(R.string.manual_today),Toast.LENGTH_SHORT).show();
                            //Intent intent = new Intent(getApplicationContext(),userHome.class);
                            finish();
                           // startActivity(intent);

                        }

                        if(daySelected < 10 && monthSelected > 9) {
                            dates = yearSelected + "-"+monthSelected+"-0"+daySelected;
                        } else if(daySelected > 9 && monthSelected <10 ) {
                            dates = yearSelected+"-0"+monthSelected+"-"+daySelected;
                        } else if(daySelected<10 && monthSelected<10){
                            dates = yearSelected+"-0"+monthSelected+"-0"+daySelected;
                        }else {
                            dates = yearSelected + "-"+monthSelected+"-"+daySelected;
                        }

                        date.setText(dates);
                        details = dbHelper.getManualEntryDetails(id,dates);

                        start1.setText(details[0]);
                        break1_start1.setText(details[1]);
                        break1_stop1.setText(details[2]);
                        break2_start1.setText(details[3]);
                        break2_stop1.setText(details[4]);
                        break3_start1.setText(details[5]);
                        break3_stop1.setText(details[6]);
                        exit1.setText(details[7]);

                    }

                }, mYear, mMonth, mDay);
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                flag = 0;
                Intent intent = new Intent(getApplicationContext(), userHome.class);
                finish();
                startActivity(intent);
            }
        });
        dpd.show();



        start1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              TimePickerDialog timePickerDialog;
              timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                  @Override
                  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                          entryhourSelected = hourOfDay;

                          entryminuteSelected = minute;

                      if (entryhourSelected < 10 && entryminuteSelected > 9) {
                          start1.setText("0" + entryhourSelected + ":" + entryminuteSelected);
                      } else if(entryhourSelected >9 && entryminuteSelected < 10){
                          start1.setText(entryhourSelected + ":0" + entryminuteSelected);
                      }else if(entryhourSelected <10 && entryminuteSelected < 10) {
                          start1.setText("0"+entryhourSelected+":0"+entryminuteSelected);
                      }else {
                          start1.setText(entryhourSelected+":"+entryminuteSelected);
                      }
                  }
              }, mHour, mMinute, true);
              timePickerDialog.setTitle("Enter Entry Time");
              timePickerDialog.show();
          }
      });

        break1_start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                            break1_hrs_start = hourOfDay;

                            break1_min_start = minute;

                        if (break1_hrs_start < 10 && break1_min_start > 9) {
                            break1_start1.setText("0" + break1_hrs_start + ":" + break1_min_start);
                        } else if(break1_hrs_start >9 && break1_min_start < 10){
                            break1_start1.setText(break1_hrs_start + ":0" + break1_min_start);
                        }else if(break1_hrs_start <10 && break1_min_start < 10) {
                            break1_start1.setText("0"+break1_hrs_start+":0"+break1_min_start);
                        }else {
                            break1_start1.setText(break1_hrs_start+":"+break1_min_start);
                        }
                    }


                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break1 Start");
                timePickerDialog.show();
            }
        });

        break1_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                            break1_hrs_stop = hourOfDay;

                            break1_min_stop = minute;

                        if (break1_hrs_stop < 10 && break1_min_stop > 9) {
                            break1_stop1.setText("0" + break1_hrs_stop + ":" + break1_min_stop);
                        } else if(break1_hrs_stop >9 && break1_min_stop < 10){
                            break1_stop1.setText(break1_hrs_stop + ":0" + break1_min_stop);
                        }else if(break1_hrs_stop <10 && break1_min_stop < 10) {
                            break1_stop1.setText("0"+break1_hrs_stop+":0"+break1_min_stop);
                        }else {
                            break1_stop1.setText(break1_hrs_stop+":"+break1_min_stop);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break1 Stop");
                timePickerDialog.show();

            }
        });

        break2_start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                            break2_hrs_start = hourOfDay;

                            break2_min_start = minute;

                        if (break2_hrs_start < 10 && break2_min_start > 9) {
                            break2_start1.setText("0" + break2_hrs_start + ":" + break2_min_start);
                        } else if(break2_hrs_start >9 && break2_min_start < 10){
                            break2_start1.setText(break2_hrs_start + ":0" + break2_min_start);
                        }else if(break2_hrs_start <10 && break2_min_start < 10) {
                            break2_start1.setText("0"+break2_hrs_start+":0"+break2_min_start);
                        }else {
                            break2_start1.setText(break2_hrs_start+":"+break2_min_start);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break2 Start");
                timePickerDialog.show();

            }
        });

        break2_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        break2_hrs_stop = hourOfDay;

                        break2_min_stop = minute;

                        if (break2_hrs_stop < 10 && break2_min_stop > 9) {
                            break2_stop1.setText("0" + break2_hrs_stop + ":" + break2_min_stop);
                        } else if (break2_hrs_stop > 9 && break2_min_stop < 10) {
                            break2_stop1.setText(break2_hrs_stop + ":0" + break2_min_stop);
                        } else if (break2_hrs_stop < 10 && break2_min_start < 10) {
                            break2_stop1.setText("0" + break2_hrs_stop + ":0" + break2_min_stop);
                        } else {
                            break2_stop1.setText(break2_hrs_stop + ":" + break2_min_stop);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break2 Stop");
                timePickerDialog.show();

            }
        });



        break3_start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        break3_hrs_start = hourOfDay;

                        break3_min_start = minute;

                        if (break3_hrs_start < 10 && break3_min_start > 9) {
                            break3_start1.setText("0" + break3_hrs_start + ":" + break3_min_start);
                        } else if (break3_hrs_start > 9 && break3_min_start < 10) {
                            break3_start1.setText(break3_hrs_start + ":0" + break3_min_start);
                        } else if (break3_hrs_start < 10 && break3_min_start < 10) {
                            break3_start1.setText("0" + break3_hrs_start + ":0" + break3_min_start);
                        } else {
                            break3_start1.setText(break3_hrs_start + ":" + break3_min_start);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break3 Start");
                timePickerDialog.show();

            }
        });

        break3_stop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            break3_hrs_stop = hourOfDay;

                            break3_min_stop = minute;


                        if (break3_hrs_stop < 10 && break3_min_stop > 9) {
                            break3_stop1.setText("0" + break3_hrs_stop + ":" + break3_min_stop);
                        } else if(break3_hrs_stop >9 && break3_min_stop < 10){
                            break3_stop1.setText(break3_hrs_stop + ":0" + break3_min_stop);
                        }else if(break3_hrs_stop <10 && break3_min_stop < 10) {
                            break3_stop1.setText("0"+break3_hrs_stop+":0"+break3_min_stop);
                        }else {
                            break3_stop1.setText(break3_hrs_stop+":"+break3_min_stop);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Break3 Stop");
                timePickerDialog.show();
            }
        });

        exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(manualEntry.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                            exithourSelected = hourOfDay;

                            exitminSelected = minute;

                        Log.e("Just to check", String.valueOf(exithourSelected));
                        if (exithourSelected < 10 && exitminSelected > 9) {
                            exit1.setText("0" + exithourSelected + ":" + exitminSelected);
                        } else if(exithourSelected >9 && exitminSelected < 10){
                            exit1.setText(exithourSelected + ":0" + exitminSelected);
                        }else if(exithourSelected <10 && exitminSelected < 10) {
                            exit1.setText("0"+exithourSelected+":0"+exitminSelected);
                        }else {
                            exit1.setText(exithourSelected+":"+exitminSelected);
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.setTitle("Enter Exit Time");
                timePickerDialog.show();


            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long working_mins = 0,break_min = 0;


                break3_stop = break3_stop1.getText().toString();
                break3_start = break3_start1.getText().toString();
                break1_start = break1_start1.getText().toString();
                break1_stop = break1_stop1.getText().toString();
                break2_stop = break2_stop1.getText().toString();
                break2_start = break2_start1.getText().toString();
                entry = start1.getText().toString();
                exit = exit1.getText().toString();


    if((!break2_start.equals("null")&& break2_stop.equals("null")) || (!break1_start.equals("null")&& break1_stop.equals("null")) || (!break3_start.equals("null")&& break3_stop.equals("null"))) {

         Toast.makeText(getApplicationContext(),"Exit Time not Provided",Toast.LENGTH_SHORT).show();
    }else {
    dbHelper.updateManualEntry(id, entry, exit, break1_start, break2_start, break3_start, break1_stop, break2_stop, break3_stop, dates);


    start1.setText("");
    exit1.setText("");
    break1_start1.setText("");
    break1_stop1.setText("");
    break2_start1.setText("");
    break2_stop1.setText("");
    break3_start1.setText("");
    break3_stop1.setText("");
    Toast.makeText(getApplication(), "UPDATE DONE", Toast.LENGTH_SHORT).show();


    edit.setEnabled(false);
    edit.setVisibility(View.INVISIBLE);

    }
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawlay_manual);          /*Drawer*/
        nv1 = (NavigationView) findViewById(R.id.draw_manual);
        abdt = new ActionBarDrawerToggle(this, drawerLayout, tb1, R.string.drawer_open, R.string.drawer_close);
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
                       // Intent intent1 = new Intent(getApplicationContext(), userHome.class);
                        // intent1.putExtra("id",id);
                        finish();
                       // startActivity(intent1);
                        break;
                    }
                    case R.id.month: {
                        Intent month = new Intent(getApplicationContext(), MonthReport.class);
                        finish();
                        startActivity(month);
                        break;
                    }

                    case R.id.day:

                        Intent intent1 = new Intent(getApplicationContext(), DayReport.class);
                        // intent1.putExtra("id",id);
                        finish();
                        startActivity(intent1);
                        break;

                    case R.id.manual:
                        Toast.makeText(getApplicationContext(), "This is Manual Entry", Toast.LENGTH_SHORT).show();
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
        //Intent intent  = new Intent(manualEntry.this,userHome.class);
       // startActivity(intent);
finish();

    }


}
