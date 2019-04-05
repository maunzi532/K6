package entity;

import item.*;

public class AttackInfo<T extends Stats1>
{
	public final XEntity entity;
	public final T stats;
	public final Item item;
	public final XEntity entityT;
	public final T statsT;
	public final Item itemT;
	public final int distance;
	public final String[] infos;

	public AttackInfo(XEntity entity, T stats, Item item,
			XEntity entityT, T statsT, Item itemT, int distance, String... infos)
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