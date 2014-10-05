/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assign3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adam
 */
public class Node {
    String name;
    private List<Node> incomings;
    private List<Node> outgoings;

    public Node(String nm, int n)
    {
        name = nm;
        incomings = new ArrayList<>(n);
        outgoings = new ArrayList<>(n);
    }
    
    public Node getIncoming(int i)
    {
        return incomings.get(i);
    }
    
    public Node geOutgoing(int i)
    {
        return outgoings.get(i);
    }
     
    public void addIncoming(int i, Node n)
    {
        incomings.add(i, n);
    }
    
    public void addOutgoing(int i, Node n)
    {
        outgoings.add(i, n);
    }
}
