package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;
    
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity){
        int entityLeftWorldX = entity.worldx + entity.solidArea.x;
        int entityRightWorldX = entity.worldx + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldy + entity.solidArea.y;
        int entityBottomWorldY = entity.worldy + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }
    
    // Check entity collision with another entity (NPCs, monsters, etc.)
    public int checkEntity(Entity entity, Entity[] targets) {
        int index = 999;
        
        for(int i = 0; i < targets.length; i++) {
            
            if(targets[i] != null) {
                
                // Get entity's solid area position
                entity.solidArea.x = entity.worldx + entity.solidArea.x;
                entity.solidArea.y = entity.worldy + entity.solidArea.y;
                
                // Get the target's solid area position
                targets[i].solidArea.x = targets[i].worldx + targets[i].solidArea.x;
                targets[i].solidArea.y = targets[i].worldy + targets[i].solidArea.y;
                
                switch(entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(targets[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(targets[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(targets[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(targets[i].solidArea)) {
                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                }
                
                // Reset solid area positions
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                targets[i].solidArea.x = targets[i].solidAreaDefaultX;
                targets[i].solidArea.y = targets[i].solidAreaDefaultY;
            }
        }
        
        return index;
    }
    
    // Check if player is colliding with an NPC
    public int checkPlayer(Entity entity) {
        int index = 999;
        
        // Get entity's solid area position
        entity.solidArea.x = entity.worldx + entity.solidArea.x;
        entity.solidArea.y = entity.worldy + entity.solidArea.y;
        
        // Get the player's solid area position
        gp.player.solidArea.x = gp.player.worldx + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldy + gp.player.solidArea.y;
        
        switch(entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                if(entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    index = 0;
                }
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                if(entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    index = 0;
                }
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                if(entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    index = 0;
                }
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                if(entity.solidArea.intersects(gp.player.solidArea)) {
                    entity.collisionOn = true;
                    index = 0;
                }
                break;
        }
        
        // Reset solid area positions
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        
        return index;
    }
}