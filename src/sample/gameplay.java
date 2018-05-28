package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class gameplay extends JPanel implements KeyListener, ActionListener {

    //deklaration av alla variabler jag kommer att använda.

    private boolean play = false;
    private int score = 0;
    private int streak = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 150;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private int ballwidth = 20;
    private int ballheight = 20;
    private int paddlewidth = 100;
    private int studskoff = paddlewidth/2;
    private MapGenerator map;

    public gameplay() {
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    //Här rutar jag ut de olika komponenterna av spelfältet
    public void paint(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1,1,692,592);

        //Drawing Map
        map.draw((Graphics2D)g);

        //scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 500,30);
        g.drawString(""+streak, 560, 30);

        //borders
        g.setColor(Color.BLACK);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //the paddle
        g.setColor(Color.CYAN);
        g.fillRect(playerX,550,paddlewidth,8);

        //Bollen
        g.setColor(Color.WHITE);
        g.fillOval(ballposX,ballposY,ballwidth,ballheight);

        if(ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Du stortorskade, syyyyyyyyyyyyynd",110,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Synd, du förlorade. Tryck enter för att starta om!",150,350);
        }
        if(totalBricks == 0){
            play = false;

            g.setColor(Color.RED);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Alla bricks eleminerade! B",200,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Du vann! Grattisen är göda!",200,350);

        }
        g.dispose();
    }
    //
    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play) {
            if (new Rectangle(ballposX, ballposY, ballwidth, ballheight).intersects(new Rectangle(playerX, 550, paddlewidth, 8))) {
                ballYdir = -ballYdir;
            }
           A: for(int i = 0; i<map.map.length; i++){
                for(int j = 0; j<map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                       int brickX = j* map.brickWidth + 80;
                       int brickY = i * map.brickHeight + 50;
                       int brickWidth = map.brickWidth;
                       int brickHeight = map.brickHeight;

                       Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                       Rectangle ballrect = new Rectangle(ballposX,ballposY,ballwidth,ballheight);
                       Rectangle brickRect = rect;

                       if(ballrect.intersects(brickRect)){
                           map.setBrickValue(0,i,j);
                           totalBricks--;
                           score += 5;

                           if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                               ballXdir = -ballXdir;
                           }else{
                               ballYdir = -ballYdir;
                           }
                           if(totalBricks < 10){
                               ballheight = 40;
                               ballwidth = 40;
                           }if(totalBricks < 15){
                               paddlewidth = 200;
                           }
                           break A;
                       }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0){
                ballXdir = -ballXdir;
            }
            if(ballposY < 0){
                ballYdir = -ballYdir;
            }
            if(ballposX > 670){
                ballXdir = -ballXdir;
            }
        }

        repaint();
    }
    //lägger till keylistners för att kunna röra paddan till höger och vänster
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    //Vad som händer då man trycker höger och vänstertangenterna (Man flyttar höger och vänster med paddeln)
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600){
                playerX = 600;
            }else{
               moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){

                //Då du har klarat banan eller förlorat så kommer det fram text, då kan du trycka enter för att starta om och denna metod
                //återställer allt till som det ska vara från början


                int maximum;
                int minimum;
                int randomNum;
                int maxatnum;
                int minstanum;
                int slumpnum;

                maximum = 400;
                minimum = 300;
                maxatnum = 550;
                minstanum = 50;
                String mittnr;

                randomNum = minimum + (int) (Math.random()*maximum);
                slumpnum = minstanum + (int) (Math.random()*maxatnum);
                play = true;
                ballposX = slumpnum;
                ballposY = randomNum;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                ballwidth = 20;
                ballheight = 20;
                paddlewidth = 100;
                map = new MapGenerator(3,7);
                streak++;

                repaint();

            }
        }
    }
    //Flytta höger och vänster
    public void moveRight() {
        play=true;
        playerX+=20;
    }
    public void moveLeft() {
        play=true;
        playerX-=20;
    }
}