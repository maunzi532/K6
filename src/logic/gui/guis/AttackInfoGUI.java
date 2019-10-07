package logic.gui.guis;

import entity.*;
import logic.*;
import logic.gui.*;
import logic.sideinfo.*;
import logic.xstate.*;

public class AttackInfoGUI extends XGUIState
{
	private XHero attacker;
	private XEntity target;
	private SideInfoFrame sideInfoFrame;
	private AttackInfo lastTargeted;

	public AttackInfoGUI(XHero attacker, XEntity target)
	{
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		sideInfoFrame = mainState.sideInfoFrame;
		sideInfoFrame.sidedInfo(attacker, target);
		ScrollList<AttackInfo> attacksView = new ScrollList<>(0, 1, 6, 6, 6, 2,
				attacker.attackInfo(target), this::itemView, this::onTarget1, target1 ->
		{
			attacker.takeAp(2);
			attacker.mainActionTaken();
			mainState.stateHolder.setState(new PreAttackState(NoneState.INSTANCE, target1));
		});
		elements.add(attacksView);
		elements.add(new CElement(new CTile(0, 0, new GuiTile(attacker.name()), 2, 1)));
		elements.add(new CElement(new CTile(4, 0, new GuiTile(target.name()), 2, 1)));
		lastTargeted = null;
		update();
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

	private Boolean onTarget1(AttackInfo target1)
	{
		if(lastTargeted != target1)
		{
			if(target1 != null)
				sideInfoFrame.attackInfo(target1);
			else
				sideInfoFrame.sidedInfo(attacker, target);
			//analysis.get(num).outcomes2().forEach(e -> System.out.println(e.readableChance() + " " + e.compareText));
			//System.out.println();
			lastTargeted = target1;
		}
		return false;
	}
}