package entity.sideinfo;

import entity.*;
import statsystem.*;
import system4.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setStandardSideInfo(XCharacter character);

	void sidedInfo(XCharacter e1, XCharacter e2);

	void setAttackSideInfo(AttackInfo aI);

	void setAttackSideInfo(AttackCalc4 aI);

	void setTextSideInfo(XCharacter character, CharSequence text, boolean r);
}