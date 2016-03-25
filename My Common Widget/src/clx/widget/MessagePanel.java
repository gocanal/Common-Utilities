package clx.widget;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

public class MessagePanel extends JPanel {
	private JTextArea area;
	
	/**
	 * Create the panel.
	 */
	public MessagePanel() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		area = new JTextArea();
		area.setEditable(false);
		((DefaultCaret)area.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(area);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
		add(scrollPane);

	}
	
	public void write (String s) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void> () {

			@Override
			protected Void doInBackground() throws Exception {
				area.append(s);
				return null;
			}
			
		};
		worker.execute();
	}
	
	public void writeln (String s) {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void> () {

			@Override
			protected Void doInBackground() throws Exception {
				synchronized(this) {
					area.append(s+"\n");
				}
				return null;
			}
			
		};
		worker.execute();
		
	}
}
