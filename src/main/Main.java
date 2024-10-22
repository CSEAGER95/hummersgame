package main;

import javax.swing.JFrame;
import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
        System.out.println("Hummer's very own game!");
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Hummer's Garden Club");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();

    }
}