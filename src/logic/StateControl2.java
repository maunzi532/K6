package logic;

import entity.*;
import geom.f1.*;
import gui.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import levelMap.*;
import levelMap.editor.*;
import logic.xstate.*;

public class StateControl2 implements XStateHolder, ConvInputConsumer
{
	private final MainState mainState;
	private final LevelEditor levelEditor;
	private NState state;
	private List<NState> menu;
	private XGUI xgui;
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
	public XGUI getGUI()
	{
		return xgui;
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
		update();
	}

	private void update()
	{
		if(state instanceof NMarkState)
			mainState.levelMap.setMarked(((NMarkState) state).marked(mainState.levelMap));
		else
			mainState.levelMap.setMarked(Map.of());
		menu = state.menu().getEntries().stream().filter(e -> e.keepInMenu(mainState)).collect(Collectors.toList());
		if(state instanceof NGUIState)
			xgui = ((NGUIState) state).gui(mainState);
		else
			xgui = NoGUI.NONE;
	}

	@Override
	public void mousePosition(double xRel, double yRel, boolean insideGUI, Tile offsetGUITile, int menuOption,
			int editorOption, Tile mapTile, boolean moved, boolean drag, int mouseKey)
	{
		//move camera
		if(state instanceof NGUIState)
		{
			if(moved)
			{
				if(insideGUI)
				{
					xgui.target(offsetGUITile.v[0], offsetGUITile.v[1]);
				}
			}
			if(mouseKey >= 0)
			{
				if(insideGUI)
				{
					xgui.click(offsetGUITile.v[0], offsetGUITile.v[1], mouseKey, this);
				}
				else if(menuOption >= 0)
				{
					onMenuClick(menuOption, mouseKey);
				}
				else
				{
					xgui.clickOutside(mouseKey, this);
				}
			}
			cursorMarker = null;
		}
		else if(menuOption >= 0)
		{
			onMenuClick(menuOption, mouseKey);
			cursorMarker = null;
		}
		else if(state.editMode() && editorOption >= 0)
		{
			//editor
			levelEditor.onEditorClick(editorOption, mouseKey);
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

	private void onMenuClick(int menuOption, int mouseKey)
	{
		if(mouseKey >= 0)
		{
			if(state instanceof NAutoState)
				return;
			NState newState = menu.get(menuOption);
			if(newState != state)
			{
				xgui.close(this, false);
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
			((NMarkState) state).onClick(mapTile, mainState.levelMap.getMarked().getOrDefault(mapTile, MarkType.NOT),
					mouseKey, mainState.levelMap, this);
		}
		else if(state.editMode() && mouseKey >= 0)
		{
			levelEditor.onMapClick(mapTile, mouseKey);
		}
		else if(mouseKey >= 0)
		{
			AdvTile advTile = mainState.levelMap.advTile(mapTile);
			XEntity entity = advTile.getEntity();
			if(entity != null)
			{
				if(entity instanceof XHero)
				{
					if(mouseKey == 1)
					{
						setState(new AdvMoveState((XHero) entity));
					}
					else if(mouseKey == 3)
					{
						setState(new CharacterInvState((XHero) entity));
					}
				}
				else if(entity instanceof XEnemy)
				{
					if(mouseKey == 1)
					{
						setState(new ReachViewState((XEnemy) entity));
					}
					else if(mouseKey == 3)
					{
						setState(new CharacterInvState((XEnemy) entity));
					}
				}
			}
		}
	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, int mouseKey, boolean finished)
	{
		if(!(state instanceof NGUIState))
		{
			dragMarker = mainState.y2.betweenArea(startTile, endTile).stream()
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
		else if(!(state instanceof NAutoState))
		{
			for(NState menuEntry : menu)
			{
				if(keyCode == menuEntry.keybind())
				{
					if(menuEntry != state)
					{
						xgui.close(this, false);
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
		mainState.levelMap.getVisMarked().addAll(state.visMarked(mainState));
		if(cursorMarker != null)
			mainState.levelMap.getVisMarked().add(cursorMarker);
		mainState.levelMap.getVisMarked().addAll(dragMarker);
	}
}