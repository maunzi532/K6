package file;

import java.util.*;

public class BlueprintCache<T extends FullBlueprint>
{
	private HashMap<String, BlueprintNode> inputs;
	private HashMap<String, T> cached;

	public BlueprintCache(String filename)
	{
		FileBlueprint fileBlueprint = new FileBlueprint(filename);
		inputs = new HashMap<>();
		cached = new HashMap<>();
		for(BlueprintNode node : fileBlueprint.startNode.inside)
		{
			inputs.put(node.get(0).data, node);
		}
	}

	public T getCached(String name)
	{
		return cached.get(name);
	}

	public BlueprintNode getInput(String name)
	{
		return inputs.get(name);
	}

	public void putCached(String name, T blueprint)
	{
		cached.put(name, blueprint);
	}
}