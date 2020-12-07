package logic;

import entity.sideinfo.*;
import levelmap.*;
import logic.xstate.*;
import system.*;

public record MainState(LevelMap4 levelMap, XStateHolder stateHolder, SideInfoFrame side, World world)
{
	public SystemScheme systemScheme()
	{
		return world.systemScheme();
	}

	//content
	//TODO    Build a real level

	//images
	//TODO replace floor images (1/5)
	//TODO downscale item images (0/3), replace (0/1)

	//save/load
	//TODO save map in SaveMode, save team in turn 0
	//TODO save maps and saves in folders
	//TODO start of world save
	//TODO (save event) end of map save

	//events
	//TODO talk event speech bubble and skip keys
	//TODO add character event
	//TODO add item event
	//TODO rearrange events file/EventPack load

	//engine mechanics
	//TODO test starting delay
	//TODO CharacterDefeatedState
	//TODO turn limit
	//TODO    all enemy reach show amount of enemies
	//TODO show TurnResources in SideInfo
	//TODO win condition/lose condition

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
	//TODO move map loading into start package
	//TODO    Initialize somewhere else than in MainVisual

	//visual engine mechanics
	//TODO move GUI so it does not overlap with SideInfo/LevelEditor
	//TODO move camera to view enemies/encounters
	//TODO    improve camera controls
	//TODO    show pause menu

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
	//Prepare Save - saves characters+ (locations), storage+, current map+, enemy range view, progress
	//End Save - saves characters, storage, progress
	//Mid Save - saves characters (locations, turnresources, linked), storage, buildings, enemy range view, current map, turn, progress
}