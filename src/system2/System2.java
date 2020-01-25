package system2;

import arrow.*;
import com.fasterxml.jackson.jr.stree.*;
import entity.*;
import entity.analysis.*;
import geom.f1.*;
import item.*;
import item.inv.*;
import java.util.*;
import java.util.stream.*;
import logic.*;
import system2.analysis.*;
import system2.animation.*;
import system2.content.*;

public class System2 implements CombatSystem<Stats2, AttackInfo2>
{
	@Override
	public int movement(MainState mainState, XEntity entity, Stats2 stats)
	{
		return stats.getMovement();
	}

	@Override
	public int dashMovement(MainState mainState, XEntity entity, Stats2 stats)
	{
		return 12;
	}

	@Override
	public int maxAccessRange(MainState mainState, XEntity entity, Stats2 stats)
	{
		return 4;
	}

	@Override
	public List<Integer> attackRanges(MainState mainState, XEntity entity, Stats2 stats, boolean counter)
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
	public List<AttackInfo2> attackInfo(MainState mainState, XEntity entity, Tile loc, Stats2 stats,
			XEntity entityT, Tile locT, Stats2 statsT)
	{
		int distance = mainState.y1.distance(loc, locT);
		return entity.outputInv()
				.viewItems(false)
				.stream()
				.filter(e -> stats.getItemFilter().canContain(e.item))
				.flatMap(e -> ((AttackItem2) e.item).attackModes().stream())
				.map(mode -> new AttackInfo2(entity, loc, stats, mode,
						entityT, locT, statsT, statsT.getLastUsed(), distance))
				.filter(e -> e.canInitiate)
				.map(e -> e.addAnalysis(this))
				.collect(Collectors.toList());
	}

	@Override
	public void preAttack(AttackInfo2 attackInfo)
	{
		attackInfo.stats.setLastUsed(attackInfo.mode);
	}

	@Override
	public List<XMode> modesForItem(Stats stats, Item item)
	{
		if(item instanceof AttackItem2)
			return ((AttackItem2) item).attackModes().stream().map(e -> AttackMode3.convert((Stats2) stats, e))
					.collect(Collectors.toList());
		return List.of();
	}

	@Override
	public Optional<Item> equippedItem(Stats stats)
	{
		return Optional.ofNullable(((Stats2) stats).getLastUsed()).map(e -> e.item);
	}

	@Override
	public List<Item> allItems()
	{
		return AttackItems2.INSTANCE.allItemsList;
	}

	@Override
	public RNGDivider supplyDivider(AttackInfo2 attackInfo)
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
		return new StandardAI();
	}

	@Override
	public AnimTimer createAnimationTimer(RNGDivider divider, MainState mainState)
	{
		return new AttackAnim((RNGDivider2) divider, mainState);
	}

	@Override
	public AnimTimer createRegenerationAnimation(XEntity entity, MainState mainState)
	{
		return new RegenerationAnim(entity, mainState.levelMap);
	}

	@Override
	public AnimTimer createPostAttackAnimation(AttackInfo aI, RNGOutcome result, MainState mainState)
	{
		return new GetExpAnim((AttackInfo2) aI, (RNGOutcome2) result, mainState);
	}

	@Override
	public XEntity loadEntity(TileType y1, MainState mainState, JrsObject data, ItemLoader itemLoader)
	{
		int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		Stats2 stats = new Stats2(((JrsObject) data.get("Stats")), itemLoader);
		Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
		return switch(classCode)
				{
					case 1 -> new XHero(location, mainState, stats, false, false, inv);
					case 2 -> new XEnemy(location, mainState, stats, new StandardAI(), inv);
					default -> throw new RuntimeException();
				};
	}

	@Override
	public XEntity loadEntityOrStartLoc(TileType y1, MainState mainState, JrsObject data, ItemLoader itemLoader, Map<String, JrsObject> characters, Inv storage)
	{
		Tile location = y1.create2(((JrsNumber) data.get("sx")).getValue().intValue(), ((JrsNumber) data.get("sy")).getValue().intValue());
		if(data.get("StartName") != null)
		{
			String startName = data.get("StartName").asText();
			boolean locked = ((JrsBoolean) data.get("Locked")).booleanValue();
			boolean invLocked = ((JrsBoolean) data.get("InvLocked")).booleanValue();
			JrsObject char1 = characters.get(startName);
			Stats2 stats = new Stats2(((JrsObject) char1.get("Stats")), itemLoader);
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
			return new XHero(location, mainState, stats, locked, invLocked, inv);
		}
		else
		{
			int classCode = ((JrsNumber) data.get("Type")).getValue().intValue();
			Stats2 stats = new Stats2(((JrsObject) data.get("Stats")), itemLoader);
				Inv inv = new WeightInv(((JrsObject) data.get("Inventory")), itemLoader);
			return switch(classCode)
					{
						case 1 -> new XHero(location, mainState, stats, false, false, inv);
						case 2 -> new XEnemy(location, mainState, stats, new StandardAI(), inv);
						default -> throw new RuntimeException();
					};
		}
	}

	@Override
	public Stats defaultStats(boolean xh)
	{
		return new Stats2(XClasses.mageClass(), 0, xh ? new PlayerLevelSystem(0, IntStream.range(0, 8).toArray(), 40) : null);
	}
}