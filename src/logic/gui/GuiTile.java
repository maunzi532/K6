package logic.gui;

import item.view.*;
import text.*;

public final class GuiTile
{
	public static final GuiTile EMPTY = new GuiTile((XText) null, null, false, null);

	public final XText text;
	public final String imageName;
	public final boolean flipped;
	public final String color;
	public final int left;
	public final int up;

	public GuiTile(XText text, String imageName, boolean flipped, String color)
	{
		this.text = text;
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		left = 0;
		up = 0;
	}

	public GuiTile(String text, String imageName, boolean flipped, String color)
	{
		this.text = new ArgsText(text);
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		left = 0;
		up = 0;
	}

	public GuiTile(XText text)
	{
		this.text = text;
		imageName = null;
		flipped = false;
		color = null;
		left = 0;
		up = 0;
	}

	public GuiTile(String text)
	{
		this.text = new ArgsText(text);
		imageName = null;
		flipped = false;
		color = null;
		left = 0;
		up = 0;
	}

	public GuiTile(XText text, String imageName, boolean flipped, String color, int right, int down)
	{
		this.text = text;
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		left = right - 1;
		up = down - 1;
	}

	public GuiTile(String text, String imageName, boolean flipped, String color, int right, int down)
	{
		this.text = new ArgsText(text);
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		left = right - 1;
		up = down - 1;
	}

	public GuiTile(GuiTile copy, int right, int down)
	{
		text = copy.text;
		imageName = copy.imageName;
		flipped = copy.flipped;
		color = copy.color;
		left = right - 1;
		up = down - 1;
	}

	public static GuiTile[] cast(GuiTile guiTile)
	{
		return new GuiTile[]{guiTile};
	}

	public static GuiTile[] textView(XText text)
	{
		return new GuiTile[]{new GuiTile(text)};
	}

	public static GuiTile[] textView(String text)
	{
		return new GuiTile[]{new GuiTile(new ArgsText(text))};
	}

	public static GuiTile[] itemViewView(ItemView itemView)
	{
		return new GuiTile[]
				{
						new GuiTile(itemView.currentWithLimit()),
						new GuiTile((XText) null, itemView.item.image(), false, null)
				};
	}
}