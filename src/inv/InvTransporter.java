package inv;

import java.util.*;

public class InvTransporter
{
	private List<DoubleInv> providingTargets;
	private List<DoubleInv> receivingTargets;
	private int amount;
	private Map<PossibleTransport, Integer> lastTransported;
	private int transportNumber;

	public InvTransporter(List<DoubleInv> providingTargets, List<DoubleInv> receivingTargets)
	{
		this.providingTargets = providingTargets;
		this.receivingTargets = receivingTargets;
		amount = 1;
		lastTransported = new HashMap<>();
	}

	public Optional<PossibleTransport> transport()
	{
		Map<Item, ItemTransportInfo> possibleItems = new HashMap<>();
		for(DoubleInv inv : providingTargets)
		{
			Inv2 inputInv = inv.inputInv();
			for(Item item : inputInv.getItemTypes())
			{
				if(inputInv.maxDecrease(item) >= amount)
				{
					if(!possibleItems.containsKey(item))
					{
						possibleItems.put(item, new ItemTransportInfo());
					}
					possibleItems.get(item).provide.add(inv);
				}
			}
		}
		for(DoubleInv inv : receivingTargets)
		{
			Inv2 outputInv = inv.outputInv();
			if(outputInv.isSpecificLimits())
			{
				for(Item item : outputInv.getItemTypes())
				{
					if(possibleItems.containsKey(item) && outputInv.maxIncrease(item, amount) >= amount)
					{
						possibleItems.get(item).require.add(inv);
					}
				}
			}
			else
			{
				for(Item item : possibleItems.keySet())
				{
					if(outputInv.maxIncrease(item, amount) >= amount)
					{
						possibleItems.get(item).require.add(inv);
					}
				}
			}
		}
		List<PossibleTransport> transports = new ArrayList<>();
		possibleItems.forEach((item, info) -> transports.addAll(info.getTransports(item, lastTransported)));
		return transports.stream().max(Comparator.comparingInt((PossibleTransport e) -> e.priorityFrom + e.priorityTo)
				.thenComparingInt((PossibleTransport e) -> -Optional.ofNullable(e.lastTransported.get(e)).orElse(-1)));
	}

	public void doTheTransport(PossibleTransport theTransport)
	{
		lastTransported.put(theTransport, transportNumber);
		transportNumber++;
		theTransport.from.decrease(theTransport.item, amount);
		theTransport.to.increase(theTransport.item, amount);
	}
}