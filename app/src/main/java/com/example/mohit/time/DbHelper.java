package com.example.mohit.time;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by munde on 16/12/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
    //destination path (location) of our database on device
    private static String DB_PATH = "";
    private static String DB_NAME ="Attendance.db";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;


    public DbHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1? Its database Version
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException
    {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }


    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException
    {
        //asset sqlite
        InputStream mInput = mContext.getAssets().open("Attendance.sqlite");
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        //Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }



    public List<String> getUserNameFromDB(){
        String query = "select uname From Login";
        Cursor cursor = mDataBase.rawQuery(query, null);
        List<String> userName = new ArrayList<>();
       int i1=0;
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    userName.add(i1, cursor.getString(0));

                    i1++;

                }while (cursor.moveToNext());
            }
        }
        //String usr1[] = {"mot","mudne"};
        return userName;

    }

    public int getId(String s, String pas) {
        int id=0;
        String query = "SELECT id FROM Login WHERE uname='"+s+"' AND passwd='"+pas+"'";
        Cursor cursor = mDataBase.rawQuery(query, null);
        cursor.moveToFirst();
        if(cursor.getCount()==1) {

            id = Integer.parseInt(cursor.getString(0));

        }
        return id;
    }

    public List<String> getPassWordFromDB(){
        String query = "select passwd From Login";
        Cursor cursor1 = mDataBase.rawQuery(query, null);
        List<String> pass = new ArrayList<>();
        int i1=0;
        if(cursor1.getCount()>0){
            if(cursor1.moveToFirst()){
                do{
                    pass.add(i1,cursor1.getString(0));
                    i1++;

                }while (cursor1.moveToNext());
            }
        }
        cursor1.close();
        return pass;

    }

    void insertDetails(int id, String entrytime, String exittime, String date) throws android.database.sqlite.SQLiteException{


        String query = "INSERT INTO Details(id,entry,exit,date) VALUES("+id+",'"+entrytime+"','"+exittime+"','"+date+"')";
            mDataBase.execSQL(query);

    }

    int getDayReportWorking(int id, String date) {

        int working_day_time = 0;

        String[] working_time = new String[2];
        String query = "SELECT strftime('%H',exit) - strftime('%H',entry) AS hour,strftime('%M',exit) - strftime('%M',entry) AS min FROM Details WHERE id = " + id + " AND date = '" + date + "'";
        Cursor cursor = mDataBase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String hour = cursor.getString(0);
                    String min = cursor.getString(1);

                    if (hour == null) {
                        working_time[0] = "0";
                    } else {
                        working_time[0] = hour;
                    }

                    if (min == null) {
                        working_time[1] = "0";
                    } else {
                        working_time[1] = min;
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();

            working_day_time = Integer.parseInt(working_time[0]) * 60;
            working_day_time += Integer.parseInt(working_time[1]);

            Log.e("working day time ", String.valueOf(working_day_time));

        }
            return working_day_time;
        }



    int getDayReportBreak(int id,String date) throws NumberFormatException{
        int break_total=0;
        String break_hours[] = new String[3];
        String break_mins[] = new String[3];

        getAllBreaks(id,date);

        String query = "SELECT strftime('%H',break1_stop) - strftime('%H',break1_start) AS b1h,strftime('%M',break1_stop) - strftime('%M',break1_start) AS b1m," +
                "strftime('%H',break2_stop) - strftime('%H',break2_start) AS b2h,strftime('%M',break2_stop) - strftime('%M',break2_start) AS b2m," +
                "strftime('%H',break3_stop) - strftime('%H',break3_start) AS b3h,strftime('%M',break3_stop) - strftime('%M',break3_start) AS b3m FROM Breaks WHERE break_id = "+id+" AND date = '"+date+"'";

        Cursor curso = mDataBase.rawQuery(query,null);



        if ((curso.getCount()>0)) {
            Log.e("check inside","inside");
            if(curso.moveToFirst()) {

                do {
                    String break1_hour = curso.getString(0);
                    String break1_min = curso.getString(1);
                    String break2_hour = curso.getString(2);
                    String break2_min = curso.getString(3);
                    String break3_hour = curso.getString(4);
                    String break3_min = curso.getString(5);

                   // Log.e("check day ", break1_hour);

                    if (break1_hour==(null)) {
                        break_hours[0] = "0";
                    } else {
                        break_hours[0] = break1_hour;
                    }
                    if (break1_min==(null)) {
                        break_mins[0] = "0";
                    } else {
                        break_mins[0] = break1_min;
                    }
                    if (break2_hour==(null)) {
                        break_hours[1] = "0";
                    } else {
                        break_hours[1] = break2_hour;
                    }
                    if (break2_min==(null)) {
                        break_mins[1] = "0";
                    } else {
                        break_mins[1] = break2_min;
                    }
                    if (break3_hour==(null)) {
                        break_hours[2] = "0";
                    } else {
                        break_hours[2] = break3_hour;
                    }
                    if (break3_min==(null)) {
                        break_mins[2] = "0";
                    } else {
                        break_mins[2] = break3_min;
                    }
                }while(curso.moveToNext());
            }
        }

        Log.e("report break",break_hours[0]+" "+break_mins[2]);
        break_total = Integer.parseInt(break_hours[0])*60 + Integer.parseInt(break_hours[1])*60 + Integer.parseInt(break_hours[2])*60;
        break_total += Integer.parseInt(break_mins[0]) + Integer.parseInt(break_mins[1]) + Integer.parseInt(break_mins[2]);

        Log.e("Break Total",String.valueOf(break_total));
        curso.close();
        return break_total;
    }

    int getMonthBreak(int id,String month)  throws NumberFormatException {
        int break_total=0, month_break_total =0 ;
        String break_hours[] = new String[3];
        String break_mins[] = new String[3];

        String query ="SELECT strftime('%H',break1_stop) - strftime('%H',break1_start) AS b1h,strftime('%M',break1_stop) - strftime('%M',break1_start) AS b1m," +
                "strftime('%H',break2_stop) - strftime('%H',break2_start) AS b2h,strftime('%M',break2_stop) - strftime('%M',break2_start) AS b2m," +
                "strftime('%H',break3_stop) - strftime('%H',break3_start) AS b3h,strftime('%M',break3_stop) - strftime('%M',break3_start) AS b3m FROM Breaks WHERE break_id = "+id+" AND strftime('%m',date)='"+month+"'";
        Cursor curso = mDataBase.rawQuery(query,null);
        if ((curso.getCount()>0)) {
            if(curso.moveToFirst()) {
                do {
                    String break1_hour = curso.getString(0);
                    String break1_min = curso.getString(1);
                    String break2_hour = curso.getString(2);
                    String break2_min = curso.getString(3);
                    String break3_hour = curso.getString(4);
                    String break3_min = curso.getString(5);

                    if (break1_hour==(null)) {
                        break_hours[0] = "0";
                    } else {
                        break_hours[0] = break1_hour;
                    }
                    if (break1_min==(null)) {
                        break_mins[0] = "0";
                    } else {
                        break_mins[0] = break1_min;
                    }
                    if (break2_hour==(null)) {
                        break_hours[1] = "0";
                    } else {
                        break_hours[1] = break2_hour;
                    }
                    if (break2_min==(null)) {
                        break_mins[1] = "0";
                    } else {
                        break_mins[1] = break2_min;
                    }
                    if (break3_hour==(null)) {
                        break_hours[2] = "0";
                    } else {
                        break_hours[2] = break3_hour;
                    }
                    if (break3_min==(null)) {
                        break_mins[2] = "0";
                    } else {
                        break_mins[2] = break3_min;
                    }


                    break_total = Integer.parseInt(break_hours[0])*60 + Integer.parseInt(break_hours[1])*60 + Integer.parseInt(break_hours[2])*60;
                    break_total += Integer.parseInt(break_mins[0]) + Integer.parseInt(break_mins[1]) + Integer.parseInt(break_mins[2]);


                    month_break_total += break_total;
                }while(curso.moveToNext());
            }
        }

        curso.close();
        Log.e("month_break_total", String.valueOf(month_break_total));
        return month_break_total;

    }

    int getDesignation(int id) {
        int desig=0;
        String query = "SELECT desig FROM Login WHERE id = "+id;

        Cursor cursor = mDataBase.rawQuery(query,null);
        if ((cursor.getCount() > 0)) {
            if(cursor.moveToFirst()) {
                do {
                    desig = Integer.parseInt(cursor.getString(0));

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return desig;
    }


    void getAllBreaks(int id,String date) {
        String query = "SELECT break1_start,break1_stop,break2_start,break2_stop,break3_start,break3_stop,date FROM Breaks WHERE break_id="+id+" AND date = '"+date+"'";
        Cursor cursor = mDataBase.rawQuery(query,null);
        Log.e("check inside","get All breaks");
        if(cursor.getCount() > 0) {

            if(cursor.moveToFirst()) {
                do {
                    Log.e("break1_start",cursor.getString(0));
                    Log.e("break1-stop",cursor.getString(1));
                    Log.e("break2_start",cursor.getString(2));
                    Log.e("break2_stop",cursor.getString(3));
                    Log.e("break3_start",cursor.getString(4));
                    Log.e("break3_stop",cursor.getString(5));
                    Log.e("get break date",cursor.getString(6));
                }while(cursor.moveToNext());
            }
        }
        cursor.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    String getUserDrawer (int id) {
        String uname="";
        String query = "SELECT uname FROM Login WHERE id="+id;
        Cursor cursor = mDataBase.rawQuery(query,null);
        if(cursor.getCount()>0) {
            if(cursor.moveToFirst()) {
                uname = cursor.getString(0);
            }
        }

        return uname;

    }

    public int monthReport(int id, String monthSelected) {


        int working_month_hour=0,working_month_min=0;
        int time=0,time_final = 0;
        String query  = "SELECT strftime('%H',exit) - strftime('%H',entry) AS hour,strftime('%M',exit) - strftime('%M',entry) AS min FROM Details WHERE id = "+id+" AND strftime('%m',date)='"+monthSelected+"'";
        Cursor cursor = mDataBase.rawQuery(query,null);
        if ((cursor.getCount()>0)) {
            if(cursor.moveToFirst()) {
                do {
                    String hour = cursor.getString(0);
                    String min = cursor.getString(1);


                    if (hour == null) {
                        working_month_hour =0;
                    } else {
                        working_month_hour =Integer.parseInt(hour);
                        Log.e("working_month_hour", String.valueOf(working_month_hour));
                    }

                    if (min == null) {
                        working_month_min = 0;
                    } else {
                        working_month_min =Integer.parseInt(min);
                        Log.e("working_month_min", String.valueOf(working_month_min));
                    }



                    time = working_month_hour*60;
                    time = time + working_month_min;

                    time_final += time;
                    Log.e("time_final", String.valueOf(time));

                } while (cursor.moveToNext());
                Log.e("time_final", String.valueOf(time_final));
            }
        }



        Log.e("Time returned after",String.valueOf(time));

        return time_final;
    }



    public void insertBreakDetails(int id, String[] break_start, String[] break_stop, String date) throws android.database.sqlite.SQLiteException{

        String query = "INSERT INTO Breaks(break_id,break1_start,break1_stop,break2_start,break2_stop,break3_start,break3_stop,date) VALUES("+id+",'"+break_start[0]+"','"+break_stop[0]+"','"+break_start[1]+"','"+break_stop[1]+"','"+break_start[2]+"','"+break_stop[2]+"','"+date+"')";
        mDataBase.execSQL(query);

    }

    public void updateManualEntry(int id, String entry, String exit, String break1_start, String break2_start, String break3_start, String break1_stop, String break2_stop, String break3_stop, String date) {
        String query1 = "UPDATE Details SET entry='"+entry+"',exit='"+exit+"',change=1 WHERE id="+id+" AND date = '"+date+"'" ;
        mDataBase.execSQL(query1);

        String query2 = "UPDATE Breaks SET break1_start='"+break1_start+"',break1_stop='"+break1_stop+"',break2_start='"+break2_start+"',break2_stop='"+break2_stop+"',break3_start='"+break3_start+"',break3_stop='"+break3_stop+"' WHERE break_id="+id+" AND date='"+date+"'";
        mDataBase.execSQL(query2);


    }

    public String[] getManualEntryDetails(int id, String dates) {

       String[] details = new String[8];
        String query = "SELECT entry,exit FROM Details WHERE id="+id+" AND date='"+dates+"'";
        Cursor cursor = mDataBase.rawQuery(query,null);

        if ((cursor.getCount()>0)) {
            if(cursor.moveToFirst()) {
                do {
                    details[0]=cursor.getString(0);
                    details[7]=cursor.getString(1);
                } while (cursor.moveToNext());
            }
        }

        String query1 = "SELECT break1_start,break1_stop,break2_start,break2_stop,break3_start,break3_stop FROM Breaks WHERE break_id="+id+" AND date='"+dates+"'";
        Cursor cursor1 = mDataBase.rawQuery(query1,null);
        if ((cursor1.getCount()>0)) {
            if(cursor1.moveToFirst()) {
                do {
                   details[1] = cursor1.getString(0);
                    details[2] = cursor1.getString(1);
                    details[3] = cursor1.getString(2);
                    details[4] = cursor1.getString(3);
                    details[5] = cursor1.getString(4);
                    details[6] = cursor1.getString(5);
                } while (cursor1.moveToNext());
            }
        }

        cursor.close();
        cursor1.close();
        return details;
    }

    public int checkDate(int id, String date) {
        String qury = "SELECT date FROM Details WHERE id = "+id;
        Cursor cursor = mDataBase.rawQuery(qury,null);
        int flag=0;
        if(cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                do {
                    if(date.equals(cursor.getString(0))) {
                        flag = 1;
                    }else {
                        flag =0;
                    }
                }while(cursor.moveToNext());
            }
        }

        return flag;
    }

    public int getIdFromUname(String uname_sel) {
        int id=0;
        String query = "SELECT id FROM Login WHERE uname = '"+uname_sel+"'";
        Cursor cursor = mDataBase.rawQuery(query,null);
        if(cursor.getCount() > 0) {
            if(cursor.moveToFirst()) {
                do {
                   id = cursor.getInt(0);

                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        return id;

    }

    public List<String> getUserNameFromDesig(int desig) {


        String query = "select uname From Login WHERE desig <= "+desig;
        Cursor cursor = mDataBase.rawQuery(query, null);
        List<String> userName = new ArrayList<>();
        int i1=0;
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    userName.add(i1, cursor.getString(0));

                    i1++;

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return userName;

    }
}
