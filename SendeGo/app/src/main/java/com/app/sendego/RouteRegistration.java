package com.app.sendego;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RouteRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_route_registration);

        Spinner spTipoRuta = findViewById(R.id.spRouteType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.route_type,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipoRuta.setAdapter(adapter);

        Button btnGuardar = findViewById(R.id.btnSave);
        EditText etNombreRuta = findViewById(R.id.etRouteName);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombreRuta.getText().toString().trim();

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre de la ruta es obligatorio", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ruta guardada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }
}