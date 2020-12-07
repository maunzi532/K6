package entity.sideinfo;

import arrow.*;
import entity.*;
import system.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setStandardSideInfo(XCharacter character);

	void sidedInfo(XCharacter e1, XCharacter e2);

	void setAttackInfoSideInfo(AttackCalc4 aI);

	void setAllyInfoSideInfo(AllyCalc4 aI);

	void setAttackSideInfo(AttackCalc4 aI, StatBar s1, StatBar s2);

	void setTextSideInfo(XCharacter character, CharSequence text, boolean r);
}