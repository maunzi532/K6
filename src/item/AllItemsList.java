package item;

public interface AllItemsList
{
	Item getItem(String name);

	String saveItem(Item item);
}