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
    private android.widget.SeekBar seekBar;
    private android.widget.TextView tvTiempoActual, tvTiempoTotal;
    private android.os.Handler handler = new android.os.Handler();
    private Runnable updateProgressAction;
    private android.media.SoundPool soundPool;
    private int idEfecto1, idEfecto2, idEfecto3, idEfecto4;
    private int sonidosCargados = 0;
    private Button btnEfecto1, btnEfecto2, btnEfecto3, btnEfecto4;


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
        seekBar = findViewById(R.id.seekBar);
        tvTiempoActual = findViewById(R.id.tv_tiempoActual);
        tvTiempoTotal = findViewById(R.id.tv_tiempoTotal);

        // 2. Crear y preparar el ExoPlayer
        player = new ExoPlayer.Builder(this).build();


        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion1);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();
        // Leer la duración cuando el reproductor está listo
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    long duracionTotal = player.getDuration();
                    seekBar.setMax((int) duracionTotal);
                    tvTiempoTotal.setText(formatearTiempo(duracionTotal));
                }
            }
        });

// Acción que actualiza la barra cada 500 ms
        updateProgressAction = new Runnable() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    long tiempoActual = player.getCurrentPosition();
                    seekBar.setProgress((int) tiempoActual);
                    tvTiempoActual.setText(formatearTiempo(tiempoActual));
                }
                handler.postDelayed(this, 500);
            }
        };
        handler.post(updateProgressAction);

// Configurar la barra para que el usuario la pueda arrastrar
        seekBar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    tvTiempoActual.setText(formatearTiempo(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(android.widget.SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });


        // 3. Configurar botones
        btnPlay.setOnClickListener(v -> {
            // Si ha terminado, vuelve al principio
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });

        btnPausa.setOnClickListener(v -> player.pause());

        // 4. Efectos de sonido (SoundPool)
        btnEfecto1 = findViewById(R.id.btn_efecto1);
        btnEfecto2 = findViewById(R.id.btn_efecto2);
        btnEfecto3 = findViewById(R.id.btn_efecto3);
        btnEfecto4 = findViewById(R.id.btn_efecto4);

        android.media.AudioAttributes atributos = new android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_GAME)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new android.media.SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            if (status == 0) {
                sonidosCargados++;
                // Cuando los 4 estén cargados en memoria, activamos los botones
                if (sonidosCargados == 4) {
                    btnEfecto1.setEnabled(true);
                    btnEfecto2.setEnabled(true);
                    btnEfecto3.setEnabled(true);
                    btnEfecto4.setEnabled(true);
                }
            }
        });

        // Cambia R.raw.xxx por los nombres reales de los audios que vayas a usar como efectos
        idEfecto1 = soundPool.load(this, R.raw.cancion2, 1);
        idEfecto2 = soundPool.load(this, R.raw.cancion3, 1);
        idEfecto3 = soundPool.load(this, R.raw.cancion4, 1);
        idEfecto4 = soundPool.load(this, R.raw.cancion5, 1);

        btnEfecto1.setOnClickListener(v -> soundPool.play(idEfecto1, 1f, 1f, 1, 0, 1f));
        btnEfecto2.setOnClickListener(v -> soundPool.play(idEfecto2, 1f, 1f, 1, 0, 1f));
        btnEfecto3.setOnClickListener(v -> soundPool.play(idEfecto3, 1f, 1f, 1, 0, 1f));
        btnEfecto4.setOnClickListener(v -> soundPool.play(idEfecto4, 1f, 1f, 1, 0, 1f));
    }
    private String formatearTiempo(long ms) {
        long segundos = ms / 1000;
        return String.format(java.util.Locale.getDefault(), "%02d:%02d",
                segundos / 60, segundos % 60);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // La barra vuelve a actualizarse al volver a la app
        handler.post(updateProgressAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Pausa la música y detiene la actualización de la barra para ahorrar recursos
        player.pause();
        handler.removeCallbacks(updateProgressAction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libera el reproductor de la memoria al cerrar la app del todo
        if (player != null) {
            player.release();
        }
        if (soundPool != null) {
            soundPool.release();
        }
    }
}