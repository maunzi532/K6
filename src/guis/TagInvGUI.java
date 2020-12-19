package guis;

import entity.*;
import gui.*;
import item.*;
import logic.*;
import xstate.*;

public class TagInvGUI extends GUIState
{
	private final XCharacter character;
	private final TagInv inv;
	private MainState mainState1;
	private TargetScrollList<NumberedStack> invView;

	public TagInvGUI(XCharacter character)
	{
		this.character = character;
		inv = (TagInv) character.inv();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState1 = mainState;
		mainState.side().sidedInfo(character, XCharacter::standardSideInfo);
		invView = new TargetScrollList<>(0, 0, 2, 5,
				2, 1, inv.viewItems(), GuiTile::itemStackView, this::invClick);
		elements.add(invView);
		update();
	}

	private void invClick(NumberedStack stack)
	{

	}

	@Override
	protected void updateBeforeDraw()
	{
		mainState1.side().sidedInfo(character, XCharacter::standardSideInfo);
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