package system2;

import entity.*;
import geom.f1.*;

public class AttackInfo2 extends AttackInfo<Stats2, AttackMode2>
{
	public static AttackInfo2 create(System2 system2, XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode,
			XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT, int distance)
	{
		int[][] info = system2.info(entity, stats, mode, entityT, statsT, modeT, distance);
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
		return new AttackInfo2(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance, info, infoV);
	}

	private int[][] info;

	private AttackInfo2(XEntity entity, Tile loc, Stats2 stats, AttackMode2 mode, XEntity entityT, Tile locT, Stats2 statsT, AttackMode2 modeT,
			int distance, int[][] info, String... infos)
	{
		super(entity, loc, stats, mode, entityT, locT, statsT, modeT, distance, infos);
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