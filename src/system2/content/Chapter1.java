package system2.content;

import geom.f1.*;
import logic.*;
import system2.*;

public class Chapter1
{
	public static void createCharacters(MainState mainState, Tile... locations)
	{
		if(locations.length > 0)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[0])
					.setStats(new Stats2(XClasses.mageClass(), 0, "Anna",
							"AN_3.png", 11, 12, 14, 9,
							10, 12, 11, 40,
							6))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardSpell())
					.create(true);
		}
		if(locations.length > 1)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[1])
					.setStats(new Stats2(XClasses.banditClass(), 2, "Kate",
							"KT_4.png", 10, 9, 7, 11,
							14, 14, 8, 65,
							6))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardAxe())
					.create(true);
		}
		if(locations.length > 2)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[2])
					.setStats(new Stats2(XClasses.squireClass(), 0, "John",
							"JN_2.png", 7, 15, 9, 13,
							11, 11, 11, 50,
							6))
					.addItem(AttackItems2.standardDagger())
					.addItem(AttackItems2.standardSpear())
					.create(true);
		}
		if(locations.length > 3)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[3])
					.setStats(new Stats2(XClasses.hexerClass(), 4, "Selen",
							null, 13, 8, 12, 12,
							9, 9, 15, 60,
							6))
					.addItem(AttackItems2.standardSpell())
					.create(true);
		}
		if(locations.length > 4)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[4])
					.setStats(new Stats2(XClasses.soldierClass(), 6, "Arch",
							null, 15, 11, 14, 14,
							8, 18, 10, 60,
							6))
					.addItem(AttackItems2.standardSpear())
					.addItem(AttackItems2.standardCrossbow())
					.create(true);
		}
		if(locations.length > 5)
		{
			new Entity2Builder(mainState)
					.setLocation(locations[5])
					.setStats(new Stats2(XClasses.pirateClass(), 7, "Zeta",
							null, 15, 14, 12, 14,
							12, 16, 13, 55,
							6))
					.addItem(AttackItems2.standardAxe())
					.addItem(AttackItems2.standardCrossbow())
					.create(true);
		}
	}
}