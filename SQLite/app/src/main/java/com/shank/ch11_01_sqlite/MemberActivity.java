package com.shank.ch11_01_sqlite;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MemberActivity extends Activity{
    private ListView listView;
    static final String DB_NAME = "MyPhoneDB";  //資料庫名稱
    static final String TB_NAME = "MyPhoneTB";  //資料表名稱
    private SQLiteDatabase db;
    private Cursor cur;  //存放查詢結果的Cursor物件
    private int recid;
    private String[] list = new String[3];
    private ArrayAdapter<String> listAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent it = getIntent();
        recid = it.getIntExtra("id", 0);

        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
        cur = db.rawQuery("SELECT * FROM " + TB_NAME + " WHERE _id = " + recid + ";", null);

        if(cur != null)
        {
            cur.moveToPosition(0);
            list[0]="名子："+cur.getString(cur.getColumnIndex("name"));
            list[1]="電話："+cur.getString(cur.getColumnIndex("phone"));
            list[2]="信箱："+cur.getString(cur.getColumnIndex("email"));
            setTitle(list[0]);
        }

        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                switch (position)
                {
                    case 1:
                        Intent it = new Intent(Intent.ACTION_DIAL);
                        it.setData(Uri.parse("tel:" + list[1]));
                        startActivity(it);
                        break;
                    case 2:
                        Intent it2 = new Intent(Intent.ACTION_VIEW);
                        it2.setData(Uri.parse("mailto:"+list[2]));
                        it2.putExtra(Intent.EXTRA_SUBJECT,  "資料送出");
                        it2.putExtra(Intent.EXTRA_TEXT, list[0]+ "您好!");
                        startActivity(it2);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                about();
                return true;
            case R.id.action_edit:
                Intent it1 = new Intent(MemberActivity.this, EditActivity.class);
                it1.putExtra("id", cur.getInt(cur.getColumnIndex("_id")));
                startActivityForResult(it1, 100);
                this.finish();
                return true;
            case R.id.action_delete:
                    dialog();
                return true;
            case android.R.id.home://返回
                Intent it = new Intent(MemberActivity.this,MainActivity.class);
                startActivity(it);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void dialog() {
        Dialog dialog= new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_action_delete)
                .setTitle("刪除")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(TB_NAME, "_id = " + recid, null);
                        Intent it2 = new Intent(MemberActivity.this, MainActivity.class);
                        startActivity(it2);
                        finish();
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
