package com.omerfpekgoz.contentprovider_rehberuygulama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dao.MyProvider;

//Aşağıda yazan kısmı manifesto dosyasına ekliyoruz
//  <provider
//            android:authorities="com.omerfpekgoz.udemy_41_contentprovider_rehberuygulama.dao.MyProvider"
//            android:name="dao.MyProvider"
//            android:exported="true"
//            android:multiprocess="true"/>
public class MainActivity extends AppCompatActivity {

    private Button btnKaydet;
    private Button btnSil;
    private Button btnGuncelle;
    private Button btnGetir;
    private EditText txtId;
    private EditText txtAd;
    private EditText txtTel;
    private TextView txtCikti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetir = findViewById(R.id.btnGetir);
        btnGuncelle = findViewById(R.id.btnGuncelle);
        btnKaydet = findViewById(R.id.btnKaydet);
        btnSil = findViewById(R.id.btnSil);
        txtAd = findViewById(R.id.txtAd);
        txtId = findViewById(R.id.txtId);
        txtTel = findViewById(R.id.txtTel);
        txtCikti = findViewById(R.id.txtCikti);

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues contentValues = new ContentValues();
                contentValues.put("ad", txtAd.getText().toString());
                contentValues.put("tel", txtTel.getText().toString());

                Uri uri = getContentResolver().insert(MyProvider.CONTENT_URL, contentValues);   //Veri kaydetme
                Toast.makeText(getApplicationContext(), "Rehber: " + uri.toString() + " kayıt edildi", Toast.LENGTH_LONG).show();

            }
        });

        btnGetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor c = getContentResolver().query(MyProvider.CONTENT_URL, null, null, null, null);

                String result = "Rehber Sonuç: ";
                while (c.moveToNext()) {

                    result = result + "\n" + c.getInt(c.getColumnIndex("id"))
                            + "--" + c.getString(c.getColumnIndex("ad"))
                            + "--" + c.getString(c.getColumnIndex("tel"));
                }

                txtCikti.setText(result);
            }
        });
        btnSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri deleteUri = Uri.parse("content://com.omerfpekgoz.contentprovider_rehberuygulama.dao.MyProvider/rehber/" + txtId.getText().toString());  //ID yi aldık
                int count = getContentResolver().delete(deleteUri, null, null);
            }
        });
        btnGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("ad", txtAd.getText().toString());
                contentValues.put("tel", txtTel.getText().toString());

                Uri updateUri = Uri.parse("content://com.omerfpekgoz.contentprovider_rehberuygulama.dao.MyProvider/rehber/" + txtId.getText().toString());  //ID yi aldık
                int count = getContentResolver().update(updateUri, contentValues, null, null);

            }
        });


    }
}
