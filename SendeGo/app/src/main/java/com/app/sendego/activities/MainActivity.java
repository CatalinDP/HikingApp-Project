package com.app.sendego.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.app.sendego.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainToobar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_rutas) {
                selectedFragment = new RouteListFragment();
            } else if (item.getItemId() == R.id.nav_favRoute) {
                selectedFragment = new FavoriteRoutesFragment();
            }else if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_about) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_item_help) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://montanasegura.com/senderismo-con-seguridad/"));
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_item_addRotue) {
            Intent intent = new Intent(MainActivity.this, RouteRegistration.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_item_salir) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}