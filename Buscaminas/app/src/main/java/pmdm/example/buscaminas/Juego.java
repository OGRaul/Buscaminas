package pmdm.example.buscaminas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Juego extends MainActivity{
    //Atributos
    private Board board;
    private GridLayout gridLayout;
    private TextView contadorMinas;
    private int nMinas;
    private TextView reset;
    private int segundos = 0;
    private TextView tiempo;
    private Timer timer;
    private Cell[][] tablero;
    private boolean esPrimera = true;
    private boolean derrota = false;
    private boolean victoria = false;

    final static float STEP = 200;
    float mRatio = 1.0f;
    int mBaseDist;
    float mBaseRatio;
    float fontsize = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.juego);


        board = new Board();
        dibujarTablero();

        //numero de minas
        contadorMinas = findViewById(R.id.tv_numero_minas);
        nMinas = board.getnMines();
        String nMinasInicial = String.valueOf(nMinas);
        String s = getResources().getString(R.string.minas) + nMinasInicial;
        contadorMinas.setText(s);

        //Boton reset
        reset = findViewById(R.id.button_reset);
        reset.setOnClickListener(e-> resetGame());

        //Contador
        tiempo = findViewById(R.id.timer);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    String s = String.valueOf(segundos);
                    String t = getResources().getString(R.string.tiempo);
                    t += s;
                    tiempo.setText(t);
                    segundos++;
                });
            }
        }, 1000, 1000);
    }

    private void resetGame(){
        Intent intent = new Intent(this, Juego.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void dibujarTablero(){

        gridLayout = findViewById(R.id.tablero);
        RelativeLayout rl = findViewById(R.id.rl);

        rl.setGravity(Gravity.CENTER);

        gridLayout.setRowCount(board.getSize());
        gridLayout.setColumnCount(board.getSize());

        tablero = board.getBoard();

        TextView[] textoCeldas = new TextView[tablero.length];

        int casillasReveladas = 0;

        for (int i = 0; i < board.getSize(); i++){
            for (int j = 0; j < board.getSize(); j++){

                //cuenta las casillas reveladas y si hay tantas como casillas sin minas ejecuta la victoria
                if(tablero[i][j].isRevealed()){
                    casillasReveladas++;
                    int size = tablero.length * tablero.length;
                    if(casillasReveladas == size - board.getnMines() && !derrota){
                        onVictoria();
                    }
                }

                int dif = tablero[i][j].getTipo();
                String tipo = "⬛";

                if(tablero[i][j].isFlagged() && !tablero[i][j].isRevealed()){
                    tipo = "\uD83D\uDEA9";
                }

                if(tablero[i][j].isRevealed()){

                    if(dif == Cell.TIPO_VACIO){
                        tipo = "-";
                    }else
                    if(dif == Cell.TIPO_MINA && !tablero[i][j].isClicked()){
                        tipo = "\uD83D\uDCA3";
                    }else
                    if(dif == Cell.TIPO_MINA && tablero[i][j].isClicked()){
                        tipo = "\uD83D\uDCA5";
                    }else
                    if(dif == Cell.TIPO_NUMERO){
                        tipo = String.valueOf(tablero[i][j].getValor());
                    }
                }

                textoCeldas[i] = new TextView(this);
                textoCeldas[i].setText(tipo);
                textoCeldas[i].setGravity(Gravity.CENTER);

                textoCeldas[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,ajustarTamanoTexto());

                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ajustarTamanoTexto(), this.getResources().getDisplayMetrics());
                textoCeldas[i].setMaxWidth(width*2);
                textoCeldas[i].setMinWidth(width);

                OnClickCelda(textoCeldas[i],i,j);
                OnLongClickCelda(textoCeldas[i], tablero[i][j]);
                gridLayout.addView(textoCeldas[i]);
            }
        }
    }

    private void onVictoria(){
        reset.setText("\uD83D\uDE01");
        timer.cancel();
        victoria = true;

        //alerta victoria
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("¡Victoria!");
        alertDialogBuilder.setPositiveButton("Jugar de nuevo", (dialogInterface, i) -> resetGame()
        );
        alertDialogBuilder.setNegativeButton("Atrás", (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void onDerrota(){
        derrota = true;
        revelarTodasLasCeldas();
        reset.setText("\uD83D\uDE1E");
        timer.cancel();
    }

    private void borrarTablero(){
        gridLayout.removeAllViews();
    }

    public void OnClickCelda(TextView textView, int column, int row){
        textView.setOnClickListener(e ->{
            //revela la celda
            revelarCeldas(column, row);

            //redibuja el tablero
            borrarTablero();
            dibujarTablero();
        });
    }

    public void OnLongClickCelda(TextView textView, Cell cell){
        textView.setOnLongClickListener(e ->{
            if(cell.isFlagged()){
                if(cell.getTipo() == Cell.TIPO_MINA){
                    cell.setClicked(true);
                    onDerrota();
                }else{
                    cell.setFlagged(false);
                    cell.setRevealed(true);
                    nMinas++;
                    String minas = String.valueOf(nMinas);
                    String s = getResources().getString(R.string.minas) + minas;
                    contadorMinas.setText(s);
                }
            }
            else if (nMinas > 0){
                cell.setFlagged(true);
                nMinas--;
                String minas = String.valueOf(nMinas);
                String s = getResources().getString(R.string.minas) + minas;
                contadorMinas.setText(s);
            }
            else {
                return true;
            }

            borrarTablero();
            dibujarTablero();
            return true;
        });
    }

    private void revelarCeldas(int column, int row){
        Cell cell = tablero[column][row];

        //si tiene bandera o ya esta revelada esa celda no hace nada
        if(cell.isFlagged() || cell.isRevealed() || victoria){
            return;
        }

        //si la primera que ibamos a clicar era una mina, creamos el tablero de nuevo
        if(esPrimera){
            if(cell.getTipo() != Cell.TIPO_VACIO){
                board = new Board();
                tablero = board.crearTablero();
                dibujarTablero();
                revelarCeldas(column, row);
                return;
            }
        }
        esPrimera = false;

        //La revela
        cell.setRevealed(true);

        //revela no solo la casilla clicada si esta vacia, si no tambien los vecinos y vecinos de vecinos vacios
        if(cell.getTipo() == Cell.TIPO_VACIO){
            //lo mismo que el if column-1 > 0
            int minColumn = (column -1) < 0 ? column : column-1;
            int minRow = (row -1) < 0 ? row : row -1;

            int maxColumn = (column +1) > tablero.length -1 ? column : column+1;
            int maxRow = (row +1) > tablero.length -1 ? row : row+1;

            for(int i = minColumn; i <= maxColumn; i++){
                for(int j = minRow; j <= maxRow; j++){
                    revelarCeldas(i,j);
                }
            }
        }else if(cell.getTipo() == Cell.TIPO_MINA){
            cell.setClicked(true);
            onDerrota();
        }
    }

    private void revelarTodasLasCeldas(){
        for(int i = 0; i < tablero.length; i++){
            for(int j = 0; j < tablero.length; j++){
                tablero[i][j].setRevealed(true);
            }
        }
    }

    private int ajustarTamanoTexto(){
        int tamano;

        final int pequeno = 18;
        final int mediano = 24;
        final int grande = 36;

        switch (dificultad){
            case Board.MEDIO:
                tamano = mediano;
                break;
            case Board.DIFICIL:
                tamano = pequeno;
                break;
            default:
                tamano = grande;
                break;
        }
        return tamano;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            int action = event.getAction();
            int pureaction = action & MotionEvent.ACTION_MASK;
            if (pureaction == MotionEvent.ACTION_POINTER_DOWN) {
                mBaseDist = getDistance(event);
                mBaseRatio = mRatio;
            } else {
                float delta = (getDistance(event) - mBaseDist) / STEP;
                float multi = (float) Math.pow(2, delta);
                mRatio = Math.min(1024.0f, Math.max(0.1f, mBaseRatio * multi));
                gridLayout.setScaleX(mRatio + 13);
            }
        }
        return true;
    }

    int getDistance(MotionEvent event) {
        int dx = (int) (event.getX(0) - event.getX(1));
        int dy = (int) (event.getY(0) - event.getY(1));
        return (int) (Math.sqrt(dx * dx + dy * dy));
    }
}
