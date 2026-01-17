package com.app.sendego;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Difficulty;
import models.Route;
import models.RouteType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Route.routeList.add(new Route(1, "Ruta Media", Difficulty.MEDIA, RouteType.LINEAL , 10, 3, "Descripción de la ruta 1", "Notas de la ruta 1", true));
        Route.routeList.add(new Route(2, "Ruta Dificil", Difficulty.DIFICIL, RouteType.CIRCULAR , 15, 1, "Descripción de la ruta 2", "Notas de la ruta 2", false));
        Route.routeList.add(new Route(3, "Ruta Facil", Difficulty.FACIL, RouteType.LINEAL ,5, 5, "Descripción de la ruta 3", "Notas de la ruta 3", true));


        Button btnRouteList = findViewById(R.id.btnSeeRoutes);
        btnRouteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoutesListActivity.class);
                startActivity(intent);
            }
        });

        Button btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://montanasegura.com/senderismo-con-seguridad/"));
                startActivity(intent);
            }
        });

        Button btnNewRoute = findViewById(R.id.btnNewRoute);
        btnNewRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RouteRegistration.class);
                startActivity(intent);
            }
        });

        Button btnAbout = findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}