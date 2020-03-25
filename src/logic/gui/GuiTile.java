package logic.gui;

import item.view.*;

public class GuiTile
{
	public static final GuiTile EMPTY = new GuiTile(null, null, false, null);

	public final String text;
	public final String imageName;
	public final boolean flipped;
	public final String color;
	public final int l;
	public final int u;

	public GuiTile(String text, String imageName, boolean flipped, String color)
	{
		this.text = text;
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		l = 0;
		u = 0;
	}

	public GuiTile(String text)
	{
		this.text = text;
		imageName = null;
		flipped = false;
		color = null;
		l = 0;
		u = 0;
	}

	public GuiTile(String text, String imageName, boolean flipped, String color, int r, int d)
	{
		this.text = text;
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		l = r - 1;
		u = d - 1;
	}

	public GuiTile(GuiTile copy, int r, int d)
	{
		text = copy.text;
		imageName = copy.imageName;
		flipped = copy.flipped;
		color = copy.color;
		l = r - 1;
		u = d - 1;
	}

	public static GuiTile[] cast(GuiTile guiTile)
	{
		return new GuiTile[]{guiTile};
	}

	public static GuiTile[] textView(String text)
	{
		return new GuiTile[]{new GuiTile(text)};
	}

	public static GuiTile[] itemViewView(ItemView itemView)
	{
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit()),
						new GuiTile(null, itemView.item.image(), false, null)
				};
	}
}