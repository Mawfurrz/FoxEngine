package com.engine.foxengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	
	Properties properties = new Properties();
	
	public void saveConfiguration(String key, int value) {
		String path = "res/settings/config.xml";
		
		try {
			File file = new File(path);
			boolean exists = file.exists();
			if (!exists) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Resolution");
		} catch (Exception e) {
			System.out.println("Failed to create config.xml");
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			String width = properties.getProperty("width");
			String height = properties.getProperty("height");
			//System.out.println("width = " + width + " height = " + height);
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			loadConfiguration(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setResolution(int width, int height) {
		Display.WIDTH = width;
		Display.HEIGHT = height;
	}
}
