package logic.gui;

public interface InvGUI
{
	void setTargeted(CTile targeted);

	void itemView(int invID, int x, int y1, int index);

	void setTile(CTile tile, GuiTile guiTile);

	void setTile(CTile tile);

	void onTarget(int invID, int num, int xi, int yi, CTile cTile);

	default void onMissedTarget(int invID)
	{
		setTargeted(CTile.NONE);
	}

	default void onClickItem(int invID, int num, int xi, int yi){}
}