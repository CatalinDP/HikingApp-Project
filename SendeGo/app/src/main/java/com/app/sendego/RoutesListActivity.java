package com.app.sendego;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoutesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_routes_list);

        String[] asd = {
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas",
                "Hola",
                "Adios",
                "Pruebas","Hola",
                "Adios",
                "Pruebas"
        };
        CustomAdapter adapter = new CustomAdapter(asd);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}