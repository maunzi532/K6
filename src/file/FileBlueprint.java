package file;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class FileBlueprint
{
	public final BlueprintNode startNode;

	public FileBlueprint(Class resultType, String resource)
	{
		String rawData;
		try
		{
			rawData = new String(Thread.currentThread().getContextClassLoader().getResourceAsStream(resource).readAllBytes());
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		System.out.println(rawData);
		ArrayDeque<BlueprintNode> deque = new ArrayDeque<>();
		startNode = new BlueprintNode(resultType.getSimpleName());
		deque.add(startNode);
		for(String line : rawData.lines().collect(Collectors.toList()))
		{
			String l1 = line.stripLeading();
			int depth = line.length() - l1.length() + 1;
			BlueprintNode node = new BlueprintNode(l1);
			while(deque.size() < depth)
			{
				BlueprintNode empty = new BlueprintNode("");
				deque.getLast().add(empty);
				deque.add(empty);
			}
			while(deque.size() > depth)
			{
				deque.removeLast();
			}
			deque.getLast().add(node);
			deque.addLast(node);
		}
	}
}