package PaooGame.Graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;

/*! \class public class ImageLoader
    \brief Clasa ce contine o metoda statica pentru incarcarea unei imagini in memorie.
 */
public class ImageLoader
{
    /*! \fn  public static BufferedImage loadImage(String path)
        \brief Incarca o imagine intr-un obiect BufferedImage si returneaza o referinta catre acesta.

        \param path Calea relativa pentru localizarea fisierul imagine.
     */
    public static BufferedImage LoadImage(String path)
    {
        System.out.println("incercare incarcare: " + path);

        try
        {
            // incearca mai intai sa incarce resursa din classpath
            InputStream inputStream = ImageLoader.class.getResourceAsStream("/" + path);

            if (inputStream != null) {
                BufferedImage img = ImageIO.read(inputStream);
                if (img != null) {
                    System.out.println("✓ Imagine incarcata din classpath: " + path + " (" + img.getWidth() + "x" + img.getHeight() + ")");
                    return img;
                }
            }

            // Daca resursa nu exista in classpath, incarca ca fisier
            File file = new File(path);
            System.out.println("Verificare fisier: " + file.getAbsolutePath());
            System.out.println("Fisierul exista: " + file.exists());

            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    System.out.println("✓ Imagine incarcata ca fisier: " + path + " (" + img.getWidth() + "x" + img.getHeight() + ")");
                    return img;
                } else {
                    System.out.println("Fisierul exista dar nu se poate citi ca imagine: " + path);
                }
            } else {
                System.out.println("Fisierul nu exista: " + file.getAbsolutePath());
            }

            // incearca si fara slash-ul de la inceput pentru classpath
            inputStream = ImageLoader.class.getResourceAsStream(path);
            if (inputStream != null) {
                BufferedImage img = ImageIO.read(inputStream);
                if (img != null) {
                    System.out.println("✓ Imagine incarcata din classpath (fara /): " + path + " (" + img.getWidth() + "x" + img.getHeight() + ")");
                    return img;
                }
            }

            System.out.println("⚠ Imagine lipsa, se creeaza temporara: " + path);

            // Returneaza o imagine temporara pentru a evita crash-ul
            BufferedImage tempImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = tempImage.createGraphics();
            g2d.setColor(java.awt.Color.MAGENTA); // Culoare vizibila pentru debugging
            g2d.fillRect(0, 0, 16, 16);
            g2d.dispose();
            return tempImage;
        }
        catch(IOException e)
        {
            System.err.println("✗ Eroare la incarcarea imaginii: " + path);
            e.printStackTrace();

            // Returneaza o imagine temporara si in caz de eroare
            BufferedImage tempImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g2d = tempImage.createGraphics();
            g2d.setColor(java.awt.Color.RED); // Culoare diferita pentru erori
            g2d.fillRect(0, 0, 16, 16);
            g2d.dispose();
            return tempImage;
        }
    }
}
