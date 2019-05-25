package entity;

import geom.f1.*;

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

	public abstract int[] getChange(boolean inverse, int num);

	public abstract int attackCount(boolean inverse);

	public abstract String[] getInfos();

	public abstract String[] getInfos(boolean inverse);

	public abstract String[] getSideInfos(boolean inverse);
}