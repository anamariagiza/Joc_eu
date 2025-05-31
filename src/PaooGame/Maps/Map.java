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

/*! \class Map
    \brief Clasa ce reprezinta o harta de joc incarcata din fisier .tmx (Tiled).
    Aceasta versiune suporta multiple layer-uri si cai personalizate pentru fisiere.
 */
public class Map {
    private RefLinks refLink;
    private int width;
    private int height;
    private String mapPath;

    // Lista de layer-uri (fiecare layer are propria sa matrice de tile-uri)
    private List<MapLayer> layers;
    private boolean mapLoaded = false;

    /*! \class MapLayer
        \brief Clasa interna pentru a reprezenta un layer al hartii
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

    /*! \fn public Map(RefLinks refLink)
        \brief Constructor ce primeste referinta principala si incarca harta TMX implicita.

        \param refLink Referinta catre obiectul shortcut
     */
    public Map(RefLinks refLink) {
        this(refLink, "res\\Mapa\\The_map.tmx");
    }

    /*! \fn public Map(RefLinks refLink, String customPath)
        \brief Constructor ce primeste referinta principala si incarca harta TMX de la calea specificata.

        \param refLink Referinta catre obiectul shortcut
        \param customPath Calea catre fisierul TMX de incarcat
     */
    public Map(RefLinks refLink, String customPath) {
        this.refLink = refLink;
        this.mapPath = customPath;
        this.layers = new ArrayList<>();

        System.out.println("=== INCARCAREA HARTII ===");
        System.out.println("Cale fisier: " + customPath);

        // Incearca sa incarce harta TMX cu toate layer-urile
        try {
            LoadWorldFromTMX(mapPath);
            mapLoaded = true;
            System.out.println("SUCCES! Harta TMX incarcata corect din: " + mapPath);
            System.out.println("Dimensiuni: " + width + "x" + height + " tile-uri");
            System.out.println("Layer-uri incarcate: " + layers.size());
        } catch (Exception e) {
            mapLoaded = false;
            System.out.println("EROARE CRITICA: Harta TMX nu s-a putut incarca din: " + mapPath);
            System.out.println("Motivul: " + e.getMessage());
            System.out.println("Stack trace complet:");
            e.printStackTrace();
            System.out.println("\nVerifica urmatoarele:");
            System.out.println("1. Fisierul " + mapPath + " exista?");
            System.out.println("2. Fisierul este un TMX valid exportat din Tiled?");
            System.out.println("3. Datele sunt in format CSV?");
            System.out.println("4. Calea catre fisier este corecta?");
        }
        System.out.println("========================");
    }

    /*! \fn public void Update()
        \brief Actualizeaza starea hartii
     */
    public void Update() {
        // actualizari ale hartii, daca este cazul
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza harta pe ecran cu toate layer-urile in ordine.

        \param g Contextul grafic in care sa se deseneze harta
     */
    public void Draw(Graphics g) {
        // Daca harta nu s-a incarcat, afiseaza un mesaj de eroare vizual
        if (!mapLoaded || layers.isEmpty() || width <= 0 || height <= 0) {
            drawErrorScreen(g);
            return;
        }

        // Calculeaza cate dale incap pe ecran
        int tilesX = (refLink.GetGame().GetWidth() / Tile.TILE_WIDTH) + 1;
        int tilesY = (refLink.GetGame().GetHeight() / Tile.TILE_HEIGHT) + 1;

        // Limiteaza la dimensiunile hartii
        tilesX = Math.min(tilesX, width);
        tilesY = Math.min(tilesY, height);

        // Deseneaza fiecare layer in ordine (primul layer e la fund, ultimul e deasupra)
        for (MapLayer layer : layers) {
            if (!layer.visible) continue; // Skip layer-urile invizibile

            DrawLayer(g, layer, tilesX, tilesY);
        }

        // Afiseaza informatii despre harta in coltul stanga-sus pentru confirmare
        drawMapInfo(g);
    }

    /*! \fn private void drawErrorScreen(Graphics g)
        \brief Deseneaza ecranul de eroare cand harta nu se incarca

        \param g Contextul grafic
     */
    private void drawErrorScreen(Graphics g)
    {
        // Deseneaza un fundal rosu pentru a indica eroarea
        g.setColor(Color.RED);
        g.fillRect(0, 0, refLink.GetGame().GetWidth(), refLink.GetGame().GetHeight());

        // Afiseaza mesajul de eroare pe ecran
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("EROARE: Harta TMX nu s-a incarcat!", 50, 100);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Verifica consola pentru detalii despre eroare.", 50, 140);
        g.drawString("Fisierul " + mapPath + " trebuie sa existe si sa fie valid.", 50, 170);
        g.drawString("Se va folosi un fundal de rezerva.", 50, 200);

        // Informatii pentru debugging
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Cale fisier: " + mapPath, 50, 250);
        g.drawString("Layers incarcate: " + layers.size(), 50, 270);
        g.drawString("Dimensiuni: " + width + "x" + height, 50, 290);
        g.drawString("Status incarcare: " + (mapLoaded ? "SUCCES" : "EROARE"), 50, 310);
    }

    /*! \fn private void drawMapInfo(Graphics g)
        \brief Deseneaza informatii despre harta

        \param g Contextul grafic
     */
    private void drawMapInfo(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Harta TMX: " + width + "x" + height + " (" + layers.size() + " layer-uri)", 10, 20);

        // Afiseaza numele layer-urilor
        for (int i = 0; i < Math.min(layers.size(), 3); i++) { // Maxim 3 layer-uri afisate
            MapLayer layer = layers.get(i);
            g.drawString("Layer " + (i + 1) + ": " + layer.name + (layer.visible ? "" : " (ascuns)"), 10, 40 + i * 15);
        }

        if (layers.size() > 3) {
            g.drawString("... si " + (layers.size() - 3) + " layer-uri suplimentare", 10, 40 + 3 * 15);
        }
    }

    /*! \fn private void DrawLayer(Graphics g, MapLayer layer, int tilesX, int tilesY)
        \brief Deseneaza un singur layer

        \param g Contextul grafic
        \param layer Layer-ul de desenat
        \param tilesX Numarul de tile-uri pe orizontala
        \param tilesY Numarul de tile-uri pe verticala
     */
    private void DrawLayer(Graphics g, MapLayer layer, int tilesX, int tilesY) {
        for (int y = 0; y < tilesY; y++) {
            for (int x = 0; x < tilesX; x++) {
                int tileId = layer.tiles[x][y];

                // Doar deseneaza tile-uri care nu sunt goale (ID 0 in multe cazuri inseamna gol)
                if (tileId > 0) {
                    Tile tile = GetTileById(tileId);
                    if (tile != null) {
                        tile.Draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
                    }
                }
            }
        }
    }

    /*! \fn public Tile GetTile(int x, int y)
        \brief Returneaza dala de pe primul layer la pozitia specificata (pentru compatibilitate).

        \param x Coordonata X a dalei
        \param y Coordonata Y a dalei
        \return Dala de la pozitia specificata
     */
    public Tile GetTile(int x, int y) {
        return GetTile(x, y, 0); // primul layer
    }

    /*! \fn public Tile GetTile(int x, int y, int layerIndex)
        \brief Returneaza dala de pe layer-ul specificat la pozitia specificata.

        \param x Coordonata X a dalei
        \param y Coordonata Y a dalei
        \param layerIndex Indexul layer-ului
        \return Dala de la pozitia specificata
     */
    public Tile GetTile(int x, int y, int layerIndex) {
        if (!mapLoaded || layers.isEmpty()) {
            throw new IllegalStateException("Harta nu este incarcata, nu pot returna tile.");
        }

        if (layerIndex < 0 || layerIndex >= layers.size()) {
            throw new IndexOutOfBoundsException("Layer-ul " + layerIndex + " nu exista. Layer-uri disponibile: 0-" + (layers.size() - 1));
        }

        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IndexOutOfBoundsException("Tile [" + x + "," + y + "] este in afara hartii.");
        }

        int tileId = layers.get(layerIndex).tiles[x][y];
        return GetTileById(tileId);
    }

    /*! \fn private Tile GetTileById(int tileId)
        \brief Returneaza tile-ul pe baza ID-ului

        \param tileId ID-ul tile-ului
        \return Tile-ul corespunzator sau null
     */
    private Tile GetTileById(int tileId) {
        if (tileId <= 0) {
            return null; // Tile gol sau invalid
        }

        if (tileId >= Tile.tiles.length) {
            System.err.println("ATENTIE: ID invalid de tile: " + tileId + " (maxim: " + (Tile.tiles.length - 1) + ")");
            return null;
        }

        Tile tile = Tile.tiles[tileId];
        if (tile == null) {
            System.err.println("ATENTIE: Tile-ul cu ID " + tileId + " este null");
            return null;
        }

        return tile;
    }

    /*! \fn private void LoadWorldFromTMX(String path)
        \brief Incarca harta dintr-un fisier .tmx cu toate layer-urile in format CSV.

        \param path Calea catre fisierul TMX
     */
    private void LoadWorldFromTMX(String path) throws Exception {
        System.out.println("Incepe incarcarea hartii TMX din: " + path);

        // Verifica existenta fisierului
        File file = new File(path);
        System.out.println("Calea absoluta catre fisier: " + file.getAbsolutePath());

        if (!file.exists()) {
            throw new Exception("Fisierul TMX nu exista la calea: " + file.getAbsolutePath());
        }

        if (!file.canRead()) {
            throw new Exception("Fisierul TMX nu poate fi citit. Verifica permisiunile.");
        }

        System.out.println("Fisierul TMX gasit si poate fi citit");

        // Parseaza XML-ul
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        Element mapElement = doc.getDocumentElement();
        System.out.println("Element root gasit: " + mapElement.getNodeName());

        // Verifica daca elementul root este 'map'
        if (!"map".equals(mapElement.getNodeName())) {
            throw new Exception("Fisierul nu este un TMX valid. Elementul root este '" +
                    mapElement.getNodeName() + "' in loc de 'map'");
        }

        // Extrage dimensiunile hartii
        String widthAttr = mapElement.getAttribute("width");
        String heightAttr = mapElement.getAttribute("height");

        if (widthAttr.isEmpty() || heightAttr.isEmpty()) {
            throw new Exception("Atributele 'width' si 'height' lipsesc din elementul 'map'");
        }

        width = Integer.parseInt(widthAttr);
        height = Integer.parseInt(heightAttr);

        System.out.println("Dimensiuni harta extrase: " + width + " x " + height + " tile-uri");

        if (width <= 0 || height <= 0) {
            throw new Exception("Dimensiunile hartii sunt invalide: " + width + "x" + height);
        }

        // Gaseste toate layer-urile
        NodeList layerList = doc.getElementsByTagName("layer");
        System.out.println("Numarul de layer-uri gasite: " + layerList.getLength());

        if (layerList.getLength() == 0) {
            throw new Exception("Nu s-au gasit layer-uri in fisierul TMX");
        }

        // Proceseaza fiecare layer
        for (int layerIndex = 0; layerIndex < layerList.getLength(); layerIndex++) {
            Element layer = (Element) layerList.item(layerIndex);
            String layerName = layer.getAttribute("name");
            String visibleAttr = layer.getAttribute("visible");
            boolean visible = visibleAttr.isEmpty() || !"0".equals(visibleAttr);

            System.out.println("Se proceseaza layer-ul " + (layerIndex + 1) + ": '" + layerName + "' (vizibil: " + visible + ")");

            // Creeaza un nou layer
            MapLayer mapLayer = new MapLayer(layerName, width, height);
            mapLayer.visible = visible;

            // Gaseste datele din layer
            NodeList dataList = layer.getElementsByTagName("data");
            if (dataList.getLength() == 0) {
                System.out.println("Layer-ul '" + layerName + "' nu are date, se sare");
                continue;
            }

            Element data = (Element) dataList.item(0);
            String encoding = data.getAttribute("encoding");
            String compression = data.getAttribute("compression");

            System.out.println("Encoding date pentru '" + layerName + "': '" + (encoding.isEmpty() ? "none" : encoding) + "'");

            // Verifica encoding-ul
            if (!encoding.isEmpty() && !"csv".equals(encoding)) {
                throw new Exception("Encoding '" + encoding + "' nu este suportat pentru layer-ul '" + layerName + "'. Foloseste CSV in Tiled.");
            }

            if (!compression.isEmpty()) {
                throw new Exception("Compresia '" + compression + "' nu este suportata pentru layer-ul '" + layerName + "'. Dezactiveaza compresia in Tiled.");
            }

            // Extrage si proceseaza datele CSV
            String csvData = data.getTextContent().trim();
            if (csvData.isEmpty()) {
                System.out.println("Layer-ul '" + layerName + "' are date goale, se umple cu 0");
                // Layer-ul ramane cu toate tile-urile 0 (goale)
            } else {
                // Proceseaza datele CSV
                csvData = csvData.replaceAll("\\s+", "");
                String[] tileIds = csvData.split(",");

                if (tileIds.length != width * height) {
                    throw new Exception("Nepotrivire in numarul de tile-uri pentru layer-ul '" + layerName + "'! Gasite: " + tileIds.length +
                            ", asteptate: " + (width * height));
                }

                // Populeaza matricea layer-ului
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int index = y * width + x;
                        try {
                            int id = Integer.parseInt(tileIds[index].trim());
                            mapLayer.tiles[x][y] = id;
                        } catch (NumberFormatException e) {
                            throw new Exception("ID de tile invalid in layer-ul '" + layerName + "' la pozitia [" + x + "," + y +
                                    "] (index " + index + "): '" + tileIds[index] + "'");
                        }
                    }
                }
            }

            // Adauga layer-ul la lista
            layers.add(mapLayer);
            System.out.println("Layer-ul '" + layerName + "' procesat cu succes");
        }

        System.out.println("Harta TMX cu " + layers.size() + " layer-uri a fost incarcata complet!");
    }

    /*! \fn public void SetLayerVisible(int layerIndex, boolean visible)
        \brief Ascunde sau afiseaza un layer

        \param layerIndex Indexul layer-ului
        \param visible true pentru afisare, false pentru ascundere
     */
    public void SetLayerVisible(int layerIndex, boolean visible) {
        if (layerIndex >= 0 && layerIndex < layers.size()) {
            layers.get(layerIndex).visible = visible;
            System.out.println("Layer " + layerIndex + " (" + layers.get(layerIndex).name + ") " +
                    (visible ? "afisat" : "ascuns"));
        }
    }

    /*! \fn public void SetLayerVisible(String layerName, boolean visible)
        \brief Ascunde sau afiseaza un layer pe baza numelui

        \param layerName Numele layer-ului
        \param visible true pentru afisare, false pentru ascundere
     */
    public void SetLayerVisible(String layerName, boolean visible) {
        for (int i = 0; i < layers.size(); i++) {
            if (layers.get(i).name.equals(layerName)) {
                SetLayerVisible(i, visible);
                return;
            }
        }
        System.out.println("Layer-ul cu numele '" + layerName + "' nu a fost gasit");
    }

    // Getters
    public int getLayerCount() {
        return layers.size();
    }

    public String getLayerName(int layerIndex) {
        if (layerIndex >= 0 && layerIndex < layers.size()) {
            return layers.get(layerIndex).name;
        }
        return null;
    }

    public boolean isMapLoaded() {
        return mapLoaded;
    }

    public String getMapPath() {
        return mapPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getMapInfo() {
        if (mapLoaded) {
            return "Harta TMX: " + width + "x" + height + " tile-uri, " + layers.size() + " layer-uri din " + mapPath;
        } else {
            return "Harta TMX nu este incarcata din " + mapPath;
        }
    }

    /*! \fn public void printMapDebugInfo()
        \brief Afiseaza informatii detaliate despre harta pentru debugging
     */
    public void printMapDebugInfo() {
        System.out.println("=== DEBUG INFO HARTA ===");
        System.out.println("Calea fisierului: " + mapPath);
        System.out.println("Status incarcare: " + (mapLoaded ? "SUCCES" : "EROARE"));
        System.out.println("Dimensiuni: " + width + "x" + height + " tile-uri");
        System.out.println("Numar layer-uri: " + layers.size());

        for (int i = 0; i < layers.size(); i++) {
            MapLayer layer = layers.get(i);
            System.out.println("  Layer " + i + ": " + layer.name + " (vizibil: " + layer.visible + ")");

            // Afiseaza primele cateva tile-uri pentru debugging
            if (layer.tiles != null && width > 0 && height > 0) {
                System.out.print("    Primele tile-uri: ");
                for (int x = 0; x < Math.min(5, width); x++) {
                    for (int y = 0; y < Math.min(2, height); y++) {
                        System.out.print(layer.tiles[x][y] + " ");
                    }
                }
                System.out.println();
            }
        }
        System.out.println("========================");
    }

    /*! \fn public boolean hasValidTiles()
        \brief Verifica daca harta are tile-uri valide

        \return true daca harta are cel putin un tile diferit de 0
     */
    public boolean hasValidTiles() {
        if (!mapLoaded || layers.isEmpty()) {
            return false;
        }

        for (MapLayer layer : layers) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (layer.tiles[x][y] > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*! \fn public int getTileIdAt(int x, int y)
        \brief Returneaza ID-ul tile-ului de la pozitia specificata (primul layer)

        \param x Coordonata X
        \param y Coordonata Y
        \return ID-ul tile-ului sau 0 daca pozitia este invalida
     */
    public int getTileIdAt(int x, int y) {
        return getTileIdAt(x, y, 0);
    }

    /*! \fn public int getTileIdAt(int x, int y, int layerIndex)
        \brief Returneaza ID-ul tile-ului de la pozitia si layer-ul specificat

        \param x Coordonata X
        \param y Coordonata Y
        \param layerIndex Indexul layer-ului
        \return ID-ul tile-ului sau 0 daca pozitia/layer-ul este invalid
     */
    public int getTileIdAt(int x, int y, int layerIndex) {
        if (!mapLoaded || layers.isEmpty()) {
            return 0;
        }

        if (layerIndex < 0 || layerIndex >= layers.size()) {
            return 0;
        }

        if (x < 0 || y < 0 || x >= width || y >= height) {
            return 0;
        }

        return layers.get(layerIndex).tiles[x][y];
    }
}