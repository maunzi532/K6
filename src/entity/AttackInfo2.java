package entity;

import item.items.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackItem2>
{
	public static AttackInfo2 create(Wugu2 wugu2, XEntity entity, Stats2 stats, AttackItem2 item,
			XEntity entityT, Stats2 statsT, AttackItem2 itemT, int distance)
	{
		int[][] info = wugu2.info(entity, stats, item, entityT, statsT, itemT, distance);
		String[] infoV = new String[8];
		if(info[0][1] > 0)
		{
			infoV[0] = String.valueOf(info[0][0]);
			infoV[2] = String.valueOf(info[0][1]);
			infoV[4] = String.valueOf(info[0][2]);
			infoV[6] = String.valueOf(info[0][3]);
		}
		else
		{
			infoV[0] = "";
			infoV[2] = "";
			infoV[4] = "";
			infoV[6] = "";
		}
		if(info[1][1] > 0)
		{
			infoV[1] = String.valueOf(info[1][0]);
			infoV[3] = String.valueOf(info[1][1]);
			infoV[5] = String.valueOf(info[1][2]);
			infoV[7] = String.valueOf(info[1][3]);
		}
		else
		{
			infoV[1] = "";
			infoV[3] = "";
			infoV[5] = "";
			infoV[7] = "";
		}
		return new AttackInfo2(entity, stats, item, entityT, statsT, itemT, distance, infoV);
	}

	private AttackInfo2(XEntity entity, Stats2 stats, AttackItem2 item, XEntity entityT, Stats2 statsT, AttackItem2 itemT,
			int distance, String... infos)
	{
		super(entity, stats, item, entityT, statsT, itemT, distance, infos);
	}
}