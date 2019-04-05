package gui.guis;

import entity.*;
import entity.hero.*;
import gui.*;
import java.util.*;
import logic.*;

public class AttackInfoGUI extends XGUI implements InvGUI
{
	private final CTile nameA;
	private final CTile nameT;

	private final XHero attacker;
	private final XEntity target;
	private final List<AttackInfo> infoList;
	private final InvGUIPart attacksView;

	public AttackInfoGUI(XHero attacker, XEntity target)
	{
		this.attacker = attacker;
		this.target = target;
		nameA = new CTile(0, 0, new GuiTile(attacker.name()), 2, 1);
		nameT = new CTile(4, 0, new GuiTile(target.getClass().getSimpleName()), 2, 1);
		infoList = attacker.attackInfo(target);
		attacksView = new InvGUIPart(0, 0, 1, 1, 3, 6, 2);
		update();
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

	private void update()
	{
		initTiles();
		attacksView.addToGUI(infoList.size(), this);
		setTile(nameA);
		setTile(nameT);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		AttackInfo info = infoList.get(index);
		tiles[x][y1] = new GuiTile(read(info, 0));
		tiles[x][y1 + 1] = new GuiTile(read(info, 4));
		tiles[x + 1][y1] = new GuiTile(read(info, 2));
		tiles[x + 1][y1 + 1] = new GuiTile(read(info, 6));
		tiles[x + 2][y1] = new GuiTile(read(info, 8), info.attackItem.image(), null);
		tiles[x + 2][y1 + 1] = new GuiTile(read(info, 10));
		tiles[x + 3][y1] = new GuiTile(read(info, 9), info.counterItem.image(), null);
		tiles[x + 3][y1 + 1] = new GuiTile(read(info, 11));
		tiles[x + 4][y1] = new GuiTile(read(info, 1));
		tiles[x + 4][y1 + 1] = new GuiTile(read(info, 5));
		tiles[x + 5][y1] = new GuiTile(read(info, 3));
		tiles[x + 5][y1 + 1] = new GuiTile(read(info, 7));
	}

	private String read(AttackInfo info, int n)
	{
		if(n >= info.infos.length)
			return "";
		return info.infos[n];
	}

	@Override
	public void target(int x, int y)
	{
		if(attacksView.target(x, y, infoList.size(), this))
			return;
		setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		attacksView.checkClick(x, y, infoList.size(), this);
		/*if(chosen != null)
		{
			//stateControl.stateInfo[3] = chosen;
			stateControl.setState(new BuildState(attacker, chosen));
			return true;
		}*/
		if(attacksView.updateGUIFlag())
			update();
		return false;
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		//chosen = BuildingBlueprint.get(blueprintCache, names[num]);
	}
}