package logic.gui;

import inv.ItemView;

public interface InvGUI
{
	void itemView(int invID, int x, int y1, int index, ItemView view);

	void onClickItem(int invID, int num);
}