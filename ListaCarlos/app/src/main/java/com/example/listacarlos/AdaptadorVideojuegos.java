package com.example.listacarlos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdaptadorVideojuegos extends RecyclerView.Adapter<AdaptadorVideojuegos.ViewHolder> {

    // Usamos tu clase en plural (Videojuegos) tal como la creaste
    private final ArrayList<Videojuegos> videojuegos;

    public AdaptadorVideojuegos(ArrayList<Videojuegos> videojuegos) {
        this.videojuegos = videojuegos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImagen;
        TextView tvTitulo;
        TextView tvSubtitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvSubtitulo = itemView.findViewById(R.id.tvSubtitulo);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Aquí inflamos el diseño que acabas de crear. Asegúrate de que se llama fila_videojuego.
        View fila = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fila_videojuego, parent, false);
        return new ViewHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Videojuegos juego = videojuegos.get(position);
        holder.tvTitulo.setText(juego.getTitulo());
        holder.tvSubtitulo.setText(juego.getPlataforma());
        holder.ivImagen.setImageResource(juego.getImagen());

        // Paso 7 (Adelantado): Mostrar un Toast al tocar la fila[cite: 13]
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), juego.getTitulo(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return videojuegos.size();
    }
}
