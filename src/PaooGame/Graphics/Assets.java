package PaooGame.Graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/*! \class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.
 */
public class Assets
{
    // Referinte catre elementele grafice pentru personaje
    public static BufferedImage heroLeft;
    public static BufferedImage heroRight;
    public static BufferedImage heroFront;
    public static BufferedImage heroBack;
    public static BufferedImage backgroundMenu;

    // Tile-uri pentru compatibilitate cu codul existent
    public static BufferedImage grass;
    public static BufferedImage grass2;
    public static BufferedImage water;
    public static BufferedImage deepWater;
    public static BufferedImage soil;
    public static BufferedImage rigthMarginSoil;
    public static BufferedImage rigthMarginSoil2;
    public static BufferedImage leftTopCornerSoil;

    // Tile-uri pentru Level 2 (folosind CaveBG.png)
    public static BufferedImage tile1_level2;
    public static BufferedImage tile2_level2;
    public static BufferedImage tile3_level2;
    public static BufferedImage tile4_level2;
    public static BufferedImage tile5_level2;
    public static BufferedImage tile6_level2;
    public static BufferedImage tile7_level2;
    public static BufferedImage tile8_level2;

    // Tile-uri pentru Level 3 (folosind CaveG.png)
    public static BufferedImage tile1_level3;
    public static BufferedImage tile2_level3;
    public static BufferedImage tile3_level3;
    public static BufferedImage tile4_level3;
    public static BufferedImage tile5_level3;
    public static BufferedImage tile6_level3;
    public static BufferedImage tile7_level3;
    public static BufferedImage tile8_level3;

    /*! \fn public static void Init()
        \brief Functia initializeza referintele catre elementele grafice utilizate.
     */
    public static void Init()
    {
        System.out.println("Initializare Assets...");

        // Incarca fundalul pentru meniu
        loadMenuBackground();

        // Incarca sprite-urile pentru personaje
        loadCharacterSprites();

        // Incarca tile-urile pentru Level 1 (forest - existent)
        loadLevel1Tiles();

        // Incarca tile-urile pentru Level 2 (cave)
        loadLevel2Tiles();

        // Incarca tile-urile pentru Level 3 (cave dark)
        loadLevel3Tiles();

        System.out.println("Assets initializate complet");
    }

    /*! \fn private static void loadMenuBackground()
        \brief Incarca fundalul pentru meniu
     */
    private static void loadMenuBackground()
    {
        System.out.println("Incarcarea fundalului pentru meniu...");
        backgroundMenu = ImageLoader.LoadImage("res/textures/FUNDAL.jpg");
        if (backgroundMenu != null) {
            System.out.println("Fundal incarcat cu succes");
        }
    }

    /*! \fn private static void loadCharacterSprites()
        \brief Incarca sprite-urile pentru personaje
     */
    private static void loadCharacterSprites()
    {
        System.out.println("Incarcarea sprite-urilor pentru personaje...");

        BufferedImage characterSheet = ImageLoader.LoadImage("res/textures/characters.png");

        if (characterSheet != null && characterSheet.getWidth() > 32 && characterSheet.getHeight() > 32) {
            int spriteWidth = characterSheet.getWidth() / 9;
            int spriteHeight = characterSheet.getHeight() / 4;

            try {
                heroBack = characterSheet.getSubimage(0, 0, spriteWidth, spriteHeight);
                heroLeft = characterSheet.getSubimage(0, spriteHeight * 1, spriteWidth, spriteHeight);
                heroFront = characterSheet.getSubimage(0, spriteHeight * 2, spriteWidth, spriteHeight);
                heroRight = characterSheet.getSubimage(0, spriteHeight * 3, spriteWidth, spriteHeight);

                System.out.println("Sprite-uri pentru personaj incarcate cu succes");
            } catch (Exception e) {
                System.err.println("EROARE la sprite-urile pentru personaj: " + e.getMessage());
            }
        }
    }

    /*! \fn private static void loadLevel1Tiles()
        \brief Incarca tile-urile pentru Level 1 (Forest theme - existent)
     */
    private static void loadLevel1Tiles()
    {
        System.out.println("Incarcarea tile-urilor pentru Level 1 (Forest)...");

        BufferedImage mapSheetImage = ImageLoader.LoadImage("res/textures/gentle forest v01.png");

        if (mapSheetImage != null) {
            SpriteSheet mapSheet = new SpriteSheet(mapSheetImage);
            try {
                grass = mapSheet.crop(1, 5);
                grass2 = mapSheet.crop(3, 7);
                water = mapSheet.crop(1, 9);
                deepWater = mapSheet.crop(4, 11);
                soil = mapSheet.crop(1, 1);
                rigthMarginSoil = mapSheet.crop(3, 6);
                rigthMarginSoil2 = mapSheet.crop(3, 7);
                leftTopCornerSoil = mapSheet.crop(0, 0);

                System.out.println("Tile-uri Level 1 incarcate cu succes");
            } catch (Exception e) {
                System.err.println("EROARE la tile-urile Level 1: " + e.getMessage());
            }
        }
    }

    /*! \fn private static void loadLevel2Tiles()
        \brief Incarca tile-urile pentru Level 2 din CaveBG.png
        Cu dimensiuni corecte: 32x23 pixeli per tile
     */
    private static void loadLevel2Tiles()
    {
        System.out.println("Incarcarea tile-urilor pentru Level 2 din CaveBG.png...");
        System.out.println("Dimensiuni tile: 32x32 pixeli");

        BufferedImage level2Image = ImageLoader.LoadImage("res/textures/CaveBG.png");

        if (level2Image != null) {
            System.out.println("CaveBG.png incarcat: " + level2Image.getWidth() + "x" + level2Image.getHeight());

            // Cream SpriteSheet cu dimensiuni corecte pentru cave tiles
            CustomSpriteSheet level2Sheet = new CustomSpriteSheet(level2Image, 32, 32);
            try {
                // Selectez tile-uri din pozitii specifice
                tile1_level2 = level2Sheet.crop(0, 0);    // ID 100 - Prima dala
                tile2_level2 = level2Sheet.crop(1, 0);    // ID 101 - A doua dala
                tile3_level2 = level2Sheet.crop(2, 0);    // ID 102 - A treia dala
                tile4_level2 = level2Sheet.crop(0, 1);    // ID 103 - Prima de pe randul 2
                tile5_level2 = level2Sheet.crop(1, 1);    // ID 104 - A doua de pe randul 2
                tile6_level2 = level2Sheet.crop(2, 1);    // ID 105 - A treia de pe randul 2
                tile7_level2 = level2Sheet.crop(0, 2);    // ID 106 - Prima de pe randul 3
                tile8_level2 = level2Sheet.crop(1, 2);    // ID 107 - A doua de pe randul 3

                System.out.println("SUCCESS: Tile-uri Level 2 incarcate din CaveBG.png cu dimensiuni 32x32");
            } catch (Exception e) {
                System.err.println("EROARE la extragerea tile-urilor din CaveBG.png: " + e.getMessage());
                createFallbackTiles("level2");
            }
        } else {
            System.err.println("EROARE: Nu pot incarca CaveBG.png");
            createFallbackTiles("level2");
        }
    }

    /*! \fn private static void loadLevel3Tiles()
        \brief Incarca tile-urile pentru Level 3 din CaveG.png
        Cu dimensiuni corecte: 32x23 pixeli per tile
     */
    private static void loadLevel3Tiles()
    {
        System.out.println("Incarcarea tile-urilor pentru Level 3 din CaveG.png...");
        System.out.println("Dimensiuni tile: 32x32 pixeli");

        BufferedImage level3Image = ImageLoader.LoadImage("res/textures/CaveG.png");

        if (level3Image != null) {
            System.out.println("CaveG.png incarcat: " + level3Image.getWidth() + "x" + level3Image.getHeight());

            // Cream SpriteSheet cu dimensiuni corecte pentru cave tiles
            CustomSpriteSheet level3Sheet = new CustomSpriteSheet(level3Image, 32, 32);
            try {
                // Selectez alte tile-uri pentru varietate
                tile1_level3 = level3Sheet.crop(0, 0);    // ID 200 - Prima dala
                tile2_level3 = level3Sheet.crop(1, 0);    // ID 201 - A doua dala
                tile3_level3 = level3Sheet.crop(2, 0);    // ID 202 - A treia dala
                tile4_level3 = level3Sheet.crop(0, 1);    // ID 203 - Prima de pe randul 2
                tile5_level3 = level3Sheet.crop(1, 1);    // ID 204 - A doua de pe randul 2
                tile6_level3 = level3Sheet.crop(2, 1);    // ID 205 - A treia de pe randul 2
                tile7_level3 = level3Sheet.crop(0, 2);    // ID 206 - Prima de pe randul 3
                tile8_level3 = level3Sheet.crop(1, 2);    // ID 207 - A doua de pe randul 3

                System.out.println("SUCCESS: Tile-uri Level 3 incarcate din CaveG.png cu dimensiuni 32x32");
            } catch (Exception e) {
                System.err.println("EROARE la extragerea tile-urilor din CaveG.png: " + e.getMessage());
                createFallbackTiles("level3");
            }
        } else {
            System.err.println("EROARE: Nu pot incarca CaveG.png");
            createFallbackTiles("level3");
        }
    }

    /*! \fn private static void createFallbackTiles(String level)
        \brief Creeaza tile-uri colorate temporare daca imaginile nu se incarca
        Cu dimensiuni corecte 32x23
     */
    private static void createFallbackTiles(String level)
    {
        System.out.println("Creez tile-uri temporare pentru " + level + " cu dimensiuni 32x32...");

        BufferedImage temp1 = createColorTile(Color.RED, "1");
        BufferedImage temp2 = createColorTile(Color.GREEN, "2");
        BufferedImage temp3 = createColorTile(Color.BLUE, "3");
        BufferedImage temp4 = createColorTile(Color.YELLOW, "4");
        BufferedImage temp5 = createColorTile(Color.MAGENTA, "5");
        BufferedImage temp6 = createColorTile(Color.CYAN, "6");
        BufferedImage temp7 = createColorTile(Color.ORANGE, "7");
        BufferedImage temp8 = createColorTile(Color.GRAY, "8");

        if ("level2".equals(level)) {
            tile1_level2 = temp1; tile2_level2 = temp2; tile3_level2 = temp3; tile4_level2 = temp4;
            tile5_level2 = temp5; tile6_level2 = temp6; tile7_level2 = temp7; tile8_level2 = temp8;
        } else if ("level3".equals(level)) {
            tile1_level3 = temp1; tile2_level3 = temp2; tile3_level3 = temp3; tile4_level3 = temp4;
            tile5_level3 = temp5; tile6_level3 = temp6; tile7_level3 = temp7; tile8_level3 = temp8;
        }
    }

    /*! \fn private static BufferedImage createColorTile(Color color, String number)
        \brief Creeaza o dala colorata cu numar pentru debugging
        Cu dimensiuni corecte 32x32
     */
    private static BufferedImage createColorTile(Color color, String number)
    {
        BufferedImage tile = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tile.createGraphics();

        g2d.setColor(color);
        g2d.fillRect(0, 0, 32, 32);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 31, 31);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (32 - fm.stringWidth(number)) / 2;
        int y = (32 + fm.getAscent()) / 2;
        g2d.drawString(number, x, y);

        g2d.dispose();
        return tile;
    }
}

/*! \class CustomSpriteSheet
    \brief SpriteSheet personalizat pentru dimensiuni custom de tile-uri
 */
class CustomSpriteSheet {
    private BufferedImage spriteSheet;
    private int tileWidth;
    private int tileHeight;

    public CustomSpriteSheet(BufferedImage image, int tileWidth, int tileHeight) {
        this.spriteSheet = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public BufferedImage crop(int x, int y) {
        if (spriteSheet == null) return null;

        int startX = x * tileWidth;
        int startY = y * tileHeight;

        // Verifica limitele
        if (startX + tileWidth > spriteSheet.getWidth() ||
                startY + tileHeight > spriteSheet.getHeight() ||
                startX < 0 || startY < 0) {
            System.err.println("ATENTIE: Coordonate crop in afara limitelor: [" + x + "," + y + "] pentru tile " + tileWidth + "x" + tileHeight);
            System.err.println("Spritesheet dimensiuni: " + spriteSheet.getWidth() + "x" + spriteSheet.getHeight());
            return null;
        }

        return spriteSheet.getSubimage(startX, startY, tileWidth, tileHeight);
    }
}