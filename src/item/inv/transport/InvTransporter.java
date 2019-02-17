package item.inv.transport;

import item.*;
import item.inv.*;
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
			Inv providingInv = inv.outputInv();
			for(Item item : providingInv.providedItemTypesX())
			{
				if(providingInv.canGive(new ItemStack(item, amount), false))
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
			Inv receivingInv = inv.inputInv();
			for(Item item : possibleItems.keySet())
			{
				if(receivingInv.canAdd(new ItemStack(item, amount), false))
				{
					possibleItems.get(item).require.add(inv);
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
		ItemStack itemStack = new ItemStack(theTransport.item, amount);
		theTransport.from.outputInv().give(itemStack, false);
		theTransport.to.inputInv().add(itemStack, false);
	}
}