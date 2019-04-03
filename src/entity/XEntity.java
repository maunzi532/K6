package entity;

import arrow.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;

public class XEntity
{
	private static final Image IMAGE = new Image("GSL1_0.png");

	protected Tile location;
	private VisualArrow replace;

	public XEntity(Tile location)
	{
		this.location = location;
	}

	public boolean isEnemy(XEntity other)
	{
		return true;
	}

	public Tile location()
	{
		return location;
	}

	public void setLocation(Tile location)
	{
		this.location = location;
	}

	public Image getImage()
	{
		return IMAGE;
	}

	public boolean isVisible()
	{
		return replace == null || replace.finished();
	}

	public void setReplacementArrow(VisualArrow arrow)
	{
		replace = arrow;
	}

	public List<Integer> attackRanges()
	{
		return List.of();
	}

	public int minCounterRange()
	{
		return 1;
	}

	public int maxCounterRange()
	{
		return 0;
	}
}