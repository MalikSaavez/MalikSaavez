import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer; // 

public class GamePanel extends JPanel {
    private final int tileSize = 20;
    private final int tilesWide = 30;
    private final int tilesHigh = 30;

    private final Timer timer;
    private final Random rng = new Random();

    private final java.util.List<Point> snake = new ArrayList<>();
    private Point fruit;
    private String direction = "RIGHT";
    private boolean gameRunning = true;

    private final JLabel scoreboard;
    private int score = 0;
    private int highScore = 0;

    private Color snakeColor = Color.GREEN;
    private Color fruitColor = Color.RED;

    public GamePanel(JLabel scoreboard, int speed) {
        this.scoreboard = scoreboard;
        setPreferredSize(new Dimension(tileSize * tilesWide, tileSize * tilesHigh));
        setBackground(Color.BLACK);
        setFocusable(true);
        setupKeyControl();

        initGame();

        timer = new Timer(speed, e -> updateGame());
        timer.start();
    }

    public void restartGame() {
        initGame();
        requestFocusInWindow(); 
        timer.start();          
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        direction = "RIGHT";
        placeFruit();
        gameRunning = true;
        score = 0;
        snakeColor = Color.GREEN;
        updateScoreLabel();
        requestFocusInWindow(); 
    }

    private void placeFruit() {
        fruit = new Point(rng.nextInt(tilesWide), rng.nextInt(tilesHigh));
        fruitColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
    }

    private void updateScoreLabel() {
        scoreboard.setText("Score: " + score + "    High Score: " + highScore);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw devil fruit
        g.setColor(fruitColor);
        g.fillOval(fruit.x * tileSize, fruit.y * tileSize, tileSize, tileSize);

        
        for (Point p : snake) {
            int[] xPoints, yPoints;

            switch (direction) {
                case "UP" -> {
                    xPoints = new int[]{p.x * tileSize + tileSize / 2, p.x * tileSize, p.x * tileSize + tileSize};
                    yPoints = new int[]{p.y * tileSize, p.y * tileSize + tileSize, p.y * tileSize + tileSize};
                }
                case "DOWN" -> {
                    xPoints = new int[]{p.x * tileSize + tileSize / 2, p.x * tileSize, p.x * tileSize + tileSize};
                    yPoints = new int[]{p.y * tileSize + tileSize, p.y * tileSize, p.y * tileSize};
                }
                case "LEFT" -> {
                    xPoints = new int[]{p.x * tileSize, p.x * tileSize + tileSize, p.x * tileSize + tileSize};
                    yPoints = new int[]{p.y * tileSize + tileSize / 2, p.y * tileSize, p.y * tileSize + tileSize};
                }
                case "RIGHT" -> {
                    xPoints = new int[]{p.x * tileSize + tileSize, p.x * tileSize, p.x * tileSize};
                    yPoints = new int[]{p.y * tileSize + tileSize / 2, p.y * tileSize, p.y * tileSize + tileSize};
                }
                default -> {
                    xPoints = new int[]{0, 0, 0};
                    yPoints = new int[]{0, 0, 0};
                }
            }

            g.setColor(snakeColor);
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private void updateGame() {
        if (!gameRunning) return;

        Point head = snake.get(0);
        Point next = new Point(head);

        switch (direction) {
            case "UP" -> next.y--;
            case "DOWN" -> next.y++;
            case "LEFT" -> next.x--;
            case "RIGHT" -> next.x++;
        }

        
        if (next.x < 0 || next.x >= tilesWide || next.y < 0 || next.y >= tilesHigh || snake.contains(next)) {
            gameRunning = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over!\nFinal Score: " + score);
            return;
        }

        
        if (next.equals(fruit)) {
            snake.add(0, next);
            placeFruit();
            score++;
            if (score > highScore) highScore = score;
            snakeColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            updateScoreLabel();
        } else {
            snake.add(0, next);
            snake.remove(snake.size() - 1);
        }

        repaint();
    }

    private void setupKeyControl() {
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        if (!direction.equals("DOWN")) direction = "UP";
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (!direction.equals("UP")) direction = "DOWN";
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (!direction.equals("RIGHT")) direction = "LEFT";
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (!direction.equals("LEFT")) direction = "RIGHT";
                    }
                }
            }
        });
    }
}
