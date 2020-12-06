package logic.guis;

import entity.*;
import item.*;
import item.view.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import gui.*;
import logic.xstate.*;
import statsystem.*;

public final class CharacterCombatGUI extends XGUIState
{
	private static final CTile UNEQUIP = new CTile(1, 6, new GuiTile("gui.stats.unequip"), 2, 1);

	private final XCharacter character;
	private final boolean control;
	private final int xhs;
	private int viewMode;
	private Item equippedItem;
	private CElement viewModeElement;
	private TargetScrollList<ItemView> invView;
	private TargetScrollList<AttackMode3> modeChooseView;
	private ScrollList<CharSequence> statsView;
	private Item chosenItem;

	public CharacterCombatGUI(XCharacter character, int viewMode)
	{
		this.character = character;
		control = character.team() == CharacterTeam.HERO;
		xhs = control ? 1 : 0;
		this.viewMode = viewMode;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		equippedItem = character.stats().lastUsed().item;
		elements.add(new CElement(new CTile(1 + xhs, 0, 3, 1), new GuiTile(character.name())));
		if(control)
		{
			elements.add(new CElement(UNEQUIP, true, () -> character.stats().lastUsed().active, this::unequip));
		}
		viewModeElement = new CElement(new CTile(3 + xhs, 6, 2, 1), true, null, () -> viewMode = (viewMode + 1) % 2);
		elements.add(viewModeElement);
		invView = new TargetScrollList<>(xhs, 1, 2, 5, 2, 1,
				null, this::itemViewView, control ? this::invClick : null);
		elements.add(invView);
		if(control)
		{
			modeChooseView = new TargetScrollList<>(0, 1, 1, 5, 1, 1,
					null, e -> GuiTile.textView(e.tile()), this::onClickMode);
			elements.add(modeChooseView);
		}
		statsView = new ScrollList<>(2 + xhs, 1, 4, 5, 1, 1,
				null, GuiTile::textView, null);
		elements.add(statsView);
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		viewModeElement.fillTile = new GuiTile(viewMode == 1 ? "gui.stats.mode.stats" : "gui.stats.mode.combatdata");
		invView.elements = null;//character.inv().viewItems(true);
		if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
		{
			statsView.elements = invView.getTargeted().item.info();
		}
		else if(control && modeChooseView.getTargeted() != null)
		{
			statsView.elements = modeChooseView.getTargeted().modeInfo();
		}
		else if(viewMode == 1)
		{
			statsView.elements = character.stats().statsInfo();
		}
		else
		{
			statsView.elements = character.stats().infoWithEquip();
		}
		if(control)
		{
			if(chosenItem != null && character.stats().getItemFilter().canContain(chosenItem))
			{
				modeChooseView.elements = modesForItem(character.stats(), chosenItem);
			}
			else
			{
				modeChooseView.elements = List.of();
			}
		}
	}

	private static List<AttackMode3> modesForItem(Stats stats, Item item)
	{
		if(item instanceof AttackItem item2)
			return item2.attackModes().stream().map(e -> AttackMode3.convert(stats, e)).collect(Collectors.toList());
		return List.of();
	}

	private void invClick(ItemView itemView)
	{
		chosenItem = (itemView.base > 0 ? itemView.item : null);
	}

	private void onClickMode(AttackMode3 mode)
	{
		character.stats().equipMode(mode.shortVersion());
		equippedItem = chosenItem;
		chosenItem = null;
	}

	private void unequip()
	{
		character.stats().equipMode(AttackMode.EVADE_MODE);
		equippedItem = null;
		chosenItem = null;
	}

	private GuiTile[] itemViewView(ItemView itemView)
	{
		boolean mark = itemView.item == equippedItem;
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit(), null, false, mark ? "gui.background.active" : null),
						new GuiTile(null, itemView.item.image(), false, mark ? "gui.background.active" : null)
				};
	}

	@Override
	public CharSequence text()
	{
		return "menu.stats";
	}

	@Override
	public String keybind()
	{
		return "state.stats";
	}

	@Override
	public XMenu menu()
	{
		if(character.team() == CharacterTeam.HERO)
			return new XMenu(new TagInvGUI(character), new TradeTargetState(character), new EndTurnState());
		else
			return new XMenu(new TagInvGUI(character), new EndTurnState());
	}

	@Override
	public int xw()
	{
		return 6 + xhs;
	}

	@Override
	public int yw()
	{
		return 7;
	}
}