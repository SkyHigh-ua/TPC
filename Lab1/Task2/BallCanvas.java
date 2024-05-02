package Task2;
import java.awt.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

public class BallCanvas extends JPanel {
    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private ArrayList<Hole> holes = new ArrayList<>();
    private int holeCounter = 0;
    private JLabel counterLabel;

    public BallCanvas(JLabel label) {
        this.counterLabel = label;
        
        holes.add(new Hole(0, 0, 20)); 
        holes.add(new Hole(430, 235, 20));
        holes.add(new Hole(430, 0, 20));
        holes.add(new Hole(0, 235, 20));
        holes.add(new Hole(225, 0, 20));
        holes.add(new Hole(225, 235, 20));
    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }

    public void add(Ball b) {
        this.balls.add(b);
    }

    public void removeBall(Ball b) {
        this.balls.remove(b);
        holeCounter++;
        updateCounterLabel();
        this.repaint();
    }

    private void updateCounterLabel() {
        counterLabel.setText("Balls in the hole: " + holeCounter);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        for (Hole hole : holes) {
            hole.draw(g2);
        }

        for (Ball b : balls) {
            b.draw(g2);
        }
    }
}
