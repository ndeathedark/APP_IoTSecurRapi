package com.htgndrk.iotsecur;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ControlActivity extends AppCompatActivity {

    private Button btnEncender, btnApagar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        db = FirebaseFirestore.getInstance();

        btnEncender = findViewById(R.id.btnEncender);
        btnApagar = findViewById(R.id.btnApagar);

        // Eventos de los botones al ser presionados
        btnEncender.setOnClickListener(v -> cambiarEstadoSirena(true));
        btnApagar.setOnClickListener(v -> cambiarEstadoSirena(false));
    }

    private void cambiarEstadoSirena(boolean encender) {
        Map<String, Object> estado = new HashMap<>();
        estado.put("sirena_encendida", encender);
        estado.put("ultimo_evento", encender ? "Sirena activada desde App Android" : "Sirena apagada desde App Android");
        estado.put("fecha_actualizacion", FieldValue.serverTimestamp());

        db.collection("sistema_iot").document("estado_actual")
            .set(estado, SetOptions.merge()) // Actualiza sin sobreescribir otros campos
            .addOnSuccessListener(aVoid -> {
                String msg = encender ? "SIRENA ENCENDIDA" : "SIRENA APAGADA";
                Toast.makeText(ControlActivity.this, msg, Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(ControlActivity.this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}
