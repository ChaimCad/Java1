import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

class JogoBase extends JFrame {
  Timer T, B;
  Random r;
  final int FUNDO = 0;
  final int GOL1 = 1;
  final int JOGADOR = 2;
  final int BOLA = 3;
  final int res_c = 5, res_p = 4;

  int i = 10;
  int tamY;
  int posX = 100;
  int posY;
  int bolX = 900;
  int bolY;
  int velX, velY, aux;
  int g = 60;
  int jog = 50, bol = 35;
  
  boolean bola = true, air = false;

  Image img[] = new Image[4];
  Desenho des = new Desenho();

  class Desenho extends JPanel {

    Desenho() {
      try {
        setPreferredSize(new Dimension(1000, 600));
        img[FUNDO] = ImageIO.read(new File("fundo.jpeg"));
        img[GOL1] = ImageIO.read(new File("gol1.jpeg"));
        img[JOGADOR] = ImageIO.read(new File("jogador.jpg"));
        img[BOLA] = ImageIO.read(new File("bola.jpeg"));
        // tamY = getSize().height - img[JOGADOR].getHeight(this) - 142;
        tamY = 408;
        //bolY = getSize().height - img[BOLA].getHeight(this) - 142;
        bolY = 412;
        posY = tamY;
        velX = 0;
        velY = 0;
        B = new Timer(30, new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            bolX -= velX;
            bolY -= velY;
            velY--;
            repaint();
            
            if(bolX <= 0) {
              velX -= res_p;
              velX *= -1;
              if(aux == -velX + res_p) bola = true;
              aux = velX;
            }
            if(bolX >= 980) {
              velX += res_p;
              velX *= -1;
              if(aux == -velX - res_p) bola = true;
              aux = velX;
            }
            if(velY < 0) air = false;
            if(bolY >= 416 && !air) {
              aux = velY;
              velY += res_c;
              velY *= -1;
              if((velY > 0 && aux > 0) || (velY < 0 && aux < 0)) bola = true;
              air = true;
            }
          }
        });
        r = new Random();
      } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "A imagem não pode ser carregada!\n" + e, "Erro",
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
      }
    }

    public void paintComponent(Graphics g) {
      //super.paintComponent(g);
      g.drawImage(img[FUNDO], 0, 0, getSize().width, getSize().height, this);
      g.drawImage(img[GOL1], 0, getSize().height - img[GOL1].getHeight(this) - 130, this);
      g.drawImage(img[JOGADOR], posX, posY, jog, jog, this);
      g.drawImage(img[BOLA], bolX, bolY, bol, bol, this);
      Toolkit.getDefaultToolkit().sync();
    }
  }

  class TrataTeclas extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_D && posX < 960) {
        posX += 10;
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_A && posX > -10) {
        posX -= 10;
        repaint();
      } else if (e.getKeyCode() == KeyEvent.VK_SPACE && posY == tamY) {
          i = 15;
          T = new Timer(30, new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                posY -= i;
                repaint();
                i--;
                if(i == -16)
                {
                  T.stop();
                }
              }
          });
          T.start();
      }
    }
  }

  JogoBase() {
    super("Trabalho");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    add(des);
    addKeyListener(new TrataTeclas());
    pack();
    setVisible(true);
    new Timer(100, new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        if(bola) {
          bolX = 900;
          bolY = 412;

          velX = r.nextInt(16) + 15;
          velY = r.nextInt(16) + 10;

          bola = false;
          B.start();
        }
      }
    }).start();
  }

  public static void main(String[] args) {
    new JogoBase();
  }
}