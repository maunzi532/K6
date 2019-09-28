package logic.gui.guis;

import building.transport.*;
import item.inv.*;
import item.view.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class Inv2GUI extends XGUIState
{
	private static final CTile textInputInv = new CTile(1, 0, new GuiTile("Input"), 2, 1);
	private static final CTile textOutputInv = new CTile(6, 0, new GuiTile("Output"), 2, 1);
	private static final CTile weightInput = new CTile(3, 0);
	private static final CTile weightOutput = new CTile(8, 0);

	private Inv inputInv;
	private Inv outputInv;
	private InvNumView weightViewInput;
	private InvNumView weightViewOutput;
	private ScrollList<ItemView> invViewInput;
	private ScrollList<ItemView> invViewOutput;

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
		invViewInput = new ScrollList<>(0, 1, 4, 5, 2, 1);
		invViewOutput = new ScrollList<>(5, 1, 4, 5, 2, 1);
		invViewInput.elements = inputInv.viewItems(true);
		invViewOutput.elements = outputInv.viewItems(true);
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
		invViewInput.update();
		invViewOutput.update();
		invViewInput.draw(tiles, GuiTile::itemViewView);
		invViewOutput.draw(tiles, GuiTile::itemViewView);
		setFilledTile(textInputInv);
		setFilledTile(textOutputInv);
		setEmptyTileAndFill(weightInput, new GuiTile(weightViewInput.currentWithLimit()));
		setEmptyTileAndFill(weightOutput, new GuiTile(weightViewOutput.currentWithLimit()));
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = invViewInput.target(x, y, false);
		if(result0.inside)
		{
			targeted = result0.targetTile;
			return;
		}
		var result1 = invViewOutput.target(x, y, false);
		targeted = result1.targetTile;
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = invViewInput.target(x, y, true);
		var result1 = invViewOutput.target(x, y, true);
		if(result0.scrolled || result1.scrolled)
			update();
	}
}