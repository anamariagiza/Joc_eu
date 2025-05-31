package PaooGame;

import PaooGame.Maps.MapLoader;

public class CollisionDetector {
    private MapLoader mapLoader;

    public CollisionDetector() {
        this.mapLoader = new MapLoader();
    }

    /**
     * Verifică coliziunea unei entități cu tile-urile din hartă
     */
    public boolean checkCollision(float entityX, float entityY, int entityWidth, int entityHeight,
                                  MapLoader.MapData mapData) {

        // Calculează tile-urile care se intersectează cu entitatea
        int leftTile = (int) (entityX / mapData.tileWidth);
        int rightTile = (int) ((entityX + entityWidth) / mapData.tileWidth);
        int topTile = (int) (entityY / mapData.tileHeight);
        int bottomTile = (int) ((entityY + entityHeight) / mapData.tileHeight);

        // Verifică fiecare tile din zona entității
        for (int y = topTile; y <= bottomTile; y++) {
            for (int x = leftTile; x <= rightTile; x++) {
                // Verifică bounds
                if (x < 0 || x >= mapData.width || y < 0 || y >= mapData.height) {
                    return true; // Coliziune cu marginea hărții
                }

                int tileId = mapData.tiles[y][x];

                // Verifică dacă tile-ul este solid
                if (mapLoader.isSolidTile(tileId)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Verifică coliziunea între două entități (James vs Animal/Enemy)
     */
    public boolean checkEntityCollision(float x1, float y1, int w1, int h1,
                                        float x2, float y2, int w2, int h2) {
        return (x1 < x2 + w2) && (x1 + w1 > x2) &&
                (y1 < y2 + h2) && (y1 + h1 > y2);
    }

    /**
     * Verifică dacă James se află pe o capcană (pentru nivelul 2)
     */
    public boolean checkTrapCollision(float entityX, float entityY, int entityWidth, int entityHeight,
                                      MapLoader.MapData mapData) {

        int centerTileX = (int) ((entityX + entityWidth/2) / mapData.tileWidth);
        int centerTileY = (int) ((entityY + entityHeight/2) / mapData.tileHeight);

        // Verifică bounds
        if (centerTileX < 0 || centerTileX >= mapData.width ||
                centerTileY < 0 || centerTileY >= mapData.height) {
            return false;
        }

        int tileId = mapData.tiles[centerTileY][centerTileX];
        return mapLoader.isDangerousTile(tileId);
    }

    /**
     * Găsește cea mai apropiată poziție sigură
     */
    public float[] findSafePosition(float currentX, float currentY,
                                    MapLoader.MapData mapData, int entityWidth, int entityHeight) {

        // Caută în spirală pentru o poziție sigură
        for (int radius = 1; radius <= 10; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    float testX = currentX + dx * mapData.tileWidth;
                    float testY = currentY + dy * mapData.tileHeight;

                    if (!checkCollision(testX, testY, entityWidth, entityHeight, mapData)) {
                        return new float[]{testX, testY};
                    }
                }
            }
        }

        // Dacă nu găsește, returnează poziția originală
        return new float[]{currentX, currentY};
    }

    /**
     * Verifică dacă o zonă este liberă pentru spawn-ul inamicilor
     */
    public boolean isAreaFree(float x, float y, int width, int height, MapLoader.MapData mapData) {
        return !checkCollision(x, y, width, height, mapData);
    }

    /**
     * Calculează distanța între două puncte
     */
    public float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Verifică dacă există o linie liberă între două puncte (pentru AI)
     */
    public boolean hasLineOfSight(float x1, float y1, float x2, float y2, MapLoader.MapData mapData) {
        float distance = getDistance(x1, y1, x2, y2);
        int steps = (int) (distance / mapData.tileWidth);

        for (int i = 0; i <= steps; i++) {
            float t = (float) i / steps;
            float checkX = x1 + t * (x2 - x1);
            float checkY = y1 + t * (y2 - y1);

            if (checkCollision(checkX, checkY, 1, 1, mapData)) {
                return false;
            }
        }

        return true;
    }
}