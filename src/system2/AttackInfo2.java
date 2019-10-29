package system2;

import entity.*;
import geom.f1.*;
import java.util.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode4>
{
	public final boolean canInitiate;
	public final AttackInfoPart3 calc;
	public final AttackInfoPart3 calcT;
	private final String[] infos;

	public AttackInfo2(XEntity entity, Tile loc, Stats2 stats, AttackMode4 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode4 modeT, int distance)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance);
		AttackMode3 attackMode = AttackMode3.convert(stats, mode);
		AttackMode3 attackModeT = AttackMode3.convert(statsT, modeT);
		canInitiate = Arrays.stream(attackMode.ranges).anyMatch(e -> e == distance);
		calc = new AttackInfoPart3(attackMode, attackModeT, distance, false);
		calcT = new AttackInfoPart3(attackModeT, attackMode, distance, true);
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

	public AttackInfoPart3 getCalc(boolean inverse)
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