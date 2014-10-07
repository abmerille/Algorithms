/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign3;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author AdamM
 */
public class Person extends Node
{
    private Map<String, Integer> ranks;
    private Map<Integer, String> nameByRank;
    //edges: -1 reverse, 0 none, 1 forward
    private Map<String, Integer> edges;    
    String match = "";
    
    public Person(String nm, int n) {
        super(nm, n);
        this.ranks = new HashMap<>(n);
        this.edges = new HashMap<>(n);
        this.nameByRank = new HashMap<>(n);
    }
    
    //used only for initialization
    public void addRank(String name, int r)
    {
        ranks.put(name, r);
        nameByRank.put(r, name);
        edges.put(name, 0);
    }
    
    public String getNameByRank(int i)
    {
        return nameByRank.get(i);
    }
    
    public int getRank(String name)
    {
        return ranks.get(name);
    }
    
    public String getAllRanks()
    {
        return ranks.toString();
    }
    
    public int getEdge(String name)
    {
        return edges.get(name);
    }
    
    public void addEdge(String name)
    {
        edges.put(name, 1);
    }
    
    public void reverseEdge(String name)
    {
        if(edges.get(name) == -1)
        {
            edges.put(name, 1);
        }
        
        else if(edges.get(name) == 1)
        {
            edges.put(name, -1);
        }
    }
    
    public Map<String, Integer> getAllEdges()
    {
        return edges;
    }
    
    public String getMatch()
    {
        return match;
    }
    
    public void setMatch(String person)
    {
        match = person;
    }
}
