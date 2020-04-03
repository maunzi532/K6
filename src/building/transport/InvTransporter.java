package building.transport;

import doubleinv.*;
import item.*;
import item.inv.*;
import java.util.*;

public final class InvTransporter
{
	private List<? extends DoubleInv> providingTargets;
	private List<? extends DoubleInv> receivingTargets;
	private int amount;
	private List<PossibleTransport> transportHistory;

	public InvTransporter(List<? extends DoubleInv> providingTargets, List<? extends DoubleInv> receivingTargets, int amount)
	{
		this.providingTargets = providingTargets;
		this.receivingTargets = receivingTargets;
		this.amount = amount;
		transportHistory = new ArrayList<>();
	}

	public InvTransporter(List<? extends DoubleInv> providingTargets, List<? extends DoubleInv> receivingTargets, int amount, List<PossibleTransport> transportHistory)
	{
		this.providingTargets = providingTargets;
		this.receivingTargets = receivingTargets;
		this.amount = amount;
		this.transportHistory = transportHistory;
	}

	public List<PossibleTransport> transportHistory()
	{
		return transportHistory;
	}

	public Optional<PossibleTransport> transport()
	{
		Map<Item, ItemTransportInfo> possibleItems = new HashMap<>();
		for(DoubleInv inv : providingTargets)
		{
			Inv providingInv = inv.inv(TradeDirection.GIVE);
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
			Inv receivingInv = inv.inv(TradeDirection.TAKE);
			for(Map.Entry<Item, ItemTransportInfo> entry : possibleItems.entrySet())
			{
				if(receivingInv.canAdd(new ItemStack(entry.getKey(), amount), false))
				{
					entry.getValue().require.add(inv);
				}
			}
		}
		List<PossibleTransport> transports = new ArrayList<>();
		possibleItems.forEach((item, info) -> transports.addAll(info.getTransports(item)));
		return transports.stream().max(Comparator.comparingInt((PossibleTransport e) -> e.priorityFrom() + e.priorityTo())
				.thenComparingInt((PossibleTransport e) -> -transportHistory.indexOf(e)));
	}

	public void doTheTransport(PossibleTransport theTransport)
	{
		transportHistory.remove(theTransport);
		transportHistory.add(theTransport);
		ItemStack itemStack = new ItemStack(theTransport.item(), amount);
		theTransport.from().inv(TradeDirection.GIVE).give(itemStack, false);
		theTransport.to().inv(TradeDirection.TAKE).add(itemStack, false);
	}
}