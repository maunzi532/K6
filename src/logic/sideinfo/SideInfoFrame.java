package logic.sideinfo;

import arrow.*;
import entity.*;
import file.*;

public class SideInfoFrame
{
	private final SideInfoHolder l0;
	private final SideInfoHolder r0;
	public final boolean xhR;

	public SideInfoFrame(SideInfoHolder l0, SideInfoHolder r0)
	{
		this.l0 = l0;
		this.r0 = r0;
		xhR = false;
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

	private boolean isInverted(XEntity e1, XEntity e2)
	{
		return (!(e1 instanceof XHero) && e2 instanceof XHero) != xhR;
	}

	public void sidedInfo(XEntity e1, XEntity e2)
	{
		boolean inverted = isInverted(e1, e2);
		l0.setSideInfo((inverted ? e2 : e1).standardSideInfo());
		r0.setSideInfo((inverted ? e1 : e2).standardSideInfo());
	}

	public void attackInfo(AttackInfo aI)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		l0.setSideInfo(new SideInfo(aI.getEntity(inverted), 0, ImageLoader.getImage(aI.getStats(inverted).imagePath()),
				StatBarX1.forEntityX1(aI.getEntity(inverted), aI.getSideInfoX1T(inverted)), aI.getSideInfos(inverted)));
		r0.setSideInfo(new SideInfo(aI.getEntity(!inverted), 0, ImageLoader.getImage(aI.getStats(!inverted).imagePath()),
				StatBarX1.forEntityX1(aI.getEntity(!inverted), aI.getSideInfoX1T(!inverted)), aI.getSideInfos(!inverted)));
	}

	public void setSideInfoXH(SideInfo sideInfo, XEntity e1)
	{
		if((e1 instanceof XHero) != xhR)
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