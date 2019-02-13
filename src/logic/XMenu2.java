package logic;

import building.*;
import entity.XEntity;
import entity.hero.XHero;
import hex.*;
import java.util.*;
import levelMap.*;
import logic.gui.XGUI;

public class XMenu2
{
	public XState2 state = XState2.NONE;
	public List<Object> stateInfo = new ArrayList<>();
	public List<XState2> menu = List.of();
	private LevelMap levelMap;
	private XGUI xgui;

	public void handleMenuClick(int menuOption, int key)
	{
		XState2 newState = menu.get(menuOption);
		onMenuClick(newState, key);
		if(newState.set)
		{
			state = newState;
		}
		update();
	}

	public void handleGUIClick(Hex guiHex, int key)
	{
		OffsetHex coordinates = new OffsetHex(guiHex);
		xgui.click(coordinates.v[0], coordinates.v[1]);
		update();
	}

	public void handleMapClick(Hex mapHex, int key)
	{
		if(state.mark)
		{
			if(levelMap.getMarked().contains(mapHex))
			{
				onClickMarked(mapHex, key);
				update();
			}
		}
		else
		{
			FullTile tile = levelMap.tile(mapHex);
			if(key == 0)
			{
				if(tile.entity != null)
				{
					addAndSetState(tile.entity);
				}
				else if(tile.building != null)
				{
					addAndSetState(tile.building);
				}
			}
			else if(key == 1)
			{
				if(tile.building != null)
				{
					addAndSetState(tile.building);
				}
				else if(tile.entity != null)
				{
					addAndSetState(tile.entity);
				}
			}
		}
	}

	private void addAndSetState(Object object)
	{
		if(object instanceof XEntity)
		{
			if(object instanceof XHero)
			{
				state = XState2.CHARACTER_MOVEMENT;
			}
			else
			{
				state = XState2.CHARACTER_MOVEMENT;
			}
		}
		else if(object instanceof Building)
		{
			if(object instanceof Transporter)
			{
				state = XState2.TRANSPORT_VIEW;
			}
			else if(object instanceof ProductionBuilding)
			{
				state = XState2.PRODUCTION_VIEW;
			}
			else
			{
				return;
			}
		}
		stateInfo.add(object);
		update();
	}

	private void onMenuClick(XState2 clicked, int key)
	{

	}

	private void onClickMarked(Hex mapHex, int key)
	{

	}

	private void update()
	{
		levelMap.setMarked(switch(state)
				{
					default -> Set.of();
				});
		menu = switch(state)
				{
					default -> List.of();
				};
		xgui = switch(state)
				{
					default -> XGUI.NONE;
				};
	}
}