package logic.gui.guis;

import building.*;
import building.blueprint.*;
import item.*;
import item.inv.*;
import item.view.*;
import java.util.*;
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
		requireView = new ScrollList<>(1, 1, 2, 5, 2, 1);
		resultView = new ScrollList<>(4, 1, 2, 5, 2, 1);
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

	private void update()
	{
		initTiles();
		Recipe recipe = building.getRecipes().get(recipeNum);
		requireView.elements = recipe.required.items;
		resultView.elements = recipe.results.items;
		requireView.update();
		resultView.update();
		requireView.draw(tiles, this::elementViewRequired);
		resultView.draw(tiles, this::elementViewResults);
		setTile(textRequires);
		setTile(textResults);
		if(recipeNum > 0)
			setTile(prev);
		if(recipeNum < building.getRecipes().size() - 1)
			setTile(next);
		setTile(arrow);
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
	public void target(int x, int y)
	{
		Optional<CTile> requireViewTarget = requireView.target(x, y, false, null, null);
		if(requireViewTarget.isPresent())
		{
			targeted = requireViewTarget.get();
			return;
		}
		Optional<CTile> resultViewTarget = resultView.target(x, y, false, null, null);
		if(resultViewTarget.isPresent())
		{
			targeted = resultViewTarget.get();
			return;
		}
		if(recipeNum > 0 && prev.contains(x, y))
			targeted = prev;
		else if(recipeNum < building.getRecipes().size() - 1 && next.contains(x, y))
			targeted = next;
		else
			targeted = CTile.NONE;
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		requireView.target(x, y, true, null, null);
		resultView.target(x, y, true, null, null);
		if(requireView.readUpdateGUIFlag() | resultView.readUpdateGUIFlag())
			update();
		else if(recipeNum > 0 && prev.contains(x, y))
		{
			recipeNum--;
			update();
		}
		else if(recipeNum < building.getRecipes().size() - 1 && next.contains(x, y))
		{
			recipeNum++;
			update();
		}
	}

	@Override
	public void close(XStateHolder stateHolder, boolean setState)
	{
		building.lastViewedRecipeNum = recipeNum;
		super.close(stateHolder, setState);
	}
}