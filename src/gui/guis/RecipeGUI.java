package gui.guis;

import building.ProductionBuilding;
import building.blueprint.Recipe;
import gui.*;
import inv.*;
import logic.*;

public class RecipeGUI extends XGUI implements InvGUI
{
	private static final CTile textRequires = new CTile(1, 0, new GuiTile("Requires"));
	private static final CTile textResults = new CTile(4, 0, new GuiTile("Results"));
	private static final CTile prev = new CTile(0, 1, new GuiTile("Previous"));
	private static final CTile next = new CTile(6, 1, new GuiTile("Next"));
	private static final CTile arrow = new CTile(3, 1, new GuiTile("Arrow"));

	private final ProductionBuilding building;
	private int recipeNum;
	private final InvGUIPart requireView;
	private final InvGUIPart resultView;

	public RecipeGUI(ProductionBuilding building)
	{
		this.building = building;
		recipeNum = building.lastViewedRecipeNum;
		requireView = new InvGUIPart(0, 1, 1, 1, 5, 2, 1);
		resultView = new InvGUIPart(1, 4, 1, 1, 5, 2, 1);
		update();
	}

	@Override
	public int xw()
	{
		return 7;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	private void update()
	{
		initTiles();
		Recipe recipe = building.getRecipes().get(recipeNum);
		requireView.addToGUI(recipe.required.items.size(), this);
		resultView.addToGUI(recipe.results.items.size(), this);
		setTile(textRequires);
		setTile(textResults);
		if(recipeNum > 0)
			setTile(prev);
		if(recipeNum < building.getRecipes().size() - 1)
			setTile(next);
		setTile(arrow);
	}

	@Override
	public void itemView(int invID, int x, int y1, int index)
	{
		Recipe recipe = building.getRecipes().get(recipeNum);
		ItemStack items = (invID == 0 ? recipe.required : recipe.results).items.get(index);
		Inv inv = invID == 0 ? building.getInputInv() : building.getOutputInv();
		ItemView itemView = inv.viewItem(items.item);
		tiles[x][y1] = new GuiTile(itemView.currentWithLimit());
		tiles[x + 1][y1] = new GuiTile(ItemView.except1(items.count), itemView.item.image(), null);
	}

	@Override
	public void target(int x, int y)
	{

	}

	@Override
	public void onTarget(int invID, int num, int xi, int yi, CTile cTile)
	{

	}

	@Override
	public boolean click(int x, int y, int key, XStateControl stateControl)
	{
		Recipe recipe = building.getRecipes().get(recipeNum);
		requireView.checkClick(x, y, recipe.required.items.size(), this);
		resultView.checkClick(x, y, recipe.results.items.size(), this);
		if(prev.contains(x, y) && recipeNum > 0)
		{
			recipeNum--;
			update();
		}
		if(next.contains(x, y) && recipeNum < building.getRecipes().size() - 1)
		{
			recipeNum++;
			update();
		}
		return false;
	}

	@Override
	public void close(XStateControl stateControl)
	{
		building.lastViewedRecipeNum = recipeNum;
		stateControl.setState(XState.NONE);
	}
}