package system2;

import entity.*;
import entity.analysis.*;
import geom.f1.*;
import java.util.*;
import system2.analysis.*;

public class AttackInfo
{
	public final XCharacter entity;
	public final Tile loc;
	public final Stats stats;
	public final AttackMode4 mode;
	public final XCharacter entityT;
	public final Tile locT;
	public final Stats statsT;
	public final AttackMode4 modeT;
	public final int distance;
	public RNGInfoAnalysis<RNGDivider2> analysis;

	public final boolean canInitiate;
	public final AttackInfoPart3 calc;
	public final AttackInfoPart3 calcT;
	private final String[] infos;

	public AttackInfo(XCharacter entity, Tile loc, AttackMode4 mode, XCharacter entityT, Tile locT, AttackMode4 modeT, int distance)
	{
		this.entity = entity;
		this.loc = loc;
		stats = entity.stats();
		this.mode = mode;
		this.entityT = entityT;
		this.locT = locT;
		statsT = entityT.stats();
		this.modeT = modeT;
		this.distance = distance;
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

	public XCharacter getEntity(boolean inverse)
	{
		return inverse ? entityT : entity;
	}

	public Stats getStats(boolean inverse)
	{
		return inverse ? statsT : stats;
	}

	public AttackMode4 getMode(boolean inverse)
	{
		return inverse ? modeT : mode;
	}

	public AttackInfo addAnalysis()
	{
		analysis = new RNGInfoAnalysis<>(new RNGDivider2(this)).create();
		return this;
	}

	public AttackInfoPart3 getCalc(boolean inverse)
	{
		return inverse ? calcT : calc;
	}

	public String[] getInfos()
	{
		return infos;
	}

	public String[] getSideInfos(boolean inverse)
	{
		return inverse ? calcT.sideInfos() : calc.sideInfos();
	}

	public String getSideInfoX1T(boolean inverse)
	{
		return inverse ? (calcT.healthCost > 0 ? "(-" + calcT.healthCost + ")" : "") : (calc.healthCost > 0 ? "(-" + calc.healthCost + ")" : "");
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof AttackInfo that)) return false;
		return distance == that.distance &&
				entity.equals(that.entity) &&
				Objects.equals(mode, that.mode) &&
				entityT.equals(that.entityT) &&
				Objects.equals(modeT, that.modeT);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(entity, mode, entityT, modeT, distance);
	}
}