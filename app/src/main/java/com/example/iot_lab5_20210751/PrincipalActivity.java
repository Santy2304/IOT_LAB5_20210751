package com.example.iot_lab5_20210751;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab5_20210751.Adapters.ComidasAdapter;
import com.example.iot_lab5_20210751.Beans.Comida;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class PrincipalActivity extends AppCompatActivity {
    Button irComida;
    Button registrarActividad;
    Button aplicarNoti;
    Button registrarPredeterminado;
    TextInputEditText nombreActividadTexto;
    TextInputEditText calActividadTexto;
    TextInputEditText nombreComidaTexto;
    TextInputEditText calComidaTexto;
    double caloriasConsumidas;
    double TMB;
    TextView TMBTetx;
    TextView textoNada;
    Spinner SpinnerMinutos;
    String channelId = "noti";
    TextView TMBindicadorText;
    Spinner spinerAlimentos;
    List<Comida> ListaComidas = new ArrayList<>();

    Slider slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);
        caloriasConsumidas = 0;
        TMB = getIntent().getDoubleExtra("TMB",0);
        TMB = (int) (TMB*100)/100.0;

        crearCanalNotificacion();
        textoNada = findViewById(R.id.textoNada);
        slider = findViewById(R.id.slider);
        slider.setTrackActiveTintList(ColorStateList.valueOf(Color.BLUE));

        calComidaTexto = findViewById(R.id.calComida);
        nombreComidaTexto = findViewById(R.id.nombreComida);
        registrarPredeterminado = findViewById(R.id.registrarAliPre);
        nombreActividadTexto = findViewById(R.id.nombreActivity);
        calActividadTexto = findViewById(R.id.calActivity);
        TMBindicadorText = findViewById(R.id.indicadorText);
        SpinnerMinutos = findViewById(R.id.minutosNotiPersonalizada);
        aplicarNoti = findViewById(R.id.aplicarNoti);
        irComida = findViewById(R.id.registrarComidas);
        spinerAlimentos = findViewById(R.id.spinnerAlimentos);
        registrarActividad = findViewById(R.id.registrarActividad);
        TMBTetx = findViewById(R.id.TMBText);
        TMBTetx.setText(TMB+" calorías");
        TMBindicadorText.setText("Te faltan "+TMB+" Calorias");
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        aplicarNoti.setOnClickListener(view -> {
            String minutosText = SpinnerMinutos.getSelectedItem().toString();
            long intervalo = convertirAMili(minutosText);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        String title = "Recordatorio";
                        String content = "Vamos, debes alcanzar tu meta de calorías, no te rindas";
                        lanzarNotificacion(title,content);
                        try {
                            Thread.sleep(intervalo);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });


        });

        irComida.setOnClickListener(view -> {
            double caloriasCom = Double.parseDouble(calComidaTexto.getText().toString());
            registrarComida(caloriasCom);
            Comida comida = new Comida();
            comida.setNombre(nombreComidaTexto.getText().toString());
            comida.setCalorias(caloriasCom);
            ListaComidas.add(comida);
        });

        registrarPredeterminado.setOnClickListener(view -> {
            String alimento = spinerAlimentos.getSelectedItem().toString();
            double calorias = 0;
            String nombre = "";
            if (alimento.equals("Platano - 89 cal")){
                nombre="Platano";
                calorias = 89;
            } else if (alimento.equals("Leche - 100 cal")) {
                calorias = 100;
                nombre="Leche";

            } else if (alimento.equals("Carne - 143 cal")) {
                calorias=143;
                nombre="Carne";

            } else if (alimento.equals("Pollo - 239 cal")) {
                calorias=239;
                nombre="Pollo";

            } else if (alimento.equals("Pescado - 206 cal")) {
                calorias=206;
                nombre="Pescado";
            }
            Comida comida = new Comida();
            comida.setNombre(nombre);
            comida.setCalorias(calorias);
            ListaComidas.add(comida);
            registrarComida(calorias);

        });

        registrarActividad.setOnClickListener(view -> {
            double caloriasAct = Double.parseDouble(calActividadTexto.getText().toString());
            Comida comida = new Comida();
            comida.setNombre(nombreActividadTexto.getText().toString());
            comida.setCalorias(caloriasAct);
            ListaComidas.add(comida);
            registrarActividad(caloriasAct);
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void crearCanalNotificacion(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            askPermission();

        }
    }

    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(PrincipalActivity.this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }

    }

    public void lanzarNotificacion(String title, String content) {
        Intent intent = new Intent(this, PrincipalActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notifications_24px)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }

    public long convertirAMili(String minutesText) {
        int minutes = 0;
        if (minutesText.equals("1 minuto")){
            minutes=1*60*1000;
        } else if (minutesText.equals("2 minutos")) {
            minutes=2*60*1000;

        } else if (minutesText.equals("5 minutos")) {
            minutes=5*60*1000;

        } else if (minutesText.equals("10 minutos")) {
            minutes=10*60*1000;

        } else if (minutesText.equals("20 minutos")) {
            minutes=20*60*1000;

        } else if (minutesText.equals("30 minutos")) {
            minutes=30*60*1000;

        } else if (minutesText.equals("60 minutos")) {
            minutes=60*60*1000;
        }
        return minutes;
    }

    public void registrarActividad(double calorias){

        caloriasConsumidas = caloriasConsumidas-calorias;

        if (caloriasConsumidas>TMB){
            double aux = caloriasConsumidas-TMB;
            TMBindicadorText.setText("Necesitas bajar "+aux+" calorias");
            String title = "Alerta";
            String content = "Has superado las calorias necesarias, has ejercicio y empieza a comer menos.";
            slider.setValue(100);
            slider.setTrackActiveTintList(ColorStateList.valueOf(Color.RED));
           lanzarNotificacion(title,content);
        } else if (caloriasConsumidas<TMB) {
            double aux = TMB-caloriasConsumidas;
            TMBindicadorText.setText("Te faltan "+aux+"Calorias");

            int per = (int) ((caloriasConsumidas / TMB) * 100);
            slider.setValue(per);

        }else {
            String title = "Felicidades";
            String content = "Ya has llegado a tu meta de calorías";
            slider.setValue(100);
            slider.setTrackActiveTintList(ColorStateList.valueOf(Color.GREEN));
            lanzarNotificacion(title,content);
        }
        textoNada.setVisibility(View.INVISIBLE);
        ComidasAdapter adapter = new ComidasAdapter();
        adapter.setContext(this);
        adapter.setListaComidas(ListaComidas);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void registrarComida(double calorias){

        caloriasConsumidas = caloriasConsumidas+calorias;

        if (caloriasConsumidas>TMB){
            double aux = caloriasConsumidas-TMB;
            TMBindicadorText.setText("Necesitas bajar "+aux+" calorias");
            String title = "Alerta";
            String content = "Has superado las calorias necesarias, has ejercicio y empieza a comer menos.";
            slider.setValue(100);
            slider.setTrackActiveTintList(ColorStateList.valueOf(Color.RED));
            lanzarNotificacion(title,content);
        } else if (caloriasConsumidas<TMB) {
            double aux = TMB-caloriasConsumidas;
            TMBindicadorText.setText("Te faltan "+aux+"Calorias");
            int per = (int) ((caloriasConsumidas / TMB) * 100);
            slider.setValue(per);
        }else {
            String title = "Felicidades";
            String content = "Ya has llegado a tu meta de calorías.";
            slider.setValue(100);
            slider.setTrackActiveTintList(ColorStateList.valueOf(Color.GREEN));
            lanzarNotificacion(title,content);
        }
        textoNada.setVisibility(View.INVISIBLE);
        ComidasAdapter adapter = new ComidasAdapter();
        adapter.setContext(this);
        adapter.setListaComidas(ListaComidas);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}