package logic.guis;

import entity.*;
import gui.*;
import item4.*;
import logic.*;
import logic.xstate.*;

public class TagInvGUI extends XGUIState
{
	private final XCharacter character;
	private final TagInv4 inv;
	private MainState mainState1;
	private TargetScrollList<NumberedStack4> invView;

	public TagInvGUI(XCharacter character)
	{
		this.character = character;
		inv = (TagInv4) character.inv();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		mainState.side().setStandardSideInfo(character);
		invView = new TargetScrollList<>(0, 0, 2, 5, 2, 1,
				inv.viewItems(), this::itemStackView, this::invClick);
		update();
	}

	private GuiTile[] itemStackView(NumberedStack4 stack)
	{
		boolean mark = false;//itemView.item == equippedItem;
		return new GuiTile[]
				{
						new GuiTile(stack.viewText(), null, false, mark ? "gui.background.active" : null),
						new GuiTile(null, stack.item().image(), false, mark ? "gui.background.active" : null)
				};
	}

	private void invClick(NumberedStack4 stack)
	{

	}

	@Override
	protected void updateBeforeDraw()
	{
		mainState1.side().setStandardSideInfo(character);
		invView.elements = inv.viewItems();
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
			return XMenu.characterGUIMenu(character);
		else
			return XMenu.enemyGUIMenu(character);
	}

	@Override
	public int xw()
	{
		return 4;
	}

	@Override
	public int yw()
	{
		return 6;
	}
}