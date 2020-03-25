package system2.content;

import item.*;
import java.util.*;

public enum Items implements Item
{
	BLUE("BLUE.png", 1),
	GSL("GSL.png", 1),
	TECHNOLOGY("Tech.png", 2),
	MATERIAL("X.png", 1);

	Items(String imageName, int weight, String... info)
	{
		this.imageName = imageName;
		this.weight = weight;
		this.info = Arrays.asList(info);

	}

	public final String imageName;
	public final int weight;
	public final List<String> info;

	@Override
	public String imageName()
	{
		return imageName;
	}

	@Override
	public int weight()
	{
		return weight;
	}

	@Override
	public List<String> info()
	{
		return info;
	}

}