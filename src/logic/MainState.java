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
	//TODO Building show floor requirements

	//Text
	//TODO GUI Text in file CharInv / replace charinv with charstats
	//TODO GUI Text in file CharLevelup / replace charlevelup somehow
	//TODO check remaining text
	//TODO    Locale_DE
	//TODO    remove X_ in SchemeFile
	//TODO RNGOutcome text change to record
	//TODO AttackInfoPart3 sideInfos
	//TODO stats edit name title
	//TODO keybind prefix
	//TODO keybind mouse button names
	//TODO keybind infoAdd

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
	//TODO fix stealing from enemies
	//TODO fix cannot build in preparation phase

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