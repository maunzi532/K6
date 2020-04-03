package building.transport;

import doubleinv.*;
import item.*;
import java.util.*;

public final class ItemTransportInfo
{
	public final List<DoubleInv> provide;
	public final List<DoubleInv> require;

	public ItemTransportInfo()
	{
		provide = new ArrayList<>();
		require = new ArrayList<>();
	}

	public List<PossibleTransport> getTransports(Item item)
	{
		List<PossibleTransport> re = new ArrayList<>();
		for(DoubleInv provided : provide)
		{
			for(DoubleInv required : require)
			{
				re.add(new PossibleTransport(item, provided, required,
						provided.priority(TradeDirection.GIVE), required.priority(TradeDirection.TAKE)));
			}
		}
		return re;
	}
}