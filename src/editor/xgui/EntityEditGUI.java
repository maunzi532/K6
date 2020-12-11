package editor.xgui;

import entity.*;
import gui.*;
import item.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import xstate.*;

public final class EntityEditGUI extends GUIState
{
	private static final AreaTile textInv = new AreaTile(2, 0, 2, 1);
	//private static final AreaTile weight = new AreaTile(0, 0);

	private final XCharacter entity;
	private List<? extends CharSequence> info;
	private TargetScrollList<NumberedStack> invView;
	private ScrollList<Integer> infoView;
	private ScrollList<Integer> changeView;
	private int changeStatNum;
	private List<? extends CharSequence> changeOptions;
	private TileElement textInvE;

	public EntityEditGUI(XCharacter entity)
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
		changeStatNum = -1;
		changeOptions = List.of();
		invView = new TargetScrollList<>(0, 1, 2, 5, 2, 1,
				entity.inv().viewItems(), GuiTile::itemStackView, null);
		elements.add(invView);
		infoView = new ScrollList<>(3, 1, 3, 5, 1, 1, null,
				e -> GuiTile.textView(info.get(e)), this::clickInfo);
		elements.add(infoView);
		changeView = new ScrollList<>(7, 1, 1, 5, 1, 1, null,
				e -> GuiTile.textView(changeOptions.get(e)), null
				/*target -> entity.stats().applyEditOption(changeStatNum, target, entity)*/);
		elements.add(changeView);
		textInvE = new TileElement(textInv);
		elements.add(textInvE);
		//elements.add(new TileElement(weight, new GuiTile(entity.inv().viewInvWeight().currentWithLimit())));
		update();
	}

	@Override
	public CharSequence text()
	{
		return "menu.edit.character";
	}

	@Override
	public String keybind()
	{
		return "state.edit.character";
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
	protected void updateBeforeDraw()
	{
		if(invView.getTargeted() != null && !invView.getTargeted().item().info().toString().isBlank())
			info = List.of(invView.getTargeted().item().info());
		else
			info = List.of("menu.edit.character"); //entity.stats().infoEdit();
		infoView.elements = IntStream.range(0, info.size()).boxed().collect(Collectors.toList());
		changeView.elements = IntStream.range(0, changeOptions.size()).boxed().collect(Collectors.toList());
		textInvE.fillTile = new GuiTile(entity.name());
	}

	private void clickInfo(int target)
	{
		changeStatNum = target;
		changeOptions = editOptions(target);
	}

	private static List<? extends CharSequence> editOptions(int num)
	{
		if(num == 0)
			return List.of("stats.editoption.name.name", "stats.editoption.name.mapimage", "stats.editoption.name.sideimage");
		if(num == 1)
			return List.of("stats.editoption.class.previous", "stats.editoption.class.next");
		if(num == 2)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.level.resetstats");
		if(num == 3)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.exp.reset");
		if(num <= 11)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.lvstat.reset");
		if(num == 12)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.health.reset");
		if(num == 13)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.exhaustion.reset");
		if(num == 14)
			return List.of("stats.editoption.plus", "stats.editoption.minus", "stats.editoption.movement.reset");
		if(num == 15)
			return List.of("stats.editoption.defendwith.autoequip");
		return List.of();
	}
}