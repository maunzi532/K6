package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import draw.*;
import entity.analysis.*;
import file.*;
import geom.f1.*;
import java.io.*;
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

	public List<RNGOutcome> outcomes(AttackInfo attackInfo)
	{
		return attackInfo.analysis.outcomes2();
	}

	public Stats getStats()
	{
		return stats;
	}

	public SideInfo standardSideInfo()
	{
		return new SideInfo(this, 1, ImageLoader.getImage(stats.imagePath()), StatBar.forEntity(this), stats.sideInfoText());
	}

	public XEntity copy(Tile copyLocation)
	{
		return new XEntity(copyLocation, mainState, stats.copy());
	}

	public int[] save(TileType y1)
	{
		List<Integer> ints = new ArrayList<>();
		ints.add(classSave());
		ints.add(y1.sx(location));
		ints.add(y1.sy(location));
		ints.addAll(stats.save());
		ints.addAll(save2());
		return ints.stream().mapToInt(e -> e).toArray();
	}

	public int classSave()
	{
		return 0;
	}

	public List<Integer> save2()
	{
		return List.of();
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, TileType y1) throws IOException
	{
		var a2 = a1.put("Type", classSave())
				.put("sx", y1.sx(location))
				.put("sy", y1.sy(location))
				.startObjectField("Stats");
		var a3 = stats.save(a2);
		return save2(a3.end());
	}

	public <T extends ComposerBase> ObjectComposer<T> save2(ObjectComposer<T> a1) throws IOException
	{
		return a1;
	}
}