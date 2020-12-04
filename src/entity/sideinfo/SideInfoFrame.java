package entity.sideinfo;

import arrow.*;
import entity.*;
import statsystem.*;
import system4.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setStandardSideInfo(XCharacter character);

	void sidedInfo(XCharacter e1, XCharacter e2);

	void setAttackSideInfo(AttackInfo aI);

	void setAttackInfoSideInfo(AttackCalc4 aI);

	void setAttackSideInfo(AttackCalc4 aI, StatBar s1, StatBar s2);

	void setTextSideInfo(XCharacter character, CharSequence text, boolean r);
}