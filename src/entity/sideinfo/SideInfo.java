package entity.sideinfo;

import arrow.*;
import entity.*;
import java.util.*;
import javafx.scene.image.*;

public class SideInfo
{
	private final XCharacter character;
	private final int type;
	private Image charImage;
	private final StatBar statBar;
	private String[] texts;

	public SideInfo(XCharacter character, int type, Image charImage, StatBar statBar, String... texts)
	{
		this.character = character;
		this.type = type;
		this.charImage = charImage;
		this.statBar = statBar;
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

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof SideInfo sideInfo)) return false;
		return Objects.equals(character, sideInfo.character);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(character, type);
	}
}