package entity.sideinfo;

import arrow.*;
import entity.*;
import javafx.scene.paint.*;
import logic.*;
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

	public void setStandardSideInfo(XCharacter character, ColorScheme colorScheme)
	{
		if((character.team() == CharacterTeam.HERO) != xhR)
		{
			l0.setSideInfo(standardSideInfo(character, colorScheme));
			r0.setSideInfo(null);
		}
		else
		{
			l0.setSideInfo(null);
			r0.setSideInfo(standardSideInfo(character, colorScheme));
		}
	}

	public void sidedInfo(XCharacter e1, XCharacter e2, ColorScheme colorScheme)
	{
		boolean inverted = isInverted(e1, e2);
		l0.setSideInfo(standardSideInfo(inverted ? e2 : e1, colorScheme));
		r0.setSideInfo(standardSideInfo(inverted ? e1 : e2, colorScheme));
	}

	private SideInfo standardSideInfo(XCharacter character, ColorScheme colorScheme)
	{
		StatBar statBar = new StatBar(colorScheme.color(character.team().healthBarColor), Color.BLACK, Color.WHITE,
				character.stats().getVisualStat(0), character.stats().getMaxVisualStat(0));
		return new SideInfo(character, character.sideImage(), statBar, character.stats().sideInfoText());
	}

	public void setAttackSideInfo(AttackInfo aI, ColorScheme colorScheme)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		l0.setSideInfo(attackSideInfo(aI.getEntity(inverted), aI.getSideInfoX1T(inverted), aI.getSideInfos(inverted), colorScheme));
		r0.setSideInfo(attackSideInfo(aI.getEntity(!inverted), aI.getSideInfoX1T(!inverted), aI.getSideInfos(!inverted), colorScheme));
	}

	private SideInfo attackSideInfo(XCharacter character, String x1Text, String[] text2, ColorScheme colorScheme)
	{
		StatBar statBar = new StatBarX1(colorScheme.color(character.team().healthBarColor), Color.BLACK, Color.WHITE,
				character.stats().getVisualStat(0), character.stats().getMaxVisualStat(0), x1Text);
		return new SideInfo(character, character.sideImage(), statBar, text2);
	}

	private boolean isInverted(XCharacter e1, XCharacter e2)
	{
		return (!(e1.team() == CharacterTeam.HERO) && e2.team() == CharacterTeam.HERO) != xhR;
	}
}