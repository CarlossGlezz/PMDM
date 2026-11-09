package com.example.listacarlos;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ArrayList<Videojuegos> listaJuegos = new ArrayList<>();
        listaJuegos.add(new Videojuegos("Red Dead Redemption 2", "PlayStation / Xbox / PC", R.drawable.rdr2));
        listaJuegos.add(new Videojuegos("God of War", "PlayStation / PC", R.drawable.gow));
        listaJuegos.add(new Videojuegos("The Last of Us", "PlayStation / PC", R.drawable.tlou));
        listaJuegos.add(new Videojuegos("Marvel's Spider-Man 2", "PlayStation 5", R.drawable.spiderman));
        listaJuegos.add(new Videojuegos("Resident Evil 2 Remake", "Multiplataforma", R.drawable.re2));
        listaJuegos.add(new Videojuegos("Elden Ring", "Multiplataforma", R.drawable.er));
        listaJuegos.add(new Videojuegos("Ghost of Tsushima", "PlayStation / PC", R.drawable.got));
        listaJuegos.add(new Videojuegos("The Witcher 3", "Multiplataforma", R.drawable.tw));

        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));

        rvLista.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvLista.setAdapter(new AdaptadorVideojuegos(listaJuegos));
    }
}