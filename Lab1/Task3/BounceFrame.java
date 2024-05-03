package Task3;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BounceFrame extends JFrame {

  private BallCanvas canvas;
  private JTextField redBallsField;
  private JTextField blueBallsField;
  public static final int WIDTH = 450;
  public static final int HEIGHT = 350;

  public BounceFrame() {
    this.setSize(WIDTH, HEIGHT);
    this.setTitle("Bounce program Task 3");
    this.canvas = new BallCanvas();
    System.out.println(
      "In Frame Thread name = " + Thread.currentThread().getName()
    );
    Container content = this.getContentPane();
    content.add(this.canvas, BorderLayout.CENTER);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.lightGray);

    redBallsField = new JTextField(5);
    blueBallsField = new JTextField(5);
    JLabel redLabel = new JLabel("Red Balls:");
    JLabel blueLabel = new JLabel("Blue Balls:");

    JButton buttonStart = new JButton("Start");
    JButton buttonStop = new JButton("Stop");
    buttonStart.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int redBalls = Integer.parseInt(redBallsField.getText());
          int blueBalls = Integer.parseInt(blueBallsField.getText());

          canvas.clear();

          for (int i = 0; i < redBalls; i++) {
            Ball redBall = new Ball(canvas, Color.RED);
            canvas.add(redBall);
            BallThread redThread = new BallThread(redBall);
            redThread.start();
          }
          for (int i = 0; i < blueBalls; i++) {
            Ball blueBall = new Ball(canvas, Color.BLUE);
            canvas.add(blueBall);
            BallThread blueThread = new BallThread(blueBall);
            blueThread.start();
          }
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

    buttonPanel.add(redLabel);
    buttonPanel.add(redBallsField);
    buttonPanel.add(blueLabel);
    buttonPanel.add(blueBallsField);
    buttonPanel.add(buttonStart);
    buttonPanel.add(buttonStop);

    content.add(buttonPanel, BorderLayout.SOUTH);
  }
}