package Task4;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 450;
    public static final int HEIGHT = 350;

    public BounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program Task 4");
        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);
        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ball blueBall = new Ball(canvas, Color.BLUE);
                Ball redBall = new Ball(canvas, Color.RED);
                canvas.add(blueBall);
                canvas.add(redBall);
        
                Thread ballThreadsController = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BallThread redThread = new BallThread(redBall);
                        BallThread blueThread = new BallThread(blueBall);
                        try {
                            redThread.start();
                            redThread.join();
                            blueThread.start();
                            blueThread.join();
                        } catch (InterruptedException ex) {}
                    }
                });
                ballThreadsController.start();
            }
        });
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
