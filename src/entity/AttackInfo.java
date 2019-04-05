package entity;

import item.*;

public class AttackInfo
{
	public final XEntity attacker;
	public final Item attackItem;
	public final XEntity target;
	public final Item counterItem;
	public final int distance;
	public final String[] infos;

	public AttackInfo(XEntity attacker, Item attackItem, XEntity target, Item counterItem, int distance, String... infos)
	{
		this.attacker = attacker;
		this.attackItem = attackItem;
		this.target = target;
		this.counterItem = counterItem;
		this.distance = distance;
		this.infos = infos;
	}
}