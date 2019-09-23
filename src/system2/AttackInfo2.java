package system2;

import entity.*;
import geom.f1.*;
import java.util.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode2>
{
	private final String[] infos;
	public final AttackInfoPart2 calc;
	public final AttackInfoPart2 calcT;

	public AttackInfo2(XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT, int distance)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance);
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
	}

	@Override
	public AttackInfo2 addAnalysis(CombatSystem combatSystem)
	{
		super.addAnalysis(combatSystem);
		return this;
	}

	public AttackInfoPart2 getCalc(boolean inverse)
	{
		return inverse ? calcT : calc;
	}

	@Override
	public String[] getInfos()
	{
		return infos;
	}

	@Override
	public String[] getSideInfos(boolean inverse)
	{
		return inverse ? calcT.sideInfos() : calc.sideInfos();
	}
}