package logic.gui;

import inv.ItemView;

public interface InvGUI
{
	void itemView(int x, int y1, ItemView view);

	void onClickItem(int invID, int num);
}