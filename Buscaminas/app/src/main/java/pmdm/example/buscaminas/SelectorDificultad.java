package pmdm.example.buscaminas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SelectorDificultad extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_dificultad);

        Button facil = findViewById(R.id.Facil);
        Button medio = findViewById(R.id.Medio);
        Button dificil = findViewById(R.id.Dificil);

        facil.setOnClickListener( e ->{
            dificultad = Board.FACIL;

            Intent intent = new Intent(this, Juego.class);
            startActivity(intent);
        });

        medio.setOnClickListener( e ->{
            dificultad = Board.MEDIO;

            Intent intent = new Intent(this, Juego.class);
            startActivity(intent);
        });

        dificil.setOnClickListener( e ->{
            dificultad = Board.DIFICIL;

            Intent intent = new Intent(this, Juego.class);
            startActivity(intent);
        });
    }
}
