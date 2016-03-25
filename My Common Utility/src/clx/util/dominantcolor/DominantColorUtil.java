/**
 * 
 */
package clx.util.dominantcolor;

import java.awt.image.BufferedImage;

/**
 * @author chulx
 *
 */
public enum DominantColorUtil {
	INSTANCE;
	
	/**
	 * 
	 * @param image the input image
	 * @return array of integer : [0] Red, [1] Green, [2] Blue
	 */
	public int [] getDominantColor (BufferedImage image) {
        CMap result = ColorThief.INSTANCE.getColorMap(image, 5);
        if (result != null) {
        	VBox dominantColor = result.vboxes.get(0);
        	return dominantColor.avg(false);
        } else {
        	// a special case if the while image is white, like https://farm8.static.flickr.com/7341/16319118027_37744664a7_s.jpg
        	int [] rgb = new int [3];
        	rgb[0] = 255;
        	rgb[1] = 255;
        	rgb[2] = 255;
        	return rgb;
        }
        	
	}
}
