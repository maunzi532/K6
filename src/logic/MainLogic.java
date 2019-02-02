package logic;

import arrow.*;
import building.*;
import building.blueprint.*;
import entity.*;
import hex.*;
import inv.*;
import java.util.*;
import levelMap.*;
import levelMap.importX.*;

public class MainLogic
{
	private LevelMap levelMap;
	private XState xState;
	private Hex xh;
	private XEntity xe;
	private Building xb;
	private FloorTile xf;
	private XMenu xMenu;

	public MainLogic()
	{
		levelMap = new LevelMap();
		xMenu = new XMenu();
		xState = XState.PLAYERPHASE;
		new TestImportSector(8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XEntity(new Hex(2, 1)));
		levelMap.addEntity(new XEntity(new Hex(0, 1)));
		levelMap.addEntity(new XEntity(new Hex(-2, -1)));
		levelMap.addArrow(new VisualArrow(new Hex(2, 0), new Hex(4, 1), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-2, 0), new Hex(4, -4), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-3, 0), new Hex(-3, 0), ArrowMode.ARROW, 120));
		levelMap.addBuilding(new Hex(-2, -2), new ProductionBuilding(
				new ProductionBlueprint(new ItemList(), new ItemList(new ItemStack(Items.BLUE, 5)),
						new Recipe(new ItemList(), new ItemList(Items.BLUE)))));
		levelMap.addBuilding(new Hex(-3, -2), new Transporter());
	}

	public LevelMap getLevelMap()
	{
		return levelMap;
	}

	public XMenu getxMenu()
	{
		return xMenu;
	}

	public void handleMenuClick(int option)
	{
		handleMenuClick(xMenu.getEntries().get(option));
	}

	private void setxState(XState state)
	{
		levelMap.setMarked(Set.of());
		xState = state;
		xMenu.updateState(state);
		updateForMenuState();
	}

	public void tick()
	{
		levelMap.tickArrows();
	}

	private void chooseTile(Hex hex, FullTile tile, int start)
	{
		if(hex.equals(xh))
		{
			if(start >= 2 && tile.entity != null)
			{
				xe = tile.entity;
				setxState(XState.ENTITY);
			}
			else if(start >= 1 && tile.building != null)
			{
				xb = tile.building;
				setxState(XState.BUILDING);
			}
			else
			{
				xf = tile.floorTile;
				setxState(XState.FLOOR);
			}
		}
		else if(tile.exists())
		{
			xh = hex;
			if(tile.entity != null)
			{
				xe = tile.entity;
				setxState(XState.ENTITY);
			}
			else if(tile.building != null)
			{
				xb = tile.building;
				setxState(XState.BUILDING);
			}
			else
			{
				xf = tile.floorTile;
				setxState(XState.FLOOR);
			}
		}
	}

	public void handleMapClick(Hex clicked, boolean primary)
	{
		FullTile tile = levelMap.tile(clicked);
		if(primary && levelMap.getMarked().contains(clicked))
		{
			switch(xMenu.getCurrent())
			{
				case CHARACTER_MOVEMENT ->
						{
							levelMap.moveEntity(xe, clicked);
							setxState(XState.PLAYERPHASE);
						}
				case BUILDING_VIEW -> {}
			}
		}
		else
		{
			chooseTile(clicked, tile, switch(xState)
					{
						case PLAYERPHASE, FLOOR -> 2;
						case ENTITY -> 1;
						case BUILDING -> 0;
					});
		}
		System.out.println(xState);
		System.out.println(xMenu.getCurrent());
	}

	public void handleMenuClick(XMenuEntry menuEntry)
	{
		if(menuEntry.direct)
		{
			switch(menuEntry)
			{
				case PRODUCTION_PHASE -> levelMap.buildingPhase();
				case TRANSPORT_PHASE -> levelMap.transportPhase();
			}
		}
		else
		{
			xMenu.setCurrent(menuEntry);
			updateForMenuState();
		}
	}

	private void updateForMenuState()
	{
		levelMap.setMarked(Set.of());
		switch(xMenu.getCurrent())
		{
			case CHARACTER_MOVEMENT -> new Pathing(xe, 4, levelMap).start().copyIntoMap();
		}
	}
}