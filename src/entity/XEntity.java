package entity;

import arrow.*;
import geom.hex.*;
import java.util.*;
import javafx.scene.image.*;

public class XEntity implements MEntity
{
	private static final Image IMAGE = new Image("GSL1_0.png");

	protected Hex location;
	private MArrow replace;

	public XEntity(Hex location)
	{
		this.location = location;
	}

	public boolean isEnemy(MEntity other)
	{
		return true;
	}

	@Override
	public Hex location()
	{
		return location;
	}

	@Override
	public void setLocation(Hex location)
	{
		this.location = location;
	}

	@Override
	public Image getImage()
	{
		return IMAGE;
	}

	@Override
	public boolean isVisible()
	{
		return replace == null || replace.finished();
	}

	@Override
	public void setReplacementArrow(MArrow arrow)
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