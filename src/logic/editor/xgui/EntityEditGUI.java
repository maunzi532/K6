package logic.editor.xgui;

import entity.*;
import item.view.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;
import statsystem.*;

public final class EntityEditGUI extends XGUIState
{
	private static final CTile textInv = new CTile(2, 0, 2, 1);
	private static final CTile weight = new CTile(0, 0);

	private final XCharacter entity;
	private List<? extends CharSequence> info;
	private TargetScrollList<ItemView> invView;
	private ScrollList<Integer> infoView;
	private ScrollList<Integer> changeView;
	private int changeStatNum;
	private List<? extends CharSequence> changeOptions;
	private CElement textInvE;

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
				entity.inputInv().viewItems(true), GuiTile::itemViewView, null);
		elements.add(invView);
		infoView = new ScrollList<>(3, 1, 3, 5, 1, 1, null,
				e -> GuiTile.textView(info.get(e)), this::clickInfo);
		elements.add(infoView);
		changeView = new ScrollList<>(7, 1, 1, 5, 1, 1, null,
				e -> GuiTile.textView(changeOptions.get(e)),
				target -> entity.stats().applyEditOption(changeStatNum, target, entity));
		elements.add(changeView);
		textInvE = new CElement(textInv);
		elements.add(textInvE);
		elements.add(new CElement(weight, new GuiTile(entity.inputInv().viewInvWeight().currentWithLimit())));
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
		if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
			info = invView.getTargeted().item.info();
		else
			info = entity.stats().infoEdit();
		infoView.elements = IntStream.range(0, info.size()).boxed().collect(Collectors.toList());
		changeView.elements = IntStream.range(0, changeOptions.size()).boxed().collect(Collectors.toList());
		textInvE.fillTile = new GuiTile(entity.name());
	}

	private void clickInfo(int target)
	{
		changeStatNum = target;
		changeOptions = Stats.editOptions(target);
	}
}