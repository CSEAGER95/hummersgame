package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    
    // Movement keys
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    
    // Action keys
    public boolean enterPressed; // Interact with NPCs
    public boolean spacePressed; // Attack
    public boolean escapePressed; // Menu
    
    // Tool keys
    public boolean hoeKeyPressed; // Use hoe
    public boolean waterKeyPressed; // Use watering can
    public boolean plantKeyPressed; // Plant seeds
    public boolean harvestKeyPressed; // Harvest crops
    
    // Inventory keys
    public boolean inventoryKeyPressed; // Open inventory
    
    // Shop keys
    public boolean buyKeyPressed; // Buy items
    public boolean sellKeyPressed; // Sell items
    
    // Game state keys
    public boolean pauseKeyPressed;
    
    private GamePanel gp;
    
    public KeyHandler() {
        // Default constructor for when GamePanel is not available yet
    }
    
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Different key handling based on game state
        if(gp != null) {
            switch(gp.gameState) {
                case 0: // Title state
                    titleState(code);
                    break;
                case 1: // Play state
                    playState(code);
                    break;
                case 2: // Pause state
                    pauseState(code);
                    break;
                case 3: // Dialogue state
                    dialogueState(code);
                    break;
                case 4: // Shop state
                    shopState(code);
                    break;
                case 5: // Game over state
                    gameOverState(code);
                    break;
            }
        } 
        else {
            // Default key handling when GamePanel is not available
            defaultKeyHandling(code);
        }
    }
    
    private void titleState(int code) {
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            // Move selection up
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            // Move selection down
        }
        if(code == KeyEvent.VK_ENTER) {
            // Confirm selection
            gp.gameState = gp.playState;
        }
    }
    
    private void playState(int code) {
        // Movement
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        
        // Interaction/Attack
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = true;
            // Attack logic
            gp.player.attack();
        }
        
        // Menu
        if(code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
            gp.gameState = gp.pauseState;
        }
        
        // Tools
        if(code == KeyEvent.VK_H) {
            hoeKeyPressed = true;
            gp.player.useHoe();
        }
        if(code == KeyEvent.VK_W) {
            waterKeyPressed = true;
            gp.player.useWateringCan();
        }
        if(code == KeyEvent.VK_P) {
            plantKeyPressed = true;
            // Show seed selection or use currently selected seed
            // For simplicity, we can assume the first seed in inventory is used
            for(int i = 0; i < gp.player.inventory.size(); i++) {
                if(gp.player.inventory.get(i).itemType == entity.Item.TYPE_SEED) {
                    gp.player.plantSeed(gp.player.inventory.get(i));
                    break;
                }
            }
        }
        if(code == KeyEvent.VK_R) {
            harvestKeyPressed = true;
            gp.player.harvestCrop();
        }
        
        // Inventory
        if(code == KeyEvent.VK_I) {
            inventoryKeyPressed = true;
            // Open inventory
        }
        
        // Shop interaction
        if(code == KeyEvent.VK_B) {
            buyKeyPressed = true;
            // Open buy menu if near shopkeeper
            if(gp.currentArea == gp.AREA_TOWN) {
                // Check if player is near a shopkeeper
                boolean nearShopkeeper = false;
                int shopkeeperIndex = -1;
                
                for(int i = 0; i < gp.npcs.length; i++) {
                    if(gp.npcs[i] != null && (gp.npcs[i].npcType == entity.NPC.MERCHANT || 
                                              gp.npcs[i].npcType == entity.NPC.SEED_VENDOR)) {
                        
                        int xDistance = Math.abs(gp.player.worldx - gp.npcs[i].worldx);
                        int yDistance = Math.abs(gp.player.worldy - gp.npcs[i].worldy);
                        
                        if(xDistance < gp.tileSize * 2 && yDistance < gp.tileSize * 2) {
                            nearShopkeeper = true;
                            shopkeeperIndex = i;
                            break;
                        }
                    }
                }
                
                if(nearShopkeeper) {
                    gp.openShop(gp.npcs[shopkeeperIndex]);
                }
            }
        }
    }
    
    private void pauseState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
            gp.gameState = gp.playState;
        }
    }
    
    private void dialogueState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            gp.gameState = gp.playState;
        }
    }
    
    private void shopState(int code) {
        if(code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
            gp.gameState = gp.playState;
        }
        
        // Shop navigation keys
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            // Buy the selected item
        }
    }
    
    private void gameOverState(int code) {
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
            // Restart the game
            gp.setupGame();
            gp.gameState = gp.playState;
        }
    }
    
    private void defaultKeyHandling(int code) {
        // Movement
        if(code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        
        // Other basic keys
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            escapePressed = true;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Movement
        if(code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        
        // Actions
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            escapePressed = false;
        }
        
        // Tools
        if(code == KeyEvent.VK_H) {
            hoeKeyPressed = false;
        }
        if(code == KeyEvent.VK_W) {
            waterKeyPressed = false;
        }
        if(code == KeyEvent.VK_P) {
            plantKeyPressed = false;
        }
        if(code == KeyEvent.VK_R) {
            harvestKeyPressed = false;
        }
        
        // Inventory
        if(code == KeyEvent.VK_I) {
            inventoryKeyPressed = false;
        }
        
        // Shop
        if(code == KeyEvent.VK_B) {
            buyKeyPressed = false;
        }
    }
}