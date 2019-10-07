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
	private CTile nameA;
	private CTile nameT;
	private ScrollList<AttackInfo> attacksView;
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
		nameA = new CTile(0, 0, new GuiTile(attacker.name()), 2, 1);
		nameT = new CTile(4, 0, new GuiTile(target.name()), 2, 1);
		attacksView = new ScrollList<>(0, 1, 6, 6, 6, 2);
		attacksView.elements = attacker.attackInfo(target);
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

	@Override
	private void update()
	{
		initTiles();
		attacksView.update();
		attacksView.draw(tiles, this::itemView);
		setFilledTile(nameA);
		setFilledTile(nameT);
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

	@Override
	public void target(int x, int y)
	{
		var result0 = attacksView.target(x, y, false);
		targeted = result0.targetTile;
		if(lastTargeted != result0.target)
		{
			if(result0.target != null)
				sideInfoFrame.attackInfo(result0.target);
			else
				sideInfoFrame.sidedInfo(attacker, target);
			//analysis.get(num).outcomes2().forEach(e -> System.out.println(e.readableChance() + " " + e.compareText));
			//System.out.println();
			lastTargeted = result0.target;
		}
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = attacksView.target(x, y, true);
		if(result0.target != null)
		{
			attacker.takeAp(2);
			attacker.mainActionTaken();
			stateHolder.setState(new PreAttackState(NoneState.INSTANCE, result0.target));
		}
		else if(result0.requiresUpdate)
			update();
	}
}