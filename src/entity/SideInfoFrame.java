package entity;

import entity.*;
import java.util.function.*;

public interface SideInfoFrame
{
	void clearSideInfo();

	void setSideInfo(SideInfo sideInfo, boolean r);

	void sidedInfo(XCharacter character, Function<? super XCharacter, SideInfo> function);

	void sidedInfo(XCharacter character, SideInfo sideInfo);

	void sidedInfo(XCharacter e1, XCharacter e2, Function<? super XCharacter, SideInfo> function);

	void sidedInfo(XCharacter e1, SideInfo s1, XCharacter e2, SideInfo s2);
}