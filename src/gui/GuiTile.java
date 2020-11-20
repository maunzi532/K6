package gui;

import item.view.*;
import item4.*;

public final class GuiTile
{
	public static final GuiTile EMPTY = new GuiTile(null, null, false, null);

	public final CharSequence text;
	public final String imageName;
	public final boolean flipped;
	public final String color;
	public final int left;
	public final int up;

	public GuiTile(CharSequence text, String imageName, boolean flipped, String color)
	{
		this.text = text;
		this.imageName = imageName;
		this.flipped = flipped;
		this.color = color;
		left = 0;
		up = 0;
	}

	public GuiTile(CharSequence text)
	{
		this.text = text;
		imageName = null;
		flipped = false;
		color = null;
		left = 0;
		up = 0;
	}

	public GuiTile(CharSequence text, String imageName, boolean flipped, String color, int right, int down)
	{
		this.text = text;
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

	public static GuiTile[] textView(CharSequence text)
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

	public static GuiTile[] itemViewView(NumberedStack4 stack)
	{
		return new GuiTile[]
				{
						new GuiTile(stack.viewText()),
						new GuiTile(null, stack.item().image(), false, null)
				};
	}
}