package inv;

import java.util.*;

public class PossibleTransport
{
	public final Item item;
	public final DoubleInv from;
	public final DoubleInv to;
	public final int priorityFrom;
	public final int priorityTo;
	public final Map<PossibleTransport, Integer> lastTransported;

	public PossibleTransport(Item item, DoubleInv from, DoubleInv to,
			int priorityFrom, int priorityTo,
			Map<PossibleTransport, Integer> lastTransported)
	{
		this.item = item;
		this.from = from;
		this.to = to;
		this.priorityFrom = priorityFrom;
		this.priorityTo = priorityTo;
		this.lastTransported = lastTransported;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof PossibleTransport)) return false;
		PossibleTransport that = (PossibleTransport) o;
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