package vis.sideinfo;

import arrow.*;
import entity.*;
import entity.sideinfo.*;
import java.util.*;
import statsystem.*;
import vis.*;

public final class VisualSideInfoFrame implements SideInfoFrame
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

	private static SideInfo standardSideInfo(XCharacter character)
	{
		StatBar statBar = new StatBar(character.team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text",
				character.stats().currentHealth(), character.stats().maxHealth());
		return new SideInfo(character, character.sideImageName(), statBar, character.stats().sideInfoText(character.team().genericName));
	}

	@Override
	public void setAttackSideInfo(AttackInfo aI)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		AttackSide side1 = inverted ? AttackSide.TARGET : AttackSide.INITIATOR;
		AttackSide side2 = inverted ? AttackSide.INITIATOR : AttackSide.TARGET;
		l0.setSideInfo(attackSideInfo(aI.getEntity(side1), aI.getSideInfoChange(side1), aI.getSideInfos(side1)));
		r0.setSideInfo(attackSideInfo(aI.getEntity(side2), aI.getSideInfoChange(side2), aI.getSideInfos(side2)));
	}

	private static SideInfo attackSideInfo(XCharacter character, int change, CharSequence[] text2)
	{
		StatBar statBar = new StatBar(character.team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text",
				character.stats().currentHealth(), character.stats().maxHealth(), change);
		return new SideInfo(character, character.sideImageName(), statBar, text2);
	}

	private boolean isInverted(XCharacter e1, XCharacter e2)
	{
		return (e1.team() != CharacterTeam.HERO && e2.team() == CharacterTeam.HERO) != xhR;
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