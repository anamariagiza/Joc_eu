package PaooGame.States;

import PaooGame.Items.Hero;
import PaooGame.Maps.Map;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.event.KeyEvent;

/*! \class PlayState
    \brief Implementeaza/controleaza jocul cu suport pentru multiple nivele.
 */
public class PlayState extends State
{
    private Hero hero;
    private Map map;

    // Variabile pentru gestionarea nivelurilor
    private static int currentLevel = 1;
    private static final int MAX_LEVELS = 3;

    // Caile catre fisierele TMX (organizate pe foldere separate)
    private static final String[] LEVEL_PATHS = {
            "res/Mapa/Level1/Level1.tmx",     // Nivelul 1 (forest)
            "res/Mapa/Level2/Level2.tmx",     // Nivelul 2 (cave)
            "res/Mapa/Level3/Level3.tmx"      // Nivelul 3 (dark cave)
    };

    // Numele nivelurilor
    private static final String[] LEVEL_NAMES = {
            "Forest Level",
            "Cave Level",
            "Dark Cave Level"
    };

    // Pozitii spawn pentru fiecare nivel (ajustate pentru tile-uri mari)
    private static final float[][] SPAWN_POSITIONS = {
            {100, 100},   // Level 1 spawn (tile-uri 16x16)
            {200, 150},   // Level 2 spawn (tile-uri 32x23)
            {250, 200}    // Level 3 spawn (tile-uri 32x23)
    };

    // Control pentru taste
    private boolean nextLevelPressed = false;
    private boolean restartPressed = false;
    private boolean menuPressed = false;
    private boolean level1Pressed = false;
    private boolean level2Pressed = false;
    private boolean level3Pressed = false;

    /*! \fn public PlayState(RefLinks refLink)
        \brief Constructorul de initializare al clasei
     */
    public PlayState(RefLinks refLink)
    {
        super(refLink);
        loadCurrentLevel();
        System.out.println("PlayState initializat pentru " + LEVEL_NAMES[currentLevel - 1]);
    }

    /*! \fn private void loadCurrentLevel()
        \brief Incarca nivelul curent
     */
    private void loadCurrentLevel()
    {
        try {
            System.out.println("=== INCARCAREA NIVELULUI " + currentLevel + " ===");

            String levelPath = LEVEL_PATHS[currentLevel - 1];
            System.out.println("Incarcarea hartii din: " + levelPath);

            map = new Map(refLink, levelPath);

            // Verifica daca harta s-a incarcat
            if (!map.isMapLoaded()) {
                System.err.println("ATENTIE: Harta nu s-a incarcat! Folosesc harta implicita.");
                map = new Map(refLink); // Fallback la The_map.tmx
            }

            refLink.SetMap(map);

            // Construieste eroul
            float spawnX = SPAWN_POSITIONS[currentLevel - 1][0];
            float spawnY = SPAWN_POSITIONS[currentLevel - 1][1];
            hero = new Hero(refLink, spawnX, spawnY);

            System.out.println("SUCCESS: Nivel " + currentLevel + " incarcat");
            System.out.println("Harta: " + levelPath);
            System.out.println("Spawn erou: " + spawnX + ", " + spawnY);
            System.out.println("===================================");

        } catch (Exception e) {
            System.err.println("EROARE la incarcarea nivelului " + currentLevel + ": " + e.getMessage());

            // Fallback complet
            map = new Map(refLink);
            refLink.SetMap(map);
            hero = new Hero(refLink, 100, 100);
        }
    }

    /*! \fn public void Update()
        \brief Actualizeaza starea curenta a jocului
     */
    @Override
    public void Update()
    {
        if (map != null) {
            map.Update();
        }
        hero.Update();

        handleLevelInput();
    }

    /*! \fn private void handleLevelInput()
        \brief Gestioneaza inputul pentru controlul nivelurilor
     */
    private void handleLevelInput()
    {
        // Trecere la urmatorul nivel cu N
        if (refLink.GetKeyManager().keys[KeyEvent.VK_N] && !nextLevelPressed) {
            nextLevelPressed = true;
            nextLevel();
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_N]) {
            nextLevelPressed = false;
        }

        // Restart nivel curent cu R
        if (refLink.GetKeyManager().keys[KeyEvent.VK_R] && !restartPressed) {
            restartPressed = true;
            loadCurrentLevel();
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_R]) {
            restartPressed = false;
        }

        // Intoarcere la meniu cu ESC
        if (refLink.GetKeyManager().keys[KeyEvent.VK_ESCAPE] && !menuPressed) {
            menuPressed = true;
            State.SetState(new MenuState(refLink));
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_ESCAPE]) {
            menuPressed = false;
        }

        // Selectare directa nivel cu tastele 1, 2, 3
        if (refLink.GetKeyManager().keys[KeyEvent.VK_1] && !level1Pressed) {
            level1Pressed = true;
            loadSpecificLevel(1);
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_1]) {
            level1Pressed = false;
        }

        if (refLink.GetKeyManager().keys[KeyEvent.VK_2] && !level2Pressed) {
            level2Pressed = true;
            loadSpecificLevel(2);
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_2]) {
            level2Pressed = false;
        }

        if (refLink.GetKeyManager().keys[KeyEvent.VK_3] && !level3Pressed) {
            level3Pressed = true;
            loadSpecificLevel(3);
        } else if (!refLink.GetKeyManager().keys[KeyEvent.VK_3]) {
            level3Pressed = false;
        }
    }

    /*! \fn private void nextLevel()
        \brief Trece la urmatorul nivel
     */
    private void nextLevel()
    {
        if (currentLevel < MAX_LEVELS) {
            currentLevel++;
            loadCurrentLevel();
            System.out.println("Trecere la nivelul " + currentLevel);
        } else {
            System.out.println("Toate nivelurile completate! Restart.");
            currentLevel = 1;
            loadCurrentLevel();
        }
    }

    /*! \fn private void loadSpecificLevel(int levelNumber)
        \brief Incarca un nivel specific
     */
    private void loadSpecificLevel(int levelNumber)
    {
        if (levelNumber >= 1 && levelNumber <= MAX_LEVELS && levelNumber != currentLevel) {
            currentLevel = levelNumber;
            loadCurrentLevel();
            System.out.println("Incarcare nivel " + currentLevel);
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza starea curenta a jocului
     */
    @Override
    public void Draw(Graphics g)
    {
        // Deseneaza harta
        if (map != null) {
            map.Draw(g);
        }

        // Deseneaza eroul
        hero.Draw(g);

        // Informatii despre nivel
        drawLevelInfo(g);

        // Controale
        drawControls(g);
    }

    /*! \fn private void drawLevelInfo(Graphics g)
        \brief Deseneaza informatii despre nivelul curent
     */
    private void drawLevelInfo(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));

        int x = refLink.GetWidth() - 200;
        int y = 30;

        g.drawString("Nivel: " + currentLevel + "/" + MAX_LEVELS, x, y);
        g.drawString(LEVEL_NAMES[currentLevel - 1], x, y + 25);

        // Status harta
        String mapStatus = (map != null && map.isMapLoaded()) ? "Harta: OK" : "Harta: FALLBACK";
        g.setColor((map != null && map.isMapLoaded()) ? Color.GREEN : Color.YELLOW);
        g.drawString(mapStatus, x, y + 50);
    }

    /*! \fn private void drawControls(Graphics g)
        \brief Deseneaza instructiunile de control
     */
    private void drawControls(Graphics g)
    {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.PLAIN, 12));

        int x = 10;
        int y = refLink.GetHeight() - 80;

        g.drawString("CONTROALE:", x, y);
        g.drawString("WASD - Miscare", x, y + 15);
        g.drawString("1/2/3 - Selectare nivel", x, y + 30);
        g.drawString("N - Nivel urmator", x, y + 45);
        g.drawString("R - Restart", x, y + 60);
        g.drawString("ESC - Meniu", x + 150, y + 15);
    }

    // Getters
    public static int getCurrentLevel() { return currentLevel; }
    public static String getCurrentLevelName() { return LEVEL_NAMES[currentLevel - 1]; }
}