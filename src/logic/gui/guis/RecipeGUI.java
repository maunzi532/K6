package logic.gui.guis;

import building.adv.*;
import building.blueprint.*;
import item.*;
import item.inv.*;
import item.view.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public final class RecipeGUI extends XGUIState
{
	private static final CTile textRequires = new CTile(1, 0, new GuiTile("Requires"), 2, 1);
	private static final CTile textResults = new CTile(4, 0, new GuiTile("Results"), 2, 1);
	private static final CTile prev = new CTile(0, 1, new GuiTile("Previous"));
	private static final CTile next = new CTile(6, 1, new GuiTile("Next"));
	private static final CTile arrow = new CTile(3, 1, new GuiTile(null, "gui.recipe.arrow", false, null));

	private final XBuilding building;
	private final ProcessInv processInv;
	private int recipeNum;
	private ScrollList<ItemStack> requireView;
	private ScrollList<ItemStack> resultView;

	public RecipeGUI(XBuilding building, ProcessInv processInv)
	{
		this.building = building;
		this.processInv = processInv;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		recipeNum = processInv.lastViewedRecipeNum;
		requireView = new ScrollList<>(1, 1, 2, 5, 2, 1, null,
				this::elementViewRequired, null);
		elements.add(requireView);
		resultView = new ScrollList<>(4, 1, 2, 5, 2, 1, null,
				this::elementViewResults, null);
		elements.add(resultView);
		elements.add(new CElement(textRequires));
		elements.add(new CElement(textResults));
		elements.add(new CElement(prev, true, () -> recipeNum > 0, () -> recipeNum--));
		elements.add(new CElement(next, true, () -> recipeNum < processInv.recipes().size() - 1, () -> recipeNum++));
		elements.add(new CElement(arrow));
		update();
	}

	@Override
	public String text()
	{
		return "View";
	}

	@Override
	public String keybind()
	{
		return "Recipe view";
	}

	@Override
	public XMenu menu()
	{
		return XMenu.productionMenu(building, processInv);
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
		Recipe recipe = processInv.recipes().get(recipeNum);
		requireView.elements = recipe.required().items;
		resultView.elements = recipe.results().items;
	}

	public GuiTile[] elementViewRequired(ItemStack stack)
	{
		Inv inv = building.inputInv();
		ItemView itemView = inv.viewRecipeItem(stack.item);
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit()),
						new GuiTile(InvNumView.except1(stack.count), itemView.item.image(), false, null)
				};
	}

	public GuiTile[] elementViewResults(ItemStack stack)
	{
		Inv inv = building.outputInv();
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
		processInv.lastViewedRecipeNum = recipeNum;
		super.close(stateHolder);
	}
}