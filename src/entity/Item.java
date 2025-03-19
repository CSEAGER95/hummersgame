package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Item extends Entity {
    
    // Item types
    public static final int TYPE_CONSUMABLE = 0;
    public static final int TYPE_WEAPON = 1;
    public static final int TYPE_ARMOR = 2;
    public static final int TYPE_SEED = 3;
    public static final int TYPE_CROP = 4;
    
    public int itemType;
    public boolean stackable = false;
    public int price;
    public int sellPrice;
    
    // For farm items
    public int growTime; // Time required to grow (for seeds)
    public int harvestValue; // Value of harvested crop
    
    public Item(GamePanel gp) {
        super();
        this.solidArea.x = 0;
        this.solidArea.y = 0;
        this.solidArea.width = 48;
        this.solidArea.height = 48;
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
    }
    
    public void setItemValues(int itemType, String name, int price, boolean stackable) {
        this.itemType = itemType;
        this.name = name;
        this.price = price;
        this.sellPrice = price / 2;
        this.stackable = stackable;
    }
    
    public void setSeedValues(String name, int price, int growTime, int harvestValue) {
        setItemValues(TYPE_SEED, name, price, true);
        this.growTime = growTime;
        this.harvestValue = harvestValue;
    }
    
    public void setCropValues(String name, int price) {
        setItemValues(TYPE_CROP, name, price, true);
    }
    
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // For world rendering
        int screenX = worldx - gp.player.worldx + gp.player.screenx;
        int screenY = worldy - gp.player.worldy + gp.player.screeny;
        
        if(worldx + gp.tileSize > gp.player.worldx - gp.player.screenx && 
           worldx - gp.tileSize < gp.player.worldx + gp.player.screenx && 
           worldy + gp.tileSize > gp.player.worldy - gp.player.screeny && 
           worldy - gp.tileSize < gp.player.worldy + gp.player.screeny) {
            
            g2.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
            
            // Draw amount if stackable and amount > 1
            if(stackable && amount > 1) {
                g2.drawString(String.valueOf(amount), screenX + 24, screenY + 24);
            }
        }
    }
    
    public Item getCropFromSeed() {
        // Factory method to create corresponding crop for this seed
        if(this.itemType == TYPE_SEED) {
            Item crop = new Item(null);
            
            // Create the appropriate crop based on seed type
            if(this.name.equals("Carrot Seed")) {
                crop.setCropValues("Carrot", harvestValue);
                // Set the image for the carrot...
            }
            else if(this.name.equals("Tomato Seed")) {
                crop.setCropValues("Tomato", harvestValue);
                // Set the image for the tomato...
            }
            // Add more crop types as needed
            
            return crop;
        }
        
        return null;
    }
    
    public boolean use() {
        // Define what happens when item is used
        // Return true if item is consumed
        return false;
    }
}
