package pmdm.example.buscaminas;

import java.util.Random;

public class Board {
    //Atributos
    private int size;
    private int nMines;
    private Cell[][] Cells;
    public static final int FACIL = 0;
    public static final int MEDIO = 1;
    public static final int DIFICIL = 2;

    //Constructores
    public Board(){
        int dificultad = MainActivity.dificultad;
        switch (dificultad){
            case FACIL:
                size = 8;
                nMines = 15;
                break;
            case MEDIO:
                size = 12;
                nMines = 30;
                break;
            case DIFICIL:
                size = 16;
                nMines = 60;
                break;
        }
        crearTablero();
    }

    //Metodos
    public Cell[][] crearTablero(){

        Cells = new Cell[size][size];

        //inicializa el array
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Cell cell = new Cell();
                //cell.setTipo(elegirTipo());
                Cells[i][j] = cell;
            }
        }

        setMinas();
        setNumeros();
        return Cells;
    }

    private void setNumeros(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){

                if(Cells[i][j].getTipo() == Cell.TIPO_VACIO){
                    int numero = 0;

                    //arriba
                    if( i >= 1 ){
                        if(Cells[i-1][j].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //abajo
                    if( i < size-1 ){
                        if(Cells[i+1][j].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //izquierda
                    if( j >= 1 ){
                        if(Cells[i][j-1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //derecha
                    if( j < size-1){
                        if(Cells[i][j+1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //arriba izquierda
                    if(i >= 1 && j >= 1){
                        if(Cells[i-1][j-1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //arriba derecha
                    if(i >= 1 && j < size-1){
                        if(Cells[i-1][j+1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //abajo izquierda
                    if( i < size-1 && j >= 1){
                        if(Cells[i+1][j-1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    //abajo derecha
                    if( i < size-1 && j < size-1){
                        if(Cells[i+1][j+1].getTipo() == Cell.TIPO_MINA){
                            numero++;
                        }
                    }

                    if(numero != 0){
                        Cells[i][j].setTipo(Cell.TIPO_NUMERO);
                        Cells[i][j].setValor(numero);
                    }
                }
            }
        }
    }

    private void setMinas(){
        while (contarMinas() != nMines){

            Random r = new Random();

            int row = r.nextInt(size);
            int column = r.nextInt(size);

            if(Cells[row][column].getTipo() == Cell.TIPO_VACIO){
                Cells[row][column].setTipo(Cell.TIPO_MINA);
            }
        }
    }

    private int  contarMinas(){
        int minas = 0;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if(Cells[i][j].getTipo() == Cell.TIPO_MINA){
                    minas++;
                }
            }
        }
        return minas;
    }

    //Getters y Setters
    public Cell[][] getBoard() {
        return Cells;
    }

    public int getSize() {
        return size;
    }

    public int getnMines() {
        return nMines;
    }
}
