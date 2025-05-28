package PaooGame.Maps;

import PaooGame.RefLinks;
import PaooGame.Tiles.Tile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa ce reprezintă o hartă de joc încărcată din fișier .tmx (Tiled).
 * Această versiune suportă multiple layer-uri care se desenează unul peste altul.
 */
public class Map {
    private RefLinks refLink;
    private int width;
    private int height;

    // Lista de layer-uri (fiecare layer are propria sa matrice de tile-uri)
    private List<MapLayer> layers;
    private boolean mapLoaded = false;

    /**
     * Clasa internă pentru a reprezenta un layer al hărții
     */
    private static class MapLayer {
        String name;
        int[][] tiles;
        boolean visible;

        public MapLayer(String name, int width, int height) {
            this.name = name;
            this.tiles = new int[width][height];
            this.visible = true;
        }
    }

    /**
     * Constructor ce primește referința principală și încarcă harta TMX cu toate layer-urile.
     */
    public Map(RefLinks refLink) {
        this.refLink = refLink;
        this.layers = new ArrayList<>();

        // Încearcă să încarce harta TMX cu toate layer-urile
        try {
            LoadWorldFromTMX("res\\Mapa\\The_map.tmx");
            mapLoaded = true;
            System.out.println("✓ SUCCES! Harta TMX încărcată corect: " + width + "x" + height + " tile-uri");
            System.out.println("✓ Layer-uri încărcate: " + layers.size());
        } catch (Exception e) {
            mapLoaded = false;
            System.out.println("EROARE CRITICĂ: Harta TMX nu s-a putut încărca!");
            System.out.println("Motivul: " + e.getMessage());
            System.out.println("Stack trace complet:");
            e.printStackTrace();
            System.out.println("\nVerifică următoarele:");
            System.out.println("1. Fișierul res/Mapa/The_map.tmx există?");
            System.out.println("2. Fișierul este un TMX valid exportat din Tiled?");
            System.out.println("3. Datele sunt în format CSV?");
            System.out.println("4. Calea către fișier este corectă?");
        }
    }

    public void Update() {
        // actualizări ale hărții, dacă este cazul
    }

    /**
     * Desenează harta pe ecran cu toate layer-urile în ordine.
     */
    public void Draw(Graphics g) {
        // Dacă harta nu s-a încărcat, afișează un mesaj de eroare vizual
        if (!mapLoaded || layers.isEmpty() || width <= 0 || height <= 0) {
            // Desenează un fundal roșu pentru a indica eroarea
            g.setColor(Color.RED);
            g.fillRect(0, 0, refLink.GetGame().GetWidth(), refLink.GetGame().GetHeight());

            // Afișează mesajul de eroare pe ecran
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("EROARE: Harta TMX nu s-a încărcat!", 50, 100);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Verifică consola pentru detalii despre eroare.", 50, 140);
            g.drawString("Fișierul res/Mapa/The_map.tmx trebuie să existe și să fie valid.", 50, 170);
            return;
        }

        // Calculează câte dale încap pe ecran
        int tilesX = (refLink.GetGame().GetWidth() / Tile.TILE_WIDTH) + 1;
        int tilesY = (refLink.GetGame().GetHeight() / Tile.TILE_HEIGHT) + 1;

        // Limitează la dimensiunile hărții
        tilesX = Math.min(tilesX, width);
        tilesY = Math.min(tilesY, height);

        // Desenează fiecare layer în ordine (primul layer e la fund, ultimul e deasupra)
        for (MapLayer layer : layers) {
            if (!layer.visible) continue; // Skip layer-urile invizibile

            DrawLayer(g, layer, tilesX, tilesY);
        }

        // Afișează informații despre hartă în colțul stâng-sus pentru confirmare
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Hartă TMX: " + width + "x" + height + " (" + layers.size() + " layer-uri)", 10, 20);

        // Afișează numele layer-urilor
        for (int i = 0; i < layers.size(); i++) {
            MapLayer layer = layers.get(i);
            g.drawString("Layer " + (i + 1) + ": " + layer.name + (layer.visible ? "" : " (ascuns)"), 10, 40 + i * 15);
        }
    }

    /**
     * Desenează un singur layer
     */
    private void DrawLayer(Graphics g, MapLayer layer, int tilesX, int tilesY) {
        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                int tileId = layer.tiles[x][y];

                // Doar desenează tile-uri care nu sunt goale (ID 0 în multe cazuri înseamnă gol)
                if (tileId > 0) {
                    Tile tile = GetTileById(tileId);
                    if (tile != null) {
                        tile.Draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
                    }
                }
            }
        }
    }

    /**
     * Returnează dala de pe primul layer la poziția specificată (pentru compatibilitate).
     */
    public Tile GetTile(int x, int y) {
        return GetTile(x, y, 0); // primul layer
    }

    /**
     * Returnează dala de pe layer-ul specificat la poziția specificată.
     */
    public Tile GetTile(int x, int y, int layerIndex) {
        if (!mapLoaded || layers.isEmpty()) {
            throw new IllegalStateException("Harta nu este încărcată, nu pot returna tile.");
        }

        if (layerIndex < 0 || layerIndex >= layers.size()) {
            throw new IndexOutOfBoundsException("Layer-ul " + layerIndex + " nu există. Layer-uri disponibile: 0-" + (layers.size() - 1));
        }

        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IndexOutOfBoundsException("Tile [" + x + "," + y + "] este în afara hărții.");
        }

        int tileId = layers.get(layerIndex).tiles[x][y];
        return GetTileById(tileId);
    }

    /**
     * Returnează tile-ul pe baza ID-ului
     */
    private Tile GetTileById(int tileId) {
        if (tileId <= 0) {
            return null; // Tile gol sau invalid
        }

        if (tileId >= Tile.tiles.length) {
            throw new IllegalArgumentException("ID invalid de tile: " + tileId);
        }

        Tile tile = Tile.tiles[tileId];
        if (tile == null) {
            throw new IllegalStateException("Tile-ul cu ID " + tileId + " este null");
        }

        return tile;
    }

    /**
     * Încarcă harta dintr-un fișier .tmx cu toate layer-urile în format CSV.
     */
    private void LoadWorldFromTMX(String path) throws Exception {
        System.out.println("🔄 Începe încărcarea hărții TMX din: " + path);

        // Verifică existența fișierului
        File file = new File(path);
        System.out.println("📁 Calea absolută către fișier: " + file.getAbsolutePath());

        if (!file.exists()) {
            throw new Exception("Fișierul TMX nu există la calea: " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new Exception("Fișierul TMX nu poate fi citit. Verifică permisiunile.");
        }

        System.out.println("✓ Fișierul TMX găsit și poate fi citit");

        // Parsează XML-ul
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        Element mapElement = doc.getDocumentElement();
        System.out.println("📋 Element root găsit: " + mapElement.getNodeName());

        // Verifică dacă elementul root este 'map'
        if (!"map".equals(mapElement.getNodeName())) {
            throw new Exception("Fișierul nu este un TMX valid. Elementul root este '" +
                    mapElement.getNodeName() + "' în loc de 'map'");
        }

        // Extrage dimensiunile hărții
        String widthAttr = mapElement.getAttribute("width");
        String heightAttr = mapElement.getAttribute("height");

        if (widthAttr.isEmpty() || heightAttr.isEmpty()) {
            throw new Exception("Atributele 'width' și 'height' lipsesc din elementul 'map'");
        }

        width = Integer.parseInt(widthAttr);
        height = Integer.parseInt(heightAttr);

        System.out.println("📏 Dimensiuni hartă extrase: " + width + " x " + height + " tile-uri");

        if (width <= 0 || height <= 0) {
            throw new Exception("Dimensiunile hărții sunt invalide: " + width + "x" + height);
        }

        // Găsește toate layer-urile
        NodeList layerList = doc.getElementsByTagName("layer");
        System.out.println("🎭 Numărul de layer-uri găsite: " + layerList.getLength());

        if (layerList.getLength() == 0) {
            throw new Exception("Nu s-au găsit layer-uri în fișierul TMX");
        }

        // Procesează fiecare layer
        for (int layerIndex = 0; layerIndex < layerList.getLength(); layerIndex++) {
            Element layer = (Element) layerList.item(layerIndex);
            String layerName = layer.getAttribute("name");
            String visibleAttr = layer.getAttribute("visible");
            boolean visible = visibleAttr.isEmpty() || !"0".equals(visibleAttr);

            System.out.println("🎯 Se procesează layer-ul " + (layerIndex + 1) + ": '" + layerName + "' (vizibil: " + visible + ")");

            // Creează un nou layer
            MapLayer mapLayer = new MapLayer(layerName, width, height);
            mapLayer.visible = visible;

            // Găsește datele din layer
            NodeList dataList = layer.getElementsByTagName("data");
            if (dataList.getLength() == 0) {
                System.out.println("⚠️ Layer-ul '" + layerName + "' nu are date, se sare");
                continue;
            }

            Element data = (Element) dataList.item(0);
            String encoding = data.getAttribute("encoding");
            String compression = data.getAttribute("compression");

            System.out.println("💾 Encoding date pentru '" + layerName + "': '" + (encoding.isEmpty() ? "none" : encoding) + "'");

            // Verifică encoding-ul
            if (!encoding.isEmpty() && !"csv".equals(encoding)) {
                throw new Exception("Encoding '" + encoding + "' nu este suportat pentru layer-ul '" + layerName + "'. Folosește CSV în Tiled.");
            }

            if (!compression.isEmpty()) {
                throw new Exception("Compresia '" + compression + "' nu este suportată pentru layer-ul '" + layerName + "'. Dezactivează compresia în Tiled.");
            }

            // Extrage și procesează datele CSV
            String csvData = data.getTextContent().trim();
            if (csvData.isEmpty()) {
                System.out.println("⚠️ Layer-ul '" + layerName + "' are date goale, se umple cu 0");
                // Layer-ul rămâne cu toate tile-urile 0 (goale)
            } else {
                // Procesează datele CSV
                csvData = csvData.replaceAll("\\s+", "");
                String[] tileIds = csvData.split(",");

                if (tileIds.length != width * height) {
                    throw new Exception("Nepotrivire în numărul de tile-uri pentru layer-ul '" + layerName + "'! Găsite: " + tileIds.length +
                            ", așteptate: " + (width * height));
                }

                // Populează matricea layer-ului
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int index = y * width + x;
                        try {
                            int id = Integer.parseInt(tileIds[index].trim());
                            mapLayer.tiles[x][y] = id;
                        } catch (NumberFormatException e) {
                            throw new Exception("ID de tile invalid în layer-ul '" + layerName + "' la poziția [" + x + "," + y +
                                    "] (index " + index + "): '" + tileIds[index] + "'");
                        }
                    }
                }
            }

            // Adaugă layer-ul la listă
            layers.add(mapLayer);
            System.out.println("✅ Layer-ul '" + layerName + "' procesat cu succes");
        }

        System.out.println("🎉 Harta TMX cu " + layers.size() + " layer-uri a fost încărcată complet!");
    }

    /**
     * Ascunde sau afișează un layer
     */
    public void SetLayerVisible(int layerIndex, boolean visible) {
        if (layerIndex >= 0 && layerIndex < layers.size()) {
            layers.get(layerIndex).visible = visible;
            System.out.println("Layer " + layerIndex + " (" + layers.get(layerIndex).name + ") " +
                    (visible ? "afișat" : "ascuns"));
        }
    }

    /**
     * Ascunde sau afișează un layer pe baza numelui
     */
    public void SetLayerVisible(String layerName, boolean visible) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).name.equals(layerName)) {
                SetLayerVisible(i, visible);
                return;
            }
        }
        System.out.println("⚠️ Layer-ul cu numele '" + layerName + "' nu a fost găsit");
    }

    /**
     * Returnează numărul de layer-uri
     */
    public int getLayerCount() {
        return layers.size();
    }

    /**
     * Returnează numele unui layer
     */
    public String getLayerName(int layerIndex) {
        if (layerIndex >= 0 && layerIndex < layers.size()) {
            return layers.get(layerIndex).name;
        }
        return null;
    }

    /**
     * Returnează true dacă harta s-a încărcat cu succes
     */
    public boolean isMapLoaded() {
        return mapLoaded;
    }

    /**
     * Returnează dimensiunile hărții pentru debugging
     */
    public String getMapInfo() {
        if (mapLoaded) {
            return "Hartă TMX: " + width + "x" + height + " tile-uri, " + layers.size() + " layer-uri";
        } else {
            return "Harta TMX nu este încărcată";
        }
    }
}