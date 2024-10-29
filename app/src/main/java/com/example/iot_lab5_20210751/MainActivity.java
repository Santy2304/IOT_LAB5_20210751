package com.example.iot_lab5_20210751;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText pesoTexto;
    TextInputEditText alturaTexto;
    TextInputEditText edadTexto;
    Spinner SpinnerGenero;
    Spinner SpinnerNivelActividad;
    Button comenzarButton;
    CheckBox bajarPeso, mantenerPeso, subirPeso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        comenzarButton = findViewById(R.id.comenzarButton);
        pesoTexto = findViewById(R.id.pesoText);
        alturaTexto = findViewById(R.id.alturaText);
        edadTexto = findViewById(R.id.edadText);
        bajarPeso = findViewById(R.id.bajarPeso);
        mantenerPeso = findViewById(R.id.mantenerPeso);
        subirPeso = findViewById(R.id.subirPeso);
        SpinnerGenero = findViewById(R.id.genero);
        SpinnerNivelActividad = findViewById(R.id.nivelActividad);


        bajarPeso.setOnCheckedChangeListener(singleCheckedChangeListener);
        mantenerPeso.setOnCheckedChangeListener(singleCheckedChangeListener);
        subirPeso.setOnCheckedChangeListener(singleCheckedChangeListener);


        comenzarButton.setOnClickListener(view -> {
            String genero = SpinnerGenero.getSelectedItem().toString();
            double peso = Double.parseDouble(pesoTexto.getText().toString());
            double altura = Double.parseDouble(alturaTexto.getText().toString());
            int edad = Integer.parseInt(edadTexto.getText().toString());
            String nivelActividad = SpinnerNivelActividad.getSelectedItem().toString();
            int factorGenero=0;
            double factorNivel=0;
            int factorObjetivo=0;
            if(genero.equals("Masculino")){
                factorGenero=5;
            } else if (genero.equals("Femenino")) {
                factorGenero=-161;
            }
            
            if (nivelActividad.equals("Ninguno")){
                factorNivel=1.2;
            } else if (nivelActividad.equals("Ligero")) {
                factorNivel=1.375;
            } else if (nivelActividad.equals("Moderado")) {
                factorNivel=1.55;
            } else if (nivelActividad.equals("Fuerte")) {
                factorNivel=1.725;
            } else if (nivelActividad.equals("Muy Fuerte")) {
                factorNivel=1.9;
            }

            if (bajarPeso.isChecked()) {
                factorObjetivo = -300;
            } else if (mantenerPeso.isChecked()) {
                factorObjetivo = 0;
            } else if (subirPeso.isChecked()) {
                factorObjetivo = 500;
            }

            double TMB = factorNivel*((10*peso)+(6.25*altura)-(5*edad)+factorGenero) + factorObjetivo;

            Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
            intent.putExtra("TMB",TMB);
            startActivity(intent);

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Codigo tomado de chatGPT para mantener un solo checkBox marcado
    private final CompoundButton.OnCheckedChangeListener singleCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView.getId() == R.id.bajarPeso) {
                    mantenerPeso.setChecked(false);
                    subirPeso.setChecked(false);
                } else if (buttonView.getId() == R.id.mantenerPeso) {
                    bajarPeso.setChecked(false);
                    subirPeso.setChecked(false);
                } else if (buttonView.getId() == R.id.subirPeso) {
                    bajarPeso.setChecked(false);
                    mantenerPeso.setChecked(false);
                }
            }
        }
    };
}