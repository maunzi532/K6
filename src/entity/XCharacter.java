package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import doubleinv.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.io.*;
import java.util.*;
import system2.*;

public class XCharacter implements DoubleInv, XBuilder
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
	private StartingSettings startingSettings;

	public XCharacter(CharacterTeam team, int startingDelay, Tile location, Stats stats, Inv inv,
			EnemyAI enemyAI, TurnResources resources, StartingSettings startingSettings)
	{
		this.team = team;
		this.startingDelay = startingDelay;
		defeated = false;
		this.location = location;
		visualReplaced = null;
		this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;
		this.resources = resources;
		this.startingSettings = startingSettings;
	}

	private XCharacter(CharacterTeam team, int startingDelay, boolean defeated, Tile location,
			Stats stats, Inv inv, EnemyAI enemyAI, TurnResources resources, StartingSettings startingSettings)
	{
		this.team = team;
		this.startingDelay = startingDelay;
		this.defeated = defeated;
		this.location = location;
		visualReplaced = null;
		this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;
		this.resources = resources;
		this.startingSettings = startingSettings;
	}

	public XCharacter copy(Tile copyLocation)
	{
		XCharacter copy = new XCharacter(team, startingDelay, defeated, copyLocation,
				stats.copy(), inv.copy(), enemyAI.copy(), resources.copy(copyLocation), StartingSettings
				.copy(startingSettings));
		copy.stats.autoEquip(copy);
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

	public void replaceVisual(XArrow visualReplaced)
	{
		this.visualReplaced = visualReplaced;
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

	public void newResources(TurnResources resources)
	{
		this.resources = resources;
	}

	public StartingSettings saveSettings()
	{
		return startingSettings;
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList);
	}

	public boolean isEnemy(XCharacter other)
	{
		return other.team != team;
	}

	@Override
	public DoubleInvType type()
	{
		return DoubleInvType.ENTITY;
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
	public boolean active()
	{
		return !defeated;
	}

	@Override
	public void afterTrading()
	{
		stats.afterTrading(this);
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return !levelStarted && (startingSettings == null || !startingSettings.startInvLocked);
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
		return outputInv().viewRecipeItem(item);
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
		if(startingSettings != null)
		{
			a1.put("StartName", stats.getName());
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
		}
		a1.end();
	}
}