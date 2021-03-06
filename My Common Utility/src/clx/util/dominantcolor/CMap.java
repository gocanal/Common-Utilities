/**
 * 
 */
package clx.util.dominantcolor;

import java.util.ArrayList;

/**
 * @author chulx
 *
 */
public class CMap {
    public final ArrayList<VBox> vboxes = new ArrayList<VBox>();

    public void push(VBox box)
    {
        vboxes.add(box);
    }

    public int[][] palette()
    {
        int numVBoxes = vboxes.size();
        int[][] palette = new int[numVBoxes][];
        for (int i = 0; i < numVBoxes; i++)
        {
            palette[i] = vboxes.get(i).avg(false);
        }
        return palette;
    }

    public int size()
    {
        return vboxes.size();
    }

    public int[] map(int[] color)
    {
        int numVBoxes = vboxes.size();
        for (int i = 0; i < numVBoxes; i++)
        {
            VBox vbox = vboxes.get(i);
            if (vbox.contains(color))
            {
                return vbox.avg(false);
            }
        }
        return nearest(color);
    }

    public int[] nearest(int[] color)
    {
        double d1 = Double.MAX_VALUE;
        double d2;
        int[] pColor = null;

        int numVBoxes = vboxes.size();
        for (int i = 0; i < numVBoxes; i++)
        {
            int[] vbColor = vboxes.get(i).avg(false);
            d2 = Math.sqrt(Math.pow(color[0] - vbColor[0], 2)
                    + Math.pow(color[1] - vbColor[1], 2)
                    + Math.pow(color[2] - vbColor[2], 2));
            if (d2 < d1)
            {
                d1 = d2;
                pColor = vbColor;
            }
        }
        return pColor;
    }

}
