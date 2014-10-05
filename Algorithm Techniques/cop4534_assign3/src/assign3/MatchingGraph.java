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
import java.io.IOException;
import java.util.Arrays;

/**
 *use adjaceny matrix
 * start with no edges (only edges out of s and in to t)
 *for k=1 .. N
 * {
 *  add edge to residualgraph where one side k and other <=k
 *  find flow, increase flow
 *  if(flow ==N done)
 * }
 * @author Adam
 */
public class MatchingGraph {
    Node start;
    Node sink;
    int maxFlow;
    int n;
    
    public MatchingGraph(String fileName) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String[] line = reader.readLine().split(":");
        String[] people = line[1].split(",");
        n = people.length;
        maxFlow = 2 * n;
        start = new Node("start", maxFlow);
        sink = new Node("sink", maxFlow);
    }
    
}
