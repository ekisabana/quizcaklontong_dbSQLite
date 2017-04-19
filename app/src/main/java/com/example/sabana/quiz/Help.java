package com.example.sabana.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Help extends AppCompatActivity {
    String[] daftar;
    Button mYes, mNo;
    protected Cursor cursor;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        dbHelper = new DataHelper(this);
        mYes = (Button)findViewById(R.id.yes);
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Help.this;
                Class destinationActivity = Dashboard.class;
                Intent intent = new Intent(context, destinationActivity);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM quiz WHERE soal = '" +
                        getIntent().getStringExtra("soal") + "'",null);
                cursor.moveToFirst();
                if (cursor.getCount()>0)
                {
                    cursor.moveToPosition(0);
                    intent.putExtra("soal",cursor.getString(1).toString());
                    intent.putExtra("helps",cursor.getString(4).toString());
                }
                startActivity(intent);
                finish();
            }
        });
        mNo = (Button)findViewById(R.id.no);
        mNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context = Help.this;
                Class destinationActivity = Dashboard.class;
                Intent intent = new Intent(context, destinationActivity);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM quiz WHERE soal = '" +
                        getIntent().getStringExtra("soal") + "'",null);
                cursor.moveToFirst();
                intent.putExtra("soal",cursor.getString(1).toString());
                intent.putExtra("clue",cursor.getString(3).toString());
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit:
                onAction("exit");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //notifikasi manu aplikasi
    public void onAction(final String pilih) {
        android.support.v7.app.AlertDialog.Builder notifikasi = new android.support.v7.app.AlertDialog.Builder(this);
        notifikasi.setMessage("Apakah Anda yakin")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (pilih.equals("exit")) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        android.support.v7.app.AlertDialog al = notifikasi.create();
        if (pilih.equals("exit")) {
            al.setTitle("Konfirmasi Exit");
        }
        al.show();
    }
}
