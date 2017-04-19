package com.example.sabana.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button mSubmit, mHelp, ton1, ton2;
    TextView mSoal, mClue;
    EditText mJawab;
    String mAnswer;

    private void checkAnswer(String answer) {
        dbHelper = new DataHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM quiz WHERE jawaban = '" +
                answer + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            cursor.moveToPosition(0);
            mClue.setText(cursor.getString(2).toString());
            Toast.makeText(Dashboard.this, "BENAR", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(Dashboard.this, "SALAH", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        dbHelper = new DataHelper(this);
        mSoal = (TextView) findViewById(R.id.soal);
        mClue = (TextView) findViewById(R.id.clue);
        mJawab = (EditText) findViewById(R.id.jawab);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM quiz WHERE no = '" +
                getIntent().getStringExtra("no") + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            mSoal.setText(cursor.getString(1).toString());
            mClue.setText(cursor.getString(3).toString());
        }
        mHelp = (Button)findViewById(R.id.help);
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = Dashboard.this;
                Class destinationActivity = Help.class;
                String soal = mSoal.getText().toString();
                Intent intent = new Intent(context, destinationActivity);
                intent.putExtra("soal",soal);
                startActivity(intent);
                finish();
            }
        });

        Intent bantuan = getIntent();
        if (bantuan.hasExtra("helps")) {
            String textSoal = getIntent().getExtras().getString("soal");
            String textBantuan = getIntent().getExtras().getString("helps");
            mSoal.setText(textSoal);
            mClue.setText(textBantuan);
        }

        mSubmit = (Button)findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswer = mJawab.getText().toString();
                checkAnswer(mAnswer);
            }
        });

        ton2 = (Button) findViewById(R.id.button2);
        ton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                onAction("share");
                return true;
            case R.id.quit:
                onAction("exit");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //notifikasi manu aplikasi
    public void onAction(final String pilih) {
        AlertDialog.Builder notifikasi = new AlertDialog.Builder(this);
        notifikasi.setMessage("Apakah Anda yakin")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (pilih.equals("share")) {
                            mJawab = (EditText) findViewById(R.id.jawab);
                            mAnswer = mJawab.getText().toString();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, mAnswer);
                            startActivity(Intent.createChooser(share, "Pilih Aplikasi"));
                        } else if (pilih.equals("exit")) {
                            finish();
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog al = notifikasi.create();
        if (pilih.equals("exit")) {
            al.setTitle("Konfirmasi Exit");
        } else if (pilih.equals("share")) {
            al.setTitle("Konfirmasi Share");
        }
        al.show();
    }
}
