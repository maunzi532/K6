package building.transport;

import item.*;
import java.util.*;

public record PossibleTransport(Item item, DoubleInv from, DoubleInv to, int priorityFrom, int priorityTo)
{
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PossibleTransport that)) return false;
		return item.equals(that.item) &&
				from.equals(that.from) &&
				to.equals(that.to);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(item, from, to);
	}
}