package visual1;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;

public class SchemeFile implements Scheme
{
	private final HashMap<String, Color> colors;
	private final HashMap<String, Image> images;

	public SchemeFile(String input)
	{
		colors = new HashMap<>();
		images = new HashMap<>();
		try
		{
			TreeNode a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			((JrsObject) a1.get("Colors")).fields().forEachRemaining(e -> colors.put(e.getKey(), Color.web(e.getValue().asText())));
			((JrsObject) a1.get("Images")).fields().forEachRemaining(e -> images.put(e.getKey(), new Image(e.getValue().asText())));
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public Color color(String key)
	{
		Color color = colors.get(key);
		if(color == null)
			throw new RuntimeException("Color key \"" + key + "\" missing");
		return color;
	}

	@Override
	public Image image(String key)
	{
		Image image = images.get(key);
		if(image == null)
			throw new RuntimeException("Image key \"" + key + "\" missing or missing Image");
		return image;
	}
}