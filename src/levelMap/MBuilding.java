package levelMap;

import com.fasterxml.jackson.jr.ob.comp.*;
import geom.f1.*;
import item.*;
import java.io.*;
import javafx.scene.image.*;

public interface MBuilding
{
	Image IMAGE = new Image("H.png");

	Tile location();

	boolean active();

	void remove();

	default void productionPhase(LevelMap levelMap){}

	default void afterProduction(){}

	default void transportPhase(LevelMap levelMap){}

	default void afterTransport(){}

	default void claimFloor2(LevelMap levelMap){}

	<T extends ComposerBase> ObjectComposer<T> save(ObjectComposer<T> a1, ItemLoader itemLoader, TileType y1) throws IOException;
}