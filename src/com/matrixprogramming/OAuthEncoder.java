package com.matrixprogramming;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class OAuthEncoder
{
	File propFile;
	Properties prop;
	FileInputStream fileInput;

	public OAuthEncoder()
	{
		propFile = new File("clientproperties.properties");
		prop = new Properties();
		try
		{
			propFile.createNewFile();
			fileInput = new FileInputStream(propFile);
			prop.load(fileInput);
		}
		catch (FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public String getOAuth()
	{
		return prop.getProperty("OAuth");
	}
}
