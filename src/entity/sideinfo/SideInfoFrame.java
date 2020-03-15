package entity.sideinfo;

import entity.*;
import system2.*;

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

	private boolean isInverted(XCharacter e1, XCharacter e2)
	{
		return (!(e1.team() == CharacterTeam.HERO) && e2.team() == CharacterTeam.HERO) != xhR;
	}

	public void sidedInfo(XCharacter e1, XCharacter e2)
	{
		boolean inverted = isInverted(e1, e2);
		l0.setSideInfo((inverted ? e2 : e1).standardSideInfo());
		r0.setSideInfo((inverted ? e1 : e2).standardSideInfo());
	}

	public void attackInfo(AttackInfo aI)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		l0.setSideInfo(new SideInfo(aI.getEntity(inverted), aI.getEntity(inverted).sideImage(),
				aI.getEntity(inverted).statBarX1(aI.getSideInfoX1T(inverted)), aI.getSideInfos(inverted)));
		r0.setSideInfo(new SideInfo(aI.getEntity(!inverted), aI.getEntity(!inverted).sideImage(),
				aI.getEntity(!inverted).statBarX1(aI.getSideInfoX1T(!inverted)), aI.getSideInfos(!inverted)));
	}

	public void setSideInfoXH(SideInfo sideInfo, XCharacter e1)
	{
		if((e1.team() == CharacterTeam.HERO) != xhR)
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