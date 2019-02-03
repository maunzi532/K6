package inv;

import java.util.*;

public class ItemTransportInfo
{
	public final List<DoubleInv> provide;
	public final List<DoubleInv> require;

	public ItemTransportInfo()
	{
		provide = new ArrayList<>();
		require = new ArrayList<>();
	}

	public List<PossibleTransport> getTransports(Item item, Map<PossibleTransport, Integer> lastTransported)
	{
		List<PossibleTransport> re = new ArrayList<>();
		for(DoubleInv provided : provide)
		{
			for(DoubleInv required : require)
			{
				re.add(new PossibleTransport(item, provided, required,
						provided.outputPriority(), required.inputPriority(), lastTransported));
			}
		}
		return re;
	}
}