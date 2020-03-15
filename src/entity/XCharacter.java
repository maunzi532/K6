package entity;

import arrow.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import doubleinv.*;
import entity.sideinfo.*;
import file.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import system2.*;

public class XCharacter implements DoubleInv, XBuilder
{
	private CharacterTeam team;
	private int startingDelay;
	private boolean defeated;
	private Tile location;
	private XArrow visualReplaced;
	private Stats stats;
	private Inv inv;
	private EnemyAI enemyAI;
	private TurnResources resources;
	private SaveSettings saveSettings;

	public XCharacter(CharacterTeam team, int initialStartingDelay, Tile location, Stats stats, Inv inv,
			EnemyAI enemyAI, TurnResources resources, SaveSettings saveSettings)
	{
		this.team = team;
		startingDelay = initialStartingDelay;
		defeated = false;
		this.location = location;
		visualReplaced = null;
		this.stats = stats;
		this.inv = inv;
		this.enemyAI = enemyAI;
		this.resources = resources;
		this.saveSettings = saveSettings;
	}

	private XCharacter(CharacterTeam team, int startingDelay, boolean defeated, Tile location,
			Stats stats, Inv inv, EnemyAI enemyAI, TurnResources resources, SaveSettings saveSettings)
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
		this.saveSettings = saveSettings;
	}

	public XCharacter copy(Tile copyLocation)
	{
		XCharacter copy = new XCharacter(team, startingDelay, defeated, copyLocation,
				stats.copy(), inv.copy(), enemyAI == null ? null : enemyAI.copy(), resources.copy(copyLocation), SaveSettings.copy(saveSettings));
		copy.stats.autoEquip(copy);
		return copy;
	}

	public CharacterTeam team()
	{
		return team;
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

	public Image mapImage()
	{
		return ImageLoader.getImage(stats.mapImagePath());
	}

	public Image sideImage()
	{
		return ImageLoader.getImage(stats.sideImagePath());
	}

	public TurnResources resources()
	{
		return resources;
	}

	public void newResources(TurnResources resources)
	{
		this.resources = resources;
	}

	public SaveSettings saveSettings()
	{
		return saveSettings;
	}

	public SideInfo standardSideInfo()
	{
		return new SideInfo(this, sideImage(), statBar(), stats.sideInfoText());
	}

	public StatBar statBar()
	{
		return new StatBar(team == CharacterTeam.HERO ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				stats.getVisualStat(0), stats.getMaxVisualStat(0));
	}

	public StatBar statBarX1(String extraText)
	{
		return new StatBarX1(team == CharacterTeam.HERO ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				stats.getVisualStat(0), stats.getMaxVisualStat(0), extraText);
	}

	public void addItems(ItemList itemList)
	{
		inv.tryAdd(itemList, false, CommitType.COMMIT);
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
		return !defeated && startingDelay <= 0;
	}

	@Override
	public void afterTrading()
	{
		stats.afterTrading(this);
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return !levelStarted && (saveSettings == null || !saveSettings.startInvLocked);
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
		if(saveSettings != null)
		{
			a1.put("StartName", stats.getName());
			a1.put("Locked", saveSettings.startLocked);
			a1.put("InvLocked", saveSettings.startInvLocked);
			a1.put("sx", y1.sx(location));
			a1.put("sy", y1.sy(location));
			if(saveSettings.startLocked)
			{
				inv.save(a1.startObjectField("Inventory"), itemLoader);
			}
			a1.end();

			var h1 = xhList.startObject();
			stats.save(h1.startObjectField("Stats"), itemLoader);
			inv.save(h1.startObjectField("Inventory"), itemLoader);
			h1.end();
		}
		else
		{
			a1.put("Type", team.name());
			a1.put("sx", y1.sx(location));
			a1.put("sy", y1.sy(location));
			stats.save(a1.startObjectField("Stats"), itemLoader);
			inv.save(a1.startObjectField("Inventory"), itemLoader);
			a1.end();
		}
	}
}