package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.io.*;
import java.util.*;
import statsystem.*;

public final class XCharacter implements DoubleInv, XBuilder
{
	private CharacterTeam team;
	private int startingDelay;
	private boolean defeated;
	private Tile location;
	private XArrow visualReplaced;
	private final Stats stats;
	private final Inv inv;
	private EnemyAI enemyAI;
	private TurnResources resources;

	public XCharacter(CharacterTeam team, int startingDelay, Tile location, Stats stats, Inv inv,
			EnemyAI enemyAI)
	{
		this.team = team;
		this.startingDelay = startingDelay;
		defeated = false;
		this.location = location;
		visualReplaced = null;
		this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;
	}

	public XCharacter createACopy(Tile copyLocation)
	{
		XCharacter copy = new XCharacter(team, startingDelay, copyLocation,
				stats.createACopy(), inv.copy(), enemyAI.copy());
		copy.stats().autoEquip(copy);
		return copy;
	}

	public CharacterTeam team()
	{
		return team;
	}

	public void startTurn()
	{
		if(startingDelay > 0)
			startingDelay--;
	}

	public boolean targetable()
	{
		return !defeated && startingDelay <= 0;
	}

	public void setDefeated()
	{
		defeated = true;
	}

	public void setLocation(Tile location)
	{
		this.location = location;
	}

	public boolean isVisible()
	{
		return visualReplaced == null || visualReplaced.finished();
	}

	public void replaceVisual(XArrow arrow)
	{
		visualReplaced = arrow;
	}

	public Stats stats()
	{
		return stats;
	}

	public String mapImageName()
	{
		return stats.mapImageName();
	}

	public String sideImageName()
	{
		return stats.sideImageName();
	}

	public TurnResources resources()
	{
		return resources;
	}

	public void newResources(TurnResources newResources)
	{
		resources = newResources;
	}

	public boolean isEnemy(XCharacter other)
	{
		return other.team() != team;
	}

	@Override
	public DoubleInvType type()
	{
		return DoubleInvType.ENTITY;
	}

	@Override
	public CharSequence name()
	{
		return stats.getName();
	}

	@Override
	public Tile location()
	{
		return location;
	}

	public Inv inv()
	{
		return inv;
	}

	@Override
	public Inv inv(TradeDirection tradeDirection)
	{
		return inv;
	}

	@Override
	public boolean active()
	{
		return !defeated;
	}

	@Override
	public void afterTrading()
	{
		stats.afterTrading(this);
	}

	public EnemyMove preferredMove(boolean hasToMove, int moveAway)
	{
		if(!resources.ready(2))
			return new EnemyMove(this, -1, null, null, 0);
		return enemyAI.preferredMove(this, resources.moveAction(), hasToMove, moveAway);
	}

	@Override
	public ItemView viewRecipeItem(Item item)
	{
		return inv.viewRecipeItem(item);
	}

	@Override
	public Optional<ItemList> tryBuildingCosts(ItemList refundable, ItemList costs, CommitType commitType)
	{
		if(inv.tryProvide(costs, false, CommitType.LEAVE).isEmpty())
			return Optional.empty();
		return inv.tryProvide(refundable, false, commitType);
	}

	public <T extends ComposerBase, H extends ComposerBase> void save(ObjectComposer<T> a1,
			ArrayComposer<H> xhList, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Type", team.name());
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		a1.put("StartingDelay", startingDelay);
		/*if(startingSettings != null)
		{
			a1.put("StartName", stats.getName().toString());
			a1.put("Locked", startingSettings.startLocked);
			a1.put("InvLocked", startingSettings.startInvLocked);
			if(startingSettings.startLocked)
			{
				inv.save(a1.startObjectField("Inventory"), itemLoader);
			}

			var h1 = xhList.startObject();
			stats.save(h1.startObjectField("Stats"), itemLoader);
			inv.save(h1.startObjectField("Inventory"), itemLoader);
			h1.end();
		}
		else
		{
			stats.save(a1.startObjectField("Stats"), itemLoader);
			inv.save(a1.startObjectField("Inventory"), itemLoader);
		}*/ //TODO
		a1.end();
	}
}