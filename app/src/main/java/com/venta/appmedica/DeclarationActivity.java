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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextPaint;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
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

public class DeclarationActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    static List<Preguntas> listaPreguntas = new ArrayList<>();
    static List<Preguntas> listaProfesional = new ArrayList<>();
    ImageButton btnRegresar;
    Button btnSiguiente;
    CheckBox declaracion;
    String tituloText;
    String descripcionText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration);

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencia.edit();
        editor.putString("TodasPreguntas","no");
        editor.apply();
        btnSiguiente = findViewById(R.id.btnDeclarationSiguiente);
        btnRegresar = findViewById(R.id.btnDeclarationRegresar);
        declaracion = findViewById(R.id.checkBox);
        listaPreguntas.clear();
        listaProfesional.clear();

        if(preferencia.getString("TodasPreguntas","no").equals("no") && preferencia.getString("TrabajadorSalud","no").equals("no")){
            btnSiguiente.setText("Finish");
        }

        btnRegresar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Intensidad3Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        declaracion.setOnClickListener(view -> {
            if(declaracion.isChecked()){
                editor.putString("TodasPreguntas","si");
                editor.apply();
                btnSiguiente.setText("Next");
            }else{
                editor.putString("TodasPreguntas","no");
                editor.apply();
                    btnSiguiente.setText("Finish");

            }
        });

        btnSiguiente.setOnClickListener(view -> {
            if(preferencia.getString("TrabajadorSalud","no").equals("si")){
                if(declaracion.isChecked()){
                    Intent intent = new Intent(this, Recomendacion1Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, IntroduccionActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }else {
                datosTablaPdf();
                crearPDF(getFilePathTabla());
                enviarEmail();
                registerPerson();
            }
        });

    }
    private void registerPerson() {

        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);

        ProgressDialog progressDialog = ProgressDialog.show(this,
                "Recording Data",
                "Please wait",
                true,
                false);

        AsyncTask.execute(() -> {
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("https://script.google.com/macros/s/AKfycbz_0u_z9Ig_9q2j9rzlFgt1hfdRIy6sV5rbV773KGfMYKWesIYUwlIe5oAlV6Bs_q6c/")
                        .build();

                IGoogleSheets iGoogleSheets = retrofit.create(IGoogleSheets.class);
                String Documento;
                String Escolaridad;
                if(preferencia.getInt("TipoDocumento",0)==1){Documento = "Cedula de ciudadania";}else if(preferencia.getInt("TipoDocumento",0)==2){ Documento = "Tarjeta de identidad";}else{ Documento = "Pasaporte";}
                if(preferencia.getInt("Escolaridad",0)==1){ Escolaridad = "Preescolar";}else if(preferencia.getInt("Escolaridad",0)==2){ Escolaridad = "Básica Primaria(5°)";
                }else if(preferencia.getInt("Escolaridad",0)==3){ Escolaridad = "Básica Secundaria(9°)";}else if(preferencia.getInt("Escolaridad",0)==4){ Escolaridad = "Bachiller(11°)";}else if(preferencia.getInt("Escolaridad",0)==5){ Escolaridad = "Técnico";
                }else if(preferencia.getInt("Escolaridad",0)==6){ Escolaridad = "Profesional";}else if(preferencia.getInt("Escolaridad",0)==7){ Escolaridad = "Maestría";}else{ Escolaridad = "Doctorado";}
                String jsonRequest = "{\n" +
                        "    \"spreadsheet_id\": \"" + Common.GOOGLE_SHEET_ID + "\",\n" +
                        "    \"sheet\": \"" + Common.SHEET_NAME + "\",\n" +
                        "    \"rows\": [\n" +
                        "        [\n" +
                        "            \"" + Documento + "\",\n" +
                        "            \"" + preferencia.getString("NumeroDocumento","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Nombre","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Fecha","N/A") + "\",\n" +
                        "            \"" + preferencia.getInt("Edad",0) + "\",\n" +
                        "            \"" + Escolaridad + "\",\n" +
                        "            \"" + preferencia.getString("Ocupacion","N/A") + "\",\n" +
                        "            \"" + preferencia.getInt("Semanas",0) + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaA","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaB","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaC","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaD","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaE","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaF","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaG","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaH","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaI","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaJ","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaK","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaL","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaM","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaN","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("PreguntaO","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Otra_Condicion","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Otra_razon","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Actividad","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Frecuencia","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Intensidad","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Duracion","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Frecuencia2","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Intensidad2","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Duracion2","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Frecuencia3","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Intensidad3","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Duracion3","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("ProfesionalQualified","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional1","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional2","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("ProfesionalAvoiding","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("ProfesionalIncluding","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional5","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional6","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("ProfesionalComentario","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional7","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional8","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional9","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional10","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional11","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional12","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional13","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional14","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional15","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional16","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional17","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional18","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional19","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional20","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional21","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional22","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional23","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional24","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional25","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional26","N/A") + "\",\n" +
                        "            \"" + preferencia.getString("Profesional27","N/A") + "\"\n" +
                        "        ]\n" +
                        "    ]\n" +
                        "}";

                Call<String> call = iGoogleSheets.getStringRequestBody(jsonRequest);

                Response<String> response = call.execute();
                int code = response.code();

                progressDialog.dismiss();
                if (code == 200) {
                    if(declaracion.isChecked()){
                        Intent intent = new Intent(this, Recomendacion1Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public String getFilePath(){
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File pdfDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(pdfDirectory, preferencia.getString("Nombre","N/A")+".pdf");
        return file.getPath();
    }
    private void enviarEmail(){
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,preferencia.getString("Email","N/A"),"Formulario "+preferencia.getString("Nombre","N/A"),"Formulario",getFilePath(),preferencia.getString("Nombre","N/A")+".pdf");
        javaMailAPI.execute();
    }

    private void datosTablaPdf(){
        String Documento;
        String Escolaridad;
        SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
        if(preferencia.getInt("TipoDocumento",0)==1){Documento = "Cedula de ciudadania";}else if(preferencia.getInt("TipoDocumento",0)==2){ Documento = "Tarjeta de identidad";}else{ Documento = "Pasaporte";}
        if(preferencia.getInt("Escolaridad",0)==1){ Escolaridad = "Preescolar";}else if(preferencia.getInt("Escolaridad",0)==2){ Escolaridad = "Básica Primaria(5°)";
        }else if(preferencia.getInt("Escolaridad",0)==3){ Escolaridad = "Básica Secundaria(9°)";}else if(preferencia.getInt("Escolaridad",0)==4){ Escolaridad = "Bachiller(11°)";}else if(preferencia.getInt("Escolaridad",0)==5){ Escolaridad = "Técnico";
        }else if(preferencia.getInt("Escolaridad",0)==6){ Escolaridad = "Profesional";}else if(preferencia.getInt("Escolaridad",0)==7){ Escolaridad = "Maestría";}else{ Escolaridad = "Doctorado";}
        tituloText = "Materna "+preferencia.getString("Nombre","N/A");
        descripcionText = "Tipo de documento: "+Documento+"\nNumero de documento: "+preferencia.getString("NumeroDocumento","N/A")+"\n" +
                "Nombre: "+preferencia.getString("Nombre","N/A")+"              Fecha de nacimiento: " + preferencia.getString("Fecha","N/A") + "\n" +
                "Edad: " + preferencia.getInt("Edad",0) +"                 Escolaridad: " + Escolaridad + "\n" +
                "Ocupación: " + preferencia.getString("Ocupacion","N/A") + "        Semanas de embarazo: " + preferencia.getInt("Semanas",0) + "\n\n";

        listaPreguntas.add(new Preguntas("¿Enfermedades respiratorias o cardiovasculares leves, moderadas o graves (p. ej., bronquitis crónica)?",preferencia.getString("PreguntaA","N/A")));
        listaPreguntas.add(new Preguntas("¿Epilepsia que no es estable?",preferencia.getString("PreguntaB","N/A")));
        listaPreguntas.add(new Preguntas("¿Diabetes tipo 1 que no es estable o su nivel de azúcar en la sangre está fuera de los rangos deseados?",preferencia.getString("PreguntaC","N/A")));
        listaPreguntas.add(new Preguntas("¿Enfermedad de la tiroides que no es estable o su función tiroidea está fuera de los rangos deseados?",preferencia.getString("PreguntaD","N/A")));
        listaPreguntas.add(new Preguntas("¿Un trastorno alimentario o desnutrición?",preferencia.getString("PreguntaE","N/A")));
        listaPreguntas.add(new Preguntas("¿Gemelos (28 semanas de embarazo o más)? ¿O está esperando trillizos o partos múltiples superiores?",preferencia.getString("PreguntaF","N/A")));
        listaPreguntas.add(new Preguntas("¿Número bajo de glóbulos rojos (anemia) con altos niveles de fatiga y/o mareos?",preferencia.getString("PreguntaG","N/A")));
        listaPreguntas.add(new Preguntas("¿Presión arterial alta (preeclampsia, hipertensión gestacional o hipertensión crónica que no es estable)?",preferencia.getString("PreguntaH","N/A")));
        listaPreguntas.add(new Preguntas("¿Un bebé que crece lentamente (restricción del crecimiento intrauterino)?",preferencia.getString("PreguntaI","N/A")));
        listaPreguntas.add(new Preguntas("¿Sangrado inexplicable, rotura de membranas o trabajo de parto antes de las 37 semanas?",preferencia.getString("PreguntaJ","N/A")));
        listaPreguntas.add(new Preguntas("¿Una placenta que cubre parcial o completamente el cuello uterino (placenta previa)?",preferencia.getString("PreguntaK","N/A")));
        listaPreguntas.add(new Preguntas("¿Tejido cervical débil (cuello uterino incompetente)?",preferencia.getString("PreguntaL","N/A")));
        listaPreguntas.add(new Preguntas("¿Un punto o cinta para reforzar el cuello uterino (cerclaje)?",preferencia.getString("PreguntaM","N/A")));
        listaPreguntas.add(new Preguntas("¿Abortos recurrentes (pérdida de su bebé antes de las 20 semanas de gestación dos o más veces)?",preferencia.getString("PreguntaN","N/A")));
        listaPreguntas.add(new Preguntas("¿Parto prematuro (antes de las 37 semanas de gestación)?",preferencia.getString("PreguntaO","N/A")));
        listaPreguntas.add(new Preguntas("¿Tiene alguna otra condición médica que pueda afectar su capacidad para estar físicamente activa durante el embarazo?",preferencia.getString("Otra_Condicion","N/A")));
        listaPreguntas.add(new Preguntas("¿Hay alguna otra razón por la que le preocupa la actividad física durante el embarazo?",preferencia.getString("Otra_razon","N/A")));
        listaPreguntas.add(new Preguntas("Durante una semana típica, ¿en qué tipo de actividades físicas participa (natación, caminata, entrenamiento de resistencia, yoga)?",preferencia.getString("Actividad","N/A")));
        listaPreguntas.add(new Preguntas("¿Qué tan activa era físicamente en los seis meses antes del embarazo?","Respuesta"));
        listaPreguntas.add(new Preguntas("Frecuencia",preferencia.getString("Frecuencia","N/A")));
        listaPreguntas.add(new Preguntas("Intensidad",preferencia.getString("Intensidad","N/A")));
        listaPreguntas.add(new Preguntas("Duración",preferencia.getString("Duracion","N/A")));
        listaPreguntas.add(new Preguntas("¿Cuán físicamente activa ha estado durante este embarazo?","Respuesta"));
        listaPreguntas.add(new Preguntas("Frecuencia",preferencia.getString("Frecuencia2","N/A")));
        listaPreguntas.add(new Preguntas("Intensidad",preferencia.getString("Intensidad2","N/A")));
        listaPreguntas.add(new Preguntas("Duración",preferencia.getString("Duracion2","N/A")));
        listaPreguntas.add(new Preguntas("¿Cuáles son sus objetivos de actividad física para el resto de su embarazo?","Respuesta"));
        listaPreguntas.add(new Preguntas("Frecuencia",preferencia.getString("Frecuencia3","N/A")));
        listaPreguntas.add(new Preguntas("Intensidad",preferencia.getString("Intensidad3","N/A")));
        listaPreguntas.add(new Preguntas("Duración",preferencia.getString("Duracion3","N/A")));
        listaProfesional.add(new Preguntas("membranas rotas",preferencia.getString("Profesional7","N/A")));
        listaProfesional.add(new Preguntas("labor prematura",preferencia.getString("Profesional8","N/A")));
        listaProfesional.add(new Preguntas("sangrado vaginal persistente inexplicable",preferencia.getString("Profesional9","N/A")));
        listaProfesional.add(new Preguntas("placenta previa después de 28 semanas de gestación",preferencia.getString("Profesional10","N/A")));
        listaProfesional.add(new Preguntas("preeclampsia",preferencia.getString("Profesional11","N/A")));
        listaProfesional.add(new Preguntas("cérvix incompetente",preferencia.getString("Profesional12","N/A")));
        listaProfesional.add(new Preguntas("restricción del crecimiento intrauterino",preferencia.getString("Profesional13","N/A")));
        listaProfesional.add(new Preguntas("embarazo múltiple de alto orden (por ejemplo, trillizos)",preferencia.getString("Profesional14","N/A")));
        listaProfesional.add(new Preguntas("diabetes tipo I no controlada",preferencia.getString("Profesional15","N/A")));
        listaProfesional.add(new Preguntas("hipertensión no controlada",preferencia.getString("Profesional16","N/A")));
        listaProfesional.add(new Preguntas("enfermedad tiroidea no controlada",preferencia.getString("Profesional17","N/A")));
        listaProfesional.add(new Preguntas("otro trastorno cardiovascular, respiratorio o sistémico grave",preferencia.getString("Profesional18","N/A")));
        listaProfesional.add(new Preguntas("pérdida recurrente del embarazo",preferencia.getString("Profesional19","N/A")));
        listaProfesional.add(new Preguntas("hipertensión gestacional",preferencia.getString("Profesional20","N/A")));
        listaProfesional.add(new Preguntas("antecedentes de parto prematuro espontáneo",preferencia.getString("Profesional21","N/A")));
        listaProfesional.add(new Preguntas("enfermedad cardiovascular o respiratoria leve/moderada",preferencia.getString("Profesional22","N/A")));
        listaProfesional.add(new Preguntas("anemia sintomática",preferencia.getString("Profesional23","N/A")));
        listaProfesional.add(new Preguntas("desnutrición",preferencia.getString("Profesional24","N/A")));
        listaProfesional.add(new Preguntas("desorden alimenticio",preferencia.getString("Profesional25","N/A")));
        listaProfesional.add(new Preguntas("embarazo gemelar despues de la semana 28",preferencia.getString("Profesional26","N/A")));
        listaProfesional.add(new Preguntas("otras condiciones médicas significativas",preferencia.getString("Profesional27","N/A")));
        listaProfesional.add(new Preguntas("Inquietud específica de su paciente y/o de un profesional calificado en ejercicio",preferencia.getString("ProfesionalQualified","N/A")));
        listaProfesional.add(new Preguntas("Actividad física sin restricciones según las Pautas canadienses para la actividad física durante el embarazo de SOGC/CSEP 2019",preferencia.getString("Profesional1","N/A")));
        listaProfesional.add(new Preguntas("Actividad física progresiva",preferencia.getString("Profesional2","N/A")));
        listaProfesional.add(new Preguntas("Recomendar evitar",preferencia.getString("ProfesionalAvoiding","N/A")));
        listaProfesional.add(new Preguntas("Recomendar que incluya",preferencia.getString("ProfesionalIncluding","N/A")));
        listaProfesional.add(new Preguntas("Recomiende la supervisión de un profesional de ejercicio calificado, si es posible",preferencia.getString("Profesional5","N/A")));
        listaProfesional.add(new Preguntas("Referir a un fisioterapeuta por dolor, deterioro y/o una evaluación del suelo pélvico",preferencia.getString("Profesional6","N/A")));
        listaProfesional.add(new Preguntas("Otro comentario",preferencia.getString("ProfesionalComentario","N/A")));
    }
    public void crearPDF(String path) {
        try {

            File dir = new File(path);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            SharedPreferences preferencia = getSharedPreferences("Datos",MODE_PRIVATE);
            File file = new File(dir, preferencia.getString("Nombre","N/A")+".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Document documento = new Document();
            PdfWriter.getInstance(documento, fileOutputStream);

            documento.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.logopdf);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , stream);
            Image myImg = Image.getInstance(stream.toByteArray());
            myImg.scaleAbsoluteWidth(150f);
            myImg.scaleAbsoluteHeight(190f);
            myImg.setAbsolutePosition(390f, 650f);

            //add image to document
            documento.add(myImg);

            Paragraph titulo = new Paragraph(
                    "GET ACTIVE QUESTIONNAIRE\nFOR PREGNANCY\n",
                    FontFactory.getFont("arial", 22, Font.BOLDITALIC, BaseColor.BLACK)
            );

            documento.add(titulo);

            Paragraph p2 = new Paragraph(
                    descripcionText,
                    FontFactory.getFont("arial", 12, Font.NORMAL, BaseColor.BLACK)
            );

            documento.add(p2);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidths(new float[] { 4, 1 });
            tabla.addCell(new Paragraph("PREGUNTA", FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)));
            tabla.addCell(new Paragraph("RESPUESTA", FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)));

            for (int i = 0 ; i < listaPreguntas.size() ; i++) {
                tabla.addCell(listaPreguntas.get(i).pregunta);
                tabla.addCell(listaPreguntas.get(i).respuesta);
            }

            documento.add(tabla);

            Paragraph p3 = new Paragraph(
                    "Trabajador de la salud \n\n",
                    FontFactory.getFont("arial", 20, Font.BOLD, BaseColor.BLACK)
            );

            documento.add(p3);

            PdfPTable tabla2 = new PdfPTable(2);
            tabla2.setWidths(new float[] { 4, 1 });
            tabla2.addCell(new Paragraph("PREGUNTA", FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)));
            tabla2.addCell(new Paragraph("RESPUESTA", FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)));

            for (int i = 0 ; i < listaProfesional.size() ; i++) {
                tabla2.addCell(listaProfesional.get(i).pregunta);
                tabla2.addCell(listaProfesional.get(i).respuesta);
            }

            documento.add(tabla2);

            documento.close();


        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getFilePathTabla(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File pdfDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        return pdfDirectory.getPath();
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