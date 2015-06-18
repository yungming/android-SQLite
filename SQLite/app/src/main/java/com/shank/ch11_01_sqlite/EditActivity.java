package com.shank.ch11_01_sqlite;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends Activity {

	static final String DB_NAME = "MyPhoneDB";  //資料庫名稱
	static final String TB_NAME = "MyPhoneTB";  //資料表名稱
	SQLiteDatabase db;
	Cursor cur;  //存放查詢結果的Cursor物件
	
	Button btnUpdate, btnCan;
	EditText edtUDName, edtUDTel, edtUDEmail;
    Intent it1 ;

	int recid; //記住目前正在更正的資料是第id的紀錄
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent it = getIntent();
		recid = it.getIntExtra("id", 0);
        it1 = new Intent(EditActivity.this, MemberActivity.class);
        it1.putExtra("id", recid);

		
		db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
		cur = db.rawQuery("SELECT * FROM " + TB_NAME + " WHERE _id = " + recid + ";", null);
		
		btnUpdate = (Button)findViewById(R.id.btnUpdate);
		btnCan = (Button)findViewById(R.id.btnCan);
		
		edtUDName = (EditText)findViewById(R.id.edtUDName);
		edtUDTel = (EditText)findViewById(R.id.edtUDTel);
		edtUDEmail = (EditText)findViewById(R.id.edtUDEmail);
		
		if(cur == null)
			edtUDName.setText("No data");
		else
		{
			cur.moveToPosition(0);
		    edtUDName.setText(cur.getString(cur.getColumnIndex("name")));		
		    edtUDTel.setText(cur.getString(cur.getColumnIndex("phone")));
			edtUDEmail.setText(cur.getString(cur.getColumnIndex("email")));
		}
		
		btnUpdate.setOnClickListener(btnUpdateOnClickListener);
		btnCan.setOnClickListener(btnCanOnClickListener);

		
	}
	
	OnClickListener btnUpdateOnClickListener = new OnClickListener()
	{
		public void onClick(View v)
		{
			String tepName = edtUDName.getText().toString().trim();
			String tepTel = edtUDTel.getText().toString().trim();
			String tepEmail = edtUDEmail.getText().toString().trim();
			
			ContentValues cv = new ContentValues(3);
			cv.put("name",  tepName);
			cv.put("phone", tepTel);
			cv.put("email", tepEmail);
			db.update(TB_NAME, cv, "_id=" + recid, null);

            startActivityForResult(it1, 100);
            finish();
		}
	};
	
	OnClickListener btnCanOnClickListener = new OnClickListener()
	{
		public void onClick(View v)
		{
            startActivityForResult(it1, 100);
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
                Intent it = new Intent(EditActivity.this,MainActivity.class);
                startActivity(it);
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
