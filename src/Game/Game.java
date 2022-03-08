package Game;

import GUI.Frame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    public static int BOMB_COUNT = 35;

    public static void getNeighbours(int i, int j, Field[][] board)
    {
        List<Field> list = new ArrayList<>();
        for(int y = i-1; y <= i+1; y++)
        {
            for(int x = j-1; x <= j+1; x++)
            {
                if(y==i&&x==j || y<0 || x<0 || y> Frame.BOARD_LENGTH-1 || x>Frame.BOARD_WIDTH-1) continue;
                list.add(board[y][x]);
            }
        }
        list.forEach((f->{
            int cnt = countMines(board, f.getY(), f.getX());
            if(!f.isNumbered())
            {
                if(cnt==0)
                {
                    f.getButton().setBackground(new Color(0xA2A2A8));
                    f.setNumbered(true);
                    getNeighbours(f.getY(), f.getX(), board);
                } else {
                    f.getButton().setForeground(Util.getRequiredColor(cnt));
                    f.getButton().setFont(new Font("Arial", Font.BOLD, 30));
                    f.getButton().setText("" + cnt);
                    f.getButton().setBackground(new Color(0xA2A2A8));
                    f.setNumbered(true);
                }
            }
        }));
    }

    public static int countMines(Field[][] board, int i, int j)
    {
        int cnt = 0;
        for(int y = i-1; y <= i+1; y++)
        {
            for(int x = j-1; x <= j+1; x++)
            {
                if(y==i&&x==j || y<0 || x<0 || y>Frame.BOARD_LENGTH-1 || x>Frame.BOARD_WIDTH-1) continue;
                if(board[y][x].isMine()) cnt++;
            }
        }
        return cnt;
    }

    public static void generateMines(List<Field> list, Field[][] board)
    {
        int counter = 0;
        List<Point> seen = new ArrayList<>();
        while(counter < BOMB_COUNT) {
            boolean skip = false;
            int ty = Util.randomY(), tx = Util.randomX();
            for (Field field : list) {
                if (field.getY() == ty && field.getX() == tx) {
                    skip = true;
                    break;
                }
            }
            for (Point point : seen) {
                if (point.getY() == ty && point.getX() == tx) {
                    skip = true;
                    break;
                }
            }
            if(!skip) {
                board[ty][tx].setMine(true);
                seen.add(new Point(tx,ty));
                counter++;
            }
        }
    }

    public static boolean checkWinner(Field[][] board, int bombCount)
    {
        if(bombCount != 0) return false;

        boolean allMatch = true;
        loop:
        for (int i = 0; i < Frame.BOARD_WIDTH; i++) {
            for (int j = 0; j < Frame.BOARD_LENGTH; j++) {
                if(!board[i][j].isFlagged() && board[i][j].isMine()) {
                    allMatch = false;
                    break loop;
                }
            }
        }
        return allMatch;
    }
}
