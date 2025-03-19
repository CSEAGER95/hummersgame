package entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenx;
    public final int screeny;
    public ArrayList<Item> inventory = new ArrayList<>();

    public Player(GamePanel gp, KeyHandler keyH) throws IOException {

        this.gp = gp;
        this.keyH = keyH;

        screenx = gp.screenWidth/2 - (gp.tileSize /2);
        screeny = gp.screenHeight/2 - (gp.tileSize /2);

        solidArea = new Rectangle(8, 16, 32, 32);

        getPlayerImage();
        setDefaultValues();


    }

    public void setDefaultValues(){

        worldx = gp.tileSize * 23;
        worldy = gp.tileSize * 21;
        speed = 3;
        direction = "down";
    }
    @SuppressWarnings("CallToPrintStackTrace")
    public void getPlayerImage() {

        try {

            up1 = ImageIO.read(getClass().getResourceAsStream("/hummer/wf1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/hummer/wf2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/hummer/wf3.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/hummer/sf1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/hummer/sf2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/hummer/sf3.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/hummer/af1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/hummer/af2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/hummer/af3.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/hummer/df1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/hummer/df2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/hummer/df3.png"));

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void update(){
        if(keyH.upPressed == true || keyH.downPressed == true ||
                keyH.leftPressed == true || keyH.rightPressed == true){

            if(keyH.upPressed == true){
                worldy -= speed;
                direction = "up";
            }

            else if(keyH.downPressed == true){
                worldy += speed;
                direction = "down";
            }

            else if(keyH.leftPressed == true){
                worldx -= speed;
                direction = "left";
            }

            if(keyH.rightPressed == true){
                worldx+= speed;
                direction = "right";
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);

            spriteCounter++;
            if(spriteCounter > 16) {
                if (spriteNum == 1) {

                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 3;
                }
                else if(spriteNum == 3){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch(direction) {
            case "up" -> {
                if(spriteNum == 1){
                    image = up1;}
                if(spriteNum == 2){
                    image = up2;}
                if(spriteNum == 3){
                    image = up3;}
            }
            case "down" ->{
                if(spriteNum == 1){
                    image = down1;}
                if(spriteNum == 2){
                    image = down2;}
                if(spriteNum == 3){
                    image = down3;}
            }
            case "left" -> {
                if(spriteNum == 1){
                    image = left1;}
                if(spriteNum == 2){
                    image = left2;}
                if(spriteNum == 3){
                    image = left3;}
            }
            case "right" -> {
                if(spriteNum == 1){
                    image = right1;}
                if(spriteNum == 2){
                    image = right2;}
                if(spriteNum == 3){
                    image = right3;}
            }
        }
        g2.drawImage(image, screenx, screeny, gp.tileSize, gp.tileSize, null);

    }

    public void useHoe() {
    // Get the tile in front of the player based on direction
    int tileX = 0;
    int tileY = 0;
    
    switch(direction) {
        case "up":
            tileX = worldx / gp.tileSize;
            tileY = (worldy - gp.tileSize) / gp.tileSize;
            break;
        case "down":
            tileX = worldx / gp.tileSize;
            tileY = (worldy + gp.tileSize) / gp.tileSize;
            break;
        case "left":
            tileX = (worldx - gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
        case "right":
            tileX = (worldx + gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
    }
    
    // Check if there's a farm plot at this position
    for(int i = 0; i < gp.farmPlots.length; i++) {
        if(gp.farmPlots[i] != null) {
            if(gp.farmPlots[i].worldx / gp.tileSize == tileX && gp.farmPlots[i].worldy / gp.tileSize == tileY) {
                // Till the soil
                gp.farmPlots[i].till();
                break;
            }
        }
    }
}

public void useWateringCan() {
    // Get the tile in front of the player based on direction
    int tileX = 0;
    int tileY = 0;
    
    switch(direction) {
        case "up":
            tileX = worldx / gp.tileSize;
            tileY = (worldy - gp.tileSize) / gp.tileSize;
            break;
        case "down":
            tileX = worldx / gp.tileSize;
            tileY = (worldy + gp.tileSize) / gp.tileSize;
            break;
        case "left":
            tileX = (worldx - gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
        case "right":
            tileX = (worldx + gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
    }
    
    // Check if there's a farm plot at this position
    for(int i = 0; i < gp.farmPlots.length; i++) {
        if(gp.farmPlots[i] != null) {
            if(gp.farmPlots[i].worldx / gp.tileSize == tileX && gp.farmPlots[i].worldy / gp.tileSize == tileY) {
                // Water the soil
                gp.farmPlots[i].water();
                break;
            }
        }
    }
}

public void plantSeed(Item seed) {
    // Get the tile in front of the player based on direction
    int tileX = 0;
    int tileY = 0;
    
    switch(direction) {
        case "up":
            tileX = worldx / gp.tileSize;
            tileY = (worldy - gp.tileSize) / gp.tileSize;
            break;
        case "down":
            tileX = worldx / gp.tileSize;
            tileY = (worldy + gp.tileSize) / gp.tileSize;
            break;
        case "left":
            tileX = (worldx - gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
        case "right":
            tileX = (worldx + gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
    }
    
    // Check if there's a farm plot at this position
    for(int i = 0; i < gp.farmPlots.length; i++) {
        if(gp.farmPlots[i] != null) {
            if(gp.farmPlots[i].worldx / gp.tileSize == tileX && gp.farmPlots[i].worldy / gp.tileSize == tileY) {
                // Plant the seed
                gp.farmPlots[i].plant(seed);
                break;
            }
        }
    }
}

public void harvestCrop() {
    // Get the tile in front of the player based on direction
    int tileX = 0;
    int tileY = 0;
    
    switch(direction) {
        case "up":
            tileX = worldx / gp.tileSize;
            tileY = (worldy - gp.tileSize) / gp.tileSize;
            break;
        case "down":
            tileX = worldx / gp.tileSize;
            tileY = (worldy + gp.tileSize) / gp.tileSize;
            break;
        case "left":
            tileX = (worldx - gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
        case "right":
            tileX = (worldx + gp.tileSize) / gp.tileSize;
            tileY = worldy / gp.tileSize;
            break;
    }
    
    // Check if there's a farm plot at this position
    for(int i = 0; i < gp.farmPlots.length; i++) {
        if(gp.farmPlots[i] != null) {
            if(gp.farmPlots[i].worldx / gp.tileSize == tileX && gp.farmPlots[i].worldy / gp.tileSize == tileY) {
                // Harvest the crop
                Item harvestedCrop = gp.farmPlots[i].harvest();
                
                // Add to inventory if we got something
                if(harvestedCrop != null) {
                    inventory.add(harvestedCrop);
                }
                break;
            }
        }
    }
}

public void attack() {
    // Simple placeholder for attack function
    System.out.println("Player attacks!");
    
    // Check for monsters in attack range
    int attackRange = gp.tileSize; // 1 tile attack range
    
    for(int i = 0; i < gp.monsters.length; i++) {
        if(gp.monsters[i] != null && gp.monsters[i].alive) {
            // Calculate distance to monster
            int monsterX = gp.monsters[i].worldx;
            int monsterY = gp.monsters[i].worldy;
            
            int diffX = Math.abs(worldx - monsterX);
            int diffY = Math.abs(worldy - monsterY);
            
            // If monster is within attack range
            if(diffX < attackRange && diffY < attackRange) {
                // Apply damage (this is simplified)
                System.out.println("Hit monster: " + gp.monsters[i].name);
                // In a more complete implementation, you'd call a method to damage the monster
            }
        }
    }
}
}
