package com.iantoxi.jetlagtrainer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.ListView;


public class HistoryActivity extends Activity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history);
        setTransitions();

        initializeDatabase();
        insertTestEntry();
        setHistoryListAdapter();
    }

    private void initializeDatabase() {
        db = openOrCreateDatabase("HistoryDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS shifts('_id' INTEGER PRIMARY KEY AUTOINCREMENT, origin VARCHAR(255), destination VARCHAR(255));");
    }

    private void insertTestEntry() {
        ContentValues values = new ContentValues();

        values.put("origin", "SFO");
        values.put("destination", "JFK");

        db.insert("shifts", null, values);
    }

    private void setHistoryListAdapter() {
        Cursor c=db.rawQuery("SELECT * FROM shifts", null);
        HistoryCursorAdapter adapter = new HistoryCursorAdapter(this, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        ListView historyList = (ListView) findViewById(R.id.history_list);
        historyList.setAdapter(adapter);
    }

    private void setTransitions() {
        Slide slide = new Slide();
        slide.excludeTarget(android.R.id.statusBarBackground, true);
        slide.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(slide);
        getWindow().setExitTransition(slide);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
