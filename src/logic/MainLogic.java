package logic;

import arrow.*;
import building.*;
import building.blueprint.*;
import entity.*;
import hex.*;
import levelMap.*;
import levelMap.importX.*;

public class MainLogic implements MenuTargets
{
	private LevelMap levelMap;
	private Hex xh;
	private XEntity xe;
	private Building xb;
	private FloorTile xf;
	private MenuLogic menuLogic;

	public MainLogic()
	{
		levelMap = new LevelMap();
		menuLogic = new MenuLogic(this, levelMap);
		new TestImportSector(8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XEntity(new Hex(2, 1)));
		levelMap.addEntity(new XEntity(new Hex(0, 1)));
		levelMap.addEntity(new XEntity(new Hex(-2, -1)));
		levelMap.addArrow(new VisualArrow(new Hex(2, 0), new Hex(4, 1), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-2, 0), new Hex(4, -4), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-3, 0), new Hex(-3, 0), ArrowMode.ARROW, 120));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-2, -2), new BuildingBlueprint("BLUE1")));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-3, -3), new BuildingBlueprint("GSL1")));
		levelMap.addBuilding(new Transporter(new Hex(-3, -2)));
	}

	public LevelMap getLevelMap()
	{
		return levelMap;
	}

	public MenuLogic getMenuLogic()
	{
		return menuLogic;
	}

	@Override
	public XEntity getEntity()
	{
		return xe;
	}

	@Override
	public Building getBuilding()
	{
		return xb;
	}

	@Override
	public FloorTile getFloorTile()
	{
		return xf;
	}

	public void tick()
	{
		levelMap.tickArrows();
	}

	public void handleGUIClick(Hex h1)
	{
		menuLogic.handleGUIClick(h1);
	}

	public void handleMenuClick(int option)
	{
		menuLogic.handleMenuClick(option);
	}

	public void handleMapClick(Hex clicked, boolean primary)
	{
		FullTile tile = levelMap.tile(clicked);
		if(primary && levelMap.getMarked().contains(clicked))
		{
			menuLogic.clickMarked(clicked);
		}
		else
		{
			chooseTile(clicked, tile, menuLogic.getxState().nextTarget);
		}
	}

	private void chooseTile(Hex hex, FullTile tile, int start)
	{
		if(!hex.equals(xh))
			start = 2;
		if(tile.exists())
		{
			xh = hex;
			if(start >= 2 && tile.entity != null)
			{
				xe = tile.entity;
				menuLogic.setxState(XState.ENTITY);
			}
			else if(start >= 1 && tile.building != null)
			{
				xb = tile.building;
				if(tile.building instanceof Transporter)
					menuLogic.setxState(XState.TRANSPORTER);
				else
					menuLogic.setxState(XState.BUILDING);
			}
			else
			{
				xf = tile.floorTile;
				menuLogic.setxState(XState.FLOOR);
			}
			menuLogic.updateGUI();
		}
	}
}