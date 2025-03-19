package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class FarmPlot extends Entity {
    
    GamePanel gp;
    
    // Farm plot states
    public static final int EMPTY = 0;
    public static final int TILLED = 1;
    public static final int WATERED = 2;
    
    // Growth images for different crops
    private BufferedImage[] carrotStages = new BufferedImage[4]; // seed, sprout, growing, ready
    private BufferedImage[] tomatoStages = new BufferedImage[4];
    private BufferedImage[] potatoStages = new BufferedImage[4];
    
    // Farm plot state
    public int state;
    public boolean needsWater;
    private int waterTimer;
    
    public FarmPlot(GamePanel gp, int col, int row) {
        this.gp = gp;
        this.type = TYPE_FARM_PLOT;
        
        // Set position
        worldx = gp.tileSize * col;
        worldy = gp.tileSize * row;
        
        // Set collision area
        solidArea = new java.awt.Rectangle(0, 0, 48, 48);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        // Initialize state
        state = EMPTY;
        planted = false;
        needsWater = false;
        
        // Load images
        getFarmPlotImages();
        getGrowthStageImages();
    }
    
    private void getFarmPlotImages() {
        try {
            // Load farm plot state images
            up1 = ImageIO.read(getClass().getResourceAsStream("/farm/plot_empty.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/farm/plot_tilled.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/farm/plot_watered.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void getGrowthStageImages() {
        try {
            // Load carrot growth stages
            carrotStages[0] = ImageIO.read(getClass().getResourceAsStream("/farm/carrot_seed.png"));
            carrotStages[1] = ImageIO.read(getClass().getResourceAsStream("/farm/carrot_sprout.png"));
            carrotStages[2] = ImageIO.read(getClass().getResourceAsStream("/farm/carrot_growing.png"));
            carrotStages[3] = ImageIO.read(getClass().getResourceAsStream("/farm/carrot_ready.png"));
            
            // Load tomato growth stages
            tomatoStages[0] = ImageIO.read(getClass().getResourceAsStream("/farm/tomato_seed.png"));
            tomatoStages[1] = ImageIO.read(getClass().getResourceAsStream("/farm/tomato_sprout.png"));
            tomatoStages[2] = ImageIO.read(getClass().getResourceAsStream("/farm/tomato_growing.png"));
            tomatoStages[3] = ImageIO.read(getClass().getResourceAsStream("/farm/tomato_ready.png"));
            
            // Load potato growth stages
            potatoStages[0] = ImageIO.read(getClass().getResourceAsStream("/farm/potato_seed.png"));
            potatoStages[1] = ImageIO.read(getClass().getResourceAsStream("/farm/potato_sprout.png"));
            potatoStages[2] = ImageIO.read(getClass().getResourceAsStream("/farm/potato_growing.png"));
            potatoStages[3] = ImageIO.read(getClass().getResourceAsStream("/farm/potato_ready.png"));
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void till() {
        if(state == EMPTY) {
            state = TILLED;
        }
    }
    
    public void water() {
        if(state == TILLED) {
            state = WATERED;
            needsWater = false;
            waterTimer = 0;
        }
    }
    
    @Override
    public void plant(Item seed) {
        if(state == WATERED && !planted) {
            planted = true;
            growthStage = 0;
            growthTimer = 0;
            crop = seed;
            // Link the seed to the appropriate crop type
            if(seed.name.equals("Carrot Seed")) {
                crop = seed.getCropFromSeed();
            }
            else if(seed.name.equals("Tomato Seed")) {
                crop = seed.getCropFromSeed();
            }
            else if(seed.name.equals("Potato Seed")) {
                crop = seed.getCropFromSeed();
            }
        }
    }
    
    @Override
    public void update() {
        if(state == WATERED) {
            waterTimer++;
            
            // Water dries up after a day (1440 game ticks at 60 FPS = 24 minutes real time)
            if(waterTimer >= 1440) {
                state = TILLED;
                needsWater = true;
                waterTimer = 0;
                // If plant needs water, it won't grow
            }
        }
        
        if(planted && state == WATERED) {
            growthTimer++;
            int growthTime = crop.growTime / maxGrowthStage;
            
            // Progress to next growth stage
            if(growthTimer >= growthTime && growthStage < maxGrowthStage - 1) {
                growthStage++;
                growthTimer = 0;
            }
        }
    }
    
    @Override
    public Item harvest() {
        if(planted && growthStage == maxGrowthStage - 1) {
            Item harvestedCrop = crop;
            planted = false;
            growthStage = 0;
            growthTimer = 0;
            state = TILLED; // Reset to tilled state after harvest
            return harvestedCrop;
        }
        return null;
    }
    
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Calculate screen position
        int screenX = worldx - gp.player.worldx + gp.player.screenx;
        int screenY = worldy - gp.player.worldy + gp.player.screeny;
        
        // Only draw if within screen bounds
        if(worldx + gp.tileSize > gp.player.worldx - gp.player.screenx && 
           worldx - gp.tileSize < gp.player.worldx + gp.player.screenx && 
           worldy + gp.tileSize > gp.player.worldy - gp.player.screeny && 
           worldy - gp.tileSize < gp.player.worldy + gp.player.screeny) {
            
            // Draw farm plot based on state
            BufferedImage plotImage = null;
            switch(state) {
                case EMPTY: plotImage = up1; break;
                case TILLED: plotImage = up2; break;
                case WATERED: plotImage = up3; break;
            }
            
            // Draw the plot first
            g2.drawImage(plotImage, screenX, screenY, gp.tileSize, gp.tileSize, null);
            
            // If planted, draw the crop at its current growth stage
            if(planted) {
                BufferedImage cropImage = null;
                
                // Select the right crop type image for the growth stage
                if(crop.name.equals("Carrot")) {
                    cropImage = carrotStages[growthStage];
                }
                else if(crop.name.equals("Tomato")) {
                    cropImage = tomatoStages[growthStage];
                }
                else if(crop.name.equals("Potato")) {
                    cropImage = potatoStages[growthStage];
                }
                
                if(cropImage != null) {
                    g2.drawImage(cropImage, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
            }
        }
    }
}