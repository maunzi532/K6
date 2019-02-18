package logic;

import arrow.*;
import building.*;
import building.blueprint.BuildingBlueprint;
import entity.XEntity;
import entity.hero.XHero;
import file.BlueprintCache;
import geom.hex.Hex;
import item.*;
import levelMap.LevelMap;
import levelMap.importX.TestImportSector;

public class MainState
{
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public final XStateControl stateControl;

	public MainState()
	{
		levelMap = new LevelMap();
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		stateControl = new XStateControl(this);
	}

	public void initialize()
	{
		new TestImportSector(8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XHero(new Hex(2, 1), this));
		levelMap.addEntity(new XEntity(new Hex(0, 1)));
		XHero h1 = new XHero(new Hex(-2, -1), this);
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		levelMap.addEntity(h1);
		levelMap.addArrow(new VisualArrow(new Hex(2, 0), new Hex(4, 1), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-2, 0), new Hex(4, -4), ArrowMode.ARROW, 120));
		levelMap.addArrow(new VisualArrow(new Hex(-3, 0), new Hex(-3, 0), ArrowMode.ARROW, 120));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-2, -2), BuildingBlueprint.get(buildingBlueprintCache, "BLUE1")));
		levelMap.addBuilding(new ProductionBuilding(new Hex(-3, -3), BuildingBlueprint.get(buildingBlueprintCache, "GSL1")));
		levelMap.addBuilding(new Transporter(new Hex(-3, -2), BuildingBlueprint.get(buildingBlueprintCache, "Transporter1")));
	}
}