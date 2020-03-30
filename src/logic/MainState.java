package logic;

import building.blueprint.*;
import doubleinv.*;
import entity.*;
import entity.sideinfo.*;
import geom.f1.*;
import item.*;
import java.util.*;
import levelMap.*;
import logic.xstate.*;

public class MainState
{
	public final TileType y1;
	public final ItemLoader itemLoader;
	public final SideInfoFrame sideInfoFrame;
	public final BlueprintFile blueprintFile;
	public final LevelMap levelMap;
	public int turnCounter;
	public final DoubleInv storage;
	public boolean preferBuildings;
	public boolean showAllEnemyReach;
	public XStateHolder stateHolder;
	public Map<Tile, Long> allEnemyReach;

	public MainState(TileType y1, ItemLoader itemLoader, SideInfoFrame sideInfoFrame, BlueprintFile blueprintFile)
	{
		this.y1 = y1;
		this.itemLoader = itemLoader;
		this.sideInfoFrame = sideInfoFrame;
		this.blueprintFile = blueprintFile;
		levelMap = new LevelMap(y1);
		turnCounter = -1;
		storage = new Storage();
	}

	public void importSave(SavedImport savedImport)
	{
		savedImport.importIntoMap3(levelMap, itemLoader, storage.inputInv());
	}

	public String turnText()
	{
		if(turnCounter <= 0)
		{
			return "Preparation Phase";
		}
		else
		{
			return "Turn " + turnCounter;
		}
	}

	public String preferBuildingsText()
	{
		return preferBuildings ? "BCM" : "CCM";
	}

	//content
	//TODO    Build a real level

	//images
	//TODO replace floor images (1/5)
	//TODO downscale item images (0/4)

	//save/load
	//TODO savestate during map
	//TODO    save maps and saves in folders
	//TODO    advanced loading system

	//engine mechanics
	//TODO all enemy reach show amount of enemies
	//TODO turn limit
	//TODO enemy reinforcements (starting delay)
	//TODO remove defeated enemies

	//GUI
	//TODO edit starting delay
	//TODO    LevelEditor edit PlayerLevelSystem
	//TODO    LevelSystem view
	//TODO    CharacterCombatGUI show stat calculation
	//TODO    AttackInfoGUI show stat calculation

	//combat system (rules)
	//TODO    change speed effects
	//TODO    levelup essence formula
	//TODO add upgraded classes
	//TODO add class abilities (0/6)

	//combat system (systems)
	//TODO Enemy AI types
	//TODO    Enemy AI type: Activate
	//TODO    Enemy AI type: Stationary
	//TODO    complicated levelup system

	//code
	//TODO    Initialize somewhere else than in MainVisual
	//TODO character class names in classes
	//TODO fix stealing from enemies

	//visual engine mechanics
	//TODO move GUI so it does not overlap with SideInfo/LevelEditor
	//TODO    improve camera controls
	//TODO move camera to view enemies/encounters
	//TODO    show pause menu
	//TODO    show better SideInfo
	//TODO show TurnResources in SideInfo

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