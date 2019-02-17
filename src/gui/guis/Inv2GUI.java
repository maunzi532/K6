package gui.guis;

import gui.*;
import inv.*;
import java.util.List;
import logic.*;

public class Inv2GUI extends XGUI implements InvGUI
{
	private static final CTile textInputInv = new CTile(1, 0, new GuiTile("Input"), 2, 1);
	private static final CTile textOutputInv = new CTile(6, 0, new GuiTile("Output"), 2, 1);
	private static final CTile weightInput = new CTile(3, 0);
	private static final CTile weightOutput = new CTile(8, 0);

	private final InvWeightView weightViewInput;
	private final InvWeightView weightViewOutput;
	private final List<ItemView> itemsViewInput;
	private final List<ItemView> itemsViewOutput;
	private final InvGUIPart invViewInput;
	private final InvGUIPart invViewOutput;

	public Inv2GUI(DoubleInv doubleInv)
	{
		weightViewInput = doubleInv.inputInv().viewInvWeight();
		weightViewOutput = doubleInv.outputInv().viewInvWeight();
		itemsViewInput = doubleInv.inputInv().viewItems(true);
		itemsViewOutput = doubleInv.outputInv().viewItems(true);
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
		tiles[x + 1][y1] = new GuiTile(null, itemView.item.image(), null);
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
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		stateControl.setState(XState.NONE);
	}
}