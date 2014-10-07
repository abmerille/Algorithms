/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author AdamM
 */
public class Assign3Main{
    //adjacency list of persons where each person can be passed a name that is hashed (bucketsize = N) to rank
    public static List<String> files;
    
    public static void main(String[] args)
    {
        files = getArgs(args);
 
        if(files != null)
        {
            try{

                for(String fileName : files)
                {
                    MatchingGraph graph = new MatchingGraph(fileName);
                    graph.computMatches();
                }
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
