package logic.gui.guis;

import entity.*;
import item.view.*;
import logic.*;
import logic.gui.*;

public class CharacterCombatGUI extends XGUIState
{
	private InvEntity character;
	private TargetScrollList<ItemView> invView;
	private ScrollList<String> statsView;
	private ScrollList<String> itemStatsView;
	private TargetScrollList<Integer> modeChooseView;

	public CharacterCombatGUI(InvEntity character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1,
				null, GuiTile::itemViewView, null);
		elements.add(invView);
		statsView = new ScrollList<>(2, 1, 4, 5, 1, 1,
				null, GuiTile::textView, null);
		elements.add(statsView);
		itemStatsView = new ScrollList<>(2, 1, 4, 5, 1, 1,
				null, GuiTile::textView, null);
		elements.add(itemStatsView);
		modeChooseView = new TargetScrollList<>(6, 1, 2, 5, 2, 1,
				null, i -> GuiTile.textView(String.valueOf(i)), null);
		elements.add(modeChooseView);
	}

	@Override
	protected void updateBeforeDraw()
	{
		invView.elements = character.outputInv().viewItems(true);
		statsView.elements = null;
		itemStatsView.elements = null;
		modeChooseView.elements = null;
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
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}
}