package vis.sideinfo;

import arrow.*;
import entity.*;
import entity.sideinfo.*;
import java.util.*;
import system.*;
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
				character.currentHP(), character.maxHP());
		return new SideInfo(character, character.sideImageName(), statBar, character.sideInfoText(character.name1()));
	}

	/*@Override
	public void setAttackSideInfo(AttackInfo aI)
	{
		boolean inverted = isInverted(aI.entity, aI.entityT);
		AttackSide side1 = inverted ? AttackSide.TARGET : AttackSide.INITIATOR;
		AttackSide side2 = inverted ? AttackSide.INITIATOR : AttackSide.TARGET;
		l0.setSideInfo(attackSideInfo(aI.getEntity(side1), aI.getSideInfoChange(side1), aI.getSideInfos(side1)));
		r0.setSideInfo(attackSideInfo(aI.getEntity(side2), aI.getSideInfoChange(side2), aI.getSideInfos(side2)));
	}*/

	@Override
	public void setAttackInfoSideInfo(AttackCalc4 aI)
	{
		boolean inverted = isInverted(aI.aI.initiator(), aI.aI.target());
		SideInfo sideInfo1 = new SideInfo(aI.aI.initiator(), aI.aI.initiator().sideImageName(), aI.aI.initiator().hpBar(), aI.sideInfo1());
		SideInfo sideInfo2 = new SideInfo(aI.aI.target(), aI.aI.target().sideImageName(), aI.aI.target().hpBar(), aI.sideInfo2());
		setWithInverted(inverted, sideInfo1, sideInfo2);
	}

	@Override
	public void setAllyInfoSideInfo(AllyCalc4 aI)
	{
		XCharacter character = aI.aI.character();
		XCharacter target = aI.aI.target();
		if(character == target)
		{
			if(xhR)
			{
				l0.setSideInfo(null);
				r0.setSideInfo(new SideInfo(character, character.sideImageName(), character.hpBar(), aI.sideInfoC()));
			}
			else
			{
				l0.setSideInfo(new SideInfo(character, character.sideImageName(), character.hpBar(), aI.sideInfoC()));
				r0.setSideInfo(null);
			}
		}
		else
		{
			boolean inverted = isInverted(character, target);
			SideInfo sideInfo1 = new SideInfo(character, character.sideImageName(), character.hpBar(), aI.sideInfo1());
			SideInfo sideInfo2 = new SideInfo(target, target.sideImageName(), target.hpBar(), aI.sideInfo2());
			setWithInverted(inverted, sideInfo1, sideInfo2);
		}
	}

	@Override
	public void setAttackSideInfo(AttackCalc4 aI, StatBar s1, StatBar s2)
	{
		boolean inverted = isInverted(aI.aI.initiator(), aI.aI.target());
		SideInfo sideInfo1 = new SideInfo(aI.aI.initiator(), aI.aI.initiator().sideImageName(), s1, aI.sideInfo1());
		SideInfo sideInfo2 = new SideInfo(aI.aI.target(), aI.aI.target().sideImageName(), s2, aI.sideInfo2());
		setWithInverted(inverted, sideInfo1, sideInfo2);
	}

	private static SideInfo attackSideInfo(XCharacter character, int change, CharSequence[] text2)
	{
		StatBar statBar = new StatBar(character.team().healthBarColor,
				"arrow.healthbar.background", "arrow.healthbar.text",
				character.currentHP(), character.maxHP(), change);
		return new SideInfo(character, character.sideImageName(), statBar, text2);
	}

	private boolean isInverted(XCharacter e1, XCharacter e2)
	{
		return (e1.team() != CharacterTeam.HERO && e2.team() == CharacterTeam.HERO) != xhR;
	}

	private void setWithInverted(boolean inverted, SideInfo sideInfo1, SideInfo sideInfo2)
	{
		if(inverted)
		{
			l0.setSideInfo(sideInfo2);
			r0.setSideInfo(sideInfo1);
		}
		else
		{
			l0.setSideInfo(sideInfo1);
			r0.setSideInfo(sideInfo2);
		}
	}

	@Override
	public void setTextSideInfo(XCharacter character, CharSequence text, boolean r)
	{
		if(r)
		{
			r0.setSideInfo(textSideInfo(character, text));
		}
		else
		{
			l0.setSideInfo(textSideInfo(character, text));
		}
	}

	private static SideInfo textSideInfo(XCharacter character, CharSequence text)
	{
		return new SideInfo(character, character.sideImageName(), null, text);
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