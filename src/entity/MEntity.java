package entity;

import arrow.*;
import geom.hex.Hex;
import javafx.scene.image.Image;

public interface MEntity
{
	Hex location();

	void setLocation(Hex location);

	Image getImage();

	boolean isVisible();

	void setReplacementArrow(MArrow arrow);
}