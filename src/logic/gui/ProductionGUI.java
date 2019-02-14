package logic.gui;

import building.*;
import building.blueprint.*;
import inv.*;
import java.util.*;
import logic.*;

public class ProductionGUI extends XGUI
{
	private ProductionBuilding building;
	private int recipeNum;

	public ProductionGUI(ProductionBuilding building, int recipeNum)
	{
		this.building = building;
		this.recipeNum = recipeNum;
		initTiles();
		tiles[0][0] = new GuiTile(building.name(), null, null);
		Recipe recipe = building.getRecipes().get(recipeNum);
		List<ItemStack> required = recipe.required.items;
		SlotInv inputInv = building.getInputInv();
		for(int i = 0; i < required.size(); i++)
		{
			Item item = required.get(i).item;
			InvSlot slot = inputInv.getSlot(item);
			tiles[1][i + 1] = new GuiTile(slot.getCurrent() + " / " +
					slot.getLimit(), null, null);
			tiles[2][i + 1] = new GuiTile(except1(required.get(i).count), item.image(), null);
		}
		List<ItemStack> results = recipe.results.items;
		SlotInv outputInv = building.getOutputInv();
		for(int i = 0; i < results.size(); i++)
		{
			Item item = results.get(i).item;
			InvSlot slot = outputInv.getSlot(item);
			tiles[4][i + 1] = new GuiTile(except1(results.get(i).count), item.image(), null);
			tiles[5][i + 1] = new GuiTile(slot.getCurrent() + " / " +
					slot.getLimit(), null, null);
		}
	}

	@Override
	public int xw()
	{
		return 7;
	}

	@Override
	public int yw()
	{
		return 5;
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