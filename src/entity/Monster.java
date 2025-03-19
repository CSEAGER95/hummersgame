package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Monster extends Entity {
    
    GamePanel gp;
    
    public static final int SLIME = 0;
    public static final int GOBLIN = 1;
    public static final int SKELETON = 2;
    
    public int monsterType;
    
    public Monster(GamePanel gp, int monsterType) {
        this.gp = gp;
        this.type = TYPE_MONSTER;
        this.monsterType = monsterType;
        
        name = "Monster";
        speed = 1;
        maxLife = 4;
        life = maxLife;
        
        solidArea = new java.awt.Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setMonsterValues();
        getMonsterImage();
    }
    
    public void setMonsterValues() {
        switch(monsterType) {
            case SLIME:
                name = "Slime";
                speed = 1;
                maxLife = 4;
                attack = 2;
                defense = 0;
                exp = 2;
                coin = 2;
                break;
            case GOBLIN:
                name = "Goblin";
                speed = 2;
                maxLife = 6;
                attack = 3;
                defense = 1;
                exp = 5;
                coin = 5;
                break;
            case SKELETON:
                name = "Skeleton";
                speed = 1;
                maxLife = 8;
                attack = 4;
                defense = 2;
                exp = 10;
                coin = 10;
                break;
        }
        
        life = maxLife;
    }
    
    public void getMonsterImage() {
        try {
            String prefix = "";
            
            switch(monsterType) {
                case SLIME:
                    prefix = "/monster/slime";
                    break;
                case GOBLIN:
                    prefix = "/monster/goblin";
                    break;
                case SKELETON:
                    prefix = "/monster/skeleton";
                    break;
            }
            
            up1 = ImageIO.read(getClass().getResourceAsStream(prefix + "_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream(prefix + "_up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream(prefix + "_up3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream(prefix + "_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream(prefix + "_down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream(prefix + "_down3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream(prefix + "_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream(prefix + "_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream(prefix + "_left3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream(prefix + "_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream(prefix + "_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream(prefix + "_right3.png"));
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void setAction() {
        actionLockCounter++;
        
        if(actionLockCounter == 60) {
            
            // Get distance from player to monster to make it chase player
            int goalCol = (gp.player.worldx + gp.player.solidArea.x) / gp.tileSize;
            int goalRow = (gp.player.worldy + gp.player.solidArea.y) / gp.tileSize;
            
            int currentCol = (worldx + solidArea.x) / gp.tileSize;
            int currentRow = (worldy + solidArea.y) / gp.tileSize;
            
            // Check if player is within aggro range (10 tiles)
            int xDistance = Math.abs(goalCol - currentCol);
            int yDistance = Math.abs(goalRow - currentRow);
            int totalDistance = xDistance + yDistance;
            
            if(totalDistance < 10) {
                // Chase player
                if(goalCol > currentCol) {
                    direction = "right";
                } else if(goalCol < currentCol) {
                    direction = "left";
                } else if(goalRow > currentRow) {
                    direction = "down";
                } else if(goalRow < currentRow) {
                    direction = "up";
                }
            } else {
                // Random movement
                Random random = new Random();
                int i = random.nextInt(100) + 1; // 1-100
                
                if(i <= 25) {
                    direction = "up";
                } else if(i <= 50) {
                    direction = "down";
                } else if(i <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }
            }
            
            actionLockCounter = 0;
        }
    }
    
    @Override
    public void update() {
        super.update();
        
        // Check collision with player for damage
        int playerIndex = gp.cChecker.checkPlayer(this);
        if(playerIndex != 999) {
            damagePlayer(attack);
        }
        
        // Check collision with tiles
        collisionOn = false;
        gp.cChecker.checkTile(this);
        
        // If collision is false, monster can move
        if(!collisionOn) {
            switch(direction) {
                case "up": worldy -= speed; break;
                case "down": worldy += speed; break;
                case "left": worldx -= speed; break;
                case "right": worldx += speed; break;
            }
        }
        
        // Monster status effects
        if(invincible) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    
    public void damagePlayer(int attack) {
        if(!gp.player.invincible) {
            // Calculate damage based on attack vs. player defense
            int damage = attack - gp.player.defense;
            if(damage < 0) {
                damage = 0;
            }
            
            // Apply damage to player
            gp.player.life -= damage;
            gp.player.invincible = true;
            
            // Check player death
            if(gp.player.life <= 0) {
                gp.player.dying = true;
            }
        }
    }
    
    public void takeDamage(int damage) {
        if(!invincible) {
            // Calculate damage based on player attack vs. monster defense
            int finalDamage = damage - defense;
            if(finalDamage < 0) {
                finalDamage = 0;
            }
            
            life -= finalDamage;
            invincible = true;
            hpBarOn = true;
            hpBarCounter = 0;
            
            if(life <= 0) {
                dying = true;
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        super.draw(g2, gp);
        
        // Draw health bar for monsters
        if(hpBarOn && !dying) {
            int screenX = worldx - gp.player.worldx + gp.player.screenx;
            int screenY = worldy - gp.player.worldy + gp.player.screeny;
            
            // Health bar coordinates
            int barX = screenX - 1;
            int barY = screenY - 16;
            
            // Health bar size
            int barWidth = gp.tileSize + 2;
            int barHeight = 10;
            
            // Draw background
            g2.setColor(new Color(35, 35, 35));
            g2.fillRect(barX, barY, barWidth, barHeight);
            
            // Calculate health percentage
            double healthPercent = (double)life / maxLife;
            int healthWidth = (int)(healthPercent * barWidth);
            
            // Draw health
            g2.setColor(new Color(255, 0, 30));
            g2.fillRect(barX, barY, healthWidth, barHeight);
            
            // HP bar timer
            hpBarCounter++;
            if(hpBarCounter > 600) {
                hpBarCounter = 0;
                hpBarOn = false;
            }
        }
        
        // Draw dying animation
        if(dying) {
            dyingCounter++;
            
            // Flashing effect
            if(dyingCounter % 5 == 0) {
                int screenX = worldx - gp.player.worldx + gp.player.screenx;
                int screenY = worldy - gp.player.worldy + gp.player.screeny;
                
                g2.setColor(new Color(255, 0, 0, 100));
                g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
            
            if(dyingCounter > 30) {
                alive = false;
                
                // Drop items/coins
                dropItems();
                
                // Give XP to player
                gp.player.exp += exp;
            }
        }
    }
    
    public void dropItems() {
        // Drop coins
        if(coin > 0) {
            // Create coin item and place it at monster's position
            Item coinItem = new Item(gp);
            coinItem.setItemValues(Item.TYPE_CONSUMABLE, "Coin", 1, true);
            coinItem.amount = coin;
            coinItem.worldx = worldx;
            coinItem.worldy = worldy;
            
            // Add to list of items in the world
            // (Would need to create an item manager in the GamePanel)
        }
        
        // Chance to drop other items based on monster type
        Random random = new Random();
        int chance = random.nextInt(100) + 1;
        
        if(monsterType == SKELETON && chance <= 20) {
            // 20% chance to drop bone
            Item bone = new Item(gp);
            bone.setItemValues(Item.TYPE_CONSUMABLE, "Bone", 5, true);
            bone.worldx = worldx;
            bone.worldy = worldy;
            
            // Add to list of items in the world
        }
    }
}