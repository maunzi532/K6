package item.items;

import item.*;
import java.util.*;
import javafx.scene.image.*;

public enum Items implements Item
{
	BLUE("BLUE.png", 1),
	GSL("GSL.png", 1),
	TECHNOLOGY("Tech.png", 2),
	MATERIAL("X.png", 1);

	Items(String imageName, int weight, String... info)
	{
		this.imageName = imageName;
		image = new Image(imageName);
		this.weight = weight;
		this.info = Arrays.asList(info);

	}

	public final String imageName;
	public final Image image;
	public final int weight;
	public final List<String> info;

	@Override
	public Image image()
	{
		return image;
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