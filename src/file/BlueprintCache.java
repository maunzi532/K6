package file;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

public class BlueprintCache<T extends FullBlueprint>
{
	private HashMap<String, T> cached;

	public BlueprintCache(String filename, Function<BlueprintNode, T> initializer)
	{
		cached = new HashMap<>();
		FileBlueprint fileBlueprint = new FileBlueprint(filename);
		for(BlueprintNode node : fileBlueprint.startNode.inside)
		{
			cached.put(node.get(0).data, initializer.apply(node));
		}
	}

	public BlueprintCache(Function<JrsObject, T> initializer, String filename)
	{
		cached = new HashMap<>();
		try
		{
			var tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(loadTextResource(filename));
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				((JrsArray) tree.get("Blueprints")).elements()
						.forEachRemaining(e -> cached.put(((JrsObject) e).get("Name").asText(), initializer.apply((JrsObject) e)));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
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

	public void saveBlueprints(String filename)
	{
		try
		{
			var a1 = JSON.std.with(JSON.Feature.PRETTY_PRINT_OUTPUT)
					.composeString()
					.startObject()
					.put("code", 0xA4D2839F);
			var a2 = a1.startArrayField("Blueprints");
			for(String key : cached.keySet())
			{
				a2 = get(key).save(a2.startObject()).end();
			}
			String text = a2.end().end().finish();
			Files.write(new File(filename).toPath(), text.getBytes());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public Set<String> allNames()
	{
		return cached.keySet();
	}

	public T get(String name)
	{
		return cached.get(name);
	}
}