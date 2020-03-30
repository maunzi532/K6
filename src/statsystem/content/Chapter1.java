package statsystem.content;

import entity.*;
import geom.tile.*;
import levelmap.*;
import statsystem.*;

public final class Chapter1
{
	public void createCharacters(LevelMap levelMap, Tile... locations)
	{
		if(locations.length > 0)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[0])
					.setStats(new Stats(XClasses.mageClass(), 0, "Anna", "mapsprite.default",
							"character.anna.standard", 6,
							new PlayerLevelSystem(0, new int[]{11, 12, 14, 9, 10, 12, 11, 8}, 40)))
					.addItem(AttackItems.standardDagger())
					.addItem(AttackItems.standardSpell())
					.create(CharacterTeam.HERO);
		}
		if(locations.length > 1)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[1])
					.setStats(new Stats(XClasses.explorerClass(), 2, "Kate", "mapsprite.default",
							"character.kate.standard", 6,
							new PlayerLevelSystem(2, new int[]{10, 9, 7, 11, 14, 14, 8, 13}, 40)))
					.addItem(AttackItems.standardAxe())
					.addItem(AttackItems.standardCrossbow())
					.create(CharacterTeam.HERO);
		}
		if(locations.length > 2)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[2])
					.setStats(new Stats(XClasses.squireClass(), 0, "John", "mapsprite.default",
							"character.john.standard", 6,
							new PlayerLevelSystem(0, new int[]{7, 15, 9, 13, 11, 11, 11, 10}, 40)))
					.addItem(AttackItems.standardDagger())
					.addItem(AttackItems.standardSpear())
					.create(CharacterTeam.HERO);
		}
		if(locations.length > 3)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[4])
					.setStats(new Stats(XClasses.hexerClass(), 4, "Selen", "mapsprite.default",
							"character.selen.standard", 6,
							new PlayerLevelSystem(4, new int[]{13, 8, 12, 12, 9, 9, 15, 12}, 40)))
					.addItem(AttackItems.standardSpell())
					.create(CharacterTeam.HERO);
		}
		if(locations.length > 4)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[5])
					.setStats(new Stats(XClasses.banditClass(), 10, "Dave", "mapsprite.default",
							"character.dave.standard", 6,
							new PlayerLevelSystem(7, new int[]{15, 14, 12, 14, 12, 16, 13, 11}, 40)))
					.addItem(AttackItems.standardDagger())
					.addItem(AttackItems.standardAxe())
					.create(CharacterTeam.HERO);
		}
		if(locations.length > 5)
		{
			new Entity2Builder(levelMap)
					.setLocation(locations[3])
					.setStats(new Stats(XClasses.soldierClass(), 12, "Edna", "mapsprite.default",
							"character.edna.standard", 6,
							new PlayerLevelSystem(6, new int[]{15, 11, 14, 14, 8, 18, 10, 12}, 40)))
					.addItem(AttackItems.standardSpear())
					.addItem(AttackItems.standardCrossbow())
					.create(CharacterTeam.HERO);
		}
	}
}