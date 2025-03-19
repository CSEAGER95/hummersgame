package main;

import entity.FarmPlot;
import entity.Monster;
import entity.NPC;
import entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
    
    // Screen settings
    final int originalTileSize = 16;
    final int scale = 3;
    
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    // Game state
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int shopState = 4;
    public final int gameOverState = 5;
    public int gameState = playState;
    
    // FPS
    int FPS = 60;
    
    // System
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player;
    
    // Entities and objects
    public NPC npcs[] = new NPC[10];
    public Monster monsters[] = new Monster[20];
    public FarmPlot farmPlots[] = new FarmPlot[50];
    
    // Areas
    public final int AREA_FARM = 0;
    public final int AREA_TOWN = 1;
    public final int AREA_DUNGEON = 2;
    public int currentArea = AREA_FARM;
    
    public GamePanel() throws IOException {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        // Initialize player
        player = new Player(this, keyH);
        
        // Set up the game world
        setupGame();
    }
    
    public void setupGame() {
        // Setup farming area
        setupFarmingArea();
        
        // Setup town
        setupTown();
        
        // Setup dungeon
        setupDungeon();
    }
    
    private void setupFarmingArea() {
        // Farm plots are in a 5x5 grid in the starting area
        int startCol = 20;
        int startRow = 18;
        
        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 5; col++) {
                int index = row * 5 + col;
                farmPlots[index] = new FarmPlot(this, startCol + col, startRow + row);
            }
        }
    }
    
    private void setupTown() {
        // Town is to the right of starting area (around col 35)
        // Create merchant
        npcs[0] = new NPC(this, NPC.MERCHANT);
        npcs[0].worldx = 35 * tileSize;
        npcs[0].worldy = 21 * tileSize;
        
        // Create seed vendor
        npcs[1] = new NPC(this, NPC.SEED_VENDOR);
        npcs[1].worldx = 38 * tileSize;
        npcs[1].worldy = 21 * tileSize;
        
        // Create villagers
        npcs[2] = new NPC(this, NPC.VILLAGER);
        npcs[2].worldx = 33 * tileSize;
        npcs[2].worldy = 23 * tileSize;
        
        npcs[3] = new NPC(this, NPC.VILLAGER);
        npcs[3].worldx = 37 * tileSize;
        npcs[3].worldy = 19 * tileSize;
    }
    
    private void setupDungeon() {
        // Dungeon is to the north of starting area (around row 10)
        // Add slimes
        monsters[0] = new Monster(this, Monster.SLIME);
        monsters[0].worldx = 23 * tileSize;
        monsters[0].worldy = 10 * tileSize;
        
        monsters[1] = new Monster(this, Monster.SLIME);
        monsters[1].worldx = 24 * tileSize;
        monsters[1].worldy = 9 * tileSize;
        
        // Add goblins
        monsters[2] = new Monster(this, Monster.GOBLIN);
        monsters[2].worldx = 22 * tileSize;
        monsters[2].worldy = 8 * tileSize;
        
        monsters[3] = new Monster(this, Monster.GOBLIN);
        monsters[3].worldx = 25 * tileSize;
        monsters[3].worldy = 7 * tileSize;
        
        // Add skeleton
        monsters[4] = new Monster(this, Monster.SKELETON);
        monsters[4].worldx = 23 * tileSize;
        monsters[4].worldy = 5 * tileSize;
    }
    
    public void respawnMonsters() {
        // Respawn dead monsters after some time
        for(int i = 0; i < monsters.length; i++) {
            if(monsters[i] != null && !monsters[i].alive) {
                monsters[i].alive = true;
                monsters[i].dying = false;
                monsters[i].life = monsters[i].maxLife;
            }
        }
    }
    
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while(gameThread != null) {
            update();
            repaint();
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;
                
                if(remainingTime < 0) {
                    remainingTime = 0;
                }
                
                Thread.sleep((long)remainingTime);
                
                nextDrawTime += drawInterval;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void update() {
        // Update based on game state
        if(gameState == playState) {
            // Update player
            player.update();
            
            // Update NPCs
            for(int i = 0; i < npcs.length; i++) {
                if(npcs[i] != null) {
                    npcs[i].update();
                }
            }
            
            // Update monsters
            for(int i = 0; i < monsters.length; i++) {
                if(monsters[i] != null && monsters[i].alive) {
                    monsters[i].update();
                }
            }
            
            // Update farm plots
            for(int i = 0; i < farmPlots.length; i++) {
                if(farmPlots[i] != null) {
                    farmPlots[i].update();
                }
            }
            
            // Check area transitions
            checkAreaTransitions();
        }
    }
    
    private void checkAreaTransitions() {
        // Check if player is moving to town (right)
        if(player.worldx > 30 * tileSize) {
            currentArea = AREA_TOWN;
        }
        // Check if player is moving to dungeon (north)
        else if(player.worldy < 15 * tileSize) {
            currentArea = AREA_DUNGEON;
        }
        // Otherwise in farm area
        else {
            currentArea = AREA_FARM;
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g;
        
        // Draw based on game state
        if(gameState == titleState) {
            // Draw title screen
        }
        else {
            // Draw tiles
            tileM.draw(g2);
            
            // Draw farm plots
            for(int i = 0; i < farmPlots.length; i++) {
                if(farmPlots[i] != null) {
                    farmPlots[i].draw(g2, this);
                }
            }
            
            // Draw NPCs
            for(int i = 0; i < npcs.length; i++) {
                if(npcs[i] != null) {
                    npcs[i].draw(g2, this);
                }
            }
            
            // Draw monsters
            for(int i = 0; i < monsters.length; i++) {
                if(monsters[i] != null) {
                    monsters[i].draw(g2, this);
                }
            }
            
            // Draw player
            player.draw(g2);
            
            // Draw UI
            drawUI(g2);
            
            // Draw game state specific UI
            if(gameState == pauseState) {
                // Draw pause screen
            }
            else if(gameState == dialogueState) {
                // Draw dialogue window
            }
            else if(gameState == shopState) {
                // Draw shop window
            }
            else if(gameState == gameOverState) {
                // Draw game over screen
            }
        }
        
        g2.dispose();
    }
    
    private void drawUI(Graphics2D g2) {
        // Draw health bar
        int x = 10;
        int y = 10;
        int width = 150;
        int height = 20;
        
        // Draw background
        g2.setColor(new Color(35, 35, 35));
        g2.fillRect(x, y, width, height);
        
        // Calculate health percentage
        double healthPercent = (double)player.life / player.maxLife;
        int healthWidth = (int)(healthPercent * width);
        
        // Draw health
        g2.setColor(new Color(255, 0, 30));
        g2.fillRect(x, y, healthWidth, height);
        
        // Draw border
        g2.setColor(Color.white);
        g2.drawRect(x, y, width, height);
        
        // Draw health text
        g2.drawString("HP: " + player.life + "/" + player.maxLife, x + 5, y + 15);
        
        // Draw level and experience
        g2.drawString("Level: " + player.level, x, y + 30);
        g2.drawString("Exp: " + player.exp + "/" + player.nextLevelExp, x, y + 45);
        
        // Draw coins
        g2.drawString("Coins: " + player.coin, x, y + 60);
        
        // Draw area name
        String areaName = "";
        switch(currentArea) {
            case AREA_FARM:
                areaName = "Farm";
                break;
            case AREA_TOWN:
                areaName = "Town";
                break;
            case AREA_DUNGEON:
                areaName = "Dungeon";
                break;
        }
        g2.drawString("Area: " + areaName, screenWidth - 150, y + 15);
        
        // Draw instructions
        if(currentArea == AREA_FARM) {
            g2.drawString("Press 'H' to use hoe", screenWidth - 150, y + 30);
            g2.drawString("Press 'W' to water", screenWidth - 150, y + 45);
            g2.drawString("Press 'P' to plant", screenWidth - 150, y + 60);
            g2.drawString("Press 'R' to harvest", screenWidth - 150, y + 75);
        }
        else if(currentArea == AREA_DUNGEON) {
            g2.drawString("Press 'SPACE' to attack", screenWidth - 150, y + 30);
        }
        else if(currentArea == AREA_TOWN) {
            g2.drawString("Press 'ENTER' to talk", screenWidth - 150, y + 30);
            g2.drawString("Press 'B' to buy/sell", screenWidth - 150, y + 45);
        }
    }
    
    // Various UI methods would go here for different game states
    
    public void openShop(NPC shopkeeper) {
        gameState = shopState;
        // Code to display shop items
    }
    
    public void showDialogue(String dialogue) {
        gameState = dialogueState;
        // Code to display dialogue
    }
    
    public void gameOver() {
        gameState = gameOverState;
        // Code for game over handling
    }
}