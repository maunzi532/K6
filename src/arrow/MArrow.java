package arrow;

import geom.hex.*;
import javafx.scene.image.*;

public interface MArrow
{
	boolean tick();

	boolean finished();

	boolean isVisible(Hex mid, int range);

	boolean showArrow();

	boolean showShine();

	boolean showTransport();

	DoubleHex visualStart();

	DoubleHex visualEnd();

	double[] getShine();

	DoubleHex[] getArrowPoints();

	DoubleHex currentTLocation();

	Image transported();
}