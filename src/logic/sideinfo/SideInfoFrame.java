package logic.sideinfo;

import arrow.*;
import entity.*;
import file.*;

public class SideInfoFrame
{
	private final SideInfoHolder l0;
	private final SideInfoHolder r0;

	public SideInfoFrame(SideInfoHolder l0, SideInfoHolder r0)
	{
		this.l0 = l0;
		this.r0 = r0;
	}

	public void setSideInfo(SideInfo sideInfoL, SideInfo sideInfoR)
	{
		l0.setSideInfo(sideInfoL);
		r0.setSideInfo(sideInfoR);
	}

	public void setSideInfoL(SideInfo sideInfoL)
	{
		l0.setSideInfo(sideInfoL);
	}

	public void setSideInfoR(SideInfo sideInfoR)
	{
		r0.setSideInfo(sideInfoR);
	}

	public void clearSideInfo()
	{
		l0.setSideInfo(null);
		r0.setSideInfo(null);
	}

	public void sidedInfo(XEntity e1, XEntity e2)
	{
		e1.location();
		e2.location();
		boolean inverted = false;
		l0.setSideInfo((inverted ? e2 : e1).standardSideInfo());
		r0.setSideInfo((inverted ? e1 : e2).standardSideInfo());
	}

	public void attackInfo(AttackInfo aI)
	{
		aI.entity.location();
		aI.entityT.location();
		boolean inverted = false;
		l0.setSideInfo(new SideInfo(aI.getEntity(inverted), 0, ImageLoader.getImage(aI.getStats(inverted).imagePath()),
				StatBar.forEntity(aI.getEntity(inverted)), aI.getSideInfos(inverted)));
		r0.setSideInfo(new SideInfo(aI.getEntity(!inverted), 0, ImageLoader.getImage(aI.getStats(!inverted).imagePath()),
				StatBar.forEntity(aI.getEntity(!inverted)), aI.getSideInfos(!inverted)));
	}

	public void setSideInfoXH(SideInfo sideInfo, boolean l)
	{
		if(l)
		{
			l0.setSideInfo(sideInfo);
			r0.setSideInfo(null);
		}
		else
		{
			l0.setSideInfo(null);
			r0.setSideInfo(sideInfo);
		}
	}
}