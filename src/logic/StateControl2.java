package logic;

import building.*;
import entity.*;
import geom.f1.*;
import logic.editor.xstate.*;
import logic.gui.*;
import logic.gui.guis.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import logic.editor.*;
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
			int editorOption, Tile mapTile, boolean moved, boolean drag, int mouseKey)
	{
		if(state instanceof XGUIState)
		{
			XGUIState xguiState = (XGUIState) state;
			if(moved)
			{
				if(insideGUI)
				{
					xguiState.target(offsetGUITile.v[0], offsetGUITile.v[1]);
				}
			}
			if(mouseKey >= 0)
			{
				if(insideGUI)
				{
					xguiState.click(offsetGUITile.v[0], offsetGUITile.v[1], mouseKey, this);
				}
				else if(menuOption >= 0)
				{
					onMenuTarget(menuOption, mouseKey);
				}
				else
				{
					xguiState.clickOutside(mouseKey, this);
				}
			}
			cursorMarker = null;
		}
		else if(menuOption >= 0)
		{
			//menu
			onMenuTarget(menuOption, mouseKey);
			cursorMarker = null;
		}
		else if(state.editMode() && editorOption >= 0)
		{
			//editor
			levelEditor.onEditorTarget(editorOption, mouseKey);
			cursorMarker = null;
		}
		else
		{
			//tile
			handleMapTarget(mapTile, mouseKey);
			cursorMarker = new VisMark(mapTile, Color.ORANGE, VisMark.d2);
		}
	}

	@Override
	public void mouseOutside()
	{
		cursorMarker = null;
	}

	private void onMenuTarget(int menuOption, int mouseKey)
	{
		if(mouseKey >= 0)
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

	private void handleMapTarget(Tile mapTile, int mouseKey)
	{
		if(state instanceof NAutoState)
			return;
		if(state instanceof NMarkState && mouseKey >= 0)
		{
			((NMarkState) state).onClick(mapTile, mainState, this, mouseKey);
		}
		else if(state.editMode() && mouseKey >= 0)
		{
			levelEditor.onMapClick(mapTile, mouseKey);
		}
		else if(mouseKey >= 0)
		{
			AdvTile advTile = mainState.levelMap.advTile(mapTile);
			if(mainState.preferBuildings)
			{
				if(advTile.getBuilding() != null)
				{
					onClickBuilding(advTile.getBuilding(), mouseKey);
				}
				else if(advTile.getEntity() != null)
				{
					onClickEntity(advTile.getEntity(), mainState.turnCounter > 0, mouseKey);
				}
			}
			else
			{
				if(advTile.getEntity() != null)
				{
					onClickEntity(advTile.getEntity(), mainState.turnCounter > 0, mouseKey);
				}
				else if(advTile.getBuilding() != null)
				{
					onClickBuilding(advTile.getBuilding(), mouseKey);
				}
			}
		}
	}

	private void onClickEntity(XEntity entity, boolean levelStarted, int mouseKey)
	{
		if(entity instanceof XHero)
		{
			if(mouseKey == 1)
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
			else if(mouseKey == 3)
			{
				setState(new CharacterInvGUI((XHero) entity));
			}
		}
		else if(entity instanceof XEnemy)
		{
			if(mouseKey == 1)
			{
				setState(new ReachViewState(entity, true));
			}
			else if(mouseKey == 3)
			{
				setState(new CharacterInvGUI((XEnemy) entity));
			}
		}
	}

	private void onClickBuilding(MBuilding building, int mouseKey)
	{
		if(building instanceof ProductionBuilding)
		{
			if(mouseKey == 1)
			{
				setState(new ProductionFloorsState((ProductionBuilding) building));
			}
			else if(mouseKey == 3)
			{
				setState(new ProductionInvGUI((ProductionBuilding) building));
			}
		}
		else if(building instanceof Transporter)
		{
			if(mouseKey == 1)
			{
				setState(new TransportTargetsState((Transporter) building));
			}
			else if(mouseKey == 3)
			{
				//setState(new TransportTargetsState((Transporter) building));
			}
		}
	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, int mouseKey, boolean finished)
	{
		if(!(state instanceof XGUIState))
		{
			dragMarker = mainState.y1.betweenArea(startTile, endTile).stream()
					.map(e -> new VisMark(e, Color.CYAN, VisMark.d3)).collect(Collectors.toList());
			if(finished && state.editMode() && mouseKey >= 0)
			{
				levelEditor.onMapDrag(startTile, endTile, mouseKey);
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
	public void handleKey(KeyCode keyCode)
	{
		if(keyCode == KeyCode.Q)
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
		else if(keyCode == KeyCode.TAB)
		{
			mainState.preferBuildings = !mainState.preferBuildings;
		}
		else if(keyCode == KeyCode.ESCAPE)
		{
			if(state instanceof XGUIState)
			{
				((XGUIState) state).clickOutside(1, this);
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
				if(keyCode == menuEntry.keybind())
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
		if(state instanceof NAutoState)
		{
			NAutoState autoState = (NAutoState) state;
			autoState.tick(mainState);
			if(autoState.finished())
			{
				setState(autoState.nextState());
			}
		}
		List<VisMark> visMarked = mainState.visMarked;
		visMarked.clear();
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