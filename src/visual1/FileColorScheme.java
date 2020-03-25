package visual1;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import file.*;
import java.io.*;
import java.util.*;
import javafx.scene.paint.*;

public class FileColorScheme implements ColorScheme
{
	private final HashMap<String, Color> colors;

	public FileColorScheme(String input)
	{
		colors = new HashMap<>();
		try
		{
			TreeNode a1 = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			((JrsObject) a1.get("Colors")).fields().forEachRemaining(e -> colors.put(e.getKey(), Color.web(e.getValue().asText())));
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
}