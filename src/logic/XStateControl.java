package logic;

import building.adv.*;
import building.transport.*;
import entity.*;
import entity.sideinfo.*;
import geom.f1.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.editor.*;
import logic.editor.xstate.*;
import logic.gui.*;
import logic.gui.guis.*;
import logic.xstate.*;

public class XStateControl implements XStateHolder, ConvInputConsumer
{
	private final MainState mainState;
	private final LevelEditor levelEditor;
	private final SideInfoFrame sideInfoFrame;
	private NState state;
	private List<NState> menu;
	private VisMark cursorMarker;
	private List<VisMark> dragMarker;
	private Map<Tile, Long> allEnemyReach;
	private final List<VisMark> visMarked;


	public XStateControl(MainState mainState, LevelEditor levelEditor, SideInfoFrame sideInfoFrame, NState state)
	{
		this.mainState = mainState;
		this.levelEditor = levelEditor;
		this.sideInfoFrame = sideInfoFrame;
		dragMarker = List.of();
		visMarked = new ArrayList<>();
		setState(state);
	}

	@Override
	public void setState(NState state)
	{
		this.state = state;
		state.onEnter(sideInfoFrame, mainState.levelMap, mainState);
		menu = state.menu().getEntries().stream().filter(e -> e.keepInMenu(mainState, mainState.levelMap)).collect(Collectors.toList());
	}

	@Override
	public NState getState()
	{
		return state;
	}

	@Override
	public List<NState> getMenu()
	{
		return menu;
	}

	@Override
	public XGUIState getGUI()
	{
		if(state instanceof XGUIState guiState)
			return guiState;
		else
			return null;
	}

	@Override
	public List<VisMark> visMarked()
	{
		return visMarked;
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
			cursorMarker = new VisMark(mapTile, "mark.cursor", VisMark.d2);
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
		if(state instanceof NMarkState markState && key.canClick())
		{
			markState.onClick(mainState, mainState.levelMap, this, mapTile, key);
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
				if(advTile.building() != null)
				{
					onClickBuilding(advTile.building(), key);
				}
				else if(advTile.entity() != null)
				{
					onClickEntity(advTile.entity(), mainState.turnCounter > 0, key);
				}
			}
			else
			{
				if(advTile.entity() != null)
				{
					onClickEntity(advTile.entity(), mainState.turnCounter > 0, key);
				}
				else if(advTile.building() != null)
				{
					onClickBuilding(advTile.building(), key);
				}
			}
		}
	}

	private void onClickEntity(XCharacter entity, boolean levelStarted, XKey key)
	{
		if(entity.team() == CharacterTeam.HERO)
		{
			if(key.hasFunction("Choose"))
			{
				if(levelStarted)
				{
					setState(new AdvMoveState(entity));
				}
				else
				{
					setState(new SwapState(entity));
				}
			}
			else if(key.hasFunction("Menu"))
			{
				setState(new CharacterInvGUI(entity));
			}
		}
		else
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
				setState(new TransportTargetsState(building, transport));
			}
		}
	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, XKey key, boolean finished)
	{
		if(!(state instanceof XGUIState))
		{
			dragMarker = mainState.levelMap.y1.betweenArea(startTile, endTile).stream()
					.map(e -> new VisMark(e, "mark.cursor.drag", VisMark.d3)).collect(Collectors.toList());
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
			if(mainState.showAllEnemyReach)
				mainState.levelMap.requireUpdate();
		}
		else if(key.hasFunction("Escape"))
		{
			if(state instanceof XGUIState guiState)
			{
				guiState.clickOutside(key, this);
			}
			else if(state instanceof NMarkState markState)
			{
				markState.onEscape(this);
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
		visMarked.clear();
		if(mainState.showAllEnemyReach)
		{
			if(mainState.levelMap.checkUpdate())
			{
				allEnemyReach = mainState.levelMap.allEnemyReach();
			}
			allEnemyReach.forEach((t, n) -> visMarked.add(new VisMark(t, "mark.reach.all", 0.8)));
		}
		if(state instanceof NMarkState markState)
		{
			visMarked.addAll(markState.visMarked(mainState));
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
		visMarked.clear();
		if(mainState.showAllEnemyReach)
		{
			if(mainState.levelMap.checkUpdate())
			{
				allEnemyReach = mainState.levelMap.allEnemyReach();
			}
			allEnemyReach.forEach((t, n) -> visMarked.add(new VisMark(t, "mark.reach.all", 0.8)));
		}
		if(state instanceof NMarkState markState)
		{
			visMarked.addAll(markState.visMarked(mainState));
		}
	}
}