package logic;

import building.*;
import entity.*;
import entity.hero.*;
import geom.f1.*;
import gui.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.xstate.*;

public class XStateControl
{
	private MainState mainState;
	private NState state;
	private List<NState> menu;
	private XGUI xgui;

	public XStateControl(MainState mainState)
	{
		this.mainState = mainState;
		state = NoneState.INSTANCE;
		update();
	}

	public NState getState()
	{
		return state;
	}

	public void setState(NState state)
	{
		this.state = state;
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

	public void handleMenuClick(int menuOption, int key)
	{
		NState newState = menu.get(menuOption);
		if(newState instanceof NClickState)
		{
			xgui.close(this);
			((NClickState) newState).onMenuClick(key, mainState);
			update();
		}
		else if(newState != state)
		{
			xgui.close(this);
			state = newState;
			update();
		}
	}

	public void target(Tile offset)
	{
		xgui.target(offset.v[0], offset.v[1]);
	}

	public void handleGUIClick(Tile offset, boolean inside, int key)
	{
		if(inside)
		{
			if(xgui.click(offset.v[0], offset.v[1], key, this))
				update();
		}
		else
		{
			if(xgui.clickOutside(key, this))
				update();
		}
	}

	public void handleMapClick(Tile mapTile, int key)
	{
		if(state instanceof NMarkState)
		{
			((NMarkState) state).onClick(mapTile, mainState.levelMap.getMarked().getOrDefault(mapTile, MarkType.NOT),
					key, mainState.levelMap, this);
			update();
		}
		else
		{
			AdvTile advTile = mainState.levelMap.advTile(mapTile);
			if(key == 1)
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
			else if(key == 3)
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

	private void setTileState(Object object)
	{
		if(object instanceof XEntity)
		{
			if(object instanceof XHero)
			{
				state = new CharacterMovementState((XHero) object);
			}
			else
			{
				return;
			}
		}
		else if(object instanceof MBuilding)
		{
			if(object instanceof Transporter)
			{
				state = new TransportTargetsState((Transporter) object);
			}
			else if(object instanceof ProductionBuilding)
			{
				state = new ProductionFloorsState((ProductionBuilding) object);
			}
			else
			{
				return;
			}
		}
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
}