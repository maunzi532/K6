package logic;

import entity.*;
import geom.f1.*;
import gui.*;
import java.util.*;
import java.util.stream.*;
import javafx.scene.input.*;
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

	public StateControl2(MainState mainState, LevelEditor levelEditor, NState state)
	{
		this.mainState = mainState;
		this.levelEditor = levelEditor;
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
		}
		else if(menuOption >= 0)
		{
			onMenuClick(menuOption, mouseKey);
		}
		else if(state.editMode() && editorOption >= 0)
		{
			//editor
			levelEditor.onEditorClick(editorOption, mouseKey);
		}
		else
		{
			//tile
			handleMapTarget(mapTile, mouseKey);
		}
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
						//setState(new AdvMoveState((XEnemy) entity));
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

		}
	}

	@Override
	public void handleKey(KeyCode keyCode)
	{
		switch(keyCode)
		{
			/*case RIGHT -> mapCamera.xShift += 0.5;
			case LEFT -> mapCamera.xShift -= 0.5;
			case UP -> mapCamera.yShift -= 0.5;
			case DOWN -> mapCamera.yShift += 0.5;
			case UP -> mainState.stateControl.handleMenuChoose(-1);
			case DOWN -> mainState.stateControl.handleMenuChoose(1);*/
			case E ->
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
	}
}