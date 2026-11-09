package com.example.logincarlos;


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
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.correcto));

                // 1.3 Recordar la contraseña
                if (swRecordar.isChecked()) {
                    tvMensaje.setText(R.string.acceso_correcto_guardado);
                } else {
                    tvMensaje.setText(R.string.acceso_correcto);
                }
            } else {
                // Falla el acceso
                intentos++; // Suma uno al contador de intentos fallidos
                tvMensaje.setTextColor(ContextCompat.getColor(this, R.color.incorrecto));

                // Punto extra: Al tercer fallo se desactiva el botón
                if (intentos >= 3) {
                    btnContinuar.setEnabled(false); // Desactiva el botón
                    tvMensaje.setText(R.string.demasiados_intentos);
                    Toast.makeText(this, "Botón bloqueado por seguridad", Toast.LENGTH_SHORT).show(); // Muestra el Toast[cite: 19]
                } else {
                    tvMensaje.setText(R.string.acceso_incorrecto);
                }
            }
        });
    }
}