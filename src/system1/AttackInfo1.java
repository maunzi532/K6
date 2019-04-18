package system1;

import entity.*;
import geom.f1.*;

public class AttackInfo1 extends AttackInfo<Stats1, AttackItem1>
{
	public static AttackInfo1 create(System1 system1, XEntity entity, Tile loc, Stats1 stats, AttackItem1 item,
			XEntity entityT, Tile locT, Stats1 statsT, AttackItem1 itemT, int distance)
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
		return new AttackInfo1(entity, loc, stats, item, entityT, locT, statsT, itemT, distance, info, infoV);
	}

	private int[][] info;

	private AttackInfo1(XEntity entity, Tile loc, Stats1 stats, AttackItem1 item, XEntity entityT, Tile locT, Stats1 statsT, AttackItem1 itemT,
			int distance, int[][] info, String... infos)
	{
		super(entity, loc, stats, item, entityT, locT, statsT, itemT, distance, infos);
		this.info = info;
	}

	@Override
	public int getChange(boolean current, boolean inverse)
	{
		return current ? 0 : -info[inverse ? 0 : 1][0];
	}

	@Override
	public int attackCount(boolean inverse)
	{
		return info[inverse ? 1 : 0][1];
	}
}