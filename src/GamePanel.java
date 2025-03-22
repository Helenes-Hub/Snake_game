import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[]=new int[GAME_UNITS];
    final int y[]= new int[GAME_UNITS];
    int bodyParts= 2;
    int applesEaten;
    int appleX;
    int appleY;
    char direction='R';
    boolean running=false;
    Timer timer;
    Random random;


    public GamePanel() {
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!running) {
                    restartGame();
                }
            }
        });

        startGame();
    }
    public void startGame(){
        newApple();
        running=true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
        g.setColor(Color.GREEN);
        g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i=0; i<bodyParts; i++){
            if (i==0){
                g.setColor(Color.MAGENTA);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(Color.MAGENTA);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        if(running) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }

    public void newApple(){
        appleX=random.nextInt((int)SCREEN_WIDTH/(int)UNIT_SIZE)*UNIT_SIZE;
        appleY=random.nextInt((int)SCREEN_HEIGHT/(int)UNIT_SIZE)*UNIT_SIZE;
    }


    public void move(){
        for (int i=bodyParts; i>0; i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }

        switch(direction){
        case 'U':
            y[0]=y[0]-UNIT_SIZE;
            break;
        case 'D':
            y[0]=y[0]+UNIT_SIZE;
            break;
        case 'L':
            x[0]=x[0]-UNIT_SIZE;
            break;
        case 'R':
            x[0]=x[0]+UNIT_SIZE;
            break;
        }
    }

    public void checkApple(){

        for (int i=bodyParts; i>0; i--){
            if (x[0]==appleX && y[0]==appleY){
                newApple();
                bodyParts++;
                applesEaten++;
            }
        }
    }

    public void checkCollision(){
        //collision with body
        for (int i=bodyParts; i>0; i--){
            if (x[0] == x[i] && y[0] == y[i]){
                System.out.println("1");
                running=false;
                break;
            }
            //collision left
            if (x[0]<0){
                System.out.println("2");
                running=false;
                break;
            }
            //collision right
            if (x[0]>= SCREEN_WIDTH){
                System.out.println("3");
                running=false;
                break;
            }
            //collision top
            if (y[0]<0){
                System.out.println("4");
                running=false;
                break;
            }
            //collision bottom
            if (y[0]>= SCREEN_HEIGHT){
                System.out.println("5");
                running=false;
                break;
            }

        }
        if (!running){
            timer.stop();}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            move();
            checkApple();
            checkCollision();

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_W:
                    if(direction!='D')
                        direction='U';
                    break;
                case KeyEvent.VK_S:
                    if(direction!='U')
                        direction='D';
                    break;
                case KeyEvent.VK_A:
                    if(direction!='R')
                        direction='L';
                    break;
                case KeyEvent.VK_D:
                    if(direction!='L')
                        direction='R';
                    break;
            }

        }
    }
    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2 - 50);


        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
                SCREEN_HEIGHT / 2);

        g.setFont(new Font("Ink Free", Font.PLAIN, 30));
        g.setColor(Color.white);
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Click anywhere to play again",
                (SCREEN_WIDTH - metrics3.stringWidth("Click anywhere to play again")) / 2,
                SCREEN_HEIGHT / 2 + 60);
    }
    public void restartGame() {
        applesEaten = 0;
        bodyParts = 2;
        direction = 'R';
        running = true;
        for (int i = 0; i < GAME_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame();
    }

}
