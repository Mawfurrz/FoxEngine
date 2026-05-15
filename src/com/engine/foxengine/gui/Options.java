package com.engine.foxengine.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.engine.foxengine.Configuration;

public class Options extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private int width = 650;
	private int height = 450;
	private JButton OK;
	private JTextField twidth, theight;
	private JLabel lwidth, lheight;
	private Rectangle rOK, rresolution;
	private Choice resolution = new Choice();
	Configuration config = new Configuration();
	
	int w = 0;
	int h = 0;
	
	private int button_width = 100;
	private int button_height = 40;
	

	JPanel window = new JPanel();
	
	public Options() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//setUndecorated(true);
		setTitle("Options - FoxEngine Launcher");
		setSize(new Dimension(width, height));
		add(window);
		setLocationRelativeTo(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		drawButtons();
		repaint();
	}
	

	private void drawButtons() {
		OK = new JButton("OK");
		rOK = new Rectangle((width-120), (height-70), button_width, button_height - 20);
		OK.setBounds(rOK);
		this.window.add(OK);
		
		rresolution = new Rectangle(50, 80, 100, 25);
		resolution.setBounds(rresolution);
		resolution.add("640, 480");
		resolution.add("800, 600");
		resolution.add("1024, 768");
		resolution.add("1280, 720");
		resolution.select(0);
		window.add(resolution);
		
		lwidth = new JLabel("Width: ");
		lwidth.setBounds(30, 150, 120, 20);
		window.add(lwidth);
		
		lheight = new JLabel("Height: ");
		lheight.setBounds(30, 180, 120, 20); 
		window.add(lheight);
		
		twidth = new JTextField();
		twidth.setBounds(90, 150, 60, 20);
		window.add(twidth);
		
		theight = new JTextField();
		theight.setBounds(90, 180, 60, 20);
		window.add(theight);
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				//new Launcher(0);
				config.saveConfiguration("width", parseWidth());
				config.saveConfiguration("height", parseHeight());
			}
		});
	}
	
	private void drop() {
		int selection = resolution.getSelectedIndex();
		if (selection == 0) {
			w = 640;
			h = 480;
		}
		
		if (selection == 1 || selection == -1) {
			w = 800;
			h = 600;
		}
		
		if (selection == 2) {
			w = 1024;
			h = 768;
		}
		
		if (selection == 3) {
			w = 1280;
			h = 720;
		}
	}
	
	private int parseWidth() {
		try {
		int w = Integer.parseInt(twidth.getText());
		return w;
		} catch (NumberFormatException e) {
			drop();
			return w;
		}
	}
	
	private int parseHeight() {
		try {
		int h = Integer.parseInt(theight.getText());
		return h;
		} catch (NumberFormatException e) {
			drop();
			return h;
		}
	}
}
