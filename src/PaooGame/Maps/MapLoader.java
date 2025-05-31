package PaooGame.Maps;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapLoader {
    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;

    public class MapData {
        public int[][] tiles;
        public int width;
        public int height;
        public int tileWidth;
        public int tileHeight;

        public MapData(int[][] tiles, int width, int height, int tileW, int tileH) {
            this.tiles = tiles;
            this.width = width;
            this.height = height;
            this.tileWidth = tileW;
            this.tileHeight = tileH;
        }
    }

    /**
     * Încarcă o hartă din format JSON exportat de Tiled
     */
    public MapData loadMap(String jsonPath) {
        try {
            InputStream is = getClass().getResourceAsStream(jsonPath);
            if (is == null) {
                System.err.println("Nu pot găsi fișierul: " + jsonPath);
                return null;
            }

            Scanner scanner = new Scanner(is);
            StringBuilder jsonContent = new StringBuilder();

            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }
            scanner.close();

            String json = jsonContent.toString();

            // Extrage dimensiunile hărții
            extractMapDimensions(json);

            // Extrage datele tile-urilor
            int[][] tiles = extractTileData(json);

            return new MapData(tiles, mapWidth, mapHeight, tileWidth, tileHeight);

        } catch (Exception e) {
            System.err.println("Eroare la încărcarea hărții: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extrage dimensiunile hărții din JSON
     */
    private void extractMapDimensions(String json) {
        try {
            // Caută "width":value
            Pattern widthPattern = Pattern.compile("\"width\":(\\d+)");
            Matcher widthMatcher = widthPattern.matcher(json);
            if (widthMatcher.find()) {
                mapWidth = Integer.parseInt(widthMatcher.group(1));
            }

            // Caută "height":value
            Pattern heightPattern = Pattern.compile("\"height\":(\\d+)");
            Matcher heightMatcher = heightPattern.matcher(json);
            if (heightMatcher.find()) {
                mapHeight = Integer.parseInt(heightMatcher.group(1));
            }

            // Caută "tilewidth":value
            Pattern tileWidthPattern = Pattern.compile("\"tilewidth\":(\\d+)");
            Matcher tileWidthMatcher = tileWidthPattern.matcher(json);
            if (tileWidthMatcher.find()) {
                tileWidth = Integer.parseInt(tileWidthMatcher.group(1));
            }

            // Caută "tileheight":value
            Pattern tileHeightPattern = Pattern.compile("\"tileheight\":(\\d+)");
            Matcher tileHeightMatcher = tileHeightPattern.matcher(json);
            if (tileHeightMatcher.find()) {
                tileHeight = Integer.parseInt(tileHeightMatcher.group(1));
            }

        } catch (Exception e) {
            // Valori default dacă nu găsește
            mapWidth = 120;
            mapHeight = 40;
            tileWidth = 32;
            tileHeight = 32;
        }
    }

    /**
     * Extrage array-ul de tile-uri din JSON
     */
    private int[][] extractTileData(String json) {
        try {
            // Găsește primul layer cu "data"
            Pattern dataPattern = Pattern.compile("\"data\":\\[(.*?)\\]");
            Matcher dataMatcher = dataPattern.matcher(json);

            if (dataMatcher.find()) {
                String dataString = dataMatcher.group(1);
                String[] values = dataString.split(",");

                int[][] tiles = new int[mapHeight][mapWidth];
                int index = 0;

                for (int y = 0; y < mapHeight && index < values.length; y++) {
                    for (int x = 0; x < mapWidth && index < values.length; x++) {
                        int rawTileId = Integer.parseInt(values[index].trim());
                        tiles[y][x] = cleanTileId(rawTileId);
                        index++;
                    }
                }

                return tiles;
            }

        } catch (Exception e) {
            System.err.println("Eroare la parsarea tile-urilor: " + e.getMessage());
        }

        return new int[mapHeight][mapWidth]; // Hartă goală dacă eșuează
    }

    /**
     * Curăță ID-urile de tile-uri de flip flags din Tiled
     */
    private int cleanTileId(int rawTileId) {
        // Elimină bit flags pentru transformări (flip horizontal/vertical/diagonal)
        if (rawTileId > 100000) { // Dacă ID-ul e suspect de mare
            return rawTileId & 0x0FFFFFFF; // Păstrează doar primii 28 biți
        }
        return rawTileId;
    }

    /**
     * Verifică dacă un tile este solid (pentru coliziuni)
     */
    public boolean isSolidTile(int tileId) {
        // Definește ce tile-uri sunt solide
        // Adaptează după tileset-ul tău
        return tileId == 901 || tileId == 902 || // pereți/stânci
                (tileId >= 1001 && tileId <= 1100); // alte obstacole
    }

    /**
     * Verifică dacă un tile este periculos (capcană/animal)
     */
    public boolean isDangerousTile(int tileId) {
        // Pentru nivelul 2 (peștera) - capcane
        return tileId == 969 || tileId == 970 || tileId == 971 || tileId == 972;
    }
}