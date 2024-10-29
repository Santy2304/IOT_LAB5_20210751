package com.example.iot_lab5_20210751.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab5_20210751.Beans.Comida;
import com.example.iot_lab5_20210751.R;

import java.util.List;

public class ComidasRecomendadasAdapter extends RecyclerView.Adapter<ComidasRecomendadasAdapter.ComidaRecomendadaViewHolder>{

    private List<Comida> listaComidasRecomendadas;
    private Context context;

    @NonNull
    @Override
    public ComidaRecomendadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_comidas_recomendadas, parent, false);
        return new ComidasRecomendadasAdapter.ComidaRecomendadaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaRecomendadaViewHolder holder, int position) {
        Comida c = listaComidasRecomendadas.get(position);
        holder.comida = c;
        TextView textViewNombre = holder.itemView.findViewById(R.id.nombreRecomendado);
        textViewNombre.setText(c.getNombre());

        TextView textViewCal = holder.itemView.findViewById(R.id.calorias);
        textViewCal.setText(c.getCalorias()+" calorías");
    }

    @Override
    public int getItemCount() {
        return listaComidasRecomendadas.size();
    }

    public class ComidaRecomendadaViewHolder extends RecyclerView.ViewHolder{
        Comida comida;
        public ComidaRecomendadaViewHolder(@NonNull View itemView) {
            super(itemView);
            Button registrarComidaRecomendadaButton = itemView.findViewById(R.id.registrarRecomendado);

        }
    }

    public List<Comida> getListaComidasRecomendadas() {
        return listaComidasRecomendadas;
    }

    public void setListaComidasRecomendadas(List<Comida> listaComidasRecomendadas) {
        this.listaComidasRecomendadas = listaComidasRecomendadas;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
