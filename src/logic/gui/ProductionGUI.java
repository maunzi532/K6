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
		super();
		this.building = building;
		this.recipeNum = recipeNum;
		tiles[0][0] = new GuiTile(building.name(), null, null);
		Recipe recipe = building.getRecipes().get(recipeNum);
		List<ItemStack> required = recipe.required.items;
		SlotInv3 inputInv = building.getInputInv();
		for(int i = 0; i < required.size(); i++)
		{
			Item item = required.get(i).item;
			InvSlot3 slot = inputInv.getSlot(item);
			tiles[1][i + 1] = new GuiTile(slot.getCurrent() + " / " +
					slot.getLimit(), null, null);
			tiles[2][i + 1] = new GuiTile(String.valueOf(required.get(i).count), item.image(), null);
		}
		List<ItemStack> results = recipe.results.items;
		SlotInv3 outputInv = building.getOutputInv();
		for(int i = 0; i < results.size(); i++)
		{
			Item item = results.get(i).item;
			InvSlot3 slot = outputInv.getSlot(item);
			tiles[4][i + 1] = new GuiTile(String.valueOf(results.get(i).count), item.image(), null);
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
	public void click(int x, int y, int key, XStateControl stateControl)
	{

	}

	@Override
	public void close(XStateControl stateControl)
	{
		stateControl.setState(XState.NONE);
	}
}