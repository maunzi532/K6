package logic;

import building.*;
import entity.*;
import levelMap.*;

public interface MenuTargets
{
	XEntity getEntity();

	Building getBuilding();

	FloorTile getFloorTile();
}