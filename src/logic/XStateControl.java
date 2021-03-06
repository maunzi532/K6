package logic;

import entity.*;
import geom.tile.*;
import java.util.*;
import java.util.stream.*;
import levelmap.*;
import editor.edit.*;
import editor.xstate.*;
import gui.*;
import guis.*;
import xstate.*;
import system.*;

public final class XStateControl implements MainState, XStateHolder, ConvInputConsumer
{
	private final SideInfoFrame sideInfoFrame;
	private final LevelEditor levelEditor;
	private final WorldControl worldControl;
	private LevelMap levelMap;
	private NState state;
	private List<NState> menu;
	private VisMark cursorMarker;
	private List<VisMark> dragMarker;
	private final List<VisMark> visMarked;
	private boolean showAllEnemyReach;
	private Map<Tile, Long> allEnemyReach;

	public XStateControl(SideInfoFrame sideInfoFrame, LevelEditor levelEditor, WorldControl worldControl)
	{
		this.sideInfoFrame = sideInfoFrame;
		this.levelEditor = levelEditor;
		this.worldControl = worldControl;
		dragMarker = List.of();
		visMarked = new ArrayList<>();
	}

	@Override
	public LevelMap levelMap()
	{
		return levelMap;
	}

	@Override
	public XStateHolder stateHolder()
	{
		return this;
	}

	@Override
	public SideInfoFrame side()
	{
		return sideInfoFrame;
	}

	@Override
	public WorldControl worldControl()
	{
		return worldControl;
	}

	@Override
	public WorldSettings worldSettings()
	{
		return worldControl.worldSettings();
	}

	@Override
	public void setState(NState state)
	{
		this.state = state;
		state.onEnter(this);
		menu = state.menu().getEntries().stream().filter(e -> e.keepInMenu(this)).collect(Collectors.toList());
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
	public GUIState getGUI()
	{
		if(state instanceof GUIState guiState)
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
	public boolean showAllEnemyReach()
	{
		return showAllEnemyReach;
	}

	@Override
	public void updateLevel(LevelMap newLevel)
	{
		levelMap = newLevel;
	}

	@Override
	public void mousePosition(boolean insideGUI, Tile offsetGUITile, int menuOption,
			int editorOption, Tile mapTile, boolean moved, boolean drag, XKey key)
	{
		if(state instanceof GUIState GUIState)
		{
			if(moved)
			{
				if(insideGUI)
				{
					GUIState.target(offsetGUITile.v()[0], offsetGUITile.v()[1]);
				}
			}
			if(key.canClick())
			{
				if(insideGUI)
				{
					GUIState.click(offsetGUITile.v()[0], offsetGUITile.v()[1], key, this);
				}
				else if(menuOption >= 0)
				{
					onMenuTarget(menuOption, key);
				}
				else
				{
					GUIState.clickOutside(key, this);
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
			levelEditor.onEditorTarget(editorOption, key, this, this);
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
			markState.onClick(this, mapTile, key);
		}
		else if(state.editMode() && key.canClick())
		{
			levelEditor.onMapClick(mapTile, key, this);
		}
		else if(key.canClick())
		{
			AdvTile advTile = levelMap.advTile(mapTile);
			if(advTile.entity() != null)
			{
				onClickEntity(advTile.entity(), key);
			}
		}
	}

	private void onClickEntity(XCharacter entity, XKey key)
	{
		if(entity.team() == CharacterTeam.HERO)
		{
			if(key.hasFunction("choose"))
			{
				if(levelMap.levelStarted())
				{
					if(entity.hasMainAction())
						setState(new AdvMoveState(entity));
					else
						setState(new ReachViewState(entity));
				}
				else
				{
					if(levelMap.canSwap(entity))
						setState(new SwapState(entity));
					else
						setState(new ReachViewState(entity));
				}
			}
			else if(key.hasFunction("menu"))
			{
				setState(new CharacterInfoGUI(entity));
			}
		}
		else
		{
			if(key.hasFunction("choose"))
			{
				setState(new ReachViewState(entity));
			}
			else if(key.hasFunction("menu"))
			{
				setState(new CharacterInfoGUI(entity));
			}
		}
	}

	@Override
	public void dragPosition(Tile startTile, Tile endTile, XKey key, boolean finished)
	{
		if(!(state instanceof GUIState))
		{
			dragMarker = levelMap.y1().betweenArea(startTile, endTile).stream()
					.map(e -> new VisMark(e, "mark.cursor.drag", VisMark.d3)).collect(Collectors.toList());
			if(finished && state.editMode() && key.canDrag())
			{
				levelEditor.onMapDrag(startTile, endTile, key, this);
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
		if(key.hasFunction("editor.toggle"))
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
		else if(key.hasFunction("allenemyreach"))
		{
			showAllEnemyReach = !showAllEnemyReach;
			if(showAllEnemyReach)
				levelMap.requireUpdate();
		}
		else if(key.hasFunction("escape"))
		{
			if(state instanceof GUIState guiState)
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
				if(menuEntry.keybind() != null && key.hasFunction(menuEntry.keybind()))
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
			autoState.tick(this);
			if(autoState.finished())
			{
				setState(autoState.nextState());
			}
		}
		visMarked.clear();
		if(showAllEnemyReach)
		{
			if(levelMap.checkUpdate())
			{
				allEnemyReach = levelMap.allEnemyReach();
			}
			allEnemyReach.forEach((tile, amount) -> visMarked.add(new VisMark(tile, "mark.reach.all", 0.8)));
		}
		if(state instanceof NMarkState markState)
		{
			visMarked.addAll(markState.visMarked(this));
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
		if(showAllEnemyReach)
		{
			if(levelMap.checkUpdate())
			{
				allEnemyReach = levelMap.allEnemyReach();
			}
			allEnemyReach.forEach((tile, amount) -> visMarked.add(new VisMark(tile, "mark.reach.all", 0.8)));
		}
		if(state instanceof NMarkState markState)
		{
			visMarked.addAll(markState.visMarked(this));
		}
	}
}