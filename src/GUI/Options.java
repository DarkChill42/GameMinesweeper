package GUI;

import Game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Options extends JFrame {
    private JTextField textField1;
    private JButton button1;
    private JPanel panel1;
    private JTextField a15TextField1;
    private JTextField a15TextField;

    public Options()
    {
        this.setVisible(true);
        this.setPreferredSize(new Dimension(800,600));
        this.setContentPane(panel1);
        this.pack();

        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {

                    int count = Integer.parseInt(textField1.getText());
                    if(count >= (Frame.BOARD_WIDTH * Frame.BOARD_LENGTH - 9))
                    {
                        count=(Frame.BOARD_LENGTH * Frame.BOARD_WIDTH - 9);
                    }
                    if(count <= 0)
                    {
                        count=1;
                    }
                    Game.BOMB_COUNT = count;
                    dispose();
                    new Frame(count);
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void createUIComponents() {

    }
}
