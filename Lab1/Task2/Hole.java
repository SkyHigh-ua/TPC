package Task2;
import java.awt.*;

public class Hole {
    private int x;
    private int y;
    private int size;

    public Hole(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y, size, size);
    }

    public boolean isBallInHole(int ballX, int ballY, int ballSize) {
        return (ballX + ballSize / 2 >= x && ballX + ballSize / 2 <= x + size &&
                ballY + ballSize / 2 >= y && ballY + ballSize / 2 <= y + size);
    }
}
