package logic.gui.guis;

import building.*;
import building.blueprint.*;
import item.*;
import item.inv.*;
import item.view.*;
import javafx.scene.input.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class RecipeGUI extends XGUIState
{
	private static final CTile textRequires = new CTile(1, 0, new GuiTile("Requires"), 2, 1);
	private static final CTile textResults = new CTile(4, 0, new GuiTile("Results"), 2, 1);
	private static final CTile prev = new CTile(0, 1, new GuiTile("Previous"));
	private static final CTile next = new CTile(6, 1, new GuiTile("Next"));
	private static final CTile arrow = new CTile(3, 1, new GuiTile(null, XGUIState.ARROW, false, null));

	private ProductionBuilding building;
	private int recipeNum;
	private ScrollList<ItemStack> requireView;
	private ScrollList<ItemStack> resultView;

	public RecipeGUI(ProductionBuilding building)
	{
		this.building = building;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		recipeNum = building.lastViewedRecipeNum;
		requireView = new ScrollList<>(1, 1, 2, 5, 2, 1, null,
				this::elementViewRequired, null);
		elements.add(requireView);
		resultView = new ScrollList<>(4, 1, 2, 5, 2, 1, null,
				this::elementViewResults, null);
		elements.add(resultView);
		elements.add(new CElement(textRequires));
		elements.add(new CElement(textResults));
		elements.add(new CElement(prev, true, () -> recipeNum > 0, () -> recipeNum--));
		elements.add(new CElement(next, true, () -> recipeNum < building.getRecipes().size() - 1, () -> recipeNum++));
		elements.add(new CElement(arrow));
		update();
	}

	@Override
	public String text()
	{
		return "View";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.V;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building);
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

	@Override
	protected void updateBeforeDraw()
	{
		Recipe recipe = building.getRecipes().get(recipeNum);
		requireView.elements = recipe.required.items;
		resultView.elements = recipe.results.items;
	}

	public GuiTile[] elementViewRequired(ItemStack stack)
	{
		Inv inv = building.getInputInv();
		ItemView itemView = inv.viewRecipeItem(stack.item);
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit()),
						new GuiTile(InvNumView.except1(stack.count), itemView.item.image(), false, null)
				};
	}

	public GuiTile[] elementViewResults(ItemStack stack)
	{
		Inv inv = building.getOutputInv();
		ItemView itemView = inv.viewRecipeItem(stack.item);
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit()),
						new GuiTile(InvNumView.except1(stack.count), itemView.item.image(), false, null)
				};
	}

	@Override
	public void close(XStateHolder stateHolder)
	{
		building.lastViewedRecipeNum = recipeNum;
		super.close(stateHolder);
	}
}