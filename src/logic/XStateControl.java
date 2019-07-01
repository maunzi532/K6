package logic;

import building.*;
import entity.*;
import geom.f1.*;
import gui.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import levelMap.editor.*;
import logic.xstate.*;

public class XStateControl implements XStateHolder
{
	private MainState mainState;
	private NState state;
	private List<NState> menu;
	private XGUI xgui;

	public XStateControl(MainState mainState)
	{
		this.mainState = mainState;
	}

	public void start()
	{
		setState(new StartTurnState());
		update();
	}

	public NState getState()
	{
		return state;
	}

	@Override
	public void setState(NState state)
	{
		this.state = state;
		state.onEnter(mainState);
	}

	public List<NState> getMenu()
	{
		return menu;
	}

	public XGUI getXgui()
	{
		return xgui;
	}

	public void handleMenuChoose(int move)
	{
		int index = menu.indexOf(state);
		int newIndex = (index < 0 && move < 0 ? menu.size() : index) + move;
		if(menu.size() > 0)
			handleMenuClick(Math.floorMod(newIndex, menu.size()), 1);
	}

	public void toggleEditMode()
	{
		if(state instanceof NoneState)
		{
			setState(EditingState.INSTANCE);
			update();
		}
		else if(state instanceof EditingState)
		{
			setState(NoneState.INSTANCE);
			update();
		}
	}

	public void handleMenuClick(int menuOption, int mouseKey)
	{
		if(state instanceof NAutoState)
			return;
		NState newState = menu.get(menuOption);
		if(newState != state)
		{
			xgui.close(this, false);
			setState(newState);
			update();
		}
	}

	public void target(Tile offset)
	{
		xgui.target(offset.v[0], offset.v[1]);
	}

	public void handleGUIClick(Tile offset, boolean inside, int mouseKey)
	{
		if(inside)
		{
			if(xgui.click(offset.v[0], offset.v[1], mouseKey, this))
				update();
		}
		else
		{
			if(xgui.clickOutside(mouseKey, this))
				update();
		}
	}

	public void handleMapClick(Tile mapTile, int mouseKey)
	{
		if(state instanceof NAutoState)
			return;
		if(state instanceof NMarkState)
		{
			((NMarkState) state).onClick(mapTile, mainState.levelMap.getMarked().getOrDefault(mapTile, MarkType.NOT),
					mouseKey, mainState.levelMap, this);
			update();
		}
		else
		{
			AdvTile advTile = mainState.levelMap.advTile(mapTile);
			if(mouseKey == 1)
			{
				if(advTile.getEntity() != null)
				{
					setTileState(advTile.getEntity());
				}
				else if(advTile.getBuilding() != null)
				{
					setTileState(advTile.getBuilding());
				}
			}
			else if(mouseKey == 3)
			{
				if(advTile.getBuilding() != null)
				{
					setTileState(advTile.getBuilding());
				}
				else if(advTile.getEntity() != null)
				{
					setTileState(advTile.getEntity());
				}
			}
		}
	}

	private void setTileState(XEntity entity)
	{
		if(entity instanceof XHero)
		{
			setState(new AdvMoveState((XHero) entity));
		}
		else if(entity instanceof XEnemy)
		{
			setState(new CharacterInvState((InvEntity) entity));
		}
		else
		{
			return;
		}
		update();
	}

	private void setTileState(MBuilding object)
	{
		if(object instanceof Transporter)
		{
			setState(new TransportTargetsState((Transporter) object));
		}
		else if(object instanceof ProductionBuilding)
		{
			setState(new ProductionFloorsState((ProductionBuilding) object));
		}
		else
		{
			return;
		}
		update();
	}

	public void handleEditMode(LevelEditor levelEditor, double x, double y, Tile mapTile, int mouseKey)
	{
		int editorClick = levelEditor.editorClickNum(x, y);
		if(editorClick >= 0)
		{
			if(levelEditor.onEditorClick(editorClick, mouseKey))
				update();
		}
		else
		{
			if(state instanceof NMarkState)
			{
				handleMapClick(mapTile, mouseKey);
			}
			else if(levelEditor.onMapClick(mapTile, mouseKey))
				update();
		}
	}

	public void tick()
	{
		if(state instanceof NAutoState)
		{
			NAutoState state1 = (NAutoState) state;
			state1.tick(mainState);
			if(state1.finished())
			{
				setState(state1.nextState());
				update();
			}
		}
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
}