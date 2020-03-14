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
	private Stats stats;
	private Inv inv;
	private EnemyAI enemyAI;
	private TurnResources resources;
	private CharacterSave save;

	public XCharacter(CharacterTeam team, int initialStartingDelay, Tile location, Stats stats, Inv inv,
			EnemyAI enemyAI, TurnResources resources, CharacterSave save)
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
		this.save = save;
	}

	private XCharacter(CharacterTeam team, int startingDelay, boolean defeated, Tile location,
			Stats stats, Inv inv, EnemyAI enemyAI, TurnResources resources, CharacterSave save)
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
		this.save = save;
	}

	public XCharacter copy(Tile copyLocation)
	{
		XCharacter copy = new XCharacter(team, startingDelay, defeated, copyLocation,
				stats.copy(), inv.copy(), enemyAI.copy(), resources.copy(copyLocation), CharacterSave.copy(save));
		//copy.stats.autoEquip(copy);
		return copy;
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

	/*public SideInfo standardSideInfo()
	{
		return new SideInfo(this, 1, ImageLoader.getImage(stats.imagePath()), statBar(), stats.sideInfoText());
	}

	public StatBar statBar()
	{
		return new StatBar(this instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				stats.getVisualStat(0), stats.getMaxVisualStat(0));
	}

	public StatBar statBarX1(String extraText)
	{
		return new StatBarX1(this instanceof XHero ? Color.GREEN : Color.GRAY, Color.BLACK, Color.WHITE,
				stats.getVisualStat(0), stats.getMaxVisualStat(0), extraText);
	}*/

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
		//stats.afterTrading(this);
	}

	/*@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return !levelStarted && !startInvLocked;
	}*/

	/*public EnemyMove preferredMove(boolean hasToMove, int moveAway)
	{
		if(!canAttack)
			return new EnemyMove(this, -1, null, null, 0);
		return think.preferredMove(this, canMove, hasToMove, moveAway);
	}*/

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

	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws
			IOException
	{
		var a2 = a1.put("Type", team.name())
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

	/*public <T extends ComposerBase> ObjectComposer<T> save4(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
	{
		var a2 = a1.put("StartName", stats.getName())
				.put("Locked", startLocked)
				.put("InvLocked", startInvLocked)
				.put("sx", y1.sx(location))
				.put("sy", y1.sy(location));
		if(startInvLocked)
		{
			a2 = inv.save(a2.startObjectField("Inventory"), itemLoader).end();
		}
		return a2;
	}*/
}