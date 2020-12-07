package logic.guis;

import entity.*;
import entity.sideinfo.*;
import gui.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import logic.xstate.*;
import system.*;

public final class AllyInfoGUI4 extends XGUIState
{
	private final XCharacter character;
	private final XCharacter target;
	private final StateReverter nextState;
	private SideInfoFrame side;
	private TargetScrollList<AllyCalc4> allyView;

	public AllyInfoGUI4(XCharacter character, XCharacter target, StateReverter nextState)
	{
		this.character = character;
		this.target = target;
		this.nextState = nextState;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		side = mainState.side();
		side.sidedInfo(character, target);
		List<AllyCalc4> allyCalc = AllyInfo4.allyOptions(character, target, mainState.levelMap().y1())
				.stream().map(AllyCalc4::new).collect(Collectors.toList());
		allyView = new TargetScrollList<>(0, 1,
				6, 6, 6, 2, allyCalc,
				AllyInfoGUI4::allyView, target1 -> clickAlly(target1, mainState.stateHolder()));
		elements.add(allyView);
		elements.add(new CElement(new CTile(0, 0, new GuiTile(character.name()), 2, 1)));
		elements.add(new CElement(new CTile(4, 0, new GuiTile(target.name()), 2, 1)));
		update();
	}

	private void clickAlly(AllyCalc4 target1, XStateHolder stateHolder)
	{
		nextState.setMainAction();
		stateHolder.setState(new HealState(nextState, character, target,
				target1.aI.item().additional().get("AbilityPower") + character.systemChar().stat(Stats4.ABILITY_POWER)));
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		stateHolder.setState(nextState);
	}

	@Override
	protected void updateBeforeDraw()
	{
		if(allyView.getTargeted() != null)
			side.setAllyInfoSideInfo(allyView.getTargeted());
		else
			side.sidedInfo(character, target);
	}

	@Override
	public CharSequence text()
	{
		return "menu.allyinfo";
	}

	@Override
	public String keybind()
	{
		return null;
	}

	@Override
	public XMenu menu()
	{
		return new XMenu(this, nextState.nextState(), new EndMoveState(character));
	}

	@Override
	public int xw()
	{
		return 6;
	}

	@Override
	public int yw()
	{
		return 7;
	}

	private static GuiTile[] allyView(AllyCalc4 aI)
	{
		CharSequence[] infos = aI.infos;
		return new GuiTile[]
				{
						new GuiTile(read(infos, 0)),
						new GuiTile(read(infos, 2)),
						new GuiTile(read(infos, 8), aI.aI.item().image(), false, null),
						new GuiTile(read(infos, 9)),
						new GuiTile(read(infos, 1)),
						new GuiTile(read(infos, 3)),
						new GuiTile(read(infos, 4)),
						new GuiTile(read(infos, 6)),
						new GuiTile(read(infos, 10)),
						new GuiTile(read(infos, 11)),
						new GuiTile(read(infos, 5)),
						new GuiTile(read(infos, 7)),
				};
	}

	private static CharSequence read(CharSequence[] infos, int n)
	{
		if(n >= infos.length)
			return null;
		return infos[n];
	}
}