package logic.gui.guis;

import building.transport.*;
import item.inv.*;
import item.view.*;
import java.util.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class Inv2GUI extends XGUIState implements InvGUI
{
	private static final CTile textInputInv = new CTile(1, 0, new GuiTile("Input"), 2, 1);
	private static final CTile textOutputInv = new CTile(6, 0, new GuiTile("Output"), 2, 1);
	private static final CTile weightInput = new CTile(3, 0);
	private static final CTile weightOutput = new CTile(8, 0);

	private Inv inputInv;
	private Inv outputInv;
	private InvNumView weightViewInput;
	private InvNumView weightViewOutput;
	private List<ItemView> itemsViewInput;
	private List<ItemView> itemsViewOutput;
	private InvGUIPart invViewInput;
	private InvGUIPart invViewOutput;

	public Inv2GUI(DoubleInv doubleInv)
	{
		inputInv = doubleInv.inputInv();
		outputInv = doubleInv.outputInv();
	}

	@Override
	public void onEnter(MainState mainState)
	{
		weightViewInput = inputInv.viewInvWeight();
		weightViewOutput = outputInv.viewInvWeight();
		itemsViewInput = inputInv.viewItems(true);
		itemsViewOutput = outputInv.viewItems(true);
		invViewInput = new InvGUIPart(0, 0, 1, 2, 5, 2, 1);
		invViewOutput = new InvGUIPart(1, 5, 1, 2, 5, 2, 1);
		update();
	}

	@Override
	public int xw()
	{
		return 9;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private void update()
	{
		initTiles();
		invViewInput.addToGUI(itemsViewInput.size(), this);
		invViewOutput.addToGUI(itemsViewOutput.size(), this);
		setTile(textInputInv);
		setTile(textOutputInv);
		setTile(weightInput, new GuiTile(weightViewInput.currentWithLimit()));
		setTile(weightOutput, new GuiTile(weightViewOutput.currentWithLimit()));
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		List<ItemView> itemsView = invID == 0 ? itemsViewInput : itemsViewOutput;
		ItemView itemView = itemsView.get(index);
		tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), false, null);
	}

	@Override
	public void target(int x, int y)
	{
		if(invViewInput.target(x, y, itemsViewInput.size(), this))
			return;
		if(invViewOutput.target(x, y, itemsViewOutput.size(), this))
			return;
		setTargeted(CTile.NONE);
	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{
		setTargeted(cTile);
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder){}
}