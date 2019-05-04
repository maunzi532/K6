package draw;

import javafx.scene.image.*;

public class SideInfo
{
	private Image charImage;
	private String[] texts;

	public SideInfo(Image charImage, String... texts)
	{
		this.charImage = charImage;
		this.texts = texts;
	}

	public Image getCharImage()
	{
		return charImage;
	}

	public String[] getTexts()
	{
		return texts;
	}

	public void setCharImage(Image charImage)
	{
		this.charImage = charImage;
	}

	public void setTexts(String[] texts)
	{
		this.texts = texts;
	}
}