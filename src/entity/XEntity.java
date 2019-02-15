package entity;

import geom.hex.*;
import javafx.scene.image.*;

public class XEntity
{
	public static final Image IMAGE = new Image("S.png");

	public Hex location;
	public VisibleReplace replace;

	public XEntity(Hex location)
	{
		this.location = location;
	}

	public boolean isAllied(XEntity other)
	{
		return true;
	}

	public boolean visible()
	{
		return replace == null || replace.finished();
	}
}