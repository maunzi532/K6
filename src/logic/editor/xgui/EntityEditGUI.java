package logic.editor.xgui;

import entity.*;
import item.view.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;

public class EntityEditGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private InvEntity entity;
	private InvNumView weightView;
	private List<String> info;
	private ScrollList<ItemView> invView;
	private ScrollList<Integer> infoView;
	private ScrollList<Integer> changeView;
	private ItemView viewing;
	private boolean targetChanged;
	private int changeStatNum;
	private List<String> changeOptions;

	public EntityEditGUI(InvEntity entity)
	{
		this.entity = entity;
	}

	@Override
	public boolean editMode()
	{
		return true;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		weightView = entity.inputInv().viewInvWeight();
		invView = new ScrollList<>(0, 1, 2, 5, 2, 1);
		invView.elements = entity.inputInv().viewItems(true);
		infoView = new ScrollList<>(3, 1, 3, 5, 1, 1);
		changeStatNum = -1;
		changeOptions = List.of();
		changeView = new ScrollList<>(7, 1, 1, 5, 1, 1);
		update();
	}

	@Override
	public String text()
	{
		return "Edit";
	}

	@Override
	public KeyCode keybind()
	{
		return KeyCode.E;
	}

	@Override
	public XMenu menu()
	{
		return XMenu.entityEditMenu(entity);
	}

	@Override
	public int xw()
	{
		return 8;
	}

	@Override
	public int yw()
	{
		return 6;
	}

	@Override
	private void update()
	{
		initTiles();
		if(viewing != null)
			info = viewing.item.info();
		else
			info = entity.getStats().infoEdit();
		infoView.elements = IntStream.range(0, info.size()).boxed().collect(Collectors.toList());
		changeView.elements = IntStream.range(0, changeOptions.size()).boxed().collect(Collectors.toList());
		invView.update();
		infoView.update();
		changeView.update();
		invView.draw(tiles, GuiTile::itemViewView);
		infoView.draw(tiles, e -> new GuiTile[]{new GuiTile(info.get(e))});
		changeView.draw(tiles, e -> new GuiTile[]{new GuiTile(changeOptions.get(e))});
		setEmptyTileAndFill(textInv, new GuiTile(entity.name()));
		setEmptyTileAndFill(weight, new GuiTile(weightView.currentWithLimit()));
	}

	@Override
	public void target(int x, int y)
	{
		var result0 = invView.target(x, y, false);
		targeted = result0.targetTile;
		if(viewing != result0.target)
		{
			viewing = result0.target;
			update();
		}
		if(result0.inside)
			return;
		var result1 = infoView.target(x, y, false);
		if(result1.inside)
		{
			targeted = result1.targetTile;
			return;
		}
		var result2 = changeView.target(x, y, false);
		if(result2.inside)
		{
			targeted = result2.targetTile;
		}
	}

	@Override
	public void click(int x, int y, int key, XStateHolder stateHolder)
	{
		var result0 = invView.target(x, y, true);
		var result1 = infoView.target(x, y, true);
		var result2 = changeView.target(x, y, true);
		if(result0.requiresUpdate || result1.requiresUpdate || result2.requiresUpdate)
			update();
		if(result1.target != null)
		{
			changeStatNum = result1.target;
			changeOptions = entity.getStats().editOptions(result1.target);
			update();
		}
		if(result2.target != null)
		{
			entity.getStats().applyEditOption(changeStatNum, result2.target, entity);
			update();
		}
	}
}