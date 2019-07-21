package system2;

import entity.*;
import geom.f1.*;
import java.util.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode2>
{
	private final Random rng;
	private final String[] infos;
	public final AttackInfoPart2 calc;
	public final AttackInfoPart2 calcT;
	private boolean[] cna;

	public AttackInfo2(Random rng, XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT,
			int distance)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance);
		this.rng = rng;
		boolean rangeOk = Arrays.stream(mode.getRanges(false)).anyMatch(e -> e == distance);
		boolean rangeOkT = Arrays.stream(modeT.getRanges(true)).anyMatch(e -> e == distance);
		calc = new AttackInfoPart2(stats, mode, rangeOk, statsT, modeT, rangeOkT);
		calcT = new AttackInfoPart2(statsT, modeT, rangeOkT, stats, mode, rangeOk);
		String[] i1 = calc.infos();
		String[] i2 = calcT.infos();
		infos = new String[i1.length * 2];
		for(int i = 0; i < i1.length; i++)
		{
			infos[i * 2] = i1[i];
			infos[i * 2 + 1] = i2[i];
		}
		cna = new boolean[2];
	}

	@Override
	public AttackInfo2 addAnalysis(CombatSystem combatSystem)
	{
		super.addAnalysis(combatSystem);
		return this;
	}

	private AttackInfoPart2 getCalc(boolean inverse)
	{
		return inverse ? calcT : calc;
	}

	@Override
	public int[] getChange(boolean inverse, int num)
	{
		int[] change = new int[2];
		if(cna[inverse ? 1 : 0])
			return change;
		AttackInfoPart2 calc1 = getCalc(inverse);
		if(num == 0)
		{
			if(getStats(inverse).getCurrentHealth() > calc1.cost)
				change[0] = -getCalc(inverse).cost;
			else
			{
				cna[inverse ? 1 : 0] = true;
				return change;
			}
		}
		if(num != 0 || !calc1.autohit1)
		{
			int rn = rng.nextInt(100);
			if(rn >= calc1.hitrate)
				return change;
		}
		boolean crit;
		int rn2 = rng.nextInt(100);
		crit = rn2 < calc1.critrate;
		if(num == 0 && calc1.melting1)
		{
			if(crit)
				change[1] = -calc1.meltCritDamage;
			else
				change[1] = -calc1.meltDamage;
		}
		else
		{
			if(crit)
				change[1] = -calc1.critDamage;
			else
				change[1] = -calc1.damage;
		}
		return change;
	}

	@Override
	public int attackCount(boolean inverse)
	{
		return getCalc(inverse).attackCount;
	}

	@Override
	public String[] getInfos()
	{
		return infos;
	}

	@Override
	public String[] getInfos(boolean inverse)
	{
		return inverse ? calcT.infos() : calc.infos();
	}

	@Override
	public String[] getSideInfos(boolean inverse)
	{
		return inverse ? calcT.sideInfos() : calc.sideInfos();
	}
}