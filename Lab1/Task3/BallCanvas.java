package Task3;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class BallCanvas extends JPanel {

  private ArrayList<Ball> balls = new ArrayList<>();

  public void add(Ball b) {
    this.balls.add(b);
  }

  public void clear() {
    for (Ball ball : balls) {
      ball.stop();
    }
    balls.clear();
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    for (int i = 0; i < balls.size(); i++) {
      Ball b = balls.get(i);
      b.draw(g2);
    }
  }
}