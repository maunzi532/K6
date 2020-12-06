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
		invView = new TargetScrollList<>(0, 0, 2, 5,
				2, 1, inv.viewItems(), GuiTile::itemStackView, this::invClick);
		elements.add(invView);
		update();
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
			return new XMenu(this, new TradeTargetState(character), new EndTurnState());
		else
			return new XMenu(this, new EndTurnState());
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