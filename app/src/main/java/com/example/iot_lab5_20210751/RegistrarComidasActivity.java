package com.example.iot_lab5_20210751;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab5_20210751.Adapters.ComidasRecomendadasAdapter;
import com.example.iot_lab5_20210751.Beans.Comida;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegistrarComidasActivity extends AppCompatActivity {
    String[] nombresComidasComunes = {
            "Platano",
            "Leche",
            "Carne",
            "Pollo",
            "Pescado"
    };

    double[] caloriasComidasComunes = {
      89, 100, 143, 239, 206
    };
    ArrayList<Comida> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_comidas);

        lista = new ArrayList<>();
        for(int i=0;i<5;i++){
            Comida comida = new Comida();
            comida.setNombre(nombresComidasComunes[i]);
            comida.setCalorias(caloriasComidasComunes[i]);
            lista.add(comida);
        }
        ComidasRecomendadasAdapter adapter = new ComidasRecomendadasAdapter();
        adapter.setContext(this);
        adapter.setListaComidasRecomendadas(lista);

        RecyclerView recyclerView = findViewById(R.id.recyclerRecomendado);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}