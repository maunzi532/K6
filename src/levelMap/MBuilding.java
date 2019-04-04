package levelMap;

import geom.f1.*;
import javafx.scene.image.*;

public interface MBuilding
{
	Image IMAGE = new Image("H.png");

	Tile location();

	default boolean active()
	{
		return true; //TODO
	}

	default void productionPhase(LevelMap levelMap){}

	default void afterProduction(){}

	default void transportPhase(LevelMap levelMap){}

	default void afterTransport(){}
}