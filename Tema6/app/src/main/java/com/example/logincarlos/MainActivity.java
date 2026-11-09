package com.example.logincarlos;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    // 1.2: Usuario y contraseña válidos
    private static final String CORREO_VALIDO = "carlos@correo.com";
    private static final String CLAVE_VALIDA = "1234";

    // Punto extra: contador de intentos como atributo de la actividad
    private int intentos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Código obligatorio de la plantilla Empty Views Activity
        androidx.activity.EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Enlazamos las vistas con el XML
        EditText etCorreo = findViewById(R.id.etCorreo);
        EditText etClave = findViewById(R.id.etClave);
        MaterialSwitch swRecordar = findViewById(R.id.swRecordar);
        Button btnContinuar = findViewById(R.id.btnContinuar);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        // Ejercicio 2: si hay correo guardado, lo escribimos y activamos el interruptor
        SharedPreferences prefsLectura = getSharedPreferences("sesion", MODE_PRIVATE);
        String guardado = prefsLectura.getString("correo", null);
        if (guardado != null) {
            etCorreo.setText(guardado);
            swRecordar.setChecked(true);
            etClave.requestFocus();
        }

        // 3. Acción al pulsar el botón Continuar
        btnContinuar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String clave = etClave.getText().toString();

            // 1.4 Campos vacíos: avisa y sale sin comprobar nada más
            if (correo.isEmpty() || clave.isEmpty()) {

                if (correo.isEmpty()) {
                    etCorreo.setError(getString(R.string.falta_correo));
                }
                if (clave.isEmpty()) {
                    etClave.setError(getString(R.string.falta_clave));
                }

                // Mensaje general en rojo en la parte inferior
                tvMensaje.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.incorrecto));
                tvMensaje.setText("Algún campo está vacío");

                return; // Salimos del lambda para que no siga comprobando
            }

            // 1.2 Comprobación de credenciales con equals
            if (correo.equals(CORREO_VALIDO) && clave.equals(CLAVE_VALIDA)) {
                intentos = 0; // Si acierta antes, el contador vuelve a 0

                // Ejercicio 2: guardar o borrar el correo
                SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
                if (swRecordar.isChecked()) {
                    prefs.edit().putString("correo", correo).apply();
                } else {
                    prefs.edit().remove("correo").apply();
                }

                // 1.2 Abrimos el menú pasándole el correo y cerramos el login
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(MenuActivity.EXTRA_CORREO, correo);
                startActivity(intent);
                finish();
            } else {
                // Falla el acceso
                intentos++; // Suma uno al contador de intentos fallidos
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.incorrecto));

                // Punto extra: Al tercer fallo se desactiva el botón
                if (intentos >= 3) {
                    btnContinuar.setEnabled(false); // Desactiva el botón
                    tvMensaje.setText(R.string.demasiados_intentos);
                    Toast.makeText(this, "Botón bloqueado por seguridad", Toast.LENGTH_SHORT).show();
                } else {
                    tvMensaje.setText(R.string.acceso_incorrecto);
                }
            }
        });
    }
}