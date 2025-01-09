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
        int entityBottomWorldY = entity.worldy + entity.worldy + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX /gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum, tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/ gp.tileSize;
                tileNum = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY - entity.speed)/ gp.tileSize;

                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                break;
            case "right":
                entityRightCol = (entityRightWorldX - entity.speed) / gp.tileSize;

                break;
        }
    }
}