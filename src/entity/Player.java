package entity;


import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenx;
    public final int screeny;

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
    public void update() {
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if(keyH.upPressed) {
                direction = "up";
            }
            else if(keyH.downPressed) {
                direction = "down";
            }
            else if(keyH.leftPressed) {
                direction = "left";
            }
            else if(keyH.rightPressed) {
                direction = "right";
            }
            
            // Check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // If collision is false, player can move
            if(!collisionOn) {
                switch(direction) {
                    case "up":
                        worldy -= speed;
                        break;
                    case "down":
                        worldy += speed;
                        break;
                    case "left":
                        worldx -= speed;
                        break;
                    case "right":
                        worldx += speed;
                        break;
                }
            }
    
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
}
