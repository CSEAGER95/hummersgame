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

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        // Add boundary checks
        if(entityLeftCol < 0) entityLeftCol = 0;
        if(entityRightCol >= gp.maxWorldCol) entityRightCol = gp.maxWorldCol - 1;
        if(entityTopRow < 0) entityTopRow = 0;
        if(entityBottomRow >= gp.maxWorldRow) entityBottomRow = gp.maxWorldRow - 1;

        int tileNum1, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                // Boundary check
                if(entityTopRow < 0) entityTopRow = 0;
                
                // Access tiles safely
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                
                // Ensure tile indices are valid
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   tileNum1 >= 0 && tileNum2 >= 0 &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
                
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                // Boundary check
                if(entityBottomRow >= gp.maxWorldRow) entityBottomRow = gp.maxWorldRow - 1;
                
                // Access tiles safely
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                
                // Ensure tile indices are valid
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   tileNum1 >= 0 && tileNum2 >= 0 &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
                
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                // Boundary check
                if(entityLeftCol < 0) entityLeftCol = 0;
                
                // Access tiles safely
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                
                // Ensure tile indices are valid
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   tileNum1 >= 0 && tileNum2 >= 0 &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
                break;
                
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                // Boundary check
                if(entityRightCol >= gp.maxWorldCol) entityRightCol = gp.maxWorldCol - 1;
                
                // Access tiles safely
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                
                // Ensure tile indices are valid
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   tileNum1 >= 0 && tileNum2 >= 0 &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
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
        
        if(gp.player != null) {
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
        }
        
        return index;
    }
}