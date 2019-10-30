package entity;

import entity.analysis.*;
import geom.f1.*;
import java.util.*;

public abstract class AttackInfo<T extends Stats, I extends XMode>
{
	public final XEntity entity;
	public final Tile loc;
	public final T stats;
	public final I mode;
	public final XEntity entityT;
	public final Tile locT;
	public final T statsT;
	public final I modeT;
	public final int distance;
	public RNGInfoAnalysis analysis;

	public AttackInfo(XEntity entity, Tile loc, T stats, I mode,
			XEntity entityT, Tile locT, T statsT, I modeT, int distance)
	{
		this.entity = entity;
		this.loc = loc;
		this.stats = stats;
		this.mode = mode;
		this.entityT = entityT;
		this.locT = locT;
		this.statsT = statsT;
		this.modeT = modeT;
		this.distance = distance;
	}

	public AttackInfo addAnalysis(CombatSystem combatSystem)
	{
		analysis = new RNGInfoAnalysis(combatSystem.supplyDivider(this)).create();
		return this;
	}

	public XEntity getEntity(boolean inverse)
	{
		return inverse ? entityT : entity;
	}

	public T getStats(boolean inverse)
	{
		return inverse ? statsT : stats;
	}

	public I getMode(boolean inverse)
	{
		return inverse ? modeT : mode;
	}

	public abstract String[] getInfos();

	public abstract String[] getSideInfos(boolean inverse);

	public abstract String getSideInfoX1T(boolean inverse);

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof AttackInfo)) return false;
		AttackInfo<?, ?> that = (AttackInfo<?, ?>) o;
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