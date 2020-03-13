package logic.gui.guis;

import entity.*;
import item.*;
import item.view.*;
import java.util.*;
import javafx.scene.paint.*;
import logic.*;
import logic.gui.*;

public class CharacterCombatGUI extends XGUIState
{
	private static final CTile NAME = new CTile(2, 0, 3, 1);
	private static final CTile UNEQUIP = new CTile(1, 6, new GuiTile("Unequip"), 2, 1);
	private static final CTile VIEW_MODE = new CTile(4, 6, 2, 1);

	private final XEntity character;
	private int viewMode;
	private CombatSystem combatSystem;
	private Item equippedItem;
	private CElement viewModeElement;
	private TargetScrollList<ItemView> invView;
	private TargetScrollList<XMode> modeChooseView;
	private ScrollList<String> statsView;
	private Item chosenItem;

	public CharacterCombatGUI(XEntity character, int viewMode)
	{
		this.character = character;
		this.viewMode = viewMode;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.sideInfoFrame.setSideInfoXH(character.standardSideInfo(), character);
		combatSystem = mainState.combatSystem;
		equippedItem = (Item) combatSystem.equippedItem(character.getStats()).orElse(null);
		elements.add(new CElement(NAME, new GuiTile(character.name())));
		elements.add(new CElement(UNEQUIP, true, () -> character.getStats().getEquippedMode() != null,
				this::unequip));
		viewModeElement = new CElement(VIEW_MODE, true, null, () -> viewMode = (viewMode + 1) % 2);
		elements.add(viewModeElement);
		invView = new TargetScrollList<>(1, 1, 2, 5, 2, 1,
				null, this::itemViewView, e -> chosenItem = (e.base > 0 ? e.item : null));
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
		viewModeElement.fillTile = new GuiTile(viewMode == 1 ? "View Mode\nStat View" : "View Mode\nCombat Data");
		invView.elements = character.outputInv().viewItems(true);
		if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
		{
			statsView.elements = invView.getTargeted().item.info();
		}
		else if(modeChooseView.getTargeted() != null)
		{
			statsView.elements = modeChooseView.getTargeted().modeInfo();
		}
		else if(viewMode == 1)
		{
			statsView.elements = character.getStats().statsInfo();
		}
		else
		{
			statsView.elements = character.getStats().infoWithEquip();
		}
		if(chosenItem != null && character.getStats().getItemFilter().canContain(chosenItem))
		{
			modeChooseView.elements = combatSystem.modesForItem(character.getStats(), chosenItem);
		}
		else
		{
			modeChooseView.elements = List.of();
		}
	}

	private void onClickMode(XMode mode)
	{
		character.getStats().equip(chosenItem, mode.shortVersion());
		equippedItem = chosenItem;
		chosenItem = null;
	}

	private void unequip()
	{
		character.getStats().equip(null, null);
		equippedItem = null;
		chosenItem = null;
	}

	public GuiTile[] itemViewView(ItemView itemView)
	{
		boolean mark = itemView.item == equippedItem;
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit(), null, false, mark ? Color.CYAN : null),
						new GuiTile(null, itemView.item.image(), false, mark ? Color.CYAN : null)
				};
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