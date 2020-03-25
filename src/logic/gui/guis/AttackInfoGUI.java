package logic.gui.guis;

import entity.*;
import entity.sideinfo.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;
import system2.*;

public class AttackInfoGUI extends XGUIState
{
	private final XCharacter attacker;
	private final XCharacter target;
	private ColorScheme colorScheme;
	private SideInfoFrame sideInfoFrame;
	private TargetScrollList<AttackInfo> attacksView;

	public AttackInfoGUI(XCharacter attacker, XCharacter target)
	{
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		colorScheme = mainState.colorScheme;
		sideInfoFrame = mainState.sideInfoFrame;
		sideInfoFrame.sidedInfo(attacker, target, colorScheme);
		attacksView = new TargetScrollList<>(0, 1, 6, 6, 6, 2,
				mainState.levelMap.attackInfo(attacker, target), this::itemView, target1 -> clickAttack(mainState, target1));
		elements.add(attacksView);
		elements.add(new CElement(new CTile(0, 0, new GuiTile(attacker.name()), 2, 1)));
		elements.add(new CElement(new CTile(4, 0, new GuiTile(target.name()), 2, 1)));
		update();
	}

	private void clickAttack(MainState mainState, AttackInfo target1)
	{
		attacker.resources().action(true, 2);
		mainState.stateHolder.setState(new PreAttackState(NoneState.INSTANCE, target1));
	}

	@Override
	protected void updateBeforeDraw()
	{
		if(attacksView.getTargeted() != null)
			sideInfoFrame.setAttackSideInfo(attacksView.getTargeted(), colorScheme);
		else
			sideInfoFrame.sidedInfo(attacker, target, colorScheme);
	}

	@Override
	public XMenu menu()
	{
		return XMenu.characterMoveMenu(attacker);
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

	private GuiTile[] itemView(AttackInfo aI)
	{
		String[] infos = aI.getInfos();
		return new GuiTile[]
				{
						new GuiTile(read(infos, 0)),
						new GuiTile(read(infos, 2)),
						new GuiTile(read(infos, 8), aI.mode.image(), false, null),
						new GuiTile(read(infos, 9), aI.modeT.image(), false, null),
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

	private String read(String[] infos, int n)
	{
		if(n >= infos.length)
			return "";
		return infos[n];
	}
}