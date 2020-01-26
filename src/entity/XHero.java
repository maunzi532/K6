package entity;

import building.adv.*;
import building.blueprint.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import logic.*;

public class XHero extends XEntity implements XBuilder
{
	private static final Image IMAGE_S = new Image("S.png");

	private boolean canMove;
	private int usedMovement;
	private int ap;
	private boolean mainAction;
	private Tile revertLocation;
	private boolean canRevert;
	private boolean startLocked;
	private boolean startInvLocked;

	public XHero(Tile location, MainState mainState, Stats stats, boolean startLocked, boolean startInvLocked,
			int weightLimit,
			ItemList itemList)
	{
		super(location, mainState, stats, weightLimit, itemList);
		this.startLocked = startLocked;
		this.startInvLocked = startInvLocked;
	}

	public XHero(Tile location, MainState mainState, Stats stats, boolean startLocked, boolean startInvLocked, Inv inv)
	{
		super(location, mainState, stats, inv);
		this.startLocked = startLocked;
		this.startInvLocked = startInvLocked;
	}

	@Override
	public XEntity copy(Tile copyLocation)
	{
		XHero copy = new XHero(copyLocation, mainState, stats, startLocked, startInvLocked, inv.copy());
		copy.stats.autoEquip(copy);
		return copy;
	}

	@Override
	public Image getImage()
	{
		return IMAGE_S;
	}

	@Override
	public boolean isEnemy(XEntity other)
	{
		return other instanceof XEnemy;
	}

	@Override
	public boolean playerTradeable(boolean levelStarted)
	{
		return !levelStarted && !startInvLocked;
	}

	@Override
	public ItemView viewRecipeItem(Item item)
	{
		return outputInv().viewRecipeItem(item);
	}

	@Override
	public Optional<ItemList> tryBuildingCosts(CostBlueprint cost, CommitType commitType)
	{
		if(inv.tryProvide(cost.costs(), false, CommitType.LEAVE).isEmpty())
			return Optional.empty();
		return inv.tryProvide(cost.refundable(), false, commitType);
	}

	public void startTurn()
	{
		canMove = true;
		ap = 2;
		mainAction = true;
		revertLocation = location;
		canRevert = true;
	}

	public boolean canMove()
	{
		return canMove && mainAction;
	}

	public int dashMovement()
	{
		return Math.max(0, mainState.combatSystem.dashMovement(mainState, this, stats) - usedMovement);
	}

	public boolean ready(int apCost)
	{
		return mainAction && apCost <= ap;
	}

	public Tile getRevertLocation()
	{
		return revertLocation;
	}

	public boolean canRevert()
	{
		return !canMove && canRevert;
	}

	public boolean isStartLocked()
	{
		return startLocked;
	}

	public boolean isStartInvLocked()
	{
		return startInvLocked;
	}

	public void toggleStartLocked()
	{
		startLocked = !startLocked;
	}

	public void toggleStartInvLocked()
	{
		startInvLocked = !startInvLocked;
	}

	public void setMoved(int used)
	{
		usedMovement += used;
		canMove = false;
	}

	public void takeAp(int take)
	{
		ap -= take;
	}

	public void mainActionTaken()
	{
		mainAction = false;
		canMove = false;
		canRevert = false;
	}

	public void irreversible()
	{
		if(!canMove)
			canRevert = false;
	}

	public void revertMovement()
	{
		mainState.levelMap.moveEntity(this, revertLocation);
		canMove = true;
	}

	@Override
	public int classSave()
	{
		return 1;
	}

	@Override
	public <T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException
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
	}
}