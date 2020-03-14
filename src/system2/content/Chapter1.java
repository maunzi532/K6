package system2.content;

import entity.*;
import geom.f1.*;
import levelMap.*;
import system2.*;

public class Chapter1
{
	public static void createCharacters(LevelMap levelMap, CombatSystem combatSystem, Tile... locations)
	{
		if(locations.length > 0)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[0])
					.setStats(new Stats(XClasses.mageClass(), 0, "Anna",
							"AN_3.png", 6, new PlayerLevelSystem(0, new int[]{11, 12, 14, 9, 10, 12, 11, 8}, 40)))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardSpell())
					.create(true);
		}
		if(locations.length > 1)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[1])
					.setStats(new Stats(XClasses.banditClass(), 2, "Kate",
							"KT_4.png", 6, new PlayerLevelSystem(2, new int[]{10, 9, 7, 11, 14, 14, 8, 13}, 40)))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardAxe())
					.create(true);
		}
		if(locations.length > 2)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[2])
					.setStats(new Stats(XClasses.squireClass(), 0, "John",
							"JN_2.png", 6, new PlayerLevelSystem(0, new int[]{7, 15, 9, 13, 11, 11, 11, 10}, 40)))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardSpear())
					.create(true);
		}
		if(locations.length > 3)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[3])
					.setStats(new Stats(XClasses.soldierClass(), 6, "Arch",
							null, 6, new PlayerLevelSystem(6, new int[]{15, 11, 14, 14, 8, 18, 10, 12}, 40)))
					.addItem(AttackItems2.standardSpear())
					.addItem(AttackItems2.standardCrossbow())
					.create(true);
		}
		if(locations.length > 4)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[4])
					.setStats(new Stats(XClasses.hexerClass(), 4, "Selen",
							null, 6, new PlayerLevelSystem(4, new int[]{13, 8, 12, 12, 9, 9, 15, 12}, 40)))
					.addItem(AttackItems2.standardSpell())
					.create(true);
		}
		if(locations.length > 5)
		{
			new Entity2Builder(levelMap, combatSystem)
					.setLocation(locations[5])
					.setStats(new Stats(XClasses.pirateClass(), 7, "Zeta",
							null, 6, new PlayerLevelSystem(7, new int[]{15, 14, 12, 14, 12, 16, 13, 11}, 40)))
					.addItem(AttackItems2.standardAxe())
					.addItem(AttackItems2.standardCrossbow())
					.create(true);
		}
	}
}