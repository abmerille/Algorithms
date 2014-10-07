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
    int k;
    Map<String, Person> graph;
    
    public MatchingGraph(String fileName) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String[] line = reader.readLine().split(":");
        String[] people = line[1].split(",");
        n = people.length;
        maxFlow = n;
        graph = new HashMap<>((n * 2) + 2);
        graph.put("start", new Person("start", (n * 2)));      
        graph.put("sink", new Person("sink", (n * 2)));        
        
        //first person and ranks
        graph.put(line[0], new Person(line[0], n));
        graph.get("start").addEdge(line[0]);
        for(int j = 0; j < n; j++)
        {
            graph.put(people[j], new Person(people[j], n));     //add woman
            graph.get(line[0]).addRank(people[j], (j+1));
            graph.get(line[0]).addRank("sink", 0);
            graph.get(people[j]).addEdge("sink");
        }
        
        //initialize men
        while(!(line = reader.readLine().split(":"))[0].equals(""))
        {
            //line = reader.readLine().split(":");
            people = line[1].split(",");
            graph.put(line[0], new Person(line[0], n));         //add man
            graph.get("start").addEdge(line[0]);
            for(int j = 0; j < n; j++)
            {
                graph.get(line[0]).addRank(people[j], (j+1));
                graph.get(line[0]).addRank("sink", 0);

            }         
        }
        
        //initialize women
        for(int i = 0; i < n; i++)
        {
            line = reader.readLine().split(":");
            people = line[1].split(",");
            for(int j = 0; j < n; j++)
            {
                graph.get(line[0]).addRank(people[j], (j+1));
            }           
        }
    }
    
    public void computMatches()
    {
        k = 0;
        while(currFlow != maxFlow)
        {
            k++;
            for(Map.Entry<String, Person> entry : graph.entrySet())
            {
                if(!(entry.getKey().equals("sink") || entry.getKey().equals("start")))
                {
                    String name = entry.getKey();
                    Person person = entry.getValue();
                    Person otherPerson = graph.get(person.getNameByRank(k));
                    if(otherPerson.getRank(name) <= k)
                    {
                        //if man ad edge to man
                        if(person.getEdge("sink") == 0)
                        {
                            person.addEdge(otherPerson.name);
                        }
                        //if woman add edge to man
                        else
                        {
                            otherPerson.addEdge(name);
                        }
                        person.setMatch(otherPerson.name);
                        otherPerson.setMatch(name);
      
                        updategraph();
                        if(currFlow == maxFlow)
                        {
                            break;
                        }
                    }
                }
            }
        }
        for(Map.Entry<String, Person> entry : graph.entrySet())
        {
            if(!(entry.getKey().equals("sink") || entry.getKey().equals("start")))
            {
            System.out.println(entry.getKey() + ": " + entry.getValue().getMatch() + entry.getValue().getRank(entry.getValue().getMatch()));
            }
        }
    }
    
    private void augmentGraph(Person female)
    {
        //look for path from femal to male, etc to sink
        //if found? return true? or pass in male that lead here to reverse path
    }
    
    private void updategraph()
    {
        Person start = graph.get("start");
        for(Map.Entry<String, Integer> entry : start.getAllEdges().entrySet())
        {
            String maleName = entry.getKey();
            int edge = entry.getValue();
            if(edge == 1)
            {
                Person male = graph.get(maleName);
                for(Map.Entry<String, Integer> femaleEntry : male.getAllEdges().entrySet())
                {
                    String femaleName = femaleEntry.getKey();
                    int fEdge = femaleEntry.getValue();
                    if(fEdge == 1 && (graph.get(femaleName).getEdge("sink") == 1))
                    {
                        graph.get(femaleName).reverseEdge("sink");
                        graph.get(femaleName).addEdge(maleName);
                        male.reverseEdge(femaleName);
                        start.reverseEdge(maleName);
                        currFlow++;
                    }
                    else if(fEdge == -1)
                    {
                        //augmentgraph(female)
                        //reversepath if found to sink
                    }
                }
            }
        }
    }
}
