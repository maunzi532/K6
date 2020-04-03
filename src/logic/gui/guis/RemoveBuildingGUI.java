package logic.gui.guis;

import building.adv.*;
import entity.*;
import item.*;
import item.inv.*;
import item.view.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public final class RemoveBuildingGUI extends XGUIState
{
	private static final CTile header = new CTile(0, 0, new GuiTile("gui.removebuilding.header"), 4, 1);
	private static final CTile weight = new CTile(4, 0, 2, 1);
	private static final CTile remove = new CTile(2, 5, new GuiTile("gui.removebuilding.remove"), 2, 1);

	private final XCharacter character;
	private XBuilding building;

	public RemoveBuildingGUI(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		building = mainState.levelMap().getBuilding(character.location());
		ItemList refunds = building.allRefundable();
		character.inv().tryAdd(refunds, true, CommitType.LEAVE);
		InvNumView weightView = character.inv().viewInvWeight();
		ScrollList<ItemView> invView = new ScrollList<>(0, 1, 3, 4, 2, 1,
				character.inv().viewItems(true), RemoveBuildingGUI::changedItemView, null);
		elements.add(invView);
		elements.add(new CElement(header));
		elements.add(new CElement(weight, new GuiTile(weightView.baseAndCurrentWithLimit())));
		elements.add(new CElement(remove, true, null, () -> onClickRemove(mainState.stateHolder())));
		update();
	}

	@Override
	public CharSequence text()
	{
		return "menu.removebuilding";
	}

	@Override
	public String keybind()
	{
		return "state.removebuilding";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		if(mainState.levelMap().getBuilding(character.location()) == null)
			return false;
		else
			return mainState.levelMap().canBuild() && character.resources().ready(1);
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

	private static GuiTile[] changedItemView(ItemView itemView)
	{
		return new GuiTile[]
				{
						new GuiTile(itemView.baseAndCurrentWithLimit()),
						new GuiTile(null, itemView.item.image(), false, null)
				};
	}

	private void onClickRemove(XStateHolder stateHolder)
	{
		if(character.inv().ok())
		{
			character.resources().action(false, 1);
			character.inv().commit();
			building.remove();
			stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		character.inv().rollback();
		super.close(stateHolder);
	}
}