package file;

import java.util.*;
import javafx.scene.image.*;

public class ImageLoader
{
	private static final HashMap<String, Image> images = new HashMap<>();

	public static Image getImage(String path)
	{
		Image image = images.get(path);
		if(image != null)
			return image;
		Image image2 = new Image(path);
		images.put(path, image2);
		return image2;
	}
}