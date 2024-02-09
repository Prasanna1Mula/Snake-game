import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private static final int GAME_SPEED = 100;

    private LinkedList<Point> snake;
    private Point food;
    private int direction; // 0: up, 1: right, 2: down, 3: left

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snake = new LinkedList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        generateFood();

        direction = 1; // initially moving to the right

        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_SIZE);
        int y = rand.nextInt(GRID_SIZE);
        food = new Point(x, y);

        // Make sure food does not appear on the snake
        while (snake.contains(food)) {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
            food.setLocation(x, y);
        }
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 0:
                newHead.y--;
                break;
            case 1:
                newHead.x++;
                break;
            case 2:
                newHead.y++;
                break;
            case 3:
                newHead.x--;
                break;
        }

        // Check collision with walls
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE) {
            gameOver();
            return;
        }

        // Check collision with itself
        if (snake.contains(newHead)) {
            gameOver();
            return;
        }

        snake.addFirst(newHead);

        // Check if the snake ate the food
        if (newHead.equals(food)) {
            generateFood();
        } else {
            snake.removeLast();
        }

        repaint();
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Change direction based on arrow keys
        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != 2) {
                    direction = 0;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 3) {
                    direction = 1;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 0) {
                    direction = 2;
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 1) {
                    direction = 3;
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.clearRect(0, 0, getWidth(), getHeight());

        // Draw the snake
        for (Point p : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw the food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}

