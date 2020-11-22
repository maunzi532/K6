package logic.guis;

import entity.*;
import entity.sideinfo.*;
import gui.*;
import logic.*;
import logic.xstate.*;
import system4.*;

public final class AttackInfoGUI4 extends XGUIState
{
	private final XCharacter attacker;
	private final XCharacter target;
	private SideInfoFrame side;
	private TargetScrollList<AttackInfo4> attacksView;

	public AttackInfoGUI4(XCharacter attacker, XCharacter target)
	{
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		side = mainState.side();
		side.sidedInfo(attacker, target);
		attacksView = new TargetScrollList<>(0, 1,
				6, 6, 6, 2,
				AttackInfo4.attackOptions(attacker, target, mainState.levelMap().y1()),
				AttackInfoGUI4::itemView, target1 -> clickAttack(target1, mainState.stateHolder()));
		elements.add(attacksView);
		elements.add(new CElement(new CTile(0, 0, new GuiTile(attacker.name()), 2, 1)));
		elements.add(new CElement(new CTile(4, 0, new GuiTile(target.name()), 2, 1)));
		update();
	}

	private void clickAttack(AttackInfo4 target1, XStateHolder stateHolder)
	{
		attacker.resources().action(true);
		stateHolder.setState(new PreAttackState(NoneState.INSTANCE, null/*target1*/));
	}

	@Override
	protected void updateBeforeDraw()
	{
		if(attacksView.getTargeted() != null)
			side.setAttackSideInfo(attacksView.getTargeted());
		else
			side.sidedInfo(attacker, target);
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

	private static GuiTile[] itemView(AttackInfo4 aI)
	{
		/*CharSequence[] infos = aI.getInfos();
		return new GuiTile[]
				{
						new GuiTile(read(infos, 0)),
						new GuiTile(read(infos, 2)),
						new GuiTile(read(infos, 8), aI.mode.imageName(), false, null),
						new GuiTile(read(infos, 9), aI.modeT.imageName(), false, null),
						new GuiTile(read(infos, 1)),
						new GuiTile(read(infos, 3)),
						new GuiTile(read(infos, 4)),
						new GuiTile(read(infos, 6)),
						new GuiTile(read(infos, 10)),
						new GuiTile(read(infos, 11)),
						new GuiTile(read(infos, 5)),
						new GuiTile(read(infos, 7)),
				};*/
		return new GuiTile[12];
	}

	private static CharSequence read(CharSequence[] infos, int n)
	{
		if(n >= infos.length)
			return null;
		return infos[n];
	}
}