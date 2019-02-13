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

public class XMenu2
{
	public XState2 state = XState2.NONE;
	private Object[] stateInfo = new Object[3];
	public List<XState2> menu = List.of();
	private LevelMap levelMap;
	public XGUI xgui = XGUI.NONE;

	public XMenu2(LevelMap levelMap)
	{
		this.levelMap = levelMap;
	}

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
			if(key == 0)
			{
				if(tile.entity != null)
				{
					clearAddAndSetState(tile.entity);
				}
				else if(tile.building != null)
				{
					clearAddAndSetState(tile.building);
				}
			}
			else if(key == 1)
			{
				if(tile.building != null)
				{
					clearAddAndSetState(tile.building);
				}
				else if(tile.entity != null)
				{
					clearAddAndSetState(tile.entity);
				}
			}
		}
	}

	private void clearAddAndSetState(Object object)
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
		stateInfo[0] = object;
		update();
	}

	private void onMenuClick(XState2 clicked, int key)
	{

	}

	private void onClickMarked(Hex mapHex, int key)
	{
		switch(state)
		{
			case CHARACTER_MOVEMENT ->
					{
						levelMap.moveEntity((XEntity) stateInfo[0], mapHex);
						state = XState2.NONE;
					}
			case TRANSPORT_TARGETS -> ((Transporter) stateInfo[0]).toggleTarget((DoubleInv) levelMap.getBuilding(mapHex));
			case TAKE_TARGET ->
					{
						stateInfo[1] = levelMap.getBuilding(mapHex);
						stateInfo[2] = stateInfo[0];
						state = XState2.DIRECTED_TRADE;
					}
			case GIVE_TARGET ->
					{
						stateInfo[1] = stateInfo[0];
						stateInfo[2] = levelMap.getBuilding(mapHex);
						state = XState2.DIRECTED_TRADE;
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
							List.of(XState2.CHARACTER_MOVEMENT, XState2.GIVE_TARGET, XState2.TAKE_TARGET);
					case TRANSPORT_VIEW, TRANSPORT_TARGETS -> List.of(XState2.TRANSPORT_VIEW, XState2.TRANSPORT_TARGETS);
					case PRODUCTION_VIEW -> List.of(XState2.PRODUCTION_VIEW);
					default -> List.of();
				};
		xgui = switch(state)
				{
					case PRODUCTION_VIEW -> new ProductionGUI((ProductionBuilding) stateInfo[0], 0);
					default -> XGUI.NONE;
				};
	}
}