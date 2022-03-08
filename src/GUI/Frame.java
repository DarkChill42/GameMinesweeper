package GUI;

import Game.Field;
import Game.Game;
import Game.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Frame extends JFrame{

    static Field[][] board;

    public static final int BOARD_LENGTH = 15;
    public static final int BOARD_WIDTH = 15;
    public static final int CELL_SIZE = 50;

    public static List<Frame> list = new ArrayList<>();
    public static int cnt = 0;
    private int BOMB_COUNT = Game.BOMB_COUNT;
    
    JLabel jl;
    JLabel jbomb;
    JLabel message;
    JMenuBar jmb;

    public Frame(int bombCount)
    {
        list.add(this);
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).dispose();
        }

        message = new JLabel("");
        jmb = new JMenuBar();
        jmb.setBackground(new Color(0xA7A7A4));
        JMenu jm1 = new JMenu("Options");
        jm1.setFont(new Font("Arial", Font.BOLD, 16));
        jm1.setForeground(new Color(0x000000));
        JMenuItem jmi1 = new JMenuItem("Restart");
        JMenuItem jmi2 = new JMenuItem("Settings");

        jm1.add(jmi1);
        jm1.add(jmi2);
        jmb.add(jm1);
        this.setJMenuBar(jmb);

        jmi1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Frame(Game.BOMB_COUNT);
            }
        });

        jmi2.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new Options();
            }
        });

        message.setFont(new Font("Arial", Font.BOLD, 20));
        message.setForeground(new Color(0x8E0C19));
        message.setBounds(0,50,600,100);
        this.add(message);

        final int[] first = new int[1];
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
                    public void mousePressed(MouseEvent e) {
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
                                    Game.generateMines(check, board);
                                }

                                if(board[finalI][finalJ].isMine())
                                {
                                    message.setText("YOU LOST!");
                                } else {
                                    int cnt = Game.countMines(board, finalI, finalJ);
                                    if(cnt!=0)
                                    {
                                        board[finalI][finalJ].getButton().setForeground(Util.getRequiredColor(cnt));
                                        board[finalI][finalJ].getButton().setText("" + cnt);
                                        board[finalI][finalJ].getButton().setFont(new Font("Arial", Font.BOLD, 30));
                                        board[finalI][finalJ].getButton().setBackground(new Color(0xA2A2A8));
                                        board[finalI][finalJ].setNumbered(true);
                                    } else {
                                        board[finalI][finalJ].getButton().setBackground(new Color(0xA2A2A8));
                                        Game.getNeighbours(finalI,finalJ, board);
                                    }
                                }
                            }
                        } else if(e.getButton() == MouseEvent.BUTTON3)
                        {
                            if(BOMB_COUNT > 0 && first[0] >= 1)
                            {
                                if(!board[finalI][finalJ].isFlagged())
                                {
                                    if(!board[finalI][finalJ].isNumbered())
                                    {
                                        board[finalI][finalJ].setFlagged(true);
                                        board[finalI][finalJ].getButton().setFont(new Font("Arial", Font.BOLD, 30));
                                        board[finalI][finalJ].getButton().setText("F");
                                        jbomb.setText((--BOMB_COUNT) + "");

                                        if(Game.checkWinner(board, BOMB_COUNT))
                                        {
                                            message.setForeground(new Color(0x02C604));
                                            message.setText("YOU WON!");
                                        }
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
        this.setSize(1080,813);
        this.setVisible(true);
        for (int i = 0; i < list.size() - 1; i++) {
            list.remove(i);
        }
    }

    void drawBoard()
    {
        int y = 0;
        int x = 265;
        int nextColumn = BOARD_LENGTH * CELL_SIZE + y;
        for(int i = 0; i < BOARD_WIDTH; i++)
        {
            for(int j = 0; j < BOARD_LENGTH; j++)
            {
                if(y%nextColumn==0)
                {
                    x+=50;
                    y=0;
                }
                board[i][j] = new Field(new JButton(), false, false, false, j, i);
                board[i][j].getButton().setBounds(x,y,CELL_SIZE,CELL_SIZE);
                board[i][j].getButton().setFocusable(false);
                board[i][j].getButton().setBackground(new Color(0x489CE8));
                board[i][j].getButton().setBorder(BorderFactory.createLineBorder(new Color(0x919193), 1));
                y+=50;
            }
        }
    }
}
