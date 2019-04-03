package logic;

import building.*;
import building.blueprint.*;
import entity.*;
import entity.hero.*;
import geom.f1.*;
import gui.*;
import gui.guis.*;
import item.inv.transport.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;

public class XStateControl
{
	private MainState mainState;
	private XState state;
	private List<XState> menu;
	private XGUI xgui;
	public final Object[] stateInfo = new Object[6];

	public XStateControl(MainState mainState)
	{
		this.mainState = mainState;
		state = XState.NONE;
		update();
	}

	public XState getState()
	{
		return state;
	}

	public void setState(XState state)
	{
		this.state = state;
	}

	public List<XState> getMenu()
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
		XState newState = menu.get(menuOption);
		if(newState == state && state.set)
			return;
		xgui.close(this);
		onMenuClick(newState, key);
		if(newState.set)
		{
			state = newState;
		}
		update();
	}

	public void target(Tile t1)
	{
		xgui.target(t1.v[0], t1.v[1]);
	}

	public void handleGUIClick(Tile t1, boolean inside, int key)
	{
		if(inside)
		{
			if(xgui.click(t1.v[0], t1.v[1], key, this))
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
		if(state.mark)
		{
			if(mainState.levelMap.getMarked().containsKey(mapTile))
			{
				onClickMarked(mapTile, mainState.levelMap.getMarked().get(mapTile), key);
			}
			else
			{
				onClickUnmarked(mapTile, key);
			}
			update();
		}
		else
		{
			FullTile tile = mainState.levelMap.tile(mapTile);
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
				return;
			}
		}
		else if(object instanceof MBuilding)
		{
			if(object instanceof Transporter)
			{
				state = XState.TRANSPORT_TARGETS;
			}
			else if(object instanceof ProductionBuilding)
			{
				state = XState.PRODUCTION_FLOORS;
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
		switch(clicked)
		{
			case PRODUCTION_PHASE -> mainState.levelMap.productionPhase();
			case TRANSPORT_PHASE -> mainState.levelMap.transportPhase();
		}
	}

	private void onClickMarked(Tile mapTile, MarkType markType, int key)
	{
		switch(state)
		{
			case CHARACTER_MOVEMENT ->
					{
						mainState.levelMap.moveEntity((XEntity) stateInfo[0], mapTile);
						state = XState.NONE;
					}
			case PRODUCTION_FLOORS -> ((ProductionBuilding) stateInfo[0]).toggleTarget(mapTile, mainState.levelMap);
			case TRANSPORT_TARGETS -> ((Transporter) stateInfo[0]).toggleTarget((DoubleInv) mainState.levelMap.getBuilding(mapTile));
			case ATTACK ->
					{
						stateInfo[4] = stateInfo[0];
						stateInfo[5] = mainState.levelMap.getEntity(mapTile);
						state = XState.ATTACK_INFO;
					}
			case TAKE_TARGET ->
					{
						stateInfo[1] = mainState.levelMap.getBuilding(mapTile);
						stateInfo[2] = stateInfo[0];
						state = XState.DIRECTED_TRADE;
					}
			case GIVE_TARGET ->
					{
						stateInfo[1] = stateInfo[0];
						stateInfo[2] = mainState.levelMap.getBuilding(mapTile);
						state = XState.DIRECTED_TRADE;
					}
		}
	}

	private void onClickUnmarked(Tile mapTile, int key)
	{
		state = XState.NONE;
	}

	private void update()
	{
		mainState.levelMap.setMarked(switch(state)
				{
					case CHARACTER_MOVEMENT -> new Pathing(mainState.levelMap.y1, (XEntity) stateInfo[0], 4, mainState.levelMap).start().getEndpoints();
					case PRODUCTION_FLOORS -> ((ProductionBuilding) stateInfo[0]).floors(mainState.levelMap);
					case TRANSPORT_TARGETS -> ((Transporter) stateInfo[0]).targets(mainState.levelMap);
					//case ATTACK -> Map.of();
					case TAKE_TARGET, GIVE_TARGET -> mainState.levelMap.y1.range(((XEntity) stateInfo[0]).location(), 0, 4).stream()
							.filter(e -> mainState.levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toMap(e -> e, e -> MarkType.TARGET));
					default -> Map.of();
				});
		menu = switch(state)
				{
					case CHARACTER_MOVEMENT, VIEW_INV, ATTACK, GIVE_TARGET, TAKE_TARGET, DIRECTED_TRADE, BUILDINGS, BUILD, REMOVE ->
							List.of(XState.CHARACTER_MOVEMENT, XState.VIEW_INV, XState.ATTACK,
									XState.GIVE_TARGET, XState.TAKE_TARGET, XState.BUILDINGS, XState.REMOVE);
					case ATTACK_INFO -> List.of(XState.CHARACTER_MOVEMENT, XState.VIEW_INV, XState.ATTACK_INFO,
							XState.GIVE_TARGET, XState.TAKE_TARGET, XState.BUILDINGS, XState.REMOVE);
					case PRODUCTION_FLOORS, PRODUCTION_VIEW, PRODUCTION_INV -> List.of(XState.PRODUCTION_FLOORS, XState.PRODUCTION_VIEW,
							XState.PRODUCTION_INV, XState.PRODUCTION_PHASE, XState.TRANSPORT_PHASE);
					case TRANSPORT_TARGETS -> List.of(XState.TRANSPORT_TARGETS, XState.PRODUCTION_PHASE, XState.TRANSPORT_PHASE);
					default -> List.of();
				};
		menu = menu.stream().filter(e -> switch(e)
				{
					//case ATTACK -> true;
					case BUILDINGS -> mainState.levelMap.getBuilding(((XHero) stateInfo[0]).location()) == null;
					case REMOVE -> mainState.levelMap.getBuilding(((XHero) stateInfo[0]).location()) instanceof Buildable;
					default -> true;
				}).collect(Collectors.toList());
		xgui = switch(state)
				{
					case VIEW_INV -> new Inv1GUI(((XHero) stateInfo[0]).outputInv());
					case PRODUCTION_VIEW -> new RecipeGUI(((ProductionBuilding) stateInfo[0]));
					case PRODUCTION_INV -> new Inv2GUI((ProductionBuilding) stateInfo[0]);
					//case ATTACK_INFO ->
					case DIRECTED_TRADE -> new DirectedTradeGUI((DoubleInv) stateInfo[1], (DoubleInv) stateInfo[2]);
					case BUILDINGS -> new BuildingsGUI(mainState.buildingBlueprintCache);
					case BUILD -> new BuildGUI((XHero) stateInfo[0], (BuildingBlueprint) stateInfo[3]);
					case REMOVE -> new RemoveGUI((XHero) stateInfo[0], (Buildable) mainState.levelMap.getBuilding(((XHero) stateInfo[0]).location()));
					default -> NoGUI.NONE;
				};
	}
}