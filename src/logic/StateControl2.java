package logic;

import building.adv.*;
import entity.*;
import geom.f1.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.editor.*;
import logic.editor.xstate.*;
import logic.gui.*;
import logic.gui.guis.*;
import logic.xstate.*;

public class StateControl2 implements XStateHolder, ConvInputConsumer
{
	private final MainState mainState;
	private final LevelEditor levelEditor;
	private NState state;
	private List<NState> menu;
	private VisMark cursorMarker;
	private List<VisMark> dragMarker;

	public StateControl2(MainState mainState, LevelEditor levelEditor, NState state)
	{
		this.mainState = mainState;
		this.levelEditor = levelEditor;
		dragMarker = List.of();
		setState(state);
	}

	@Override
	public NState getState()
	{
		return state;
	}

	@Override
	public XGUIState getGUI()
	{
		if(state instanceof XGUIState)
			return (XGUIState) state;
		else
			return null;
	}

	@Override
	public List<NState> getMenu()
	{
		return menu;
	}

	@Override
	public void setState(NState state)
	{
		this.state = state;
		state.onEnter(mainState);
		menu = state.menu().getEntries().stream().filter(e -> e.keepInMenu(mainState)).collect(Collectors.toList());
	}

	@Override
	public void mousePosition(boolean insideGUI, Tile offsetGUITile, int menuOption,
			int editorOption, Tile mapTile, boolean moved, boolean drag, XKey key)
	{
		if(state instanceof XGUIState xguiState)
		{
			if(moved)
			{
				if(insideGUI)
				{
					xguiState.target(offsetGUITile.v()[0], offsetGUITile.v()[1]);
				}
			}
			if(key.canClick())
			{
				if(insideGUI)
				{
					xguiState.click(offsetGUITile.v()[0], offsetGUITile.v()[1], key, this);
				}
				else if(menuOption >= 0)
				{
					onMenuTarget(menuOption, key);
				}
				else
				{
					xguiState.clickOutside(key, this);
				}
			}
			cursorMarker = null;
		}
		else if(menuOption >= 0)
		{
			//menu
			onMenuTarget(menuOption, key);
			cursorMarker = null;
		}
		else if(state.editMode() && editorOption >= 0)
		{
			//editor
			levelEditor.onEditorTarget(editorOption, key);
			cursorMarker = null;
		}
		else
		{
			//tile
			handleMapTarget(mapTile, key);
			cursorMarker = new VisMark(mapTile, Color.ORANGE, VisMark.d2);
		}
	}

	@Override
	public void mouseOutside()
	{
		cursorMarker = null;
	}

	private void onMenuTarget(int menuOption, XKey key)
	{
		if(key.canClick())
		{
			if(state instanceof NAutoState)
				return;
			NState newState = menu.get(menuOption);
			if(newState != state)
			{
				setState(newState);
			}
		}
	}

	private void handleMapTarget(Tile mapTile, XKey key)
	{
		if(state instanceof NAutoState)
			return;
		if(state instanceof NMarkState && key.canClick())
		{
			((NMarkState) state).onClick(mapTile, mainState, this, key);
		}
		else if(state.editMode() && key.canClick())
		{
			levelEditor.onMapClick(mapTile, key);
		}
		else if(key.canClick())
		{
			AdvTile advTile = mainState.levelMap.advTile(mapTile);
			if(mainState.preferBuildings)
			{
				if(advTile.getBuilding() != null)
				{
					onClickBuilding(advTile.getBuilding(), key);
				}
				else if(advTile.getEntity() != null)
				{
					onClickEntity(advTile.getEntity(), mainState.turnCounter > 0, key);
				}
			}
			else
			{
				if(advTile.getEntity() != null)
				{
					onClickEntity(advTile.getEntity(), mainState.turnCounter > 0, key);
				}
				else if(advTile.getBuilding() != null)
				{
					onClickBuilding(advTile.getBuilding(), key);
				}
			}
		}
	}

	private void onClickEntity(XEntity entity, boolean levelStarted, XKey key)
	{
		if(entity instanceof XHero)
		{
			if(key.hasFunction("Choose"))
			{
				if(levelStarted)
				{
					setState(new AdvMoveState((XHero) entity));
				}
				else
				{
					setState(new SwapState((XHero) entity));
				}
			}
			else if(key.hasFunction("Menu"))
			{
				setState(new CharacterInvGUI(entity));
			}
		}
		else if(entity instanceof XEnemy)
		{
			if(key.hasFunction("Choose"))
			{
				setState(new ReachViewState(entity, true));
			}
			else if(key.hasFunction("Menu"))
			{
				setState(new CharacterInvGUI(entity));
			}
		}
	}

	private void onClickBuilding(XBuilding building, XKey key)
	{
		if(building.function() instanceof ProcessInv processInv)
		{
			if(key.hasFunction("Choose"))
			{
				setState(new ProductionFloorsState(building, processInv));
			}
			else if(key.hasFunction("Menu"))
			{
				setState(new ProductionInvGUI(building, processInv));
			}
		}
		else if(building.function() instanceof Transport transport)
		{
			if(key.hasFunction("Choose"))
			{
				setState(new TransportTargetsState(building, transport));
			}
			else if(key.hasFunction("Menu"))
			{
				//setState(new TransportTargetsState(building, transport));
			}
		}
	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, XKey key, boolean finished)
	{
		if(!(state instanceof XGUIState))
		{
			dragMarker = mainState.y1.betweenArea(startTile, endTile).stream()
					.map(e -> new VisMark(e, Color.CYAN, VisMark.d3)).collect(Collectors.toList());
			if(finished && state.editMode() && key.canDrag())
			{
				levelEditor.onMapDrag(startTile, endTile, key);
			}
		}
		else
		{
			dragMarker = List.of();
		}
	}

	@Override
	public void noDrag()
	{
		dragMarker = List.of();
	}

	@Override
	public void handleKey(XKey key)
	{
		if(key.hasFunction("Editor Mode"))
		{
			if(state instanceof NoneState)
			{
				setState(EditingState.INSTANCE);
			}
			else if(state instanceof EditingState)
			{
				setState(NoneState.INSTANCE);
			}
		}
		else if(key.hasFunction("Click Mode"))
		{
			mainState.preferBuildings = !mainState.preferBuildings;
		}
		else if(key.hasFunction("All Enemy Reach"))
		{
			mainState.showAllEnemyReach = !mainState.showAllEnemyReach;
		}
		else if(key.hasFunction("Escape"))
		{
			if(state instanceof XGUIState)
			{
				((XGUIState) state).clickOutside(key, this);
			}
			else if(state instanceof NMarkState)
			{
				((NMarkState) state).onEscape(this);
			}
		}
		else if(!(state instanceof NAutoState))
		{
			for(NState menuEntry : menu)
			{
				if(key.hasFunction(menuEntry.keybind()))
				{
					if(menuEntry != state)
					{
						setState(menuEntry);
					}
					return;
				}
			}
		}
	}

	@Override
	public void tick()
	{
		if(state instanceof NAutoState autoState)
		{
			autoState.tick(mainState);
			if(autoState.finished())
			{
				setState(autoState.nextState());
			}
		}
		List<VisMark> visMarked = mainState.visMarked;
		visMarked.clear();
		if(mainState.showAllEnemyReach)
		{
			Map<Tile, Long> v = mainState.levelMap.getEntitiesE().stream().flatMap(character ->
				new Pathing(mainState.y1, character, character.movement(),
						mainState.levelMap, null).start().getEndpoints()
						.stream().flatMap(loc -> character.attackRanges(false).stream()
						.flatMap(e -> mainState.y1.range(loc, e, e).stream())).distinct())
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			v.forEach((t, n) -> visMarked.add(new VisMark(t, Color.BLACK, 0.8)));
		}
		if(state instanceof NMarkState)
		{
			visMarked.addAll(((NMarkState) state).visMarked(mainState));
		}
		if(cursorMarker != null)
		{
			visMarked.add(cursorMarker);
		}
		visMarked.addAll(dragMarker);
	}

	@Override
	public void tickPaused()
	{
		List<VisMark> visMarked = mainState.visMarked;
		visMarked.clear();
		if(state instanceof NMarkState)
		{
			visMarked.addAll(((NMarkState) state).visMarked(mainState));
		}
	}
}