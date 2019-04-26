
package vipir;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class Base {
	private static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 36);
	private static final Color FILL_COLOR = Color.WHITE;
	private static final Color EDGE_COLOR = Color.BLACK;
	private static String message;

	public static void main(String[] args) {
		message = "Hello World!";
		JFrame frame = new JFrame("Base");
		JPanel panel = new HelloPanel(message);
		frame.setBounds(50, 50, 600, 600);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	private static final class HelloPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private final String message;

		public HelloPanel(String message) {
			this.message = ((message != null) ? message : "");
		}

		public void paintComponent(Graphics g) {
			FontMetrics fm = g.getFontMetrics(FONT);
			int fw = fm.stringWidth(message);
			int fh = fm.getMaxAscent() + fm.getMaxDescent();
			int x = (getWidth() - fw) / 2;
			int y = (getHeight() - fh) / 2;
			Rectangle r = new Rectangle(x, y, fw + 4, fh + 1);
			if (FILL_COLOR != null) {
				g.setColor(FILL_COLOR);
				g.fillRect(r.x, r.y, r.width - 1, r.height - 1);
			}
			if (EDGE_COLOR != null) {
				g.setColor(EDGE_COLOR);
				g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
				g.setFont(FONT);
				g.drawString(message, r.x + 2, r.y + fm.getMaxAscent());
			}
		}
	}
}

//******************************************************************************
