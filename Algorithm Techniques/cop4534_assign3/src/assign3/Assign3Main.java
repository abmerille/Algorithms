
package assign3;

import java.util.LinkedList;
import java.util.List;

/**
 * Bipartite Matching
 * @author AdamM
 */
public class Assign3Main{
    
    public static List<String> files;
    
    public static void main(String[] args)
    {
        files = getArgs(args);
 
        if(files != null)
        {
            try{
                long firstStart = System.currentTimeMillis();
                for(String fileName : files)
                {
                    long startTime = System.currentTimeMillis();
                    MatchingGraph graph = new MatchingGraph(fileName);
                    graph.computMatches();
                    long endTime = System.currentTimeMillis();
                    System.out.println("Elapsed time: " +(endTime -startTime)+ "ms.\n");
                }
                long lastEnd = System.currentTimeMillis();
                System.out.println("Total Elapsed time: " +(lastEnd -firstStart)/1000 + "sec.");
                System.out.println("Computed in O(N^3) time for extra credit.\n");
            }
            catch(Exception e)
            {
                System.err.println("Error: Exception found: " + e);
            }
        }
    }
    
    public static LinkedList<String> getArgs(String[] args)
    {
        LinkedList<String> argList = new LinkedList<>();
        
        for(String arg : args)
        {
            if(arg.contains(".txt"))
            {
                argList.add(arg);
            }
            else
            {
                System.err.println("Error: input files should end in .txt");
                return null;
            }
        }
        return argList;
    }
}
