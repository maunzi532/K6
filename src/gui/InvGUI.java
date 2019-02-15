package gui;

public interface InvGUI
{
	void itemView(int invID, int x, int y1, int index);

	default void onClickItem(int invID, int num, int xi, int yi){}
}