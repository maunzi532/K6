package entity.analysis;

import java.util.*;

public abstract class RNGInfoAnalysis<Divider extends RNGDivider>
{
	private Divider start;
	private List<Divider> stack;
	private List<Integer> snum;

	public RNGInfoAnalysis(Divider start)
	{
		this.start = start;
		stack = new ArrayList<>();
		snum = new ArrayList<>();
	}

	public void create()
	{
		stack.add(start);
		snum.add(0);
		while(stack.size() > 0)
		{
			current().build();
			next();
		}
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
	}

	public Divider getStart()
	{
		return start;
	}
}