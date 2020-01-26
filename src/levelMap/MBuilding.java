package levelMap;

import building.transport.*;
import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import java.io.*;
import javafx.scene.image.*;

public interface MBuilding extends DoubleInv
{
	Image IMAGE = new Image("H.png");

	void remove();

	default void productionPhase(LevelMap levelMap){}

	default void afterProduction(){}

	default void transportPhase(LevelMap levelMap){}

	default void afterTransport(){}

	void loadConnect(LevelMap levelMap);

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException;
}