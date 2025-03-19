package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.GamePanel;

public class NPC extends Entity {
    
    GamePanel gp;
    public ArrayList<Item> inventory = new ArrayList<>();
    
    // NPC types
    public static final int MERCHANT = 0;
    public static final int SEED_VENDOR = 1;
    public static final int VILLAGER = 2;
    
    public int npcType;
    
    public NPC(GamePanel gp, int npcType) {
        this.gp = gp;
        this.type = TYPE_NPC;
        this.npcType = npcType;
        
        direction = "down";
        speed = 1;
        
        // Set default NPC collision area
        solidArea = new java.awt.Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setNPCImages();
        setDialogue();
        setItems();
    }
    
    public void setNPCImages() {
        try {
            String prefix = "";
            
            switch(npcType) {
                case MERCHANT:
                    prefix = "/npc/merchant";
                    break;
                case SEED_VENDOR:
                    prefix = "/npc/seedvendor";
                    break;
                case VILLAGER:
                    prefix = "/npc/villager";
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
    
    public void setDialogue() {
        switch(npcType) {
            case MERCHANT:
                dialogues[0] = "Welcome to my shop! What would you like to buy?";
                dialogues[1] = "Thank you for your purchase!";
                dialogues[2] = "Come again soon!";
                break;
            case SEED_VENDOR:
                dialogues[0] = "Fresh seeds! Get your fresh seeds here!";
                dialogues[1] = "These seeds will grow into wonderful crops!";
                dialogues[2] = "Plant them in good soil and water them daily!";
                break;
            case VILLAGER:
                dialogues[0] = "Hello there!";
                dialogues[1] = "Beautiful day, isn't it?";
                dialogues[2] = "I hear the dungeon to the north is dangerous...";
                break;
        }
    }
    
    public void setItems() {
        switch(npcType) {
            case MERCHANT:
                addItem("Potion", 50, Item.TYPE_CONSUMABLE);
                addItem("Sword", 150, Item.TYPE_WEAPON);
                addItem("Shield", 100, Item.TYPE_ARMOR);
                break;
            case SEED_VENDOR:
                addSeed("Carrot Seed", 20, 300, 60);
                addSeed("Tomato Seed", 25, 450, 75);
                addSeed("Potato Seed", 30, 600, 100);
                break;
            case VILLAGER:
                // Villagers don't sell items
                break;
        }
    }
    
    private void addItem(String name, int price, int type) {
        Item item = new Item(gp);
        item.setItemValues(type, name, price, true);
        
        // You would set the appropriate image here
        try {
            item.down1 = ImageIO.read(getClass().getResourceAsStream("/items/" + name.toLowerCase().replace(" ", "") + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        inventory.add(item);
    }
    
    private void addSeed(String name, int price, int growTime, int harvestValue) {
        Item seed = new Item(gp);
        seed.setSeedValues(name, price, growTime, harvestValue);
        
        // You would set the appropriate image here
        try {
            seed.down1 = ImageIO.read(getClass().getResourceAsStream("/items/" + name.toLowerCase().replace(" ", "") + ".png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        inventory.add(seed);
    }
    
    public void speak() {
        // Display a random dialogue
        super.speak();
    }
    
    @Override
    public void setAction() {
        actionLockCounter++;
        
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // 1-100
            
            if(i <= 25) {
                direction = "up";
            }
            else if(i <= 50) {
                direction = "down";
            }
            else if(i <= 75) {
                direction = "left";
            }
            else {
                direction = "right";
            }
            
            actionLockCounter = 0;
        }
    }
    
    public void sell(Item item, Player player) {
        if(player.coin >= item.price) {
            player.coin -= item.price;
            // Add the item to player's inventory
            player.inventory.add(item);
            speak();
        }
        else {
            dialogues[0] = "You don't have enough coins!";
            speak();
        }
    }
    
    public void buy(Item item, Player player) {
        if(player.inventory.contains(item)) {
            player.coin += item.sellPrice;
            player.inventory.remove(item);
            speak();
        }
    }
}