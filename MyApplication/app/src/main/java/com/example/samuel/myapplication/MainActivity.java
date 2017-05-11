package com.example.samuel.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView;

public class MainActivity extends Activity {

    private static final String dbNmae = "Tickets.db",
            dbTable = "Tickets";
    private SQLiteDatabase Tickets;

    private EditText edate,
            enumber,
            eprice,
            elist;

    private Button mBtnAdd,
            mBtnDelete,
            mBtnQuery,
            mBtnUpdate,
            mBtnList;
    private static final String ID = "_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbhelper database =
                new dbhelper(getApplicationContext(), dbTable, null, 1);
        Tickets = database.getWritableDatabase();

        // 檢查資料表是否已經存在，如果不存在，就建立一個。
        Cursor cursor = Tickets.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        dbTable + "'", null);

        if (cursor != null) {
            if (cursor.getCount() == 0)    // 沒有資料表，要建立一個資料表。
                Tickets.execSQL("CREATE TABLE " + dbTable + " (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "date TEXT NOT NULL," +
                        "number TEXT," +
                        "price TEXT);");

            cursor.close();

        }

        edate = (EditText) findViewById(R.id.txtNo);
        enumber = (EditText) findViewById(R.id.txtNo1);
        eprice = (EditText) findViewById(R.id.txtNo2);
        elist = (EditText) findViewById(R.id.txtNo3);


        mBtnAdd = (Button) findViewById(R.id.button1);
        mBtnDelete = (Button) findViewById(R.id.button2);
        mBtnUpdate = (Button) findViewById(R.id.button3);
        mBtnList = (Button) findViewById(R.id.button4);
        mBtnQuery = (Button) findViewById(R.id.button5);

        mBtnAdd.setOnClickListener(btnAddOnClick);
        mBtnDelete.setOnClickListener(btnDeleteOnClick);
        mBtnUpdate.setOnClickListener(btnUpdateOnClick);
        mBtnList.setOnClickListener(btnListOnClick);
        mBtnQuery.setOnClickListener(btnQueryOnClick);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Tickets.close();
    }

    private View.OnClickListener btnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            ContentValues newRow = new ContentValues();
            newRow.put("date", edate.getText().toString());
            newRow.put("number", enumber.getText().toString());
            newRow.put("price", eprice.getText().toString());
            Tickets.insert(dbTable, null, newRow);
        }
    };

    private View.OnClickListener btnDeleteOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Tickets.delete(dbTable, "_id=1", null);
        }
    };

    private View.OnClickListener btnUpdateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues newRow = new ContentValues();
            newRow.put("date", edate.getText().toString());
            newRow.put("number", enumber.getText().toString());
            newRow.put("price", eprice.getText().toString());
            Tickets.update(dbTable, newRow,  "_id = 1",
                    null);
        }
    };

    private View.OnClickListener btnQueryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            Cursor c = null;

            if (!edate.getText().toString().equals("")) {
                c = Tickets.query(true, dbTable, new String[]{"date", "number",
                        "price"}, "date=" + "\"" + edate.getText().toString()
                        + "\"", null, null, null, null, null);
            } else if (!enumber.getText().toString().equals("")) {
                c = Tickets.query(true, dbTable, new String[]{"date", "number",
                        "price"}, "number=" + "\"" + enumber.getText().toString()
                        + "\"", null, null, null, null, null);
            } else if (!eprice.getText().toString().equals("")) {
                c = Tickets.query(true, dbTable, new String[]{"date", "number",
                        "price"}, "price=" + "\"" + eprice.getText().toString()
                        + "\"", null, null, null, null, null);
            }

            if (c == null)
                return;

            if (c.getCount() == 0) {
                elist.setText("");
                Toast.makeText(MainActivity.this, "沒有這筆資料", Toast.LENGTH_LONG)
                        .show();
            } else {
                c.moveToFirst();
                elist.setText(c.getString(0) + c.getString(1) + c.getString(2));

                while (c.moveToNext())
                    elist.append("\n" + c.getString(0) + c.getString(1) +
                            c.getString(2));
            }
        }
    };

    private View.OnClickListener btnListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Cursor c = Tickets.query(true, dbTable, new String[]{"date", "number",
                    "price"}, 	null, null, null, null, null, null);

            if (c == null)
                return;

            if (c.getCount() == 0) {
                elist.setText("");
                Toast.makeText(MainActivity.this, "沒有資料", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                c.moveToFirst();
                elist.setText("\n" +"日期: "+ c.getString(0) +"號碼: "+ c.getString(1)  +
                        "金額:  "+c.getString(2));

                while (c.moveToNext())
                    elist.append("\n" +"日期: "+ c.getString(0) +"號碼: "+ c.getString(1)  +
                            "金額:  "+c.getString(2));
            }
        }
    };

}




