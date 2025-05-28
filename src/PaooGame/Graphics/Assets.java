package PaooGame.Graphics;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets
{
    /// Referinte catre elementele grafice (dale) utilizate in joc.
    public static BufferedImage grass;
    public static BufferedImage grass2;
    public static BufferedImage water;
    public static BufferedImage deepWater;
    public static BufferedImage soil;
    public static BufferedImage rigthMarginSoil;
    public static BufferedImage rigthMarginSoil2;
    public static BufferedImage leftTopCornerSoil;
    public static BufferedImage heroLeft;
    public static BufferedImage heroRight;
    public static BufferedImage heroFront;  /*!< Sprite pentru fata eroului (S) */
    public static BufferedImage heroBack;   /*!< Sprite pentru spatele eroului (W) */
    public static BufferedImage backgroundMenu; /*!< Imaginea de fundal pentru meniu*/

    /*! \fn public static void Init()
        \brief Functia initializeza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
        System.out.println("Initializare Assets...");

        // Incarca fundalul pentru meniu
        System.out.println("Incarcarea fundalului pentru meniu...");
        backgroundMenu = ImageLoader.LoadImage("res/textures/FUNDAL.jpg");
        if (backgroundMenu != null) {
            System.out.println("✓ Fundal incarcat cu succes: " + backgroundMenu.getWidth() + "x" + backgroundMenu.getHeight());
        } else {
            System.err.println("✗ EROARE: Nu s-a putut incarca fundalul din res/textures/FUNDAL.jpg");
        }

        // Incarca sprite sheet-ul pentru personaje
        loadCharacterSprites();

        // Incarca dalele pentru harta
        loadMapTiles();

        System.out.println("✓ Assets initializate complet");
    }

    /*! \fn private static void loadCharacterSprites()
        \brief Incarca sprite-urile pentru personaje din sprite sheet
     */
    private static void loadCharacterSprites()
    {
        System.out.println("Incarcarea sprite-urilor pentru personaje...");

        // Incarca sprite sheet-ul cu personaje
        BufferedImage characterSheet = ImageLoader.LoadImage("res/textures/characters.png");

        if (characterSheet != null && characterSheet.getWidth() > 32 && characterSheet.getHeight() > 32) {
            System.out.println("✓ Character sprite sheet gasit: " + characterSheet.getWidth() + "x" + characterSheet.getHeight());

            // Calculeaza dimensiunea unei sprite
            int spriteWidth = characterSheet.getWidth() / 9;   // 14 coloane
            int spriteHeight = characterSheet.getHeight() / 4;  // 9 randuri

            System.out.println("Dimensiune sprite calculata: " + spriteWidth + "x" + spriteHeight);

            try {
                // Extrage sprite-urile pentru fiecare directie
                heroBack = characterSheet.getSubimage(0, 0, spriteWidth, spriteHeight);                    // Randul 0 - W (spate)
                heroLeft = characterSheet.getSubimage(0, spriteHeight * 1, spriteWidth, spriteHeight);     // Randul 1 - A (stanga)
                heroFront = characterSheet.getSubimage(0, spriteHeight * 2, spriteWidth, spriteHeight);    // Randul 2 - S (fata)
                heroRight = characterSheet.getSubimage(0, spriteHeight * 3, spriteWidth, spriteHeight);    // Randul 3 - D (dreapta)

                System.out.println("✓ Sprite-uri pentru personaj incarcate cu succes:");
                System.out.println("  heroBack (W): " + heroBack.getWidth() + "x" + heroBack.getHeight());
                System.out.println("  heroLeft (A): " + heroLeft.getWidth() + "x" + heroLeft.getHeight());
                System.out.println("  heroFront (S): " + heroFront.getWidth() + "x" + heroFront.getHeight());
                System.out.println("  heroRight (D): " + heroRight.getWidth() + "x" + heroRight.getHeight());

            } catch (Exception e) {
                System.err.println("✗ EROARE la extragerea sprite-urilor pentru personaj: " + e.getMessage());
                // Nu cream imagini temporare - lasam null pentru a forta debugging
            }
        } else {
            System.err.println("✗ EROARE: Nu s-a putut incarca character sprite sheet din res/textures/characters.png");
            // Nu cream imagini temporare - lasam null pentru a forta debugging
        }
    }

    /*! \fn private static void loadMapTiles()
        \brief Incarca dalele pentru harta din sprite sheet
     */
    private static void loadMapTiles()
    {
        System.out.println("Incarcarea dalelor pentru harta...");

        // Incarca sprite sheet-ul pentru harta (dale)
        BufferedImage mapSheetImage = ImageLoader.LoadImage("res/textures/gentle forest v01.png");

        if (mapSheetImage != null && mapSheetImage.getWidth() > 32 && mapSheetImage.getHeight() > 16) {
            System.out.println("✓ Map sprite sheet gasit: " + mapSheetImage.getWidth() + "x" + mapSheetImage.getHeight());

            SpriteSheet mapSheet = new SpriteSheet(mapSheetImage);
            try {
                // Incarca doar dalele pe care le-am definit explicit
//                public static BufferedImage grass;
//                public static BufferedImage water;
//                public static BufferedImage deepWater;
//                public static BufferedImage soil;
                grass = mapSheet.crop(1, 5);
                grass2 = mapSheet.crop(3,7);
                water = mapSheet.crop(1, 9);
                deepWater = mapSheet.crop(4, 11);
                soil = mapSheet.crop(1, 1);
                rigthMarginSoil = mapSheet.crop(3,6);
                rigthMarginSoil2 = mapSheet.crop(3,7);
                leftTopCornerSoil = mapSheet.crop(0,0);

                System.out.println("✓ Dale incarcate cu succes:");
                System.out.println("  grass: " + grass.getWidth() + "x" + grass.getHeight() + " de la pozitia (0,0)");

            } catch (Exception e) {
                System.err.println("✗ EROARE la extragerea dalelor din sprite sheet: " + e.getMessage());
                // Nu cream dale temporare - lasam null pentru a forta debugging
            }
        } else {
            System.err.println("✗ EROARE: Nu s-a putut incarca map sprite sheet din res/textures/gentle forest v01.png");
            // Nu cream dale temporare - lasam null pentru a forta debugging
        }
    }

    /*! \fn private static BufferedImage flipImageHorizontally(BufferedImage image)
        \brief Oglindeste o imagine pe orizontala

        \param image Imaginea de oglindit
        \return Imaginea oglindita
     */
    private static BufferedImage flipImageHorizontally(BufferedImage image)
    {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(image, null);
    }
}