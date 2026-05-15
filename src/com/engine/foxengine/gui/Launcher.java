package com.engine.foxengine.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.Point;
//import java.awt.Point;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.engine.foxengine.Configuration;
//import com.engine.foxengine.Display;
import com.engine.foxengine.RunGame;
import com.engine.foxengine.input.InputHandler;

public class Launcher extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	protected JPanel window = new JPanel();
	//private JButton play, options, help, quit;
	//private Rectangle rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();
	static Thread thread;
	InputHandler input;
	
	private int width = 800;
	private int height = 400;
	protected int button_width = 100;
	protected int button_height = 40;
	boolean running = false;
	JFrame frame = new JFrame();
	
	public Launcher(int id) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setUndecorated(true);
		setTitle("FoxEngine Launcher");
		setSize(new Dimension(width, height));
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		
		if (id == 0) {
			//drawButtons();
		}
		
		input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		
		//renderMenu();
		startMenu();
		//display.start();
		//repaint();
		//SwingUtilities.updateComponentTreeUI(window);
		
	}
	
	@SuppressWarnings("static-access")
	private void renderMenu() throws IllegalStateException {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 800, 400);
		try {
			g.drawImage(ImageIO.read(Launcher.class.getResource("/launcher_Image.png")), 0, 0, 800, 400, null);
			
			// PLAY BUTTON
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 60 && InputHandler.MouseY < 60+30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/playOn.png")), 650, 60, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/menuArrow.png")), 720, 60, 30, 30, null);
				if (InputHandler.MouseButton == 1) {
					config.loadConfiguration("res/settings/config.xml");
					dispose();
					new RunGame();
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/playOff.png")), 650, 60, 80, 30, null);
			}
			
			// OPTIONS BUTTON
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 150 && InputHandler.MouseY < 150+30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/optionsOn.png")), 650, 150, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/menuArrow.png")), 730, 150, 30, 30, null);
				if (InputHandler.MouseButton == 1) {
					try {
						thread.sleep(250); // TEMPORARY SOLUTION: will not prevent multiple instances of class
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					new Options();
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/optionsOff.png")), 650, 150, 80, 30, null);
			}
			
			// HELP BUTTON
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 240 && InputHandler.MouseY < 240+30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/helpOn.png")), 650, 240, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/menuArrow.png")), 720, 240, 30, 30, null);
				if (InputHandler.MouseButton == 1) {
					
					// This works here, because it makes sure that thousands of instances
					// aren't open within a short amount of time.
					// but it does force a set time for things to be executed.
					try {
						thread.sleep(250);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//System.out.println("Help!");
					try {
						if (!Desktop.isDesktopSupported()) {
							System.err.println("Desktop isn't supported");
							return;
						}
						Desktop desktop = Desktop.getDesktop();
						
						if (!desktop.isSupported(Desktop.Action.BROWSE)) {
							System.err.println("Browsing is not supported on this platform.");
			                return;
						}
						
						// CHANGE TO THE GITHUB PAGE WHEN FINISHED!!!!
						URI uri = new URI("https://github.com/Mawfurrz/FoxEngine");
						desktop.browse(uri);
					} catch (URISyntaxException e) {
						System.err.println("Invalid URL format");
					}
				}
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/helpOff.png")), 650, 240, 80, 30, null);
			}
			
			// QUIT BUTTON
			if (InputHandler.MouseX > 650 && InputHandler.MouseX < 650 + 80 && InputHandler.MouseY > 330 && InputHandler.MouseY < 330+30) {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/quitOn.png")), 650, 330, 80, 30, null);
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/menuArrow.png")), 720, 330, 30, 30, null);
				if (InputHandler.MouseButton == 1) {
					System.exit(0);
				}
				
			} else {
				g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/quitOff.png")), 650, 330, 80, 30, null);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		//g.setColor(Color.WHITE);
		//g.setFont(new Font("Cascadia Code", 0, 30));
		//g.drawString("Play", 720, 90);
		g.dispose();
		bs.show();
	}
	
	public void updateFrame() {
		//System.out.println("updateFrame method running!");
		//System.out.println("X: " + InputHandler.MouseX + " Y: " + InputHandler.MouseY + " | DX: " + InputHandler.MouseDX + " DY: " + InputHandler.MouseDY);
		if (InputHandler.dragged) {
			Point p = getLocation();
			setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, p.y + InputHandler.MouseDY - InputHandler.MousePY);
		}
	}
	
	public synchronized void startMenu() {
		if (running) {
			return;
		}
		
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}
	
	public synchronized void stopMenu() {
		if (!running) {
			return;
		}
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		requestFocus();
		// TODO Auto-generated method stub
		//renderMenu();
		while (running) {
			//System.out.println("Running menu Thread!");
			try {
				renderMenu();
			} catch (IllegalStateException e) {
				//e.printStackTrace();
				//System.out.println("Handled exception");
			}
			updateFrame();
			
			try {
				thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	// NOT NEEDED ANYMORE
	/*@SuppressWarnings("unused")
	private void drawButtons() {
		// Creating and adding buttons to window
		play = new JButton("Play");
		rplay = new Rectangle((width / 2)-(button_width / 2), 40, button_width, button_height);
		play.setBounds(rplay);
		window.add(play);
		
		options = new JButton("Options");
		roptions = new Rectangle((width / 2)-(button_width / 2), 90, button_width, button_height);
		options.setBounds(roptions);
		window.add(options);
		
		help = new JButton("Help");
		rhelp = new Rectangle((width / 2)-(button_width / 2), 140, button_width, button_height);
		help.setBounds(rhelp);
		window.add(help);
		
		quit = new JButton("Quit");
		rquit = new Rectangle((width / 2)-(button_width / 2), 190, button_width, button_height);
		quit.setBounds(rquit);
		window.add(quit);
		
		// Creating acitonListeners to each button
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				config.loadConfiguration("res/settings/config.xml");
				dispose();
				new RunGame();
			}
		});
		
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				//System.out.println("Options!");
				dispose();
				new Options();
			}
		});
		
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				System.out.println("Help!");
			}
		});
		
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e ) {
				System.exit(0);
			}
		});
	}*/
}
