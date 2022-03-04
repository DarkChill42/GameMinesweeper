package Game;

import javax.swing.*;

public class Field {

    private JButton button;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isNumbered;

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isNumbered() {
        return isNumbered;
    }

    public void setNumbered(boolean numbered) {
        isNumbered = numbered;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Field(JButton button, boolean isMine, boolean isFlagged, boolean isNumbered, int x, int y)
    {
        this.button = button;
        this.isMine = isMine;
        this.isFlagged = isFlagged;
        this.isNumbered = isNumbered;
        this.x = x;
        this.y = y;
    }

}
