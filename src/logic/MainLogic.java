package logic;

import arrow.*;
import building.*;
import building.blueprint.*;
import entity.*;
import hex.*;
import inv.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import levelMap.importX.*;
import logic.gui.*;

public class MainLogic
{
	private LevelMap levelMap;
	private XState xState;
	private Hex xh;
	private XEntity xe;
	private Building xb;
	private FloorTile xf;
	private XMenu xMenu;
	private XGUI xgui;

	public MainLogic()
	{
		levelMap = new LevelMap();
		xMenu = new XMenu();
		xgui = new XGUI();
		xState = XState.PLAYERPHASE;
		new TestImportSector(8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XEntity(new Hex(2, 1)));
		levelMap.addEntity(new XEntity(new Hex(0, 1)));
		levelMap.addEntity(new XEntity(new Hex(-2, -1)));
		levelMap.addArrow(new VisualArrow(new Hex(2, 0), new Hex(4, 1), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-2, 0), new Hex(4, -4), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-3, 0), new Hex(-3, 0), ArrowMode.ARROW, 120));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-2, -2),
				new BuildingBlueprint("BLUE", null,
						new ProductionBlueprint(new ItemList(), new ItemList(new ItemStack(Items.BLUE, 5)),
						new Recipe(new ItemList(), new ItemList(Items.BLUE))) )));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-3, -3),
				new BuildingBlueprint("GSL", null,
						new ProductionBlueprint(new ItemList(new ItemStack(Items.BLUE, 5)),
						new ItemList(new ItemStack(Items.GSL, 5)),
						new Recipe(new ItemList(Items.BLUE), new ItemList(Items.GSL))))));
		levelMap.addBuilding(new Transporter(new Hex(-3, -2)));
	}

	public LevelMap getLevelMap()
	{
		return levelMap;
	}

	public XMenu getxMenu()
	{
		return xMenu;
	}

	public XGUI getXgui()
	{
		return xgui;
	}

	public void tick()
	{
		levelMap.tickArrows();
	}

	public void handleGUIClick(Hex h1)
	{
		OffsetHex offsetHex = new OffsetHex(h1);
		int x = offsetHex.v[0];
		int y = offsetHex.v[1];
		if(x >= 0 && x < xgui.xw() && y >= 0 && y < xgui.yw())
			xgui.click(x, y);
	}

	public void handleMenuClick(int option)
	{
		XMenuEntry menuEntry = xMenu.getEntries().get(option);
		if(menuEntry.direct)
		{
			clickDirectMenu(menuEntry);
		}
		else if(menuEntry.withGUI)
		{
			xMenu.setCurrent(menuEntry);
		}
		else
		{
			xMenu.setCurrent(menuEntry);
			updateMarked();
		}
		updateGUI();
	}

	public void handleMapClick(Hex clicked, boolean primary)
	{
		FullTile tile = levelMap.tile(clicked);
		if(primary && levelMap.getMarked().contains(clicked))
		{
			clickMarked(clicked);
		}
		else
		{
			chooseTile(clicked, tile, switch(xState)
			{
				case PLAYERPHASE, FLOOR -> 2;
				case ENTITY -> 1;
				case BUILDING, TRANSPORTER -> 0;
			});
		}
		System.out.println(xState);
		System.out.println(xMenu.getCurrent());
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
				setxState(XState.ENTITY);
			}
			else if(start >= 1 && tile.building != null)
			{
				xb = tile.building;
				if(tile.building instanceof Transporter)
					setxState(XState.TRANSPORTER);
				else
					setxState(XState.BUILDING);
			}
			else
			{
				xf = tile.floorTile;
				setxState(XState.FLOOR);
			}
			updateGUI();
		}
	}

	private void setxState(XState state)
	{
		levelMap.setMarked(Set.of());
		xState = state;
		xMenu.updateState(state);
		updateMarked();
	}

	private void clickDirectMenu(XMenuEntry menuEntry)
	{
		switch(menuEntry)
		{
			case PRODUCTION_PHASE -> levelMap.buildingPhase();
			case TRANSPORT_PHASE -> levelMap.transportPhase();
		}
	}

	private void updateMarked()
	{
		levelMap.setMarked(switch(xMenu.getCurrent())
		{
			case DUMMY -> Set.of();
			case CHARACTER_MOVEMENT -> new Pathing(xe, 4, levelMap).start().getEndpoints();
			case PRODUCTION_VIEW, TRANSPORT_VIEW -> Set.of();
			case EDIT_TARGETS -> xb.location().range(0, ((Transporter) xb).range()).stream()
					.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toSet());
			default -> throw new RuntimeException();
		});
	}

	private void clickMarked(Hex clicked)
	{
		switch(xMenu.getCurrent())
		{
			case CHARACTER_MOVEMENT ->
			{
				levelMap.moveEntity(xe, clicked);
				setxState(XState.PLAYERPHASE);
			}
			case EDIT_TARGETS -> ((Transporter) xb).toggleTarget((DoubleInv) levelMap.getBuilding(clicked));
		}
	}

	private void updateGUI()
	{
		xgui = switch(xMenu.getCurrent())
		{
			case PRODUCTION_VIEW -> new ProductionGUI((ProductionBuilding) xb, 0);
			default -> new XGUI();
		};
	}
}