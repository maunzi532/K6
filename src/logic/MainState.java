package logic;

import building.blueprint.*;
import draw.*;
import entity.*;
import file.*;
import geom.d1.*;
import levelMap.*;
import levelMap.importX.*;
import system2.*;
import system2.content.*;

public class MainState
{
	//public static final int CL = 20;
	public final DoubleType y2;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public final XStateControl stateControl;
	public final CombatSystem combatSystem;
	public final VisualSideInfo visualSideInfo;

	public MainState(DoubleType y2, VisualSideInfo visualSideInfo1)
	{
		this.y2 = y2;
		levelMap = new LevelMap(y2);
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		stateControl = new XStateControl(this);
		//combatSystem = new System1();
		combatSystem = new System2();
		visualSideInfo = visualSideInfo1;
	}

	public void initialize()
	{
		SavedImport savedImport = new SavedImport();
		if(savedImport.hasFile())
		{
			savedImport.importIntoMap(this);
		}
		else
		{
			//new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
			/*new Entity2Builder(this).setLocation(y2.create2(-2, 1)).setStats(new Stats2(XClasses.mageClass(), 0))
					.addItem(AttackItems2.standardDagger()).create(true);
			new Entity2Builder(this).setLocation(y2.create2(-2, -1)).setStats(new Stats2(XClasses.banditClass(), 0))
					.addItem(AttackItems2.standardAxe()).create(true);
			new Entity2Builder(this).setLocation(y2.create2(2, 1)).setStats(new Stats2(XClasses.soldierClass(), 0))
					.addItem(AttackItems2.standardSpear()).create(false);
			new Entity2Builder(this).setLocation(y2.create2(2, 0)).setStats(new Stats2(XClasses.pirateClass(), 0))
					.addItem(AttackItems2.standardCrossbow()).create(false);*/
			new Entity2Builder(this).setLocation(y2.create2(2, -1)).setStats(new Stats2(XClasses.hexerClass(), 0))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(this, y2.create2(-2, 1), y2.create2(-2, -1), y2.create2(-4, 1)/*,
					y2.create2(-3, 1), y2.create2(-3, -1), y2.create2(-5, 1)*/);
		}
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

	//TODO Enemy AI types
	//TODO Better floor textures (Hex height same as Quad height)
	//TODO Improve floor placing tool
	//TODO Change menu bar to not overlap with SideInfo
	//TODO Build a real level
}