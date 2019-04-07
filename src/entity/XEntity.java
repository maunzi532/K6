package entity;

import arrow.*;
import geom.f1.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;

public class XEntity
{
	private static final Image IMAGE = new Image("GSL1_0.png");

	protected Tile location;
	private XArrow replace;
	protected MainState mainState;
	protected Stats1 stats;

	public XEntity(Tile location, MainState mainState, Stats1 stats)
	{
		this.location = location;
		this.mainState = mainState;
		this.stats = stats;
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

	public void setReplacementArrow(XArrow arrow)
	{
		replace = arrow;
	}

	public int movement()
	{
		return mainState.wugu1.movement(mainState, this, stats);
	}

	public int maxAccessRange()
	{
		return mainState.wugu1.maxAccessRange(mainState, this, stats);
	}

	public List<Integer> attackRanges(boolean counter)
	{
		return mainState.wugu1.attackRanges(mainState, this, stats, counter);
	}

	public List<AttackInfo> attackInfo(XEntity target)
	{
		return mainState.wugu1.attackInfo(mainState, this, stats, target, target.stats);
	}
}