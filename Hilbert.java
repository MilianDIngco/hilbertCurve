import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class Hilbert {

  HashMap<Integer, Pair> hm = new HashMap<>();

  int order = 1;
  Pair[] order1 = {
      new Pair(0, 0), new Pair(0, 1),
      new Pair(1, 1), new Pair(1, 0)
  };
  int[][] arr;

  int[][] getSquareArray(int order) {
    int size = (int) Math.pow(2, order);

    int[][] res = new int[size][size];

    for (int i = 0; i < size; i++) {
      for (int n = 0; n < size; n++) {
        res[i][n] = i + n;
      }
    }

    return res;
  }

  Pair getCoord(int index) {
    // bounds checking; Number of points is ( 2 ^ order ) ^ 2 since it's a square
    // array for now
    if (index >= Math.pow(4, order))
      return null;

    // Depth of recursion is mapped by (log base 4) + 1
    // Index < 4 : order 1
    // Index < 16 : order 2 ...
    int depth = order;

    int quadrant = index & 3;
    Pair coord = new Pair(order1[quadrant]);
    // Start at order 1
    for (int i = 1; i < depth; i++) {
      // Gets if in quadrant 0, 1, 2, or 3
      index >>= 2;
      int nextq = index & 3;

      // First quadrant ? Reflect across y = x
      // Multiply by matrix
      // [ 0 1 ]
      // [ 1 0 ]
      // Equivalent to swapping x and y
      if (nextq == 0) {
        int temp = coord.y;
        coord.y = coord.x;
        coord.x = temp;
        // Fourth quadrant ?
      } else if (nextq == 3) {
        // Translate to center the origin
        double dist = (Math.pow(2, i) - 1) / 2.0; // dist is half the width of a quadrant

        double xTranslated = ((double) coord.x) - dist;
        double yTranslated = ((double) coord.y) - dist;
        double temp = yTranslated;
        // Swap and take the negatives
        yTranslated = -xTranslated;
        xTranslated = -temp;
        // Translate back to regular
        coord.x = (int) (xTranslated + dist);
        coord.y = (int) (yTranslated + dist);
      }

      // Right quadrants ?
      // Add 2^order to x
      // Moves coordinate over by half of the width of the new square
      if (nextq == 2 || nextq == 3) {
        coord.x += Math.pow(2, i);
      }

      // Bottom quadrants ?
      // Add 2^order to y
      // Moves coordinate over by half of the width of the new square
      if (nextq == 1 || nextq == 2) {
        coord.y += Math.pow(2, i);
      }
    }
    return coord;
  }

  public static void main(String[] args) {

    JFrame frame = new JFrame("Hilbert");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(800, 900));

    DrawPanel dp = new DrawPanel();
    dp.drawThread.start();

    frame.add(dp);
    frame.setVisible(true);
  }

}
