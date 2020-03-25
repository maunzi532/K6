package file;

import java.io.*;
import java.net.*;

public class ImageLoader
{
	public static String loadTextResource(String location)
	{
		URL resource = Thread.currentThread().getContextClassLoader().getResource(location);
		if(resource == null)
		{
			throw new RuntimeException("Resource not found: \"" + location + "\"");
		}
		try
		{
			return new String(resource.openStream().readAllBytes());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}