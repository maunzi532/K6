package item;

public interface Item
{
	int num();

	CharSequence name();

	String image();

	CharSequence info();

	int stackLimit();

	boolean ghost();
}