package com.htgndrk.iotsecur;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etRegistroCorreo);
        etPassword = findViewById(R.id.etRegistroPassword);
        Button btnRegistrar = findViewById(R.id.btnConfirmarRegistro);
        Button btnVolver = findViewById(R.id.btnVolver);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrarUsuario() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Registrado con éxito en Firebase", Toast.LENGTH_SHORT).show();
                    // Redirigir al login o panel principal
                    finish();
                } else {
                    String errorMsg = task.getException() != null ? task.getException().getMessage() : "Error desconocido";
                    Toast.makeText(this, "Error al registrar: " + errorMsg, Toast.LENGTH_LONG).show();
                }
            });
    }
}
