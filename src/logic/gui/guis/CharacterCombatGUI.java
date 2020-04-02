package logic.gui.guis;

import entity.*;
import item.*;
import item.view.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import logic.gui.*;
import logic.xstate.*;
import statsystem.*;

public final class CharacterCombatGUI extends XGUIState
{
	private static final CTile NAME = new CTile(2, 0, 3, 1);
	private static final CTile UNEQUIP = new CTile(1, 6, new GuiTile("gui.stats.unequip"), 2, 1);
	private static final CTile VIEW_MODE = new CTile(4, 6, 2, 1);

	private final XCharacter character;
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
		this.viewMode = viewMode;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		equippedItem = character.stats().lastUsed().item;
		elements.add(new CElement(NAME, new GuiTile(character.name())));
		elements.add(new CElement(UNEQUIP, true, () -> character.stats().lastUsed().active, this::unequip));
		viewModeElement = new CElement(VIEW_MODE, true, null, () -> viewMode = (viewMode + 1) % 2);
		elements.add(viewModeElement);
		invView = new TargetScrollList<>(1, 1, 2, 5, 2, 1,
				null, this::itemViewView, e -> chosenItem = (e.base > 0 ? e.item : null));
		elements.add(invView);
		modeChooseView = new TargetScrollList<>(0, 1, 1, 5, 1, 1,
				null, e -> GuiTile.textView(e.tile()), this::onClickMode);
		elements.add(modeChooseView);
		statsView = new ScrollList<>(3, 1, 4, 5, 1, 1,
				null, GuiTile::textView, null);
		elements.add(statsView);
		update();
	}

	@Override
	protected void updateBeforeDraw()
	{
		viewModeElement.fillTile = new GuiTile(viewMode == 1 ? "gui.stats.mode.stats" : "gui.stats.mode.combatdata");
		invView.elements = character.outputInv().viewItems(true);
		if(invView.getTargeted() != null && !invView.getTargeted().item.info().isEmpty())
		{
			statsView.elements = invView.getTargeted().item.info();
		}
		else if(modeChooseView.getTargeted() != null)
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
		if(chosenItem != null && character.stats().getItemFilter().canContain(chosenItem))
		{
			modeChooseView.elements = modesForItem(character.stats(), chosenItem);
		}
		else
		{
			modeChooseView.elements = List.of();
		}
	}

	private static List<AttackMode3> modesForItem(Stats stats, Item item)
	{
		if(item instanceof AttackItem item2)
			return item2.attackModes().stream().map(e -> AttackMode3.convert(stats, e)).collect(Collectors.toList());
		return List.of();
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
		return XMenu.characterGUIMenu(character);
		/*if(character.team() == CharacterTeam.HERO)
			return XMenu.characterGUIMenu(character);
		else
			return XMenu.enemyGUIMenu(character);*/
	}

	@Override
	public int xw()
	{
		return 7;
	}

	@Override
	public int yw()
	{
		return 7;
	}
}