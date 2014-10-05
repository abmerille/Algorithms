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
    
    public Person(String nm, int n) {
        super(nm, n);
        this.ranks = new HashMap<>(n);
    }
    
    public void addRank(String name, int r)
    {
        ranks.put(name, r);
    }
    
    public int getrank(String name)
    {
        return ranks.get(name);
    }
}
