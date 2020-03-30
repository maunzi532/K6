package entity.sideinfo;

import entity.*;
import system2.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setStandardSideInfo(XCharacter character);

	void sidedInfo(XCharacter e1, XCharacter e2);

	void setAttackSideInfo(AttackInfo aI);
}