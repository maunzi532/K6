package levelMap.importX;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import levelMap.*;

public class SavedImport
{
	public void importIntoMap(LevelMap levelMap)
	{
		try
		{
			byte[] bytes = Files.readAllBytes(new File("W").toPath());
			ByteBuffer sb = ByteBuffer.wrap(bytes);
			if(sb.get() != (byte) 0xA4 || sb.get() != (byte) 0xD2 || sb.get() != (byte) 0x83 || sb.get() != (byte) 0x9F)
				throw new RuntimeException("Wrong file");
			while(sb.hasRemaining())
			{
				levelMap.createTile(sb.get(), sb.get(), sb.get(), sb.get());
			}
		}catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}