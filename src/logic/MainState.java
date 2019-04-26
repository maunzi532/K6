package logic;

import building.blueprint.*;
import entity.*;
import file.*;
import geom.d1.*;
import levelMap.*;
import levelMap.importX.*;
import system2.*;

public class MainState
{
	//public static final int CL = 20;
	public final DoubleType y2;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public final XStateControl stateControl;
	public final CombatSystem combatSystem;

	public MainState(DoubleType y2)
	{
		this.y2 = y2;
		levelMap = new LevelMap(y2);
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		stateControl = new XStateControl(this);
		//combatSystem = new System1();
		combatSystem = new System2();
	}

	public void initialize()
	{
		new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
		/*ItemList a1 = new ItemList(AttackItem1.item1());
		levelMap.addEntity(new XHero(y2.create2(-2, 1), this, Stats1.create2(), CL, a1));
		XHero h1 = new XHero(y2.create2(-2, -1), this, Stats1.create1(), CL, a1);
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		h1.addItems(new ItemList(Items.BLUE, Items.GSL, Items.MATERIAL, Items.TECHNOLOGY));
		levelMap.addEntity(h1);
		levelMap.addEntity(new XEnemy(y2.create2(2, 1), this, Stats1.create1(), CL, a1));
		levelMap.addEntity(new XEnemy(y2.create2(2, 0), this, Stats1.create2(), CL, a1));
		levelMap.addEntity(new XEnemy(y2.create2(2, -1), this, Stats1.create1(), CL, a1));*/
		new Entity2Builder(this).setLocation(y2.create2(-2, 1)).addItem(AttackItem2.item2()).setStats(Stats2.create2()).create(true);
		new Entity2Builder(this).setLocation(y2.create2(-2, -1)).addItem(AttackItem2.item2()).setStats(Stats2.create1()).create(true);
		new Entity2Builder(this).setLocation(y2.create2(2, 1)).addItem(AttackItem2.item2()).setStats(Stats2.create1()).create(false);
		new Entity2Builder(this).setLocation(y2.create2(2, 0)).addItem(AttackItem2.item2()).setStats(Stats2.create2()).create(false);
		new Entity2Builder(this).setLocation(y2.create2(2, -1)).addItem(AttackItem2.item2()).setStats(Stats2.create1()).create(false);
		/*levelMap.addArrow(new ShineArrow(List.of(y2.create2(2, 0), y2.create2(4, 1)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-2, 0), y2.create2(4, -4)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-3, 0)), 120, true, null, true));*/
		/*ProductionBuilding blue1 = new ProductionBuilding(y2.create2(-2, -2), BuildingBlueprint.get(buildingBlueprintCache, "BLUE1"));
		levelMap.addBuilding(blue1);
		blue1.claimFloor(levelMap);
		ProductionBuilding gsl1 = new ProductionBuilding(y2.create2(-3, -3), BuildingBlueprint.get(buildingBlueprintCache, "GSL1"));
		levelMap.addBuilding(gsl1);
		gsl1.claimFloor(levelMap);
		Transporter transporter1 = new Transporter(y2.create2(-3, -2), BuildingBlueprint.get(buildingBlueprintCache, "Transporter1"));
		levelMap.addBuilding(transporter1);*/
	}
}