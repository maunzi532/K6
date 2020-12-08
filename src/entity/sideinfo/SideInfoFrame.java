package entity.sideinfo;

import arrow.*;
import entity.*;
import system.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setStandardSideInfo(XCharacter character);

	void sidedInfo(XCharacter e1, XCharacter e2);

	void setAttackInfoSideInfo(AttackCalc aI);

	void setAllyInfoSideInfo(AllyCalc aI);

	void setAttackSideInfo(AttackCalc aI, StatBar s1, StatBar s2);

	void setTextSideInfo(XCharacter character, CharSequence text, boolean r);
}