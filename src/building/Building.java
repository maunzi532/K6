package building;

import arrow.*;
import hex.*;
import javafx.scene.image.*;

public interface Building
{
	Image IMAGE = new Image("H.png");

	Hex location();

	default void productionPhase(CanAddArrows canAddArrows){}

	default void afterProduction(){}

	default void transportPhase(CanAddArrows canAddArrows){}

	default void afterTransport(){}
}