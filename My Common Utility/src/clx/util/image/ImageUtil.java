/**
 * 
 */
package clx.util.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author chulx
 *
 */
public enum ImageUtil {
	INSTANCE;

	// with potential alpha channel data
	public byte [] getBytes (String filename) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
			return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	//
	public byte [] getBytesFromBufferedImage (BufferedImage image) {
		if (image == null)
			return null;
		
		byte [] data = null; 
		
		try {
			data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		} catch (Exception e) {
			if (e instanceof ClassCastException) {
				// rasterdata is Integer, which is a combination of RGB, using the lowest 24 bits
				// Java int is 32 bits
				int [] rasterdata = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
				data = new byte [image.getWidth() * image.getHeight() * 3];
				for (int i = 0; i < rasterdata.length; i++) {
					data[i*3] += (rasterdata[i] & 0x0000000000ff0000) >>> 16;
					data[i*3+1] += (rasterdata[i] & 0x000000000000ff00) >>> 8;
					data[i*3+2] += rasterdata[i] & 0x00000000000000ff;
				}

			}
		}

		return data;
	}
	
	// without alpha channel data
	public byte [] getRGBBytes (String filename) {
		BufferedImage image;
		try {
			image = ImageIO.read(new File(filename));
			byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			if (image.getAlphaRaster() != null) {
				// one pixel has 4 bytes : alpha, R, G, B. so need to get the last three bytes for every pixel
				int total = image.getWidth() * image.getHeight() * 3;
				byte [] rgb = new byte [total];
				int index;
				for (int i = 0; i < total; i=+3) {
					index = 4 * i / 3 + 1;
					rgb[i] = pixels[index];
					rgb[i+1] =pixels[index+1];
					rgb[i+2] = pixels[index+2];
				}
				
				return rgb;
			} else
				return pixels;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * scale the image so that the height will match the desired one.
	 * 
	 * @param img		the original image
	 * @param imageType https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html#TYPE_3BYTE_BGR
	 * @param newWidth	
	 * @param newHeight	
	 * @return
	 */
	public BufferedImage scaleToHeight (BufferedImage img, int imageType, int newWidth, int newHeight, boolean keepAspectRatio) {
	    BufferedImage dbi = null;
	    double scaleX, scaleY;
	    if(img != null && newHeight != img.getHeight()) {
	    	scaleY = (double)newHeight / img.getHeight();
	    	if (keepAspectRatio) {
	    		dbi = new BufferedImage(newWidth, newHeight, imageType);
	    		scaleX = scaleY;
	    	} else {
	    		dbi = new BufferedImage (newWidth, newHeight, imageType);
	    		scaleX = (double)newWidth / img.getWidth();
	    	}
	    	
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(scaleX, scaleY);
	        g.drawRenderedImage(img, at);
	    }
	    return dbi;
		
	}
}
