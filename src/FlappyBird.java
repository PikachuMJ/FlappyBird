import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

@SuppressWarnings("ALL")
public class FlappyBird extends JFrame implements ActionListener, KeyListener {
    private final Timer timer;
    private int birdY = 200;
    private int birdVel = 0;
    private final int gravity = 2;
    private final int gap = 200;
    private final int pipeWidth = 50;
    private int pipeHeight = 400;
    private int pipeX = 800;
    private int score = 0;
    private boolean gameOver = false;

    private BufferedImage birdImage;

    private final Image[] offScrIma = new Image[3];
    private int currBuffer = 0;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);

        try {
            birdImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("Bird.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer = new Timer(20, this);
        timer.start();

        setVisible(true);
    }

    public void paint(Graphics g) {
        if (offScrIma[currBuffer] == null) {
            offScrIma[currBuffer] = createImage(getWidth(), getHeight());
        }
        Graphics offScrGra = offScrIma[currBuffer].getGraphics();
        offScrGra.setColor(Color.CYAN);
        offScrGra.fillRect(0, 0, getWidth(), getHeight());

        offScrGra.setColor(Color.GREEN);
        offScrGra.fillRect(pipeX, 0, pipeWidth, pipeHeight);
        offScrGra.fillRect(pipeX, pipeHeight + gap, pipeWidth, getHeight() - pipeHeight - gap);

        offScrGra.drawImage(birdImage, 100, birdY, null);

        offScrGra.setColor(Color.BLACK);
        offScrGra.setFont(new Font("Arial", Font.PLAIN, 30));
        offScrGra.drawString("Score: " + score, 50, 70);

        if (gameOver) {
            offScrGra.setFont(new Font("Arial", Font.BOLD, 60));
            offScrGra.drawString("Game Over", 250, getHeight() / 2);
        }

        g.drawImage(offScrIma[currBuffer], 0, 0, this);
    }

    public void actionPerformed(ActionEvent e) {
        currBuffer = (currBuffer + 1) % 3;

        if (!gameOver) {
            birdVel += gravity;
            birdY += birdVel;

            pipeX -= 5;
            if (pipeX + pipeWidth < 0) {
                pipeX = getWidth();
                pipeHeight = (int) (Math.random() * 300) + 100;
                score++;
            }

            if (birdY < 0 || birdY > getHeight()) {
                gameOver = true;
            } else if (pipeX < 150 && pipeX + pipeWidth > 100 && (birdY < pipeHeight || birdY > pipeHeight + gap)) {
                gameOver = true;
            }
        }

        // Refresh screen
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                birdVel = -20;
            } else {
                restart();
            }
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    private void restart() {
        birdY = 200;
        birdVel = 0;
        pipeX = 800;
        score = 0;
        gameOver = false;
        repaint();
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}
