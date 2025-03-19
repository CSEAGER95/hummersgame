package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Entity {
    public int worldx, worldy;
    public int speed;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    
    // Character attributes
    public int maxLife;
    public int life;
    public int level;
    public int exp;
    public int nextLevelExp;
    public int strength; // More damage
    public int dexterity; // More defense
    public int attack; // Total attack value
    public int defense; // Total defense value
    public int coin;
    
    // Item attributes
    public boolean stackable = false;
    public int amount = 1;
    public int value = 0;
    public String name;
    public String description = "";
    
    // NPC/Monster types
    public int type; // 0 = player, 1 = NPC, 2 = monster, 3 = farmable plot
    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_NPC = 1;
    public static final int TYPE_MONSTER = 2;
    public static final int TYPE_FARM_PLOT = 3;
    
    // NPC/Monster behavior
    public String dialogues[] = new String[20];
    public int dialogueIndex = 0;
    
    // Monster attributes
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public int dyingCounter = 0;
    public boolean hpBarOn = false;
    public int hpBarCounter = 0;
    public int actionLockCounter = 0;
    
    // Farming system
    public boolean planted = false;
    public int growthStage = 0;
    public int growthTimer = 0;
    public int maxGrowthStage = 3; // Seed -> Sprout -> Growing -> Ready
    public Item crop;
    
    public void setAction() {}
    
    public void update() {
        setAction();
        
        collisionOn = false;
        // Advanced game would check collision with tiles and other entities here
        
        // Sprite animation
        spriteCounter++;
        if(spriteCounter > 16) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            else if(spriteNum == 2) {
                spriteNum = 3;
            }
            else if(spriteNum == 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {
        BufferedImage image = null;
        int screenX = worldx - gp.player.worldx + gp.player.screenx;
        int screenY = worldy - gp.player.worldy + gp.player.screeny;
        
        // Only draw if entity is visible within the screen
        if(worldx + gp.tileSize > gp.player.worldx - gp.player.screenx && 
           worldx - gp.tileSize < gp.player.worldx + gp.player.screenx && 
           worldy + gp.tileSize > gp.player.worldy - gp.player.screeny && 
           worldy - gp.tileSize < gp.player.worldy + gp.player.screeny) {
            
            switch(direction) {
                case "up":
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;
                    if(spriteNum == 3) image = up3;
                    break;
                case "down":
                    if(spriteNum == 1) image = down1;
                    if(spriteNum == 2) image = down2;
                    if(spriteNum == 3) image = down3;
                    break;
                case "left":
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                    if(spriteNum == 3) image = left3;
                    break;
                case "right":
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                    if(spriteNum == 3) image = right3;
                    break;
            }
            
            // Monster health bar
            if(type == TYPE_MONSTER && hpBarOn) {
                // Draw health bar here
            }
            
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
    
    public void speak() {
        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        // Logic to display dialogue would go here
    }
    
    public void damageMonster(int index, int attack) {
        if(index != 999) {
            // Logic to damage monster, calculate based on attack and defense
        }
    }
    
    public void updateFarmPlot() {
        if(planted) {
            growthTimer++;
            if(growthTimer >= 600 && growthStage < maxGrowthStage) { // 10 seconds per growth stage at 60 FPS
                growthStage++;
                growthTimer = 0;
            }
        }
    }
    
    public void plant(Item seed) {
        if(!planted) {
            planted = true;
            growthStage = 0;
            growthTimer = 0;
            crop = seed;
        }
    }
    
    public Item harvest() {
        if(planted && growthStage == maxGrowthStage) {
            Item harvestedCrop = crop;
            planted = false;
            growthStage = 0;
            growthTimer = 0;
            crop = null;
            return harvestedCrop;
        }
        return null;
    }
}