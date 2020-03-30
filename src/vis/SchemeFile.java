package vis;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;

public final class SchemeFile implements Scheme
{
	private final HashMap<String, Color> colors;
	private final HashMap<String, Image> images;
	private final HashMap<String, String> settings;

	public SchemeFile(String input)
	{
		colors = new HashMap<>();
		images = new HashMap<>();
		settings = new HashMap<>();
		try
		{
			TreeNode a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			((JrsObject) a1.get("Colors")).fields().forEachRemaining(e -> colors.put(e.getKey(), Color.web(e.getValue().asText())));
			((JrsObject) a1.get("Images")).fields().forEachRemaining(e -> images.put(e.getKey(), new Image(e.getValue().asText())));
			((JrsObject) a1.get("Settings")).fields().forEachRemaining(e -> settings.put(e.getKey(), e.getValue().asText()));
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

	@Override
	public String setting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return setting;
	}

	@Override
	public int intSetting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return Integer.parseInt(setting);
	}

	@Override
	public double doubleSetting(String key)
	{
		String setting = settings.get(key);
		if(setting == null)
			throw new RuntimeException("Setting key \"" + key + "\" missing");
		return Double.parseDouble(setting);
	}
}