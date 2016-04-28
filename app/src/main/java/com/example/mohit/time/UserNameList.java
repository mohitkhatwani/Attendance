package com.example.mohit.time;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserNameList extends ActionBarActivity {

   ListView listView;

    DbHelper dbHelper;

    int id,desig;

    List<String> unames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_list);

        dbHelper = new DbHelper(getApplicationContext());
        try {
            dbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final SharedPreferences sharedpreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        id = sharedpreferences.getInt("id", 0);

        desig = dbHelper.getDesignation(id);
        unames = new ArrayList<>();
        unames = dbHelper.getUserNameFromDesig(desig);

        for(int i=0;i<unames.size();i++)
            Log.e("check_unames_inside",unames.get(i));

        listView = (ListView) findViewById(R.id.listView);

        MyAdapter aa = new MyAdapter(unames);
        listView.setAdapter(aa);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("inside","onclick");
                int  idl = dbHelper.getIdFromUname(unames.get(position));
                Intent intent = new Intent(getApplicationContext(),DayReport.class);
                intent.putExtra("id_for_report",idl);
                finish();
                startActivity(intent);
            }
        });


    }


    private class MyAdapter extends BaseAdapter {
        List<String> uname;

        public MyAdapter(List<String> name) {
            this.uname = name;

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
            LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_uname, parent, false);
            TextView text = (TextView) rowView.findViewById(R.id.user_name);
            text.setText(uname.get(position));
            return rowView;
        }




    }

}
