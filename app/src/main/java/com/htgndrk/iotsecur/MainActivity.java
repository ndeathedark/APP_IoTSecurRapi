package com.htgndrk.iotsecur;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etCorreo, etPassword;
    private Button btnIngresar, btnRegistrar, btnLimpiar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        relacionarVistas();

        btnIngresar.setOnClickListener(v -> {
            String email = etCorreo.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Inicio exitoso, abrir ControlActivity
                        Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                });
        });

        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLimpiar.setOnClickListener(v -> {
            etCorreo.setText("");
            etPassword.setText("");
        });
    }

    private void relacionarVistas() {
        etCorreo = findViewById(R.id.correo);
        etPassword = findViewById(R.id.password);
        btnIngresar = findViewById(R.id.acceso);
        btnRegistrar = findViewById(R.id.btnRegistro);
        btnLimpiar = findViewById(R.id.mostrar);
    }
}
