package logic.gui.guis;

import building.*;
import entity.*;
import logic.gui.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import javafx.scene.input.*;
import logic.*;
import logic.xstate.*;

public class RemoveBuildingGUI extends NGUIState implements InvGUI
{
	private static final CTile textInv = new CTile(0, 0, new GuiTile("Remove Building?"), 4, 1);
	private static final CTile weight = new CTile(4, 0, 2, 1);
	private static final CTile remove = new CTile(2, 5, new GuiTile("Remove"), 2, 1);

	private final XHero character;
	private Buildable building;
	private ItemList refunds;
	private InvNumView weightView;
	private List<ItemView> itemsView;
	private InvGUIPart invView;

	public RemoveBuildingGUI(XHero character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfo(character.standardSideInfo(), null);
		building = (Buildable) mainState.levelMap.getBuilding(character.location());
		refunds = building.getRefundable();
		character.inputInv().tryAdd(refunds, true, CommitType.LEAVE);
		weightView = character.inputInv().viewInvWeight();
		itemsView = character.inputInv().viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 3, 4, 2, 1);
		update();
	}

	@Override
	public String text()
	{
		return "Remove";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.R;
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return character.ready(1) && mainState.levelMap.getBuilding(character.location()) instanceof Buildable;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterGUIMenu(character);
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private void update()
	{
		initTiles();
		invView.addToGUI(itemsView.size(), this);
		setTile(textInv);
		setTile(weight, new GuiTile(weightView.baseAndCurrentWithLimit()));
		setTile(remove);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		ItemView itemView = itemsView.get(index);
		tiles[x][y1] = new GuiTile(itemView.baseAndCurrentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, null);
	}

	@Override
	public void target(int x, int y)
	{
		if(invView.target(x, y, itemsView.size(), this))
			return;
		if(remove.contains(x, y))
			setTargeted(remove);
		else
			setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		if(remove.contains(x, y) && character.inputInv().ok())
		{
			character.takeAp(1);
			character.irreversible();
			character.inputInv().commit();
			building.remove();
			stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState)
	{
		character.inputInv().rollback();
		super.close(stateHolder, setState);
	}
}