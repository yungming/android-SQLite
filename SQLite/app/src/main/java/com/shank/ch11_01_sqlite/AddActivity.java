package com.shank.ch11_01_sqlite;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends Activity {

    static final String DB_NAME = "MyPhoneDB";  //資料庫名稱
    static final String TB_NAME = "MyPhoneTB";  //資料表名稱

    String[] from = {"name", "phone", "email"};
    int[] to = {R.id.name, R.id.phone, R.id.email};

    SQLiteDatabase db;

    Cursor cur;  //存放查詢結果的Cursor物件

    Button btnInsert;
    EditText edtADName, edtADTel, edtADEmail;
    //TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        btnInsert = (Button) findViewById(R.id.btnInsert);
        edtADName = (EditText) findViewById(R.id.edtAdName);
        edtADTel = (EditText) findViewById(R.id.edtAdTel);
        edtADEmail = (EditText) findViewById(R.id.edtAdEmail);

        btnInsert.setOnClickListener(btnInsertOnClickListener);

    }

    OnClickListener btnInsertOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            String tepName = edtADName.getText().toString().trim();
            String tepTel = edtADTel.getText().toString().trim();
            String tepEmail = edtADEmail.getText().toString().trim();

            ContentValues cv = new ContentValues(3);
            cv.put("name", tepName);
            cv.put("phone", tepTel);
            cv.put("email", tepEmail);
            db.insert(TB_NAME, null, cv);
            db.close();
            Intent it = new Intent(AddActivity.this,MainActivity.class);
            startActivity(it);
            finish();

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                about();
                return true;
            case android.R.id.home:
                Intent it = new Intent(AddActivity.this,MainActivity.class);
                startActivity(it);
                this.finish();
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
