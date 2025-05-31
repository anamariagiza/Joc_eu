package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class Tile
    \brief Retine toate dalele intr-un vector si ofera posibilitatea regasirii dupa un id.
 */
public class Tile
{
    private static final int NO_TILES = 2000;
    public static Tile[] tiles = new Tile[NO_TILES];

    // TILE-URI EXISTENTE (pentru compatibilitate cu Level 1)
    public static Tile grassTile         = new GrassTile(82);
    public static Tile grassTile2        = new GrassTile2(98);
    public static Tile soilTile          = new SoilTile(17);
    public static Tile waterTile         = new WaterTile(145);
    public static Tile deepWaterTile     = new DeepWaterTile(180);
    public static Tile rigthMarginSoil   = new RigthMarginSoilTile(83);
    public static Tile rigthMarginSoil2  = new RigthMarginSoilTile2(99);
    public static Tile leftTopCornerSoil = new LeftTopCornerSoil(0);

    // LEVEL 2 TILES (Cave theme) - ID-uri independente 1-8
    public static Tile tile1_L2 = new Level2Tile1(1);
    public static Tile tile2_L2 = new Level2Tile2(2);
    public static Tile tile3_L2 = new Level2Tile3(3);
    public static Tile tile4_L2 = new Level2Tile4(4);
    public static Tile tile5_L2 = new Level2Tile5(5);
    public static Tile tile6_L2 = new Level2Tile6(6);
    public static Tile tile7_L2 = new Level2Tile7(7);
    public static Tile tile8_L2 = new Level2Tile8(8);

    // LEVEL 3 TILES (Dark Cave theme) - ID-uri independente 1-8
    public static Tile tile1_L3 = new Level3Tile1(1);
    public static Tile tile2_L3 = new Level3Tile2(2);
    public static Tile tile3_L3 = new Level3Tile3(3);
    public static Tile tile4_L3 = new Level3Tile4(4);
    public static Tile tile5_L3 = new Level3Tile5(5);
    public static Tile tile6_L3 = new Level3Tile6(6);
    public static Tile tile7_L3 = new Level3Tile7(7);
    public static Tile tile8_L3 = new Level3Tile8(8);

    public static final int TILE_WIDTH  = 32;   // Dimensiuni corecte pentru cave tiles
    public static final int TILE_HEIGHT = 32;   // Dimensiuni corecte pentru cave tiles

    protected BufferedImage img;
    protected final int id;

    /*! \fn public Tile(BufferedImage texture, int id)
        \brief Constructorul aferent clasei.
     */
    public Tile(BufferedImage image, int idd)
    {
        img = image;
        id = idd;
        if (idd >= 0 && idd < tiles.length) {
            tiles[id] = this;
        }
    }

    /*! \fn public void Update()
        \brief Actualizeaza proprietatile dalei.
     */
    public void Update() { }

    /*! \fn public void Draw(Graphics g, int x, int y)
        \brief Deseneaza in fereastra dala.
     */
    public void Draw(Graphics g, int x, int y)
    {
        if (img != null) {
            g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
        } else {
            // Placeholder daca imaginea lipseste
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, TILE_WIDTH, TILE_HEIGHT);
            g.setFont(new Font("Arial", Font.PLAIN, 8));
            g.drawString(String.valueOf(id), x + 2, y + 12);
        }
    }

    /*! \fn public boolean IsSolid()
        \brief Returneaza proprietatea de dala solida (supusa coliziunilor) sau nu.
     */
    public boolean IsSolid() { return false; }

    /*! \fn public int GetId()
        \brief Returneaza id-ul dalei.
     */
    public int GetId() { return id; }
}

// =============================================================================
// TILE-URI EXISTENTE (pentru compatibilitate cu Level 1)
// =============================================================================

/*! \class GrassTile
    \brief Dala de iarba pentru Level 1
 */
class GrassTile extends Tile {
    public GrassTile(int id) { super(Assets.grass, id); }
    @Override public boolean IsSolid() { return false; }
}

class GrassTile2 extends Tile {
    public GrassTile2(int id) { super(Assets.grass2, id); }
    @Override public boolean IsSolid() { return false; }
}

class SoilTile extends Tile {
    public SoilTile(int id) { super(Assets.soil, id); }
    @Override public boolean IsSolid() { return true; }
}

class WaterTile extends Tile {
    public WaterTile(int id) { super(Assets.water, id); }
    @Override public boolean IsSolid() { return true; }
}

class DeepWaterTile extends Tile {
    public DeepWaterTile(int id) { super(Assets.deepWater, id); }
    @Override public boolean IsSolid() { return true; }
}

class RigthMarginSoilTile extends Tile {
    public RigthMarginSoilTile(int id) { super(Assets.rigthMarginSoil, id); }
    @Override public boolean IsSolid() { return true; }
}

class RigthMarginSoilTile2 extends Tile {
    public RigthMarginSoilTile2(int id) { super(Assets.rigthMarginSoil2, id); }
    @Override public boolean IsSolid() { return true; }
}

class LeftTopCornerSoil extends Tile {
    public LeftTopCornerSoil(int id) { super(Assets.leftTopCornerSoil, id); }
    @Override public boolean IsSolid() { return true; }
}

// =============================================================================
// LEVEL 2 TILES (Cave theme) - folosesc CaveBG.png
// =============================================================================

class Level2Tile1 extends Tile {
    public Level2Tile1(int id) { super(Assets.tile1_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea cave
}

class Level2Tile2 extends Tile {
    public Level2Tile2(int id) { super(Assets.tile2_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea cave
}

class Level2Tile3 extends Tile {
    public Level2Tile3(int id) { super(Assets.tile3_level2, id); }
    @Override public boolean IsSolid() { return true; } // Obstacol/zid
}

class Level2Tile4 extends Tile {
    public Level2Tile4(int id) { super(Assets.tile4_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea
}

class Level2Tile5 extends Tile {
    public Level2Tile5(int id) { super(Assets.tile5_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea
}

class Level2Tile6 extends Tile {
    public Level2Tile6(int id) { super(Assets.tile6_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea
}

class Level2Tile7 extends Tile {
    public Level2Tile7(int id) { super(Assets.tile7_level2, id); }
    @Override public boolean IsSolid() { return true; } // Zid/obstacol
}

class Level2Tile8 extends Tile {
    public Level2Tile8(int id) { super(Assets.tile8_level2, id); }
    @Override public boolean IsSolid() { return false; } // Podea
}

// =============================================================================
// LEVEL 3 TILES (Dark Cave theme) - folosesc CaveG.png
// =============================================================================

class Level3Tile1 extends Tile {
    public Level3Tile1(int id) { super(Assets.tile1_level3, id); }
    @Override public boolean IsSolid() { return false; } // Podea dark cave
}

class Level3Tile2 extends Tile {
    public Level3Tile2(int id) { super(Assets.tile2_level3, id); }
    @Override public boolean IsSolid() { return true; } // Obstacol inchis
}

class Level3Tile3 extends Tile {
    public Level3Tile3(int id) { super(Assets.tile3_level3, id); }
    @Override public boolean IsSolid() { return true; } // Zid dark
}

class Level3Tile4 extends Tile {
    public Level3Tile4(int id) { super(Assets.tile4_level3, id); }
    @Override public boolean IsSolid() { return false; } // Podea dark
}

class Level3Tile5 extends Tile {
    public Level3Tile5(int id) { super(Assets.tile5_level3, id); }
    @Override public boolean IsSolid() { return false; } // Podea texturata
}

class Level3Tile6 extends Tile {
    public Level3Tile6(int id) { super(Assets.tile6_level3, id); }
    @Override public boolean IsSolid() { return false; } // Podea
}

class Level3Tile7 extends Tile {
    public Level3Tile7(int id) { super(Assets.tile7_level3, id); }
    @Override public boolean IsSolid() { return true; } // Zid solid dark
}

class Level3Tile8 extends Tile {
    public Level3Tile8(int id) { super(Assets.tile8_level3, id); }
    @Override public boolean IsSolid() { return true; } // Obstacol dark
}