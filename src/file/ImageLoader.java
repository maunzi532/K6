package file;

import java.io.*;
import java.util.*;
import javafx.scene.image.*;

public class ImageLoader
{
	private static final HashMap<String, Image> images = new HashMap<>();

	public static Image getImage(String path)
	{
		if(path == null)
			return null;
		Image image = images.get(path);
		if(image != null)
			return image;
		try
		{
			Image image2 = new Image(path);
			images.put(path, image2);
			return image2;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	public static String loadTextResource(String resource)
	{
		try
		{
			//noinspection ConstantConditions
			return new String(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource).readAllBytes());
		}catch(IOException | NullPointerException e)
		{
			throw new RuntimeException(e);
		}
	}
}