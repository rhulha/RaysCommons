package net.raysforge.commons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class SwingUtils {

	public static void setSystemLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void centerFrame(Frame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	 public static JMenuItem buildMenuItem(String text, String actioncmd, ActionListener al)
	    {
	        JMenuItem mi = new JMenuItem();
	        mi.setBackground(Color.lightGray);
	        mi.setText(text);
	        mi.setActionCommand(actioncmd);
	        mi.addActionListener(al);
	        return mi;
	    }

	public static void hideMouseCursor(JFrame frame) {
		BufferedImage cur = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cur, new Point(0, 0), ""));
	}

	public static File getFileFromDialog(JFrame frame) {
		JFileChooser jFileChooser = new JFileChooser();
		if (jFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			return jFileChooser.getSelectedFile();
		} else {
			return null;
		}
	}

}
