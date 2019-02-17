package gui;

import javafx.scene.image.Image;

public interface InvGUI
{
	Image ARROW = new Image("Arrow.png");

	void setTargeted(CTile targeted);

	void itemView(int invID, int x, int y1, int index);

	void setTile(CTile tile, GuiTile guiTile);

	void setTile(CTile tile);

	void onTarget(int invID, int num, int xi, int yi, CTile cTile);

	default void onClickItem(int invID, int num, int xi, int yi){}
}