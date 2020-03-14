package system2;

import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;
import levelMap.*;
import system2.analysis.*;
import system2.animation.*;
import system2.content.*;

public class System2 implements CombatSystem
{
	private LevelMap levelMap;

	public void setLevelMap(LevelMap levelMap)
	{
		this.levelMap = levelMap;
	}

	@Override
	public int movement(XEntity entity, Stats stats)
	{
		return stats.getMovement();
	}

	@Override
	public int dashMovement(XEntity entity, Stats stats)
	{
		return 12;
	}

	@Override
	public int maxAccessRange(XEntity entity, Stats stats)
	{
		return 4;
	}

	@Override
	public List<Integer> attackRanges(XEntity entity, Stats stats, boolean counter)
	{
		List<int[]> v = entity.outputInv().viewItems(false)
				.stream().filter(e -> stats.getItemFilter().canContain(e.item))
				.map(e -> ((AttackItem2) e.item).getRanges(counter)).collect(Collectors.toList());
		HashSet<Integer> ints2 = new HashSet<>();
		for(int[] ints : v)
		{
			for(int i = 0; i < ints.length; i++)
			{
				ints2.add(ints[i]);
			}
		}
		return ints2.stream().sorted().collect(Collectors.toList());
	}

	@Override
	public List<AttackInfo> attackInfo(XEntity entity, Tile loc, Stats stats,
			XEntity entityT, Tile locT, Stats statsT)
	{
		int distance = levelMap.y1.distance(loc, locT);
		return entity.outputInv()
				.viewItems(false)
				.stream()
				.filter(e -> stats.getItemFilter().canContain(e.item))
				.flatMap(e -> ((AttackItem2) e.item).attackModes().stream())
				.map(mode -> new AttackInfo(entity, loc, stats, mode,
						entityT, locT, statsT, statsT.getLastUsed(), distance))
				.filter(e -> e.canInitiate)
				.map(e -> e.addAnalysis(this))
				.collect(Collectors.toList());
	}

	@Override
	public void preAttack(AttackInfo attackInfo)
	{
		attackInfo.stats.setLastUsed(attackInfo.mode);
	}

	@Override
	public List<AttackMode3> modesForItem(Stats stats, Item item)
	{
		if(item instanceof AttackItem2)
			return ((AttackItem2) item).attackModes().stream().map(e -> AttackMode3.convert(stats, e))
					.collect(Collectors.toList());
		return List.of();
	}

	@Override
	public Optional<Item> equippedItem(Stats stats)
	{
		return Optional.ofNullable((stats).getLastUsed()).map(e -> e.item);
	}

	@Override
	public List<Item> allItems()
	{
		return AttackItems2.INSTANCE.allItemsList;
	}

	@Override
	public RNGDivider2 supplyDivider(AttackInfo attackInfo)
	{
		return new RNGDivider2(attackInfo);
	}

	@Override
	public double enemyAIScore(List<RNGOutcome> outcomes)
	{
		return new EnemyThoughts2(outcomes).score();
	}

	@Override
	public EnemyAI standardAI()
	{
		return new StandardAI(levelMap, this);
	}

	@Override
	public AnimTimer createAnimationTimer(RNGDivider2 divider)
	{
		return new AttackAnim(divider, levelMap, levelMap);
	}

	@Override
	public AnimTimer createRegenerationAnimation(XEntity entity)
	{
		return new RegenerationAnim(entity, levelMap);
	}

	@Override
	public AnimTimer createPostAttackAnimation(AttackInfo aI, RNGOutcome result)
	{
		return new GetExpAnim(aI, (RNGOutcome2) result, levelMap);
	}

	@Override
	public XEntity loadEntity(TileType y1, JrsObject data, ItemLoader itemLoader)
	{
		int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		return switch(classCode)
				{
					case 1 -> new XHero(location, this, stats, false, false, inv);
					case 2 -> new XEnemy(location, this, stats, new StandardAI(levelMap, this), inv);
					default -> throw new RuntimeException();
				};
	}

	@Override
	public XEntity loadEntityOrStartLoc(TileType y1, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage)
	{
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		if(data.get("StartName") != null)
		{
			String startName = data.get("StartName").asText();
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			JrsObject char1 = characters.get(startName);
			Stats stats = new Stats(((JrsObject) char1.get("Stats")), itemLoader);
			Inv inv;
			if(invLocked)
			{
				inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
				Inv inv2 = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
				storage.tryAdd(inv2.allItems(), false, CommitType.COMMIT);
			}
			else
			{
				inv = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
			}
			return new XHero(location, this, stats, locked, invLocked, inv);
		}
		else
		{
			int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
			Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
				Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			return switch(classCode)
					{
						case 1 -> new XHero(location, this, stats, false, false, inv);
						case 2 -> new XEnemy(location, this, stats, new StandardAI(levelMap, this), inv);
						default -> throw new RuntimeException();
					};
		}
	}

	public XCharacter loadCharacterOrStartLoc(TileType y1, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage)
	{
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
		if(data.get("StartName") != null)
		{
			String startName = data.get("StartName").asText();
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			JrsObject char1 = characters.get(startName);
			Stats stats = new Stats(((JrsObject) char1.get("Stats")), itemLoader);
			Inv inv;
			if(invLocked)
			{
				inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
				Inv inv2 = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
				storage.tryAdd(inv2.allItems(), false, CommitType.COMMIT);
			}
			else
			{
				inv = new WeightInv(((JrsObject) char1.get("Inventory")), itemLoader);
			}
			return switch(classCode)
					{
						case 1 -> new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
								null, null, new CharacterSave(locked, invLocked));
						case 2 -> new XCharacter(CharacterTeam.ENEMY, 0, location, stats, inv,
								null, null, new CharacterSave(locked, invLocked));
						default -> throw new RuntimeException();
					};
		}
		else
		{
			Stats stats = new Stats(((JrsObject) data.get("Stats")), itemLoader);
			Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			return switch(classCode)
					{
						case 1 -> new XCharacter(CharacterTeam.HERO, 0, location, stats, inv,
							null, null, null);
						case 2 -> new XCharacter(CharacterTeam.ENEMY, 0, location, stats, inv,
							new StandardAI(levelMap, this), null, null);
						default -> throw new RuntimeException();
					};
		}
	}

	@Override
	public Stats defaultStats(boolean xh)
	{
		return new Stats(XClasses.mageClass(), 0, xh ? new PlayerLevelSystem(0, IntStream.range(0, 8).toArray(), 40) : null);
	}
}