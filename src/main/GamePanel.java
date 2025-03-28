package main;

import entity.NPC;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale =3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize *maxScreenRow;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;


    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    public NPC[] npcs;

    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        setupNPCs();
    }

    public void setupNPCs() {
        npcs = new NPC[5];
        
        npcs[0] = new NPC(this);
        npcs[0].worldx = tileSize * 23;
        npcs[0].worldy = tileSize * 10;
        npcs[0].getNPCImage("merchant");
        npcs[0].name = "Merchant";
        npcs[0].setDialogue(new String[] {
            "Welcome to my shop!",
            "Feel free to browse around.",
            "We have the finest goods in the village."
        });
        
        // Seed Vendor
        npcs[1] = new NPC(this);
        npcs[1].worldx = tileSize * 30;
        npcs[1].worldy = tileSize * 15;
        npcs[1].getNPCImage("seedvendor");
        npcs[1].name = "Seed Vendor";
        npcs[1].setMovementType(1); // Random movement
        npcs[1].setDialogue(new String[] {
            "Fresh seeds for sale!",
            "These will grow into beautiful plants.",
            "Perfect for your garden."
        });
        
        // Regular Villager
        npcs[2] = new NPC(this);
        npcs[2].worldx = tileSize * 25;
        npcs[2].worldy = tileSize * 21;
        npcs[2].getNPCImage("villager");
        npcs[2].name = "Villager";
        npcs[2].setDialogue(new String[] {
            "Hello there!",
            "What a lovely day in our village.",
            "Hope you're enjoying your stay."
        });
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null){

            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0){
                    remainingTime =0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    public void update(){
        player.update();
        for(int i = 0; i < npcs.length; i++) {
            if(npcs[i] != null) {
                npcs[i].update();
            }
        }
    }
    @SuppressWarnings("override")
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileM.draw(g2);

        for(int i = 0; i < npcs.length; i++) {
            if(npcs[i] != null) {
                npcs[i].draw(g2);
            }
        }

        player.draw(g2);

        g2.dispose();
    }
}
