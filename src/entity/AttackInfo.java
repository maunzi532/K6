package entity;

import item.*;

public class AttackInfo<T extends Stats, I extends Item>
{
	public final XEntity entity;
	public final T stats;
	public final I item;
	public final XEntity entityT;
	public final T statsT;
	public final I itemT;
	public final int distance;
	public final String[] infos;

	public AttackInfo(XEntity entity, T stats, I item,
			XEntity entityT, T statsT, I itemT, int distance, String... infos)
	{
		this.entity = entity;
		this.stats = stats;
		this.item = item;
		this.entityT = entityT;
		this.statsT = statsT;
		this.itemT = itemT;
		this.distance = distance;
		this.infos = infos;
	}
}