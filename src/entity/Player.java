package entity;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Player(GamePanel gp, KeyHandler keyH) throws IOException {

        this.gp = gp;
        this.keyH = keyH;

        getPlayerImage();
        setDefaultValues();


    }

    public void setDefaultValues(){

        x = 100;
        y = 100;
        speed = 4;
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
        if(keyH.upPressed == true){
            y -= speed;
            direction = "up";
        }

        else if(keyH.downPressed == true){
            y += speed;
            direction = "down";
        }

        else if(keyH.leftPressed == true){
            x -= speed;
            direction = "left";
        }

        if(keyH.rightPressed == true){
            x+= speed;
            direction = "right";
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch(direction) {
            case "up" -> image = up1;
            case "down" -> image = down1;
            case "left" -> image = left1;
            case "right" -> image = right1;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

    }
}
