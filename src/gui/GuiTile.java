package gui;

import javafx.scene.image.*;
import javafx.scene.paint.*;

public class GuiTile
{
	public static final GuiTile EMPTY = new GuiTile(null, null, null);

	public final String text;
	public final Image image;
	public final Color color;
	public final int l;
	public final int u;

	public GuiTile(String text, Image image, Color color)
	{
		this.text = text;
		this.image = image;
		this.color = color;
		l = 0;
		u = 0;
	}

	public GuiTile(String text)
	{
		this.text = text;
		image = null;
		color = null;
		l = 0;
		u = 0;
	}

	public GuiTile(String text, Image image, Color color, int r, int d)
	{
		this.text = text;
		this.image = image;
		this.color = color;
		l = r - 1;
		u = d - 1;
	}

	public GuiTile(GuiTile copy, int r, int d)
	{
		text = copy.text;
		image = copy.image;
		color = copy.color;
		l = r - 1;
		u = d - 1;
	}
}