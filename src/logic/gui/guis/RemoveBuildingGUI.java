package logic.gui.guis;

import building.*;
import entity.*;
import item.*;
import item.inv.*;
import item.view.*;
import javafx.scene.input.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class RemoveBuildingGUI extends XGUIState
{
	private static final CTile textInv = new CTile(0, 0, new GuiTile("Remove Building?"), 4, 1);
	private static final CTile weight = new CTile(4, 0, 2, 1);
	private static final CTile remove = new CTile(2, 5, new GuiTile("Remove"), 2, 1);

	private final XHero character;
	private Buildable building;
	private ItemList refunds;
	private InvNumView weightView;
	private ScrollList<ItemView> invView;

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
		invView = new ScrollList<>(0, 1, 3, 4, 2, 1);
		invView.elements = character.inputInv().viewItems(true);
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
		invView.update();
		invView.draw(tiles, this::changedItemView);
		setFilledTile(textInv);
		setEmptyTileAndFill(weight, new GuiTile(weightView.baseAndCurrentWithLimit()));
		setFilledTile(remove);
	}

	private GuiTile[] changedItemView(ItemView itemView)
	{
		return new GuiTile[]
				{
						new GuiTile(itemView.baseAndCurrentWithLimit()),
						new GuiTile(null, itemView.item.image(), false, null)
				};
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = invView.target(x, y, false);
		if(result0.inside)
		{
			targeted = result0.targetTile;
			return;
		}
		if(remove.contains(x, y))
			setTargeted(remove);
		else
			setTargeted(CTile.NONE);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = invView.target(x, y, true);
		if(result0.scrolled)
			update();
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
	public void close(XStateHolder stateHolder)
	{
		character.inputInv().rollback();
		super.close(stateHolder);
	}
}