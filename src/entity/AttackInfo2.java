package entity;

import item.*;

public class AttackInfo2 extends AttackInfo<Stats2>
{

	public static AttackInfo2 create(XEntity entity, Stats2 stats, Item item,
			XEntity entityT, Stats2 statsT, Item itemT, int distance)
	{
		String[] infos = new String[8];
		infos[0] = String.valueOf(Math.max(0, stats.getAttack() - statsT.getDefense()));
		infos[2] = stats.getSpeed() - statsT.getSpeed() >= 5 ? "x2" : "";
		infos[4] = String.valueOf(Math.min(100, Math.max(0, (stats.getAccuracy() - statsT.getAccuracy()) * 5 + 80)));
		infos[6] = String.valueOf(Math.min(100, Math.max(0, (stats.getCrit() - statsT.getCrit()) * 2)));
		infos[1] = String.valueOf(Math.max(0, statsT.getAttack() - stats.getDefense()));
		infos[3] = statsT.getSpeed() - stats.getSpeed() >= 5 ? "x2" : "";
		infos[5] = String.valueOf(Math.min(100, Math.max(0, (statsT.getAccuracy() - stats.getAccuracy()) * 5 + 80)));
		infos[7] = String.valueOf(Math.min(100, Math.max(0, (statsT.getCrit() - stats.getCrit()) * 2)));
		return new AttackInfo2(entity, stats, item, entityT, statsT, itemT, distance, infos);
	}

	private AttackInfo2(XEntity entity, Stats2 stats, Item item, XEntity entityT, Stats2 statsT, Item itemT,
			int distance, String... infos)
	{
		super(entity, stats, item, entityT, statsT, itemT, distance, infos);
	}
}