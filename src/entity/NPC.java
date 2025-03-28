package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;

public class NPC extends Entity {

    GamePanel gp;

    public String name;
    public String[] dialogues;
    public int dialogueIndex = 0;

    public int actionLockCounter = 0;
    public boolean moving = false;
    private int movementType; // 0 = stationary, 1 = random, 2 = fixed path

    public NPC(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle(8, 16, 32, 32);
        direction = "down";
        speed = 1;

        setDefaultValues();
    }

    public void setDefaultValues() {
        worldx = gp.tileSize * 23;
        worldy = gp.tileSize * 21;
        speed = 1;
        movementType = 0; // Default is stationary
    }

    public void setDialogue(String[] dialogues) {
        this.dialogues = dialogues;
    }

    public String getDialogue() {
        if (dialogues == null || dialogues.length == 0) {
            return "Hello!";
        }

        String dialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        if (dialogueIndex >= dialogues.length) {
            dialogueIndex = 0;
        }

        return dialogue;
    }

    public void setMovementType(int type) {
        this.movementType = type;
    }

    public void getNPCImage(String npcType) {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_up3.png"));
            
            down1 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_down3.png"));
            
            left1 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_left3.png"));
            
            right1 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/npc/" + npcType + "_right3.png"));
            

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter == 120) { // Change action every 2 seconds (60FPS × 2)
            if (movementType == 1) { // Random movement
                int random = (int)(Math.random() * 100 + 1);

                if (random <= 25) {
                    direction = "up";
                    moving = true;
                } else if (random <= 50) {
                    direction = "down";
                    moving = true;
                } else if (random <= 75) {
                    direction = "left";
                    moving = true;
                } else if (random <= 100) {
                    direction = "right";
                    moving = true;
                }
            }

            // Reset counter
            actionLockCounter = 0;
        }
    }

    public void update() {
        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (moving) {
            if (!collisionOn) {
                switch(direction) {
                    case "up" -> worldy -= speed;
                    case "down" -> worldy += speed;
                    case "left" -> worldx -= speed;
                    case "right" -> worldx += speed;
                }
            }

            // Sprite animation
            spriteCounter++;
            if (spriteCounter > 16) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldx - gp.player.worldx + gp.player.screenx;
        int screenY = worldy - gp.player.worldy + gp.player.screeny;

        // Only render if on screen
        if (worldx + gp.tileSize > gp.player.worldx - gp.player.screenx &&
                worldx - gp.tileSize < gp.player.worldx + gp.player.screenx &&
                worldy + gp.tileSize > gp.player.worldy - gp.player.screeny &&
                worldy - gp.tileSize < gp.player.worldy + gp.player.screeny) {

            BufferedImage image = null;

            switch(direction) {
                case "up" -> {
                    if (spriteNum == 1) image = up1;
                    if (spriteNum == 2) image = up2;
                    if (spriteNum == 3) image = up3;
                }
                case "down" -> {
                    if (spriteNum == 1) image = down1;
                    if (spriteNum == 2) image = down2;
                    if (spriteNum == 3) image = down3;
                }
                case "left" -> {
                    if (spriteNum == 1) image = left1;
                    if (spriteNum == 2) image = left2;
                    if (spriteNum == 3) image = left3;
                }
                case "right" -> {
                    if (spriteNum == 1) image = right1;
                    if (spriteNum == 2) image = right2;
                    if (spriteNum == 3) image = right3;
                }
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}