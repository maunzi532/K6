package file;

import java.util.*;

public class BlueprintNode
{
	public final String data;
	public final List<BlueprintNode> inside;

	public BlueprintNode(String data)
	{
		this.data = data;
		inside = new ArrayList<>();
	}

	public int dataInt()
	{
		return Integer.parseInt(data);
	}

	public int size()
	{
		return inside.size();
	}

	public BlueprintNode get(int i)
	{
		return inside.get(i);
	}

	public void add(BlueprintNode node)
	{
		inside.add(node);
	}
}