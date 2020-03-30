package entity.sideinfo;

import arrow.*;
import entity.*;

public final class SideInfo
{
	private final XCharacter identifier;
	private String imageName;
	private final StatBar statBar;
	private String[] texts;

	public SideInfo(XCharacter identifier, String imageName, StatBar statBar, String... texts)
	{
		this.identifier = identifier;
		this.imageName = imageName;
		this.statBar = statBar;
		this.texts = texts;
	}

	public XCharacter identifier()
	{
		return identifier;
	}

	public String imageName()
	{
		return imageName;
	}

	public String[] texts()
	{
		return texts;
	}

	public StatBar statBar()
	{
		return statBar;
	}

	public void setImageName(String imageName)
	{
		this.imageName = imageName;
	}

	public void setTexts(String[] texts)
	{
		this.texts = texts;
	}
}