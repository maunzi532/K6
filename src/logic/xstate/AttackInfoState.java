package logic.xstate;

import entity.*;
import gui.*;
import gui.guis.*;
import logic.*;

public class AttackInfoState implements NGUIState
{
	private XHero attacker;
	private XEntity target;

	public AttackInfoState(XHero attacker, XEntity target)
	{
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.visualSideInfo.sidedInfo(attacker, target);
	}

	@Override
	public String text()
	{
		return "Error";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMoveMenu(attacker);
	}

	@Override
	public XGUI gui(MainState mainState)
	{
		return new AttackInfoGUI(mainState.visualSideInfo, attacker, target);
	}
}