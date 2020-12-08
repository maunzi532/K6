package gui;

import item.*;

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

	public static GuiTile[] itemStackView(NumberedStack stack)
	{
		return itemStackView(stack, false);
	}

	public static GuiTile[] itemStackView(NumberedStack stack, boolean active)
	{
		String color = active ? "gui.background.active" : null;
		return new GuiTile[]
				{
						new GuiTile(stack.item().name(), null, false, color),
						new GuiTile(stack.viewText(), null, false, color)
						//new GuiTile(stack.item().name(), stack.item().image(), false, color)
				};
	}
}