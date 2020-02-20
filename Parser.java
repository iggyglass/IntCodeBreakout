import java.nio.file.*; 
import java.io.*; 

// Static classes cannot be declared if not
// nested -- thus this class which is "static"
public final class Parser
{
    private Parser() {}

    public static long[] ParseCode(String code)
    {
        String[] result = code.split(",");
        long[] data = new long[result.length];

        for (int i = 0; i < result.length; i++)
        {
            data[i] = Long.parseLong(result[i]);
        }

        return data;
    }

    public static String GetFirstLine(String path) throws IOException
    {
        return Files.readAllLines(Paths.get(path)).get(0);
    }
}