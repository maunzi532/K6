package logic.gui.guis;

import entity.*;
import item.*;
import item.view.*;
import java.util.*;
import logic.*;
import logic.gui.*;

public class CharacterCombatGUI extends XGUIState
{
	private static final CTile NAME = new CTile(2, 0, 3, 1);
	private static final CTile UNEQUIP = new CTile(1, 6, new GuiTile("Unequip"), 2, 1);
	private static final CTile OTHER_VIEW = new CTile(4, 6, 2, 1);

	private InvEntity character;
	private CombatSystem combatSystem;
	private TargetScrollList<ItemView> invView;
	private TargetScrollList<XMode> modeChooseView;
	private ScrollList<String> statsView;
	private Item chosenItem;

	public CharacterCombatGUI(InvEntity character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		combatSystem = mainState.combatSystem;
		elements.add(new CElement(NAME, new GuiTile(character.name())));
		elements.add(new CElement(UNEQUIP, true, () -> character.getStats().getEquippedMode() != null,
				() -> character.getStats().equip(null, null)));
		invView = new TargetScrollList<>(1, 1, 2, 5, 2, 1,
				null, GuiTile::itemViewView, e -> chosenItem = (e.base > 0 ? e.item : null));
		elements.add(invView);
		modeChooseView = new TargetScrollList<>(0, 1, 1, 5, 1, 1,
				null, e -> GuiTile.textView(e.tile()), this::onClickMode);
		elements.add(modeChooseView);
		statsView = new ScrollList<>(3, 1, 4, 5, 1, 1,
				null, GuiTile::textView, null);
		elements.add(statsView);
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		invView.elements = character.outputInv().viewItems(true);
		if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
		{
			statsView.elements = invView.getTargeted().item.info();
		}
		else if(modeChooseView.getTargeted() != null)
		{
			statsView.elements = modeChooseView.getTargeted().info();
		}
		else
		{
			statsView.elements = List.of();
		}
		if(chosenItem != null && character.getStats().getItemFilter().canContain(chosenItem))
		{
			modeChooseView.elements = combatSystem.modesForItem(chosenItem);
		}
		else
		{
			modeChooseView.elements = List.of();
		}
	}

	private void onClickMode(XMode mode)
	{
		character.getStats().equip(chosenItem, mode);
		chosenItem = null;
	}

	@Override
	public String text()
	{
		return "Stats";
	}

	@Override
	public String keybind()
	{
		return "Combat Info";
	}

	@Override
	public int xw()
	{
		return 7;
	}

	@Override
	public int yw()
	{
		return 7;
	}
}