package com.venta.appmedica;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.venta.appmedica.email.JavaMailAPI;
import com.venta.appmedica.models.IGoogleSheets;
import com.venta.appmedica.models.Preguntas;
import com.venta.appmedica.utils.Common;
import com.venta.appmedica.utils.NetworkChangeListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RelativeContraindicationsActivityEs extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton btnRegresar;
    Button btnSiguiente;
    CheckBox checkRelative1, checkRelative2, checkRelative3, checkRelative4, checkRelative5, checkRelative6, checkRelative7,
            checkRelative8, checkRelative9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative_contraindications_es);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        btnSiguiente = findViewById(R.id.btnRelativeSiguiente);
        btnRegresar = findViewById(R.id.btnRelativeRegresar);
        checkRelative1 = findViewById(R.id.checkBoxRelative1);
        checkRelative2 = findViewById(R.id.checkBoxRelative2);
        checkRelative3 = findViewById(R.id.checkBoxRelative3);
        checkRelative4 = findViewById(R.id.checkBoxRelative4);
        checkRelative5 = findViewById(R.id.checkBoxRelative5);
        checkRelative6 = findViewById(R.id.checkBoxRelative6);
        checkRelative7 = findViewById(R.id.checkBoxRelative7);
        checkRelative8 = findViewById(R.id.checkBoxRelative8);
        checkRelative9 = findViewById(R.id.checkBoxRelative9);

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, AbsoluteContraindicationsActivityEs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        btnSiguiente.setOnClickListener(view -> {
            if(checkRelative1.isChecked()){editor.putString("Profesional19","Si");}
            if(checkRelative2.isChecked()){editor.putString("Profesional20","Si");}
            if(checkRelative3.isChecked()){editor.putString("Profesional21","Si");}
            if(checkRelative4.isChecked()){editor.putString("Profesional22","Si");}
            if(checkRelative5.isChecked()){editor.putString("Profesional23","Si");}
            if(checkRelative6.isChecked()){editor.putString("Profesional24","Si");}
            if(checkRelative7.isChecked()){editor.putString("Profesional25","Si");}
            if(checkRelative8.isChecked()){editor.putString("Profesional26","Si");}
            if(checkRelative9.isChecked()){editor.putString("Profesional27","Si");}
            editor.apply();

            Intent intent = new Intent(this, ProviderActivityEs.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}