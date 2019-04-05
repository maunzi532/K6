package item;

import javafx.scene.image.*;

public enum Items implements Item
{
	BLUE("BLUE.png", 1),
	GSL("GSL.png", 1),
	TECHNOLOGY("Tech.png", 2),
	MATERIAL("X.png", 1);

	Items(String imageName, int weight)
	{
		this.imageName = imageName;
		image = new Image(imageName);
		this.weight = weight;
	}

	public final String imageName;
	public final Image image;
	public final int weight;

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
}