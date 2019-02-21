package levelMap;

import geom.hex.Hex;
import javafx.scene.image.Image;

public interface MBuilding
{
	Image IMAGE = new Image("H.png");

	Hex location();

	default void productionPhase(LevelMap levelMap){}

	default void afterProduction(){}

	default void transportPhase(LevelMap levelMap){}

	default void afterTransport(){}
}