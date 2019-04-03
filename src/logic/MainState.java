package logic;

import building.*;
import building.blueprint.*;
import entity.enemy.*;
import entity.hero.*;
import file.*;
import geom.d1.*;
import geom.f1.*;
import item.*;
import levelMap.*;
import levelMap.importX.*;

public class MainState
{
	public final TileType y1;
	public final DoubleType y2;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public final XStateControl stateControl;

	public MainState()
	{
		y1 = new HexTileType();
		y2 = new HexDoubleType();
		levelMap = new LevelMap(y1, y2);
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		stateControl = new XStateControl(this);
	}

	public void initialize()
	{
		new TestImportSector(y1, 8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XHero(y1.create2(-2, 1), this));
		XHero h1 = new XHero(y1.create2(-2, -1), this);
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		levelMap.addEntity(h1);
		levelMap.addEntity(new XEnemy(y1.create2(2, 1)));
		levelMap.addEntity(new XEnemy(y1.create2(2, 0)));
		levelMap.addEntity(new XEnemy(y1.create2(2, -1)));
		/*levelMap.addArrow(new VisualArrow(y1.create2(2, 0), y1.create2(4, 1), ArrowMode.ARROW, 120, null));
		levelMap.addArrow(new VisualArrow(y1.create2(-2, 0), y1.create2(4, -4), ArrowMode.ARROW, 120, null));
		levelMap.addArrow(new VisualArrow(y1.create2(-3, 0), y1.create2(-3, 0), ArrowMode.ARROW, 120, null));*/
		ProductionBuilding blue1 = new ProductionBuilding(y1.create2(-2, -2), BuildingBlueprint.get(buildingBlueprintCache, "BLUE1"));
		levelMap.addBuilding(blue1);
		blue1.claimFloor(levelMap);
		ProductionBuilding gsl1 = new ProductionBuilding(y1.create2(-3, -3), BuildingBlueprint.get(buildingBlueprintCache, "GSL1"));
		levelMap.addBuilding(gsl1);
		gsl1.claimFloor(levelMap);
		Transporter transporter1 = new Transporter(y1.create2(-3, -2), BuildingBlueprint.get(buildingBlueprintCache, "Transporter1"));
		levelMap.addBuilding(transporter1);
	}
}