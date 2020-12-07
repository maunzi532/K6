package vis;

import com.fasterxml.jackson.jr.stree.*;
import java.io.*;
import java.nio.file.*;
import levelmap.*;
import load.*;
import logic.*;
import system.*;

public class WorldLoader implements World
{
	private Path worldPath;
	private JrsObject teamData;
	private JrsObject levelData;
	private JrsObject suspendData;
	private SystemScheme systemScheme;

	public void loadFile(String file)
	{
		try
		{
			Path filePath = Path.of(file);
			JrsObject data = LoadHelper.startLoad(filePath);
			loadSystemScheme(data, filePath);
			loadLevel(data);
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void loadSystemScheme(JrsObject data, Path filePath)
	{
		try
		{
			if(data.get("WorldPath") != null)
			{
				Path worldPathNew = filePath.resolve(data.get("WorldPath").asText());
				if(worldPathNew != worldPath)
				{
					worldPath = worldPathNew;
					systemScheme = SystemScheme.load(LoadHelper.startLoad(worldPath.resolve("World")));
				}
			}
			else if(data.get("StartLevel") != null)
			{
				Path worldPathNew = filePath.getParent();
				if(worldPathNew != worldPath)
				{
					worldPath = worldPathNew;
					systemScheme = SystemScheme.load(data);
				}
			}
			else
			{
				throw new RuntimeException("Not a WorldFile, TeamFile or SuspendFile");
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void loadLevel(JrsObject data)
	{
		try
		{
			if(data.get("WorldFile") != null)
			{
				if(data.get("TurnCounter") != null)
				{
					teamData = null;
					levelData = null;
					suspendData = data;
				}
				else
				{
					teamData = data;
					levelData = LoadHelper.startLoad(worldPath.resolve(data.get("CurrentLevel").asText()));
					suspendData = null;
				}
			}
			else if(data.get("StartLevel") != null)
			{
				teamData = null;
				levelData = LoadHelper.startLoad(worldPath.resolve(data.get("StartLevel").asText()));
				suspendData = null;
			}
			else
			{
				throw new RuntimeException("Not a WorldFile, TeamFile or SuspendFile");
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void nextLevel(String teamSave, String nextLevel)
	{
		try
		{
			teamData = LoadHelper.startLoad(Path.of(teamSave));
			levelData = LoadHelper.startLoad(worldPath.resolve(nextLevel));
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public SystemScheme systemScheme()
	{
		return systemScheme;
	}

	@Override
	public LevelMap4 createLevel()
	{
		if(suspendData != null)
		{
			return LevelMap4.resume(suspendData, systemScheme);
		}
		else
		{
			LevelMap4 levelMap = LevelMap4.load(levelData, systemScheme);
			if(teamData != null)
			{
				levelMap.loadTeam(teamData, systemScheme);
				//levelMap.setEventPacks(EventPack.read(world, currentMap, scheme.setting("file.locale.level"), itemLoader, levelMap.y1));
			}
			return levelMap;
		}
	}
}