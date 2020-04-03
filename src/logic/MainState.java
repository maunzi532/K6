package logic;

import building.blueprint.*;
import entity.sideinfo.*;
import item.*;
import levelmap.*;
import logic.xstate.*;

public record MainState(LevelMap levelMap, XStateHolder stateHolder, SideInfoFrame side, ItemLoader itemLoader, BlueprintFile blueprintFile)
{
	//content
	//TODO    Build a real level

	//images
	//TODO replace floor images (1/5)
	//TODO downscale item images (0/4)

	//save/load
	//TODO create y1/y2 in LevelMap load
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
	//TODO Building show floor requirements
	//TODO Edit starting locations

	//Text
	//TODO    Locale_DE
	//TODO    remove X_ in SchemeFile
	//TODO RNGOutcome text change to record
	//TODO stats edit name title

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
	//TODO give/take NPE

	//visual engine mechanics
	//TODO move GUI so it does not overlap with SideInfo/LevelEditor
	//TODO    improve camera controls
	//TODO move camera to view enemies/encounters
	//TODO    show pause menu
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

	//Save System
	//Map Save - saves tiles+, buildings+, enemies+, starting locations+ (inv override), events (story, additional characters), next map
	//Prepare Save - saves characters+ (locations), storage+, current map_, enemy range view, progress
	//Mid Save - saves characters (locations, turnresources, linked), storage, buildings, enemy range view, current map, turn, progress
	//End Save - saves characters, storage, progress
}