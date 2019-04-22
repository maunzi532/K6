package system2;

import entity.*;
import geom.f1.*;
import java.util.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode2>
{
	private final Random rng;
	private final String[] infos;
	private final AttackInfoPart2 calc;
	private final AttackInfoPart2 calcT;

	public AttackInfo2(Random rng, XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT,
			int distance)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance);
		this.rng = rng;
		calc = new AttackInfoPart2(stats, mode, statsT, modeT);
		calcT = new AttackInfoPart2(statsT, modeT, stats, mode);
		infos = new String[8];
		this.infos[0] = stats.getCurrentHealth() + "/" + stats.getToughness();
		if(calc.attackCount > 0)
		{
			this.infos[2] = calc.damage + "x" + calc.attackCount;
			this.infos[4] = String.valueOf(calc.hitrate);
			this.infos[6] = String.valueOf(calc.critrate);
		}
		else
		{
			this.infos[2] = "";
			this.infos[4] = "";
			this.infos[6] = "";
		}
		this.infos[1] = statsT.getCurrentHealth() + "/" + statsT.getToughness();
		if(calcT.attackCount > 0)
		{
			this.infos[3] = calcT.damage + "x" + calcT.attackCount;
			this.infos[5] = String.valueOf(calcT.hitrate);
			this.infos[7] = String.valueOf(calcT.critrate);
		}
		else
		{
			this.infos[3] = "";
			this.infos[5] = "";
			this.infos[7] = "";
		}
	}

	private AttackInfoPart2 getCalc(boolean inverse)
	{
		return inverse ? calcT : calc;
	}

	@Override
	public int getChange(boolean current, boolean inverse, int num)
	{
		if(current)
			return 0;
		AttackInfoPart2 calc1 = getCalc(!inverse);
		int rn = rng.nextInt(100);
		if(rn >= calc1.hitrate)
			return 0;
		int rn2 = rng.nextInt(100);
		if(rn2 >= calc1.critrate)
			return -calc1.damage;
		return -calc1.critDamage;
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
}