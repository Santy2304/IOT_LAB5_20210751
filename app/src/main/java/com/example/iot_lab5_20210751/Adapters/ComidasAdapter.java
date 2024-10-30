package com.example.iot_lab5_20210751.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab5_20210751.Beans.Comida;
import com.example.iot_lab5_20210751.R;

import java.util.List;

public class ComidasAdapter extends RecyclerView.Adapter<ComidasAdapter.ComidasViewHolder>{

    private List<Comida> listaComidas;
    private Context context;

    @NonNull
    @Override
    public ComidasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.irv_comidas_recomendadas, parent, false);
        return new ComidasAdapter.ComidasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidasViewHolder holder, int position) {
        Comida c = listaComidas.get(position);
        holder.comida = c;
        TextView textViewNombre = holder.itemView.findViewById(R.id.nombre);
        textViewNombre.setText(c.getNombre());

        TextView textViewCal = holder.itemView.findViewById(R.id.calorias);
        textViewCal.setText(c.getCalorias()+" calorías");
    }

    @Override
    public int getItemCount() {
        return listaComidas.size();
    }

    public class ComidasViewHolder extends RecyclerView.ViewHolder{
        Comida comida;
        public ComidasViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public List<Comida> getListaComidas() {
        return listaComidas;
    }

    public void setListaComidas(List<Comida> listaComidasRecomendadas) {
        this.listaComidas = listaComidasRecomendadas;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
