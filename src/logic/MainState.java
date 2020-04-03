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
	//TODO Char edit add/remove StartLocation
	//TODO StartingLocation replace SaveSettings
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
	//TODO unlimited TurnResources in Preparation Phase

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
	//End Save - saves characters, storage, progress
	//Prepare Save - saves characters with locations, storage, current map, progress, (enemy range view)
	//Mid Save - saves characters (with locations, turnresources, linked), buildings, current map, turn, progress
	//Map Save - saves starting locations, enemies, buildings, story, additional characters, next map
}