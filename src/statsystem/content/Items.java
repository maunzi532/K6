package statsystem.content;

import item.*;
import java.util.*;

public enum Items implements Item
{
	BLUE("resource.blue", 1),
	GSL("resource.gsl", 1),
	TECHNOLOGY("resource.tech", 2),
	MATERIAL("resource.material", 1);

	Items(String image, int weight, CharSequence... info)
	{
		this.image = image;
		this.weight = weight;
		this.info = Arrays.asList(info);

	}

	private final String image;
	private final int weight;
	private final List<CharSequence> info;

	@Override
	public String image()
	{
		return image;
	}

	@Override
	public int weight()
	{
		return weight;
	}

	@Override
	public List<CharSequence> info()
	{
		return info;
	}

}