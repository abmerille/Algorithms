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
import java.util.HashMap;
import java.util.Map;

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
    
    int maxFlow;
    int n;
    int currFlow = 0;
    Map<String, Person> graph;
    //Node[] graph;
    
    public MatchingGraph(String fileName) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String[] line = reader.readLine().split(":");
        String[] people = line[1].split(",");
        n = people.length;
        maxFlow = 2 * n;
        graph = new HashMap<>(maxFlow + 2);
        graph.put("start", new Person("start", maxFlow));      //start
        graph.put("sink", new Person("sink", maxFlow));        //sink
        
        graph.put(line[0], new Person(line[0], n));
        for(int j = 0; j < n; j++)
        {
            graph.put(people[j], new Person(people[j], n));
            graph.get(line[0]).addRank(people[j], (j+1));
        }
        
        //initialize men
        while(!(line = reader.readLine().split(":"))[0].equals(""))
        {
            //line = reader.readLine().split(":");
            people = line[1].split(",");
            graph.put(line[0], new Person(line[0], n));
            for(int j = 0; j < n; j++)
            {
                graph.put(people[j], new Person(people[j], n));
                graph.get(line[0]).addRank(people[j], (j+1));
            }
            
        }
        
        //reader.readLine();          //eat blank line
        
        //initialize women
        /*for(int i = 0; i < n; i++)
        {
            graph.put(line[0], new Node(line[0], n));
            for(int j = 0; j < n; j++)
            {
                graph.put(people[j], new Node(people[j], n));
            }
            line = reader.readLine().split(":");
            people = line[1].split(",");
        }*/
        
        System.out.println(graph.toString());
        for(Map.Entry<String, Person> person : graph.entrySet())
        {
            Person p = person.getValue();
            System.out.println(p.getAllRanks());
        }
    }
    
}
