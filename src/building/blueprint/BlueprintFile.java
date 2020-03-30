package building.blueprint;

import com.fasterxml.jackson.jr.ob.*;
import com.fasterxml.jackson.jr.stree.*;
import item.*;
import java.io.*;
import java.util.*;

public final class BlueprintFile
{
	private final HashMap<String, BuildingBlueprint> blueprints;

	public BlueprintFile(String input, ItemLoader itemLoader)
	{
		blueprints = new HashMap<>();
		try
		{
			var tree = JSON.std.with(new JacksonJrsTreeCodec()).treeFrom(input);
			if(((JrsNumber) tree.get("code")).getValue().intValue() == 0xA4D2839F)
			{
				((JrsArray) tree.get("Blueprints")).elements()
						.forEachRemaining(e -> blueprints.put(((JrsObject) e).get("Name").asText(), BuildingBlueprint.create((JrsObject) e, itemLoader)));
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public BuildingBlueprint get(String name)
	{
		return blueprints.get(name);
	}

	public List<BuildingBlueprint> allBlueprints()
	{
		return new ArrayList<>(blueprints.values());
	}

	public String saveBlueprints(ItemLoader itemLoader)
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
				get(key).save(a2.startObject(), itemLoader);
			}
			a2.end();
			return a1.end().finish();
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}