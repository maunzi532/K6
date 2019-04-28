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
	protected Stats stats;

	public XEntity(Tile location, MainState mainState, Stats stats)
	{
		this.location = location;
		this.mainState = mainState;
		this.stats = stats;
	}

	public boolean isEnemy(XEntity other)
	{
		return false;
	}

	public String name()
	{
		return stats.getName();
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
		return mainState.combatSystem.movement(mainState, this, stats);
	}

	public int maxAccessRange()
	{
		return mainState.combatSystem.maxAccessRange(mainState, this, stats);
	}

	public List<Integer> attackRanges(boolean counter)
	{
		return mainState.combatSystem.attackRanges(mainState, this, stats, counter);
	}

	public List<AttackInfo> attackInfo(XEntity target)
	{
		return mainState.combatSystem.attackInfo(mainState, this, stats, target, target.stats);
	}

	public Stats getStats()
	{
		return stats;
	}
}