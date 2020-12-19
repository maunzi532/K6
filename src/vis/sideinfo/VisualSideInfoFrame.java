package vis.sideinfo;

import entity.*;
import java.util.*;
import java.util.function.*;
import vis.vis.*;

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
	public void setSideInfo(SideInfo sideInfo, boolean r)
	{
		(r ? r0 : l0).setSideInfo(sideInfo);
	}

	@Override
	public void sidedInfo(XCharacter character, Function<? super XCharacter, SideInfo> function)
	{
		sidedInfo(character, function.apply(character));
	}

	@Override
	public void sidedInfo(XCharacter character, SideInfo sideInfo)
	{
		if((character.team() == CharacterTeam.HERO) != xhR)
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

	@Override
	public void sidedInfo(XCharacter e1, XCharacter e2, Function<? super XCharacter, SideInfo> function)
	{
		sidedInfo(e1, function.apply(e1), e2, function.apply(e2));
	}

	@Override
	public void sidedInfo(XCharacter e1, SideInfo s1, XCharacter e2, SideInfo s2)
	{
		boolean inverted = isInverted(e1, e2);
		l0.setSideInfo(inverted ? s2 : s1);
		r0.setSideInfo(inverted ? s1 : s2);
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