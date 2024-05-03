package Task3;
import java.awt.*;
import java.awt.geom.*;

public class Ball {

  private Component canvas;
  private static final int XSIZE = 20;
  private static final int YSIZE = 20;
  private int x = 0;
  private int y = 0;
  private int dx = 2;
  private int dy = 2;
  private boolean stopped = false;
  private Color color;

  public Ball(Component c, Color color) {
    this.canvas = c;
    this.color = color;
    if(color == Color.RED) {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    } else {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }
  }

  public void draw(Graphics2D g2) {
    g2.setColor(color);
    g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
  }

  public void move() {
    if (stopped) {
        Thread.currentThread().interrupt();
    }
    x += dx;
    y += dy;
    if (x < 0) {
      x = 0;
      dx = -dx;
    }
    if (x + XSIZE >= this.canvas.getWidth()) {
      x = this.canvas.getWidth() - XSIZE;
      dx = -dx;
    }
    if (y < 0) {
      y = 0;
      dy = -dy;
    }
    if (y + YSIZE >= this.canvas.getHeight()) {
      y = this.canvas.getHeight() - YSIZE;
      dy = -dy;
    }
    this.canvas.repaint();
  }

  public void stop() {
    stopped = true;
  }
}
