package visual1.sideinfo;

import arrow.*;
import entity.*;
import entity.sideinfo.*;
import java.util.*;
import system2.*;
import visual1.*;

public class VisualSideInfoFrame implements SideInfoFrame
{
	private final VisualSideInfo l0;
	private final VisualSideInfo r0;
	private final List<VisualSideInfo> viewers;
	private boolean xhR;

	public VisualSideInfoFrame(XGraphics graphics, boolean xhR)
	{
		l0 = new VisualSideInfo(graphics, false);
		r0 = new VisualSideInfo(graphics, true);
		viewers = List.of(l0, r0);
		this.xhR = xhR;
	}

	@Override
	public void clearSideInfo()
	{
		l0.setSideInfo(null);
		r0.setSideInfo(null);
	}

	@Override
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

	@Override
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

	@Override
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

	public void tick()
	{
		viewers.forEach(VisualSideInfo::tick);
	}

	public void draw(Scheme scheme)
	{
		viewers.forEach(viewer -> viewer.draw(scheme));
	}

	public double takeY2()
	{
		return r0.takeY2();
	}
}