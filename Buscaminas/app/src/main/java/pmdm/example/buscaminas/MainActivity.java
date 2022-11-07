package pmdm.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static int dificultad = Board.FACIL; //dificultad facil de predeterminado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        Button nuevaPartida = findViewById(R.id.nuevaPartida);
        Button selectorDificultad = findViewById(R.id.selectorDificultad);

        nuevaPartida.setOnClickListener(e -> {
            Intent intent = new Intent(this, Juego.class);
            startActivity(intent);

        });

        selectorDificultad.setOnClickListener(e -> {
            Intent intent = new Intent(this, SelectorDificultad.class);
            startActivity(intent);
        });
    }
}