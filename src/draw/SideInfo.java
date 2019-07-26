package draw;

import arrow.*;
import entity.*;
import java.util.*;
import javafx.scene.image.*;

public class SideInfo
{
	private XEntity character;
	private int type;
	private Image charImage;
	private StatBar statBar;
	private String[] texts;

	public SideInfo(XEntity character, int type, Image charImage, StatBar statBar, String... texts)
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
		if(!(o instanceof SideInfo)) return false;
		SideInfo sideInfo = (SideInfo) o;
		return Objects.equals(character, sideInfo.character);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(character, type);
	}
}