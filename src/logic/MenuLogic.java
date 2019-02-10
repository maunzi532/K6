package logic;

import building.*;
import entity.*;
import hex.*;
import inv.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import logic.gui.*;

public class MenuLogic
{
	private MenuTargets menuTargets;
	private LevelMap levelMap;
	private XState xState;
	private XMenu xMenu;
	private XGUI xgui;

	public MenuLogic(MenuTargets menuTargets, LevelMap levelMap)
	{
		this.menuTargets = menuTargets;
		this.levelMap = levelMap;
		xState = XState.PLAYERPHASE;
		xMenu = new XMenu();
		xgui = XGUI.NONE;
	}

	public XState getxState()
	{
		return xState;
	}

	public XMenu getxMenu()
	{
		return xMenu;
	}

	public XGUI getXgui()
	{
		return xgui;
	}

	public void setxState(XState state)
	{
		levelMap.setMarked(Set.of());
		xState = state;
		xMenu.updateState(state);
		updateMarked();
		updateGUI();
	}

	public void handleGUIClick(Hex h1)
	{
		OffsetHex offsetHex = new OffsetHex(h1);
		int x = offsetHex.v[0];
		int y = offsetHex.v[1];
		if(x >= 0 && x < xgui.xw() && y >= 0 && y < xgui.yw())
			xgui.click(x, y);
		else
			xgui.click();
	}

	public void handleMenuClick(int option)
	{
		XMenuEntry menuEntry = xMenu.getEntries().get(option);
		if(menuEntry.direct)
		{
			clickDirectMenu(menuEntry);
		}
		else
		{
			xMenu.setCurrent(menuEntry);
		}
		updateMarked();
		updateGUI();
	}

	public void clickDirectMenu(XMenuEntry menuEntry)
	{
		switch(menuEntry)
		{
			case PRODUCTION_PHASE -> levelMap.buildingPhase();
			case TRANSPORT_PHASE -> levelMap.transportPhase();
		}
	}

	public void updateMarked()
	{
		levelMap.setMarked(switch(xMenu.getCurrent())
		{
			case CHARACTER_MOVEMENT -> new Pathing(menuTargets.getEntity(), 4, levelMap).start().getEndpoints();
			case EDIT_TARGETS -> menuTargets.getBuilding().location().range(0, ((Transporter) menuTargets.getBuilding()).range()).stream()
					.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toSet());
			case TAKE -> menuTargets.getEntity().location.range(0, 4).stream()
					.filter(e -> levelMap.getBuilding(e) instanceof DoubleInv).collect(Collectors.toSet());
			default -> Set.of();
		});
	}

	public void clickMarked(Hex clicked)
	{
		switch(xMenu.getCurrent())
		{
			case CHARACTER_MOVEMENT ->
			{
				levelMap.moveEntity(menuTargets.getEntity(), clicked);
				setxState(XState.PLAYERPHASE);
			}
			case EDIT_TARGETS -> ((Transporter) menuTargets.getBuilding()).toggleTarget((DoubleInv) levelMap.getBuilding(clicked));
			case TAKE ->
			{
				xMenu.setCurrent(XMenuEntry.DIRECTED_TRADE);
				updateMarked();
				updateGUI();
			}
		}
	}

	public void updateGUI()
	{
		xgui = switch(xMenu.getCurrent())
		{
			case PRODUCTION_VIEW -> new ProductionGUI((ProductionBuilding) menuTargets.getBuilding(), 0);
			default -> XGUI.NONE;
		};
	}
}