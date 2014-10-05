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

/**
 *
 * @author Adam
 */
public class MatchingGraph {
    
    public MatchingGraph(String fileName) throws FileNotFoundException
    {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
    }
    
}
