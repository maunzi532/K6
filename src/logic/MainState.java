package logic;

import building.blueprint.*;
import draw.*;
import entity.*;
import file.*;
import geom.d1.*;
import levelMap.*;
import levelMap.importX.*;
import logic.xstate.*;
import system2.*;
import system2.content.*;

public class MainState
{
	//public static final int CL = 20;
	public final DoubleType y2;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public XStateHolder stateHolder;
	public final CombatSystem combatSystem;
	public final VisualSideInfo visualSideInfo;

	public MainState(DoubleType y2, VisualSideInfo visualSideInfo1)
	{
		this.y2 = y2;
		levelMap = new LevelMap(y2);
		buildingBlueprintCache = new BlueprintCache<>("buildings");
		combatSystem = new System2();
		visualSideInfo = visualSideInfo1;
	}

	public void initialize(String loadFile)
	{
		SavedImport savedImport = loadFile != null ? new SavedImport(loadFile) : new SavedImport();
		if(savedImport.hasFile())
		{
			savedImport.importIntoMap2(this);
		}
		else
		{
			//new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
			new Entity2Builder(this).setLocation(y2.create2(2, -1)).setStats(new Stats2(XClasses.hexerClass(), 0, null))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(this, y2.create2(-2, 1), y2.create2(-2, -1), y2.create2(-4, 1),
					y2.create2(-3, 1), y2.create2(-3, -1), y2.create2(-5, 1));
		}
		//System.out.println(levelMap.saveDataJSON());
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

	//TODO Build a real level

	//TODO show enemy reach (all enemies)
	//TODO get levelup essence
	//TODO get levelup
	//TODO equip weapon without attacking
	//TODO chooseable starting locations
	//TODO Save characters and level separately
	//TODO enemy reinforcements
	//TODO Fix game crashing when you lose

	//TODO screenshake on crit
	//TODO scale tiles, menubar, gui when resizing
	//TODO Make menu bar not overlap with SideInfo
	//TODO Add visMark to GiveOrTakeState, ProductionFloorsState, TransportTargetsState
	//TODO add keybind info
	//TODO improve camera
	//TODO Enemy AI types

	//LK char - move/attack
	//RK char - inv/trade
	//LK enemy - view reach
	//RK enemy - inv
	//LK building -- claimed area
	//RK building -- inv
	//LK transporter -- connected buildings
	//RK transporter --

	//enter -- end turn
	//esc -- back to NoneState
}