import javax.swing.JPanel;
import javax.swing.JSlider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements Runnable {

    public Thread drawThread;
    int FPS = 1;
    Hilbert h;
    JSlider FPSlider = new JSlider(3, 200);
    int index = 0;
    double drawInterval;

    ArrayList<Pair> pairs = new ArrayList<>();

    public DrawPanel() {
        drawThread = new Thread(this);
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBackground(Color.WHITE);
        h = new Hilbert();
        h.order = 8;

        for (int i = 0; i < Math.pow(4, h.order); i++) {
            pairs.add(h.getCoord(i));
        }

        // FPSlider.setSize(300, 40);
        // FPSlider.setLocation(new Point(0, 800));
        // this.add(FPSlider);
        // for (Pair pair : pairs) {
        // System.out.println(pair);
        // }

    }

    @Override
    public void run() {
        drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (drawThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                update();

                repaint(0, 0, 800, 800);

                delta--;
            }

        }
    }

    public void print(ArrayList<Pair> arr) {
        for (Pair i : arr) {
            System.out.println(i);
        }
    }

    public void update() {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1f));

        int offset = this.getWidth() / (int) Math.pow(2, h.order);
        int add = offset / 2;
        for (int i = 0; i < pairs.size() - 1; i++) {
            g2.drawLine(pairs.get(i).x * offset + add,
                    pairs.get(i).y * offset + add,
                    pairs.get(i + 1).x * offset + add,
                    pairs.get(i + 1).y * offset + add);
            // g2.drawString(i + "", pairs.get(i).x * offset + add + 10, pairs.get(i).y *
            // offset + add + 10);
        }

        g2.dispose();
    }

}
