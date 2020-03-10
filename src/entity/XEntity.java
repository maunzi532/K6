package entity;

import arrow.*;
import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import entity.analysis.*;
import file.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;
import logic.sideinfo.*;

public class XEntity implements DoubleInv
{
	private static final Image IMAGE = new Image("GSL1_0.png");

	protected Tile location;
	private XArrow replace;
	protected MainState mainState;
	protected Stats stats;
	protected Inv inv;

	public XEntity(Tile location, MainState mainState, Stats stats, Inv inv)
	{
		this.location = location;
		this.mainState = mainState;
		this.stats = stats;
		this.inv = inv;
	}

	public XEntity(Tile location, MainState mainState, Stats stats, int weightLimit, ItemList itemList)
	{
		this(location, mainState, stats, new WeightInv(weightLimit));
		addItems(itemList);
	}

	public boolean isEnemy(XEntity other)
	{
		return false;
	}

	@Override
	public String name()
	{
		return stats.getName();
	}

	@Override
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

	@Override
	public DoubleInvType type()
	{
		return DoubleInvType.ENTITY;
	}

	@Override
	public Inv inputInv()
	{
		return inv;
	}

	@Override
	public Inv outputInv()
	{
		return inv;
	}

	@Override
	public void afterTrading()
	{
		stats.afterTrading(this);
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
	}

	public XEntity copy(Tile copyLocation)
	{
		return new XEntity(copyLocation, mainState, stats.copy(), inv.copy());
	}

	public int classSave()
	{
		return 0;
	}

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		var a2 = a1.put("Type", classSave())
				.put("sx", y1.sx(location))
				.put("sy", y1.sy(location))
				.startObjectField("Stats");
		var a3 = stats.save(a2, itemLoader);
		return save2(a3.end(), itemLoader);
	}

	public <T extends ComposerBase> ObjectComposer<T> save2(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		return inv.save(a1.startObjectField("Inventory"), itemLoader).end();
	}

	public <T extends ComposerBase> ObjectComposer<T> save3(ObjectComposer<T> a1, ItemLoader itemLoader) throws IOException
	{
		var a2 = a1.startObjectField("Stats");
		var a3 = stats.save(a2, itemLoader);
		return save2(a3.end(), itemLoader);
	}
}