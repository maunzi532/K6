package logic.guis;

import entity.*;
import gui.*;
import item.*;
import java.util.*;
import logic.*;
import logic.xstate.*;
import system.*;

public class CharacterInfoGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);

	private final XCharacter character;
	private final SystemChar systemChar;
	private MainState mainState1;
	private TargetScrollList<NumberedStack4> invView;
	private ScrollList<CharSequence> itemView;

	public CharacterInfoGUI(XCharacter character)
	{
		this.character = character;
		systemChar = character.systemChar();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		mainState.side().setStandardSideInfo(character);
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1, null,
				GuiTile::itemStackView, null);
		elements.add(invView);
		itemView = new ScrollList<>(3, 1, 3, 5, 1, 1, null,
				GuiTile::textView, null);
		elements.add(itemView);
		elements.add(new CElement(textInv, new GuiTile(character.name())));
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		mainState1.side().setStandardSideInfo(character);
		invView.elements = systemChar.inv().viewItems();
		itemView.elements = info();
	}

	protected List<? extends CharSequence> info()
	{
		if(invView.getTargeted() != null)
		{
			if(invView.getTargeted().item() instanceof EquipableItem4 item)
			{
				return item.statsInfo();
			}
			else
			{
				return List.of();
			}
		}
		else
		{
			return systemChar.statsInfo();
		}
	}

	@Override
	public CharSequence text()
	{
		return "menu.stats";
	}

	@Override
	public String keybind()
	{
		return "state.stats";
	}

	@Override
	public XMenu menu()
	{
		if(character.team() == CharacterTeam.HERO)
			return new XMenu(this, new TradeTargetState(character), new EndTurnState());
		else
			return new XMenu(this, new EndTurnState());
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
}