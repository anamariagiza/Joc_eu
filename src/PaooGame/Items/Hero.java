package PaooGame.Items;

import PaooGame.Graphics.Assets;
import PaooGame.RefLinks;

import java.awt.*;
import java.awt.image.BufferedImage;

/*! \class public class Hero extends Character
    \brief Implementeaza notiunea de erou/player (caracterul controlat de jucator).

    Elementele suplimentare pe care le aduce fata de clasa de baza sunt:
        imaginea (acest atribut poate fi ridicat si in clasa de baza)
        deplasarea
        atacul (nu este implementat momentan)
        dreptunghiul de coliziune
 */
public class Hero extends Character
{
    private BufferedImage image;    /*!< Referinta catre imaginea curenta a eroului.*/

    /*! \fn public Hero(RefLinks refLink, float x, float y)
        \brief Constructorul de initializare al clasei Hero.

        \param refLink Referinta catre obiectul shortcut (obiect ce retine o serie de referinte din program).
        \param x Pozitia initiala pe axa X a eroului.
        \param y Pozitia initiala pe axa Y a eroului.
     */
    public Hero(RefLinks refLink, float x, float y)
    {
        ///Apel al constructorului clasei de baza
        super(refLink, x,y, Character.DEFAULT_CREATURE_WIDTH, Character.DEFAULT_CREATURE_HEIGHT);
        ///Seteaza imaginea de start a eroului (priveste spre dreapta initial)
        image = Assets.heroRight;
        ///Stabilieste pozitia relativa si dimensiunea dreptunghiului de coliziune, starea implicita(normala)
        normalBounds.x = 16;
        normalBounds.y = 16;
        normalBounds.width = 16;
        normalBounds.height = 32;

        ///Stabilieste pozitia relativa si dimensiunea dreptunghiului de coliziune, starea de atac
        attackBounds.x = 10;
        attackBounds.y = 10;
        attackBounds.width = 38;
        attackBounds.height = 38;

        System.out.println("✓ Hero creat - imagine initiala setata");
    }

    /*! \fn public void Update()
        \brief Actualizeaza pozitia si imaginea eroului.
     */
    @Override
    public void Update()
    {
        ///Verifica daca a fost apasata o tasta
        GetInput();
        ///Actualizeaza pozitia
        Move();
        ///Actualizeaza imaginea in functie de directia de miscare
        UpdateImage();
    }

    /*! \fn private void UpdateImage()
        \brief Actualizeaza imaginea eroului in functie de directia de miscare.
     */
    private void UpdateImage()
    {
        // Mapare corecta bazata pe organizarea sprite sheet-ului:
        // W = spate (randul 0), S = fata (randul 2), D = dreapta (randul 3)
        // A = foloseste acelasi sprite ca D (randul 3) pentru corpul spre dreapta

        if(refLink.GetKeyManager().up) // W - cu spatele
        {
            image = Assets.heroBack;    // Randul 0
        }
        else if(refLink.GetKeyManager().left) // A - cu corpul spre stanga
        {
            image = Assets.heroLeft;   // Randul 1
        }
        else if(refLink.GetKeyManager().down) // S - cu fata
        {
            image = Assets.heroFront;   // Randul 2
        }
        else if(refLink.GetKeyManager().right) // D - normal spre dreapta
        {
            image = Assets.heroRight;   // Randul 3
        }
        // Daca nu se apasa nicio tasta, pastreaza imaginea curenta
    }

    /*! \fn private void GetInput()
        \brief Verifica daca a fost apasata o tasta din cele stabilite pentru controlul eroului.
     */
    private void GetInput()
    {
        ///Implicit eroul nu trebuie sa se deplaseze daca nu este apasata o tasta
        xMove = 0;
        yMove = 0;
        ///Verificare apasare tasta "sus"
        if(refLink.GetKeyManager().up)
        {
            yMove = -speed;
        }
        ///Verificare apasare tasta "jos"
        if(refLink.GetKeyManager().down)
        {
            yMove = speed;
        }
        ///Verificare apasare tasta "stanga" (A)
        if(refLink.GetKeyManager().left)
        {
            xMove = -speed; // Valoare negativa = miscare spre stanga
        }
        ///Verificare apasare tasta "dreapta" (D)
        if(refLink.GetKeyManager().right)
        {
            xMove = speed; // Valoare pozitiva = miscare spre dreapta
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Randeaza/deseneaza eroul in noua pozitie.

        \brief g Contextul grafi in care trebuie efectuata desenarea eroului.
     */
    @Override
    public void Draw(Graphics g)
    {
        g.drawImage(image, (int)x, (int)y, width, height, null);

        ///doar pentru debug daca se doreste vizualizarea dreptunghiului de coliziune altfel se vor comenta urmatoarele doua linii
        //g.setColor(Color.blue);
        //g.fillRect((int)(x + bounds.x), (int)(y + bounds.y), bounds.width, bounds.height);
    }
}