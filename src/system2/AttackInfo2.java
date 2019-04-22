package system2;

import entity.*;
import geom.f1.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode2>
{
	private final String[] infos;
	private final AttackInfoPart2 calc;
	private final AttackInfoPart2 calcT;

	public AttackInfo2(System2 system2, XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT,
			int distance)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance);
		calc = new AttackInfoPart2(stats, mode, statsT, modeT);
		calcT = new AttackInfoPart2(statsT, modeT, stats, mode);
		infos = new String[8];
		if(calc.attackCount > 0)
		{
			this.infos[0] = String.valueOf(calc.damage);
			this.infos[2] = String.valueOf(calc.attackCount);
			this.infos[4] = String.valueOf(calc.hitrate);
			this.infos[6] = String.valueOf(calc.critrate);
		}
		else
		{
			this.infos[0] = "";
			this.infos[2] = "";
			this.infos[4] = "";
			this.infos[6] = "";
		}
		if(calcT.attackCount > 0)
		{
			this.infos[1] = String.valueOf(calcT.damage);
			this.infos[3] = String.valueOf(calcT.attackCount);
			this.infos[5] = String.valueOf(calcT.hitrate);
			this.infos[7] = String.valueOf(calcT.critrate);
		}
		else
		{
			this.infos[1] = "";
			this.infos[3] = "";
			this.infos[5] = "";
			this.infos[7] = "";
		}
	}

	@Override
	public int getChange(boolean current, boolean inverse)
	{
		return current ? 0 : inverse ? -calc.damage : -calcT.damage;
	}

	@Override
	public int attackCount(boolean inverse)
	{
		return inverse ? calcT.attackCount : calc.attackCount;
	}

	@Override
	public String[] getInfos()
	{
		return infos;
	}
}