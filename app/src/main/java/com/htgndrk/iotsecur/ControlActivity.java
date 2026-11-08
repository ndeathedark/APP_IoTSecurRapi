package com.htgndrk.iotsecur;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ControlActivity extends AppCompatActivity {

    private TextView tvEstadoSirena;
    private FirebaseFirestore db;
    private ListenerRegistration estadoListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        db = FirebaseFirestore.getInstance();

        tvEstadoSirena = findViewById(R.id.tvEstadoSirena);
        Button btnEncender = findViewById(R.id.btnEncender);
        Button btnApagar = findViewById(R.id.btnApagar);

        btnEncender.setOnClickListener(v -> cambiarEstadoSirena(true));
        btnApagar.setOnClickListener(v -> cambiarEstadoSirena(false));

        escucharEstadoSirena();
    }

    private void escucharEstadoSirena() {
        DocumentReference docRef = db.collection("sistema_iot").document("estado_actual");
        estadoListener = docRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                tvEstadoSirena.setText("Estado: Error al sincronizar");
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Boolean sirenaEncendida = snapshot.getBoolean("sirena_encendida");
                if (sirenaEncendida != null && sirenaEncendida) {
                    tvEstadoSirena.setText("Estado: SIRENA ENCENDIDA");
                    tvEstadoSirena.setTextColor(0xFFD32F2F);
                } else {
                    tvEstadoSirena.setText("Estado: SIRENA APAGADA");
                    tvEstadoSirena.setTextColor(0xFF388E3C);
                }
            } else {
                tvEstadoSirena.setText("Estado: Sin datos");
            }
        });
    }

    private void cambiarEstadoSirena(boolean encender) {
        Map<String, Object> estado = new HashMap<>();
        estado.put("sirena_encendida", encender);
        estado.put("ultimo_evento", encender ? "Sirena activada desde App Android" : "Sirena apagada desde App Android");
        estado.put("fecha_actualizacion", FieldValue.serverTimestamp());

        db.collection("sistema_iot").document("estado_actual")
            .set(estado, SetOptions.merge())
            .addOnSuccessListener(aVoid -> {
                String msg = encender ? "SIRENA ENCENDIDA" : "SIRENA APAGADA";
                Toast.makeText(ControlActivity.this, msg, Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> Toast.makeText(ControlActivity.this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (estadoListener != null) {
            estadoListener.remove();
        }
    }
}
