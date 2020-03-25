package entity.sideinfo;

import arrow.*;
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

	public void clearSideInfo()
	{
		l0.setSideInfo(null);
		r0.setSideInfo(null);
	}

	public void setStandardSideInfo(XCharacter character)
	{
		if((character.team() == CharacterTeam.HERO) != xhR)
		{
			l0.setSideInfo(standardSideInfo(character));
			r0.setSideInfo(null);
		}
		else
		{
			l0.setSideInfo(null);
			r0.setSideInfo(standardSideInfo(character));
		}
	}

	public void sidedInfo(XCharacter e1, XCharacter e2)
	{
		boolean inverted = isInverted(e1, e2);
		l0.setSideInfo(standardSideInfo(inverted ? e2 : e1));
		r0.setSideInfo(standardSideInfo(inverted ? e1 : e2));
	}

	private SideInfo standardSideInfo(XCharacter character)
	{
		StatBar statBar = new StatBar(character.team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text",
				character.stats().getVisualStat(0), character.stats().getMaxVisualStat(0), "");
		return new SideInfo(character, character.sideImageName(), statBar, character.stats().sideInfoText());
	}

	public void setAttackSideInfo(AttackInfo aI)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		l0.setSideInfo(attackSideInfo(aI.getEntity(inverted), aI.getSideInfoX1T(inverted), aI.getSideInfos(inverted)));
		r0.setSideInfo(attackSideInfo(aI.getEntity(!inverted), aI.getSideInfoX1T(!inverted), aI.getSideInfos(!inverted)));
	}

	private SideInfo attackSideInfo(XCharacter character, String x1Text, String[] text2)
	{
		StatBar statBar = new StatBar(character.team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text",
				character.stats().getVisualStat(0), character.stats().getMaxVisualStat(0), x1Text);
		return new SideInfo(character, character.sideImageName(), statBar, text2);
	}

	private boolean isInverted(XCharacter e1, XCharacter e2)
	{
		return (!(e1.team() == CharacterTeam.HERO) && e2.team() == CharacterTeam.HERO) != xhR;
	}
}