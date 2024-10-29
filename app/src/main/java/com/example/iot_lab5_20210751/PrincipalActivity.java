package com.example.iot_lab5_20210751;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;

public class PrincipalActivity extends AppCompatActivity {
    Button irComida;
    Button irActividad;
    Button aplicarNoti;
    double TMB;
    TextView TMBTetx;
    Spinner SpinnerMinutos;
    String channelId = "noti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_principal);

        crearCanalNotificacion();
        SpinnerMinutos = findViewById(R.id.minutosNotiPersonalizada);
        aplicarNoti = findViewById(R.id.aplicarNoti);
        irComida = findViewById(R.id.irComidas);
        irActividad = findViewById(R.id.irActividades);
        TMBTetx = findViewById(R.id.TMBText);
        TMB = getIntent().getDoubleExtra("TMB",0);
        TMBTetx.setText(TMB+" calorías");
        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        aplicarNoti.setOnClickListener(view -> {
            String minutosText = SpinnerMinutos.getSelectedItem().toString();
            long intervalo = convertirAMili(minutosText);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        lanzarNotificacion();
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
            Intent intent = new Intent(PrincipalActivity.this,RegistrarComidasActivity.class);
            intent.putExtra("TMB",TMB);
            startActivity(intent);
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

    public void lanzarNotificacion() {
        Intent intent = new Intent(this, PrincipalActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.notifications_24px)
                .setContentTitle("Recordatorio")
                .setContentText("Vamos, debes alcanzar tu meta de calorías, no te rindas")
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
}