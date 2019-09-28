package logic.editor.xgui;

import entity.*;
import logic.*;
import logic.editor.xstate.*;
import logic.gui.*;
import item.view.*;
import java.util.*;
import javafx.scene.input.*;
import logic.xstate.*;

public class EntityEditGUI extends XGUIState implements InvGUI
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private InvEntity entity;
	private InvNumView weightView;
	private List<ItemView> items;
	private InvGUIPart invView;
	private InvGUIPart infoView;
	private InvGUIPart changeView;
	private List<String> info;
	private ItemView viewing;
	private boolean targetChanged;
	private int changeStatNum;
	private List<String> changeOptions;

	public EntityEditGUI(InvEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		weightView = this.entity.inputInv().viewInvWeight();
		items = this.entity.inputInv().viewItems(true);
		invView = new InvGUIPart(0, 0, 1, 1, 5, 2, 1);
		info = entity.getStats().infoEdit();
		infoView = new InvGUIPart(1, 3, 1, 3, 5, 1, 1);
		changeStatNum = -1;
		changeOptions = List.of();
		changeView = new InvGUIPart(2, 7, 1, 1, 5, 1, 1);
		update();
	}

	@Override
	public String text()
	{
		return "Edit";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.E;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public int xw()
	{
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private void update()
	{
		initTiles();
		if(viewing != null)
			info = viewing.item.info();
		else
			info = entity.getStats().infoEdit();
		invView.addToGUI(items.size(), this);
		infoView.addToGUI(info.size(), this);
		changeView.addToGUI(changeOptions.size(), this);
		setTile(textInv, new GuiTile(entity.name()));
		setTile(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		if(invID == 0)
		{
			ItemView itemView = items.get(index);
			tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
			tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, null);
		}
		else if(invID == 1)
		{
			if(index < info.size())
				tiles[x][y1] = new GuiTile(info.get(index));
		}
		else
		{
			tiles[x][y1] = new GuiTile(changeOptions.get(index));
		}
	}

	@Override
	public void target(int x, int y)
	{
		boolean tiv = invView.target(x, y, items.size(), this);
		if(!tiv || getTargeted() == CTile.NONE)
		{
			setTargeted(CTile.NONE);
			if(viewing != null)
				targetChanged = true;
			viewing = null;
		}
		if(targetChanged)
		{
			targetChanged = false;
			update();
		}
		if(tiv)
			return;
		if(infoView.target(x, y, info.size(), this))
			return;
		changeView.target(x, y, changeOptions.size(), this);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
		if(invID == 0)
		{
			if(viewing != items.get(num))
			{
				targetChanged = true;
				viewing = items.get(num);
			}
		}
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		invView.checkClick(x, y, items.size(), this);
		infoView.checkClick(x, y, info.size(), this);
		changeView.checkClick(x, y, changeOptions.size(), this);
		if(invView.updateGUIFlag() | infoView.updateGUIFlag() | changeView.updateGUIFlag())
		{
			update();
		}
	}

	@Override
	public void onClickItem(int invID, int num, int xi, int yi)
	{
		if(invID == 1)
		{
			changeStatNum = num;
			changeOptions = entity.getStats().editOptions(num);
			update();
		}
		if(invID == 2)
		{
			entity.getStats().applyEditOption(changeStatNum, num, entity);
			update();
		}
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState)
	{
		if(setState)
			stateHolder.setState(EditingState.INSTANCE);
	}
}