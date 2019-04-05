package logic;

import building.*;
import building.blueprint.*;
import entity.*;
import entity.enemy.*;
import entity.hero.*;
import file.*;
import geom.d1.*;
import item.*;
import levelMap.*;
import levelMap.importX.*;

public class MainState
{
	public final DoubleType y2;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public final XStateControl stateControl;
	public final Wugu1 wugu1;

	public MainState(DoubleType y2)
	{
		this.y2 = y2;
		levelMap = new LevelMap(y2, y2);
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		stateControl = new XStateControl(this);
		wugu1 = new Wugu2();
	}

	public void initialize()
	{
		new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
		levelMap.addEntity(new XHero(y2.create2(-2, 1), this, new Stats2()));
		XHero h1 = new XHero(y2.create2(-2, -1), this, new Stats2());
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		levelMap.addEntity(h1);
		levelMap.addEntity(new XEnemy(y2.create2(2, 1), this, new Stats2()));
		levelMap.addEntity(new XEnemy(y2.create2(2, 0), this, new Stats2()));
		levelMap.addEntity(new XEnemy(y2.create2(2, -1), this, new Stats2()));
		/*levelMap.addArrow(new VisualArrow(y2.create2(2, 0), y1.create2(4, 1), ArrowMode.ARROW, 120, null));
		levelMap.addArrow(new VisualArrow(y2.create2(-2, 0), y1.create2(4, -4), ArrowMode.ARROW, 120, null));
		levelMap.addArrow(new VisualArrow(y2.create2(-3, 0), y1.create2(-3, 0), ArrowMode.ARROW, 120, null));*/
		ProductionBuilding blue1 = new ProductionBuilding(y2.create2(-2, -2), BuildingBlueprint.get(buildingBlueprintCache, "BLUE1"));
		levelMap.addBuilding(blue1);
		blue1.claimFloor(levelMap);
		ProductionBuilding gsl1 = new ProductionBuilding(y2.create2(-3, -3), BuildingBlueprint.get(buildingBlueprintCache, "GSL1"));
		levelMap.addBuilding(gsl1);
		gsl1.claimFloor(levelMap);
		Transporter transporter1 = new Transporter(y2.create2(-3, -2), BuildingBlueprint.get(buildingBlueprintCache, "Transporter1"));
		levelMap.addBuilding(transporter1);
	}
}