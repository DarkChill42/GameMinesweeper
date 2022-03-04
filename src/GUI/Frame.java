package GUI;

import Game.Field;
import Game.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Frame extends JFrame{

    static Field[][] board;

    static final int BOARD_LENGTH = 15;
    static final int BOARD_WIDTH = 15;
    static final int MINE_CHANCE = 20;
    static int BOMB_COUNT = 0;
    
    JLabel jl;
    JLabel jbomb;
    JLabel message;

    public Frame()
    {
        message = new JLabel("");
        message.setFont(new Font("Arial", Font.BOLD, 20));
        message.setForeground(new Color(0x8E0C19));
        message.setBounds(0,50,600,100);
        this.add(message);

        final int[] first = new int[1];
        first[0]=0;
        board = new Field[BOARD_LENGTH][BOARD_WIDTH];



        drawBoard();
        for(int i = 0; i < BOARD_WIDTH; i++)
        {
            for(int j = 0; j < BOARD_LENGTH; j++)
            {
                this.add(board[i][j].getButton());

                int finalI = i;
                int finalJ = j;
                board[finalI][finalJ].getButton().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(e.getButton() == MouseEvent.BUTTON1)
                        {
                            if(!board[finalI][finalJ].isFlagged())
                            {
                                first[0]++;
                                if(first[0]==1)
                                {
                                    List<Field> check = new ArrayList<>();
                                    for(int y = finalI-1; y <= finalI+1; y++)
                                    {
                                        for(int x = finalJ-1; x <= finalJ+1; x++)
                                        {
                                            if(y<0 || x<0 || y>BOARD_LENGTH-1 || x>BOARD_WIDTH-1) continue;
                                            check.add(board[y][x]);
                                        }
                                    }
                                    generateMines(check);
                                }

                                if(board[finalI][finalJ].isMine())
                                {
                                    message.setText("YOU LOST XD!");
                                } else {
                                    int cnt = countMines(board, finalI, finalJ);
                                    if(cnt!=0)
                                    {
                                        board[finalI][finalJ].getButton().setForeground(Util.getRequiredColor(cnt));
                                        board[finalI][finalJ].getButton().setText("" + cnt);
                                        board[finalI][finalJ].getButton().setFont(new Font("Arial", Font.BOLD, 30));
                                        board[finalI][finalJ].getButton().setBackground(new Color(0xA2A2A8));
                                        board[finalI][finalJ].setNumbered(true);
                                    } else {
                                        board[finalI][finalJ].getButton().setBackground(new Color(0xA2A2A8));
                                        getNeighbours(finalI,finalJ);
                                    }
                                }
                            }
                        } else if(e.getButton() == MouseEvent.BUTTON3)
                        {
                            if(BOMB_COUNT > 0)
                            {
                                if(!board[finalI][finalJ].isFlagged())
                                {
                                    if(!board[finalI][finalJ].isNumbered())
                                    {
                                        board[finalI][finalJ].setFlagged(true);
                                        board[finalI][finalJ].getButton().setFont(new Font("Arial", Font.BOLD, 30));
                                        board[finalI][finalJ].getButton().setText("F");
                                        jbomb.setText((--BOMB_COUNT) + "");
                                    }
                                } else {
                                    if(!board[finalI][finalJ].isNumbered())
                                    {
                                        board[finalI][finalJ].setFlagged(false);
                                        board[finalI][finalJ].getButton().setText("");
                                        jbomb.setText((++BOMB_COUNT) + "");
                                    }
                                }
                            } else {
                                if(!board[finalI][finalJ].isNumbered() && board[finalI][finalJ].isFlagged())
                                {
                                    board[finalI][finalJ].setFlagged(false);
                                    board[finalI][finalJ].getButton().setText("");
                                    jbomb.setText((++BOMB_COUNT) + "");
                                }
                            }
                        }
                    }
                });
            }
        }
        jl = new JLabel("Bomb count: ");
        jl.setForeground(new Color(0x2B75D5));
        jbomb = new JLabel(BOMB_COUNT + "");
        jbomb.setForeground(new Color(0xDB5407));
        jbomb.setBounds(105,0,600,100);
        jbomb.setFont(new Font("Arial",Font.BOLD, 20));
        jl.setBounds(0,0,600,100);
        jl.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(jl);
        this.add(jbomb);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1080,1080);
        this.setVisible(true);


    }

    void getNeighbours(int i, int j)
    {
        List<Field> list = new ArrayList<>();
        for(int y = i-1; y <= i+1; y++)
        {
            for(int x = j-1; x <= j+1; x++)
            {
                if(y==i&&x==j || y<0 || x<0 || y>BOARD_LENGTH-1 || x>BOARD_WIDTH-1) continue;
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
                    getNeighbours(f.getY(), f.getX());
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
    int countMines(Field[][] board, int i, int j)
    {
        int cnt = 0;
        for(int y = i-1; y <= i+1; y++)
        {
            for(int x = j-1; x <= j+1; x++)
            {
                if(y==i&&x==j || y<0 || x<0 || y>BOARD_LENGTH-1 || x>BOARD_WIDTH-1) continue;
                if(board[y][x].isMine()) cnt++;
            }
        }
        return cnt;
    }

    void generateMines(List<Field> list)
    {
        Random r = new Random();
        loop:
        for(int i = 0; i < BOARD_WIDTH; i++)
        {
            for(int j = 0; j < BOARD_LENGTH; j++)
            {
                for(int k = 0; k < list.size(); k++)
                {
                    if(list.get(k).getX() == j && list.get(k).getY() == i) continue loop;
                }

                int ran = r.nextInt(100) + 1;
                board[i][j].setMine(ran <= MINE_CHANCE);
                if(ran <= MINE_CHANCE) BOMB_COUNT++;
            }
        }
        jbomb.setText(BOMB_COUNT + "");
    }

    void drawBoard()
    {

        int y = 25;
        int x = 200;

        for(int i = 0; i < BOARD_WIDTH; i++)
        {
            for(int j = 0; j < BOARD_LENGTH; j++)
            {
                if(y%775==0)
                {
                    x+=50;
                    y=25;
                }
                board[i][j] = new Field(new JButton(), false, false, false, j, i);
                board[i][j].getButton().setBounds(x,y,50,50);
                board[i][j].getButton().setFocusable(false);
                board[i][j].getButton().setBackground(new Color(0x489CE8));
                board[i][j].getButton().setBorder(BorderFactory.createLineBorder(new Color(0x919193), 1));
                y+=50;
            }
        }
    }
}
