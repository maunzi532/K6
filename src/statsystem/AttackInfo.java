package statsystem;

import entity.*;
import entity.analysis.*;
import java.util.*;
import statsystem.analysis.*;

public final class AttackInfo
{
	public final XCharacter entity;
	public final Stats stats;
	public final AttackMode mode;
	public final XCharacter entityT;
	public final Stats statsT;
	public final AttackMode modeT;
	public final int distance;
	public RNGInfoAnalysis<RNGOutcome2, RNGDivider2> analysis;

	public final boolean canInitiate;
	public final AttackInfoPart3 calc;
	public final AttackInfoPart3 calcT;
	private final CharSequence[] infos;

	public AttackInfo(XCharacter entity, AttackMode mode, XCharacter entityT, AttackMode modeT, int distance)
	{
		this.entity = entity;
		stats = entity.stats();
		this.mode = mode;
		this.entityT = entityT;
		statsT = entityT.stats();
		this.modeT = modeT;
		this.distance = distance;
		AttackMode3 attackMode = AttackMode3.convert(stats, mode);
		AttackMode3 attackModeT = AttackMode3.convert(statsT, modeT);
		canInitiate = Arrays.stream(attackMode.ranges(AttackSide.INITIATOR)).anyMatch(e -> e == distance);
		calc = new AttackInfoPart3(attackMode, attackModeT, distance, AttackSide.INITIATOR);
		calcT = new AttackInfoPart3(attackModeT, attackMode, distance, AttackSide.TARGET);
		CharSequence[] i1 = calc.infos();
		CharSequence[] i2 = calcT.infos();
		infos = new CharSequence[i1.length * 2];
		for(int i = 0; i < i1.length; i++)
		{
			infos[i * 2] = i1[i];
			infos[i * 2 + 1] = i2[i];
		}
	}

	public XCharacter getEntity(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> entity;
					case TARGET -> entityT;
				};
	}

	public Stats getStats(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> stats;
					case TARGET -> statsT;
				};
	}

	public AttackInfo addAnalysis()
	{
		analysis = new RNGInfoAnalysis<>(new RNGDivider2(this)).create();
		return this;
	}

	public AttackInfoPart3 getCalc(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> calc;
					case TARGET -> calcT;
				};
	}

	public CharSequence[] getInfos()
	{
		return infos;
	}

	public CharSequence[] getSideInfos(AttackSide side)
	{
		return getCalc(side).sideInfos();
	}

	public int getSideInfoChange(AttackSide side)
	{
		return switch(side)
				{
					case INITIATOR -> -calc.healthCost;
					case TARGET -> -calcT.healthCost;
				};
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) return true;
		if(!(obj instanceof AttackInfo other)) return false;
		return distance == other.distance &&
				entity.equals(other.entity) &&
				Objects.equals(mode, other.mode) &&
				entityT.equals(other.entityT) &&
				Objects.equals(modeT, other.modeT);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(entity, mode, entityT, modeT, distance);
	}
}