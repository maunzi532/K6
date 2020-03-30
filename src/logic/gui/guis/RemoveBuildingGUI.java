package logic.gui.guis;

import building.adv.*;
import entity.*;
import entity.sideinfo.*;
import item.*;
import item.inv.*;
import item.view.*;
import levelMap.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class RemoveBuildingGUI extends XGUIState
{
	private static final CTile textInv = new CTile(0, 0, new GuiTile("Remove Building?"), 4, 1);
	private static final CTile weight = new CTile(4, 0, 2, 1);
	private static final CTile remove = new CTile(2, 5, new GuiTile("Remove"), 2, 1);

	private final XCharacter character;
	private XBuilding building;

	public RemoveBuildingGUI(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		side.setStandardSideInfo(character);
		building = levelMap.getBuilding(character.location());
		ItemList refunds = building.allRefundable();
		character.inputInv().tryAdd(refunds, true, CommitType.LEAVE);
		InvNumView weightView = character.inputInv().viewInvWeight();
		ScrollList<ItemView> invView = new ScrollList<>(0, 1, 3, 4, 2, 1,
				character.inputInv().viewItems(true), this::changedItemView, null);
		elements.add(invView);
		elements.add(new CElement(textInv));
		elements.add(new CElement(weight, new GuiTile(weightView.baseAndCurrentWithLimit())));
		elements.add(new CElement(remove, true, null, () -> onClickRemove(mainState)));
		update();
	}

	@Override
	public String text()
	{
		return "Remove";
	}

	@Override
	public String keybind()
	{
		return "Remove Building";
	}

	@Override
	public boolean keepInMenu(MainState mainState, LevelMap levelMap)
	{
		return character.resources().ready(1) && levelMap.getBuilding(character.location()) != null;
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


	private GuiTile[] changedItemView(ItemView itemView)
	{
		return new GuiTile[]
				{
						new GuiTile(itemView.baseAndCurrentWithLimit()),
						new GuiTile(null, itemView.item.image(), false, null)
				};
	}

	private void onClickRemove(MainState mainState)
	{
		if(character.inputInv().ok())
		{
			character.resources().action(false, 1);
			character.inputInv().commit();
			building.remove();
			mainState.stateHolder.setState(NoneState.INSTANCE);
		}
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		character.inputInv().rollback();
		super.close(stateHolder);
	}
}