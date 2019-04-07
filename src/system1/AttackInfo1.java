package system1;

import entity.*;

public class AttackInfo1 extends AttackInfo<Stats1, AttackItem1>
{
	public static AttackInfo1 create(System1 system1, XEntity entity, Stats1 stats, AttackItem1 item,
			XEntity entityT, Stats1 statsT, AttackItem1 itemT, int distance)
	{
		int[][] info = system1.info(entity, stats, item, entityT, statsT, itemT, distance);
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
		return new AttackInfo1(entity, stats, item, entityT, statsT, itemT, distance, infoV);
	}

	private AttackInfo1(XEntity entity, Stats1 stats, AttackItem1 item, XEntity entityT, Stats1 statsT, AttackItem1 itemT,
			int distance, String... infos)
	{
		super(entity, stats, item, entityT, statsT, itemT, distance, infos);
	}
}