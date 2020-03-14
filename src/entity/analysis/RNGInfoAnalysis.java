package entity.analysis;

import java.util.*;
import java.util.stream.*;

public class RNGInfoAnalysis<Divider extends RNGDivider>
{
	private Divider start;
	private List<Divider> stack;
	private List<Integer> snum;
	private List<RNGOutcome> outcomes;

	public RNGInfoAnalysis(Divider start)
	{
		this.start = start;
		stack = new ArrayList<>();
		snum = new ArrayList<>();
		outcomes = new ArrayList<>();
	}

	public RNGInfoAnalysis<Divider> create()
	{
		stack.add(start);
		snum.add(0);
		while(stack.size() > 0)
		{
			current().build();
			if(current().paths.isEmpty())
				outcomes.add(current().asOutcome());
			next();
		}
		return this;
	}

	private Divider current()
	{
		return stack.get(stack.size() - 1);
	}

	private void next()
	{
		while(snum.get(snum.size() - 1) >= current().paths.size())
		{
			stack.remove(stack.size() - 1);
			snum.remove(snum.size() - 1);
			if(stack.size() <= 0)
				return;
		}
		stack.add((Divider) current().paths.get(snum.get(snum.size() - 1)));
		snum.set(snum.size() - 1, snum.get(snum.size() - 1) + 1);
		snum.add(0);
	}

	public Divider getStart()
	{
		return start;
	}

	public List<RNGOutcome> outcomes()
	{
		return outcomes;
	}

	public List<RNGOutcome> outcomes2()
	{
		return outcomes.stream().collect(Collectors.groupingBy(e -> e.compareText))
				.values().stream().map(RNGOutcome::new).collect(Collectors.toList());
	}
}