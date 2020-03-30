package logic.gui.guis;

import doubleinv.*;
import entity.sideinfo.*;
import item.inv.*;
import item.view.*;
import levelMap.*;
import logic.*;
import logic.gui.*;

public class Inv2GUI extends XGUIState
{
	private static final CTile textInputInv = new CTile(1, 0, new GuiTile("Input"), 2, 1);
	private static final CTile textOutputInv = new CTile(6, 0, new GuiTile("Output"), 2, 1);
	private static final CTile weightInput = new CTile(3, 0);
	private static final CTile weightOutput = new CTile(8, 0);

	private final Inv inputInv;
	private final Inv outputInv;

	public Inv2GUI(DoubleInv doubleInv)
	{
		inputInv = doubleInv.inputInv();
		outputInv = doubleInv.outputInv();
	}

	@Override
	public void onEnter(SideInfoFrame side, LevelMap levelMap, MainState mainState)
	{
		InvNumView weightViewInput = inputInv.viewInvWeight();
		InvNumView weightViewOutput = outputInv.viewInvWeight();
		ScrollList<ItemView> invViewInput = new ScrollList<>(0, 1, 4, 5, 2, 1,
				inputInv.viewItems(true), GuiTile::itemViewView, null);
		elements.add(invViewInput);
		ScrollList<ItemView> invViewOutput = new ScrollList<>(5, 1, 4, 5, 2, 1,
				outputInv.viewItems(true), GuiTile::itemViewView, null);
		elements.add(invViewOutput);
		elements.add(new CElement(textInputInv));
		elements.add(new CElement(textOutputInv));
		elements.add(new CElement(weightInput, new GuiTile(weightViewInput.currentWithLimit())));
		elements.add(new CElement(weightOutput, new GuiTile(weightViewOutput.currentWithLimit())));
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
}