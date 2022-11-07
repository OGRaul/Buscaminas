package pmdm.example.buscaminas;

public class Cell {
    //Atributos
    private int tipo = 0;
    private boolean isRevealed = false;
    private boolean isFlagged = false;
    private boolean isClicked = false;
    private int valor = 0;
    public static final int TIPO_VACIO = 0;
    public static final int TIPO_MINA = 1;
    public static final int TIPO_NUMERO = 2;

    //Constructores
    public Cell (){}

    //Getters y Setters
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
