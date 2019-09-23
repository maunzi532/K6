package logic;

import building.blueprint.*;
import entity.*;
import file.*;
import geom.f1.*;
import item.*;
import levelMap.*;
import levelMap.importX.*;
import logic.sideinfo.*;
import logic.xstate.*;
import system2.*;
import system2.content.*;

public class MainState
{
	//public static final int CL = 20;
	public final TileType y1;
	public final ItemLoader itemLoader;
	public int turnCounter;
	public final LevelMap levelMap;
	public final BlueprintCache<BuildingBlueprint> buildingBlueprintCache;
	public XStateHolder stateHolder;
	public final CombatSystem combatSystem;
	public final SideInfoFrame sideInfoFrame;

	public MainState(TileType y1, SideInfoFrame sideInfoFrame)
	{
		this.y1 = y1;
		itemLoader = new ItemLoader2();
		turnCounter = -1;
		levelMap = new LevelMap(y1);
		buildingBlueprintCache = new BlueprintCache<>("BuildingBlueprints", e -> new BuildingBlueprint(e, itemLoader));
		combatSystem = new System2();
		this.sideInfoFrame = sideInfoFrame;
	}

	public void initialize(String loadFile, String loadFile2)
	{
		SavedImport savedImport = loadFile != null ? new SavedImport(loadFile, loadFile2) : new SavedImport();
		if(savedImport.hasFile())
		{
			savedImport.importIntoMap3(this);
		}
		else
		{
			//new TestImportSector(y2, 8).generate().importIntoMap(levelMap);
			new Entity2Builder(this).setLocation(y1.create2(2, -1)).setStats(new Stats2(XClasses.hexerClass(), 0, null))
					.addItem(AttackItems2.standardSpell()).create(false);
			Chapter1.createCharacters(this, y1.create2(-2, 1), y1.create2(-2, -1), y1.create2(-4, 1),
					y1.create2(-3, 1), y1.create2(-3, -1), y1.create2(-5, 1));
		}
		/*levelMap.addArrow(new ShineArrow(List.of(y2.create2(2, 0), y2.create2(4, 1)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-2, 0), y2.create2(4, -4)), 120, true, null, true));
		levelMap.addArrow(new ShineArrow(List.of(y2.create2(-3, 0)), 120, true, null, true));*/
		/*levelMap.addBuilding2(new ProductionBuilding(y2.create2(-2, -2), buildingBlueprintCache.get("BLUE1")));
		levelMap.addBuilding2(new ProductionBuilding(y2.create2(-3, -3), buildingBlueprintCache.get("GSL1")));
		levelMap.addBuilding(new Transporter(y2.create2(-3, -2), buildingBlueprintCache.get("Transporter1")));*/
	}

	//TODO Build a real level

	//TODO target building when entity is on the tile
	//TODO XEntity item trading

	//TODO equip weapon without attacking
	//TODO always show xhero characters on the same side
	//TODO show turn counter
	//TODO ReachViewState for all characters (menu as param)
	//TODO change xmenu options on turn 0
	//TODO chooseable starting locations
	//TODO turn 0 item trading
	//TODO enemy reinforcements
	//TODO Fix game crashing when you lose
	//TODO ESC to go back to NoneState/EditingState
	//TODO Editable starting location settings
	//TODO get levelup essence
	//TODO get levelup

	//TODO Refactor LevelMap
	//TODO LevelEditor create XEntity
	//TODO show enemy reach (all enemies)
	//TODO screenshake on crit
	//TODO scale tiles, menubar, gui when resizing
	//TODO Make menu bar not overlap with SideInfo
	//TODO add keybind info for keybinds other than menu
	//TODO improve camera
	//TODO Enemy AI types
	//TODO save transporter history

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