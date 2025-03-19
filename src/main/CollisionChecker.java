package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;
    
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

public void checkTile(Entity entity) {
    // Calculate entity's position
    int entityLeftWorldX = entity.worldx + entity.solidArea.x;
    int entityRightWorldX = entity.worldx + entity.solidArea.x + entity.solidArea.width;
    int entityTopWorldY = entity.worldy + entity.solidArea.y;
    int entityBottomWorldY = entity.worldy + entity.solidArea.y + entity.solidArea.height;

    // Calculate column and row of the entity in the tile map
    int entityLeftCol = entityLeftWorldX / gp.tileSize;
    int entityRightCol = entityRightWorldX / gp.tileSize;
    int entityTopRow = entityTopWorldY / gp.tileSize;
    int entityBottomRow = entityBottomWorldY / gp.tileSize;

    // Variables to store the tile numbers
    int tileNum1 = 0, tileNum2 = 0;
    
    // The modified positions based on direction
    int entityNextRow = 0, entityNextCol = 0;

    switch(entity.direction) {
        case "up":
            // When moving up, check if the next position will hit a wall
            entityNextRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            
            // Check if the calculated position is valid
            if(entityNextRow >= 0 && entityLeftCol >= 0 && entityRightCol < gp.maxWorldCol) {
                // Check left and right top corners
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityNextRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityNextRow];
                
                // Check if either tile has collision
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
            } else {
                // Outside map boundary, set collision
                entity.collisionOn = true;
            }
            break;
            
        case "down":
            // When moving down, check if the next position will hit a wall
            entityNextRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            
            if(entityNextRow < gp.maxWorldRow && entityLeftCol >= 0 && entityRightCol < gp.maxWorldCol) {
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityNextRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityNextRow];
                
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
            } else {
                entity.collisionOn = true;
            }
            break;
            
        case "left":
            // When moving left, check if the next position will hit a wall
            entityNextCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            
            if(entityNextCol >= 0 && entityTopRow >= 0 && entityBottomRow < gp.maxWorldRow) {
                tileNum1 = gp.tileM.mapTileNum[entityNextCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityNextCol][entityBottomRow];
                
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
            } else {
                entity.collisionOn = true;
            }
            break;
            
        case "right":
            // When moving right, check if the next position will hit a wall
            entityNextCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            
            if(entityNextCol < gp.maxWorldCol && entityTopRow >= 0 && entityBottomRow < gp.maxWorldRow) {
                tileNum1 = gp.tileM.mapTileNum[entityNextCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityNextCol][entityBottomRow];
                
                if(tileNum1 < gp.tileM.tile.length && tileNum2 < gp.tileM.tile.length &&
                   gp.tileM.tile[tileNum1] != null && gp.tileM.tile[tileNum2] != null) {
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        entity.collisionOn = true;
                    }
                }
            } else {
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