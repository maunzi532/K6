package logic.gui;

import building.*;
import building.blueprint.*;
import inv.*;
import java.util.*;

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
		Inv2 inputInv = building.inputInv();
		for(int i = 0; i < required.size(); i++)
		{
			Item item = required.get(i).item;
			tiles[1][i + 1] = new GuiTile(inputInv.current(item) + " / " +
					inputInv.limit(item), null, null);
			tiles[2][i + 1] = new GuiTile(String.valueOf(required.get(i).count), item.image(), null);
		}
		List<ItemStack> results = recipe.results.items;
		Inv2 outputInv = building.outputInv();
		for(int i = 0; i < results.size(); i++)
		{
			Item item = results.get(i).item;
			tiles[4][i + 1] = new GuiTile(String.valueOf(results.get(i).count), item.image(), null);
			tiles[5][i + 1] = new GuiTile(outputInv.current(item) + " / " +
					outputInv.limit(item), null, null);
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
	public void click(int x, int y)
	{

	}
}