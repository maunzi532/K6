package inv;

import javafx.scene.image.*;

public enum Items implements Item
{
	BLUE("BLUE.png"),
	GSL("GSL.png"),
	TECHNOLOGY("H.png");

	Items(String imageName)
	{
		this.imageName = imageName;
		image = new Image(imageName);
	}

	public final String imageName;
	public final Image image;

	@Override
	public Image image()
	{
		return image;
	}
}