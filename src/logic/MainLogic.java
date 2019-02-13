package logic;

import arrow.*;
import building.*;
import building.blueprint.*;
import entity.*;
import entity.hero.*;
import file.*;
import hex.*;
import levelMap.*;
import levelMap.importX.*;

public class MainLogic
{
	private LevelMap levelMap;
	private XMenu2 menu2;

	public MainLogic()
	{
		levelMap = new LevelMap();
		menu2 = new XMenu2(levelMap);
		new TestImportSector(8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XHero(new Hex(2, 1)));
		levelMap.addEntity(new XEntity(new Hex(0, 1)));
		levelMap.addEntity(new XEntity(new Hex(-2, -1)));
		levelMap.addArrow(new VisualArrow(new Hex(2, 0), new Hex(4, 1), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-2, 0), new Hex(4, -4), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-3, 0), new Hex(-3, 0), ArrowMode.ARROW, 120));
		BlueprintCache<BuildingBlueprint> cache1 = new BlueprintCache<>("buildings");
		levelMap.addBuilding(new ProductionBuilding(new Hex(-2, -2), BuildingBlueprint.get(cache1, "BLUE1")));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-3, -3), BuildingBlueprint.get(cache1, "GSL1")));
		levelMap.addBuilding(new Transporter(new Hex(-3, -2)));
	}

	public LevelMap getLevelMap()
	{
		return levelMap;
	}

	public XMenu2 getMenu()
	{
		return menu2;
	}

	public void tick()
	{
		levelMap.tickArrows();
	}

	public void handleGUIClick(Hex h1, int mouseKey)
	{
		menu2.handleGUIClick(h1, mouseKey);
	}

	public void handleMenuClick(int option, int mouseKey)
	{
		menu2.handleMenuClick(option, mouseKey);
	}

	public void handleMapClick(Hex clicked, int mouseKey)
	{
		menu2.handleMapClick(clicked, mouseKey);
	}
}