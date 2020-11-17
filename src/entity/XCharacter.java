package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import com.fasterxml.jackson.jr.stree.*;
import doubleinv.*;
import geom.tile.*;
import item.*;
import item.inv.*;
import java.io.*;
import levelmap.*;
import statsystem.*;

public final class XCharacter implements DoubleInv
{
	private CharacterTeam team;
	private int startingDelay;
	private Tile location;
	private final Stats stats;
	private final Inv inv;
	private EnemyAI enemyAI;
	private TurnResources resources;
	private XArrow visualReplaced;
	private boolean defeated;

	public XCharacter(CharacterTeam team, int startingDelay, Tile location, Stats stats, Inv inv, EnemyAI enemyAI, boolean unlimitedTR)
	{
		this.team = team;
		this.startingDelay = startingDelay;
		this.location = location;
		this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;
		if(unlimitedTR)
		{
			resources = TurnResources.unlimited();
		}
		else
		{
			resources = new TurnResources(location);
		}
		visualReplaced = null;
		defeated = false;
	}

	public XCharacter createACopy(Tile copyLocation, boolean unlimitedTR)
	{
		XCharacter copy = new XCharacter(team, startingDelay, copyLocation,
				stats.createACopy(), inv.copy(), enemyAI.copy(), unlimitedTR);
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
		}*/
		a1.end();
	}

	public static XCharacter loadFromMap(JrsObject data, ItemLoader itemLoader, LevelMap levelMap)
	{
		CharacterTeam team = CharacterTeam.valueOf(data.get("Team").asText());
		int startingDelay = ((JrsNumber) data.get("StartingDelay")).getValue().intValue();
		Tile location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		EnemyAI enemyAI = EnemyAI.load((JrsObject) data.get("EnemyAI"), itemLoader, levelMap);
		return new XCharacter(team, startingDelay, location, stats, inv, enemyAI, false);
	}

	public <T extends ComposerBase> void saveToMap(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Team", team.name());
		a1.put("StartingDelay", startingDelay);
		a1.put("sx", y1.sx(location));
		a1.put("sy", y1.sy(location));
		stats.save(a1.startObjectField("Stats"), itemLoader);
		inv.save(a1.startObjectField("Inventory"), itemLoader);
		enemyAI.save(a1.startObjectField("EnemyAI"), itemLoader, y1);
	}

	public static XCharacter loadFromTeam(JrsObject data, int startingDelay, Tile defaultStart, Inv invOverride, Inv storageInv, ItemLoader itemLoader, LevelMap levelMap)
	{
		CharacterTeam team = CharacterTeam.valueOf(data.get("Team").asText());
		Tile location;
		if(data.get("sx") != null && data.get("sy") != null)
		{
			location = levelMap.y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		}
		else
		{
			location = defaultStart;
		}
		Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv;
		if(invOverride != null)
		{
			Inv toStorage = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			storageInv.tryAdd(toStorage.allItems());
			inv = invOverride;
		}
		else
		{
			inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		}
		EnemyAI enemyAI = EnemyAI.load((JrsObject) data.get("EnemyAI"), itemLoader, levelMap);
		return new XCharacter(team, startingDelay, location, stats, inv, enemyAI, true);
	}

	public <T extends ComposerBase> void saveToTeam(ObjectComposer<T> a1, boolean saveLocation, boolean canTrade, ItemLoader itemLoader, TileType y1) throws IOException
	{
		a1.put("Team", team.name());
		if(saveLocation)
		{
			a1.put("sx", y1.sx(location));
			a1.put("sy", y1.sy(location));
		}
		stats.save(a1.startObjectField("Stats"), itemLoader);
		if(canTrade)
		{
			inv.save(a1.startObjectField("Inventory"), itemLoader);
		}
		else
		{
			new WeightInv(inv.viewInvWeight().limit).save(a1.startObjectField("Inventory"), itemLoader);
		}
		enemyAI.save(a1.startObjectField("EnemyAI"), itemLoader, y1);
	}
}