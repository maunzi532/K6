package file;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class BlueprintCache<T extends FullBlueprint>
{
	private final HashMap<String, T> blueprints;

	public BlueprintCache(String filename, Function<JrsObject, T> initializer)
	{
		blueprints = new HashMap<>();
		try
		{
			var tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(ImageLoader.loadTextResource(filename));
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				((JrsArray) tree.get("Blueprints")).elements()
						.forEachRemaining(e -> blueprints.put(((JrsObject) e).get("Name").asText(), initializer.apply((JrsObject) e)));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public T get(String name)
	{
		return blueprints.get(name);
	}

	public Set<String> allNames()
	{
		return blueprints.keySet();
	}

	public List<T> allBlueprints()
	{
		return new ArrayList<>(blueprints.values());
	}

	public void saveBlueprints(String filename, ItemLoader itemLoader)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
			var a2 = a1.startArrayField("Blueprints");
			for(String key : blueprints.keySet())
			{
				a2 = get(key).save(a2.startObject(), itemLoader).end();
			}
			String text = a2.end().end().finish();
			Files.write(new File(filename).toPath(), text.getBytes());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}