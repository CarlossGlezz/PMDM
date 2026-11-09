package com.example.reproductorcarlos;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private Button btnPlay, btnPausa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Enlazar vistas
        btnPlay = findViewById(R.id.btn_play);
        btnPausa = findViewById(R.id.btn_pausa);

        // 2. Crear y preparar el ExoPlayer
        player = new ExoPlayer.Builder(this).build();


        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion1);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        // 3. Configurar botones
        btnPlay.setOnClickListener(v -> {
            // Si ha terminado, vuelve al principio
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });

        btnPausa.setOnClickListener(v -> player.pause());
    }
}