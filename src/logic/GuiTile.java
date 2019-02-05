package logic;

import javafx.scene.image.*;
import javafx.scene.paint.*;

public class GuiTile
{
	public final String text;
	public final Image image;
	public final Color color;

	public GuiTile(String text, Image image, Color color)
	{
		this.text = text;
		this.image = image;
		this.color = color;
	}
}