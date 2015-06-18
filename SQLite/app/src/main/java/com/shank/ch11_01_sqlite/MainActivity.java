package com.shank.ch11_01_sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends Activity {
    static final String DB_NAME = "MyPhoneDB";  //資料庫名稱
    static final String TB_NAME = "MyPhoneTB";  //資料表名稱

    String [] from = {"name", "phone", "email"};
    int [] to = {R.id.name, R.id.phone, R.id.email};

    SQLiteDatabase db;

    Cursor cur;  //存放查詢結果的Cursor物件
    SimpleCursorAdapter adp;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.lv2);

        //開啟或建立資料庫
        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        // 建立資料表
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + TB_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name VARCHAR(32), "
                + "phone VARCHAR(16), "
                + "email VARCHAR(64))";
        db.execSQL(sqlCreateTable);

        cur = db.rawQuery("SELECT * FROM " + TB_NAME, null);


        //建立Adapter物件
        adp = new SimpleCursorAdapter(MainActivity.this, R.layout.item, cur, from, to, 0);

        lv.setAdapter(adp);
        lv.setOnItemClickListener(lvOnItemClickListener);
        requery();

    }

    private void requery()
    {
        cur = db.rawQuery("SELECT * FROM " + TB_NAME,  null);
        adp.changeCursor(cur);
    }

    AdapterView.OnItemClickListener lvOnItemClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> p, View v, int pos, long id)
        {
            cur.moveToPosition(pos);  //移動Cursor至使用者選取的項目
            Intent it1 = new Intent(MainActivity.this, MemberActivity.class);
            it1.putExtra("id", cur.getInt(cur.getColumnIndex("_id")));
            startActivityForResult(it1, 100);
            //requery();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent it)
    {
        if(resultCode == RESULT_OK)
            requery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //this.menu = menu;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();

            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    cur=db.rawQuery("SELECT * FROM " + TB_NAME + " WHERE name LIKE '" + query + "%';", null);
                    adp.changeCursor(cur);
                    return true;

                }

            });

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Intent it = new Intent(MainActivity.this, AddActivity.class);
                startActivity(it);
                this.finish();
                requery();
                return true;
            case R.id.action_settings:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void about() {
        Dialog dialog= new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_action_about)
                .setTitle("About")
                .setMessage("作者：李元銘")
                .setPositiveButton("github", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("https://github.com/yungming/android.git");
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(i);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
