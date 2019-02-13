package logic;

import building.*;
import entity.*;
import entity.hero.XHero;
import hex.*;
import inv.DoubleInv;
import java.util.*;
import java.util.stream.Collectors;
import levelMap.*;
import logic.gui.*;

public class XStateControl
{
	private XState state = XState.NONE;
	private Object[] stateInfo = new Object[3];
	private List<XState> menu = List.of();
	private LevelMap levelMap;
	private XGUI xgui = XGUI.NONE;

	public XStateControl(LevelMap levelMap)
	{
		this.levelMap = levelMap;
	}

	public XState getState()
	{
		return state;
	}

	public List<XState> getMenu()
	{
		return menu;
	}

	public XGUI getXgui()
	{
		return xgui;
	}

	public void handleMenuClick(int menuOption, int key)
	{
		XState newState = menu.get(menuOption);
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
		xgui.click(coordinates.v[0], coordinates.v[1], key);
		update();
	}

	public void handleMapClick(Hex mapHex, int key)
	{
		if(state.mark)
		{
			if(levelMap.getMarked().contains(mapHex))
			{
				onClickMarked(mapHex, key);
			}
			else
			{
				onClickUnmarked(mapHex, key);
			}
			update();
		}
		else
		{
			FullTile tile = levelMap.tile(mapHex);
			if(key == 1)
			{
				if(tile.entity != null)
				{
					setTileState(tile.entity);
				}
				else if(tile.building != null)
				{
					setTileState(tile.building);
				}
			}
			else if(key == 3)
			{
				if(tile.building != null)
				{
					setTileState(tile.building);
				}
				else if(tile.entity != null)
				{
					setTileState(tile.entity);
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
				state = XState.CHARACTER_MOVEMENT;
			}
			else
			{
				state = XState.CHARACTER_MOVEMENT;
			}
		}
		else if(object instanceof Building)
		{
			if(object instanceof Transporter)
			{
				state = XState.TRANSPORT_VIEW;
			}
			else if(object instanceof ProductionBuilding)
			{
				state = XState.PRODUCTION_VIEW;
			}
			else
			{
				return;
			}
		}
		stateInfo[0] = object;
		update();
	}

	private void onMenuClick(XState clicked, int key)
	{

	}

	private void onClickMarked(Hex mapHex, int key)
	{
		switch(state)
		{
			case CHARACTER_MOVEMENT ->
					{
						levelMap.moveEntity((XEntity) stateInfo[0], mapHex);
						state = XState.NONE;
					}
			case TRANSPORT_TARGETS -> ((Transporter) stateInfo[0]).toggleTarget((DoubleInv) levelMap.getBuilding(mapHex));
			case TAKE_TARGET ->
					{
						stateInfo[1] = levelMap.getBuilding(mapHex);
						stateInfo[2] = stateInfo[0];
						state = XState.DIRECTED_TRADE;
					}
			case GIVE_TARGET ->
					{
						stateInfo[1] = stateInfo[0];
						stateInfo[2] = levelMap.getBuilding(mapHex);
						state = XState.DIRECTED_TRADE;
					}
		}
	}

	private void onClickUnmarked(Hex mapHex, int key)
	{

	}

	private void update()
	{
		levelMap.setMarked(switch(state)
				{
					case CHARACTER_MOVEMENT -> new Pathing((XEntity) stateInfo[0], 4, levelMap).start().getEndpoints();
					case TRANSPORT_TARGETS -> ((Transporter) stateInfo[0]).location().range(0, ((Transporter) stateInfo[0]).range()).stream()
							.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toSet());
					case TAKE_TARGET, GIVE_TARGET -> ((XEntity) stateInfo[0]).location.range(0, 4).stream()
							.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toSet());
					default -> Set.of();
				});
		menu = switch(state)
				{
					case CHARACTER_MOVEMENT, GIVE_TARGET, TAKE_TARGET, DIRECTED_TRADE ->
							List.of(XState.CHARACTER_MOVEMENT, XState.GIVE_TARGET, XState.TAKE_TARGET);
					case TRANSPORT_VIEW, TRANSPORT_TARGETS -> List.of(XState.TRANSPORT_VIEW, XState.TRANSPORT_TARGETS);
					case PRODUCTION_VIEW -> List.of(XState.PRODUCTION_VIEW);
					default -> List.of();
				};
		xgui = switch(state)
				{
					case PRODUCTION_VIEW -> new ProductionGUI((ProductionBuilding) stateInfo[0], 0);
					default -> XGUI.NONE;
				};
	}
}