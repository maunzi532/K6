package logic.xstate;

import doubleinv.*;
import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import logic.*;
import logic.guis.*;

public final class TradeTargetState implements NMarkState
{
	private final XCharacter character;
	private List<InvHolder> possibleTargets;
	private List<VisMark> visMarked;

	public TradeTargetState(XCharacter character)
	{
		this.character = character;
	}

	@Override
	public void onEnter(MainState mainState)
	{
		mainState.side().setStandardSideInfo(character);
		LevelMap4 levelMap = mainState.levelMap();
		List<Tile> range = levelMap.y1().range(character.location(), 0, character.accessRange());
		possibleTargets = new ArrayList<>();
		possibleTargets.add(levelMap.storage());
		range.stream().map(levelMap::getEntity).filter(e -> e != null && e != character
				&& levelMap.playerTradeable(e)).forEachOrdered(possibleTargets::add);
		visMarked = possibleTargets.stream().map(e -> new VisMark(getLocation(e),
				"mark.trade.target", VisMark.d2)).collect(Collectors.toList());
	}

	@Override
	public CharSequence text()
	{
		return "menu.trade";
	}

	@Override
	public String keybind()
	{
		return "state.trade";
	}

	@Override
	public boolean keepInMenu(MainState mainState)
	{
		return mainState.levelMap().playerTradeable(character);
	}

	@Override
	public XMenu menu()
	{
		return new XMenu(new TagInvGUI(character), this, new EndTurnState());
	}

	@Override
	public void onClick(MainState mainState, Tile mapTile, XKey key)
	{
		Optional<InvHolder> list = possibleTargets.stream().filter(e -> mapTile.equals(getLocation(e))).findFirst();
		if(list.isPresent())
		{
			startTradeState(mainState.stateHolder(), list.orElseThrow());
		}
		else
		{
			mainState.stateHolder().setState(NoneState.INSTANCE);
		}
	}

	private Tile getLocation(InvHolder possibleTarget)
	{
		if(possibleTarget instanceof Storage4)
			return character.location();
		else
			return possibleTarget.location();
	}

	private void startTradeState(XStateHolder stateHolder, InvHolder inv1)
	{
		stateHolder.setState(new DoubleTradeGUI(character, inv1));
	}

	@Override
	public List<VisMark> visMarked(MainState mainState)
	{
		return visMarked;
	}
}