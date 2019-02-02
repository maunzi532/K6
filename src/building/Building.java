package building;

import arrow.*;
import javafx.scene.image.*;

public interface Building
{
	Image IMAGE = new Image("H.png");

	default void productionPhase(CanAddArrows canAddArrows){}

	default void afterProduction(){}

	default void transportPhase(CanAddArrows canAddArrows){}

	default void afterTransport(){}
}