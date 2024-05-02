package Task1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BounceFrame extends JFrame {

  private BallCanvas canvas;
  public static final int WIDTH = 450;
  public static final int HEIGHT = 350;

  public BounceFrame() {
    this.setSize(WIDTH, HEIGHT);
    this.setTitle("Bounce program");
    this.canvas = new BallCanvas();
    System.out.println(
      "In Frame Thread name = " + Thread.currentThread().getName()
    );
    Container content = this.getContentPane();
    content.add(this.canvas, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.lightGray);
    JButton buttonStart = new JButton("Start");
    JButton buttonStop = new JButton("Stop");
    buttonStart.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println(
            Thread.currentThread().getName()
          );
          Ball b = new Ball(canvas);
          canvas.add(b);

          BallThread thread = new BallThread(b);
          thread.start();
          System.out.println("Thread name = " + thread.getName());
        }
      }
    );
    buttonStop.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      }
    );

    buttonPanel.add(buttonStart);
    buttonPanel.add(buttonStop);

    content.add(buttonPanel, BorderLayout.SOUTH);
  }
}
