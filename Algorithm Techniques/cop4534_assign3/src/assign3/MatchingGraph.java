
package assign3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to create flow graph and compute matching 
 * @author Adam
 */
public class MatchingGraph {
    
    int maxFlow;
    int n;
    int currFlow = 0;
    int k;
    Map<String, Person> graph;
    Map<String, String> matches;
    String graphfile = "";
    
    public MatchingGraph(String fileName) throws FileNotFoundException, IOException
    {
        graphfile = fileName;
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String[] line = reader.readLine().split(":");
        String[] people = line[1].split(",");
        n = people.length;
        maxFlow = n;
        graph = new HashMap<>((n * 2) + 2);
        matches = new HashMap<>(n * 2);
        graph.put("start", new Person("start", (n * 2)));      
        graph.put("sink", new Person("sink", (n * 2)));        
        
        //first person and ranks
        graph.put(line[0], new Person(line[0], n));
        matches.put(line[0], "");
        graph.get("start").addEdge(line[0]);
        for(int j = 0; j < n; j++)
        {
            graph.put(people[j], new Person(people[j], n));     //add woman
            matches.put(people[j], "");
            graph.get(line[0]).addRank(people[j], (j+1));
            graph.get(line[0]).addRank("sink", 0);
            graph.get(people[j]).addEdge("sink");
        }
        
        //initialize men
        while(!(line = reader.readLine().split(":"))[0].equals(""))
        {
            people = line[1].split(",");
            graph.put(line[0], new Person(line[0], n));         //add man
            matches.put(line[0], "");
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
                        //if man
                        if(person.getEdge("sink") == 0)
                        {
                            person.addEdge(otherPerson.name);
                            otherPerson.addEdge(name);
                            otherPerson.reverseEdge(name);
                            if((!person.hasFlow) && (graph.get(otherPerson.name).getEdge("sink") == 1))
                            {
                                updateWithSink(person, otherPerson);
                                currFlow++;
                            }
                            else if((!person.hasFlow) && augmentGraph(otherPerson))
                            {       
                               updateWithAugment(person, otherPerson);
                                currFlow++;
                            }
                        }
                        //if woman 
                        else
                        {
                            otherPerson.addEdge(name);
                            person.addEdge(otherPerson.name);
                            person.reverseEdge(otherPerson.name);
                            if((!otherPerson.hasFlow) && (graph.get(person.name).getEdge("sink") == 1))
                            {
                                updateWithSink(otherPerson, person);
                                currFlow++;
                            }
                            
                            else if((!otherPerson.hasFlow) && augmentGraph(person))
                            {
                                updateWithAugment(otherPerson, person);
                                currFlow++;
                            }
                        }
                        
                        if(currFlow == maxFlow)
                        {
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("Everybody matched with top " + k + " preferences in " + graphfile +":");
        for(Map.Entry<String, Person> entry : graph.entrySet())
        {
            if(!(entry.getKey().equals("sink") || entry.getKey().equals("start")))
            {
            System.out.println(entry.getKey() + ": " + 
                    "matched to "+ matches.get(entry.getKey()) + 
                    " (rank "+entry.getValue().getRank(matches.get(entry.getKey())) +")");
            }
        }
    }
    
    private boolean augmentGraph(Person female)
    {
        //look for path from female to male to nextFemale to sink
        for(Map.Entry<String, Integer> entry : female.getAllEdges().entrySet())
        {
            String maleName = entry.getKey();
            Person nextMale = graph.get(maleName);
            if(!(maleName.equals("sink")) && (nextMale.getEdge(female.name) == -1))
            {
                for(Map.Entry<String, Integer> fEntry : nextMale.getAllEdges().entrySet())
                {
                    String femaleName = fEntry.getKey();
                    int fEdge = fEntry.getValue();
                    Person nextFemale = graph.get(femaleName);
                    if(fEdge == 1 && (nextFemale.getEdge("sink") == 1) 
                            && (nextFemale.getRank(maleName) <= k))
                    {
                        //reverse everything
                        graph.get(femaleName).reverseEdge("sink");
                        graph.get(femaleName).addEdge(maleName);
                        nextMale.reverseEdge(femaleName);
                        nextMale.reverseEdge(female.name);
                        
                        //Update matches
                        matches.put(maleName, femaleName);
                        matches.put(femaleName, maleName);

                        return true;                      
                    }
                }
            }
        }
        return false;
    }
    
    private void updateWithSink(Person male, Person female)
    {
        female.reverseEdge("sink");
        female.addEdge(male.name);
        male.reverseEdge(female.name);
        male.hasFlow = true;
        matches.put(male.name, female.name);
        matches.put(female.name, male.name);
    }
    
    public void updateWithAugment(Person male, Person female)
    {
        female.addEdge(male.name);
        male.reverseEdge(female.name);
        male.hasFlow = true;
        matches.put(male.name, female.name);
        matches.put(female.name, male.name);
    }
}
