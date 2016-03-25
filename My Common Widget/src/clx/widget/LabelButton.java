/**
 * 
 */
package clx.widget;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * @author chulx
 *
 */
public class LabelButton extends JLabel {
	
	public LabelButton (String icon) {
		setIcon (new ImageIcon(this.getClass().getResource(icon)));
		addMouseListener (new MouseAdapter () {
			@Override
			public void mouseEntered (MouseEvent evt) {
				setCursor (new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseExited (MouseEvent evt) {
				setCursor (new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

}
