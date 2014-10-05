/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign3;

import java.util.LinkedList;
import java.util.List;

/**
 * use adjaceny matrix
 * start with no edges (only edges out of s and in to t)
 *for k=1 .. N
 * {
 *  add edge to residualgraph where one side k and other <=k
 *  find flow, increase flow
 *  if(flow ==N done)
 * }
 * @author AdamM
 */
public class Assign3Main{
    //adjacency list of persons where each person can be passed a name that is hashed (bucketsize = N) to rank
    public static List<String> files;
    
    public static void main(String[] args)
    {
        files = getArgs(args);
        
        for(int i =0; i < files.size(); i++)
        {
            
        }
        
    }
    
    public static LinkedList<String> getArgs(String[] args)
    {
        LinkedList<String> argList = new LinkedList<>();
        String arg = args[0];
        if(arg.contains(".txt"))
        {
            argList.add(arg);
        }
        else
        {
            System.err.println("Error: input files should end in .txt");
        }
        return argList;
    }
}
