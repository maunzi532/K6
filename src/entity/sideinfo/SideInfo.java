package entity.sideinfo;

import arrow.*;
import entity.*;
import javafx.scene.image.*;

public class SideInfo
{
	private final XCharacter identifier;
	private Image charImage;
	private final StatBar statBar;
	private String[] texts;

	public SideInfo(XCharacter identifier, Image charImage, StatBar statBar, String... texts)
	{
		this.identifier = identifier;
		this.charImage = charImage;
		this.statBar = statBar;
		this.texts = texts;
	}

	public XCharacter identifier()
	{
		return identifier;
	}

	public Image getCharImage()
	{
		return charImage;
	}

	public String[] getTexts()
	{
		return texts;
	}

	public StatBar getStatBar()
	{
		return statBar;
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