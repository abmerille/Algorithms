/*
 * Class for each vertex to keep track of walls around it and distance
 */
package assign2;

import java.util.List;

/**
 *
 * @author adam
 */
public class Square
{
    private int row;
    private int col;
    private boolean hasSouth = false;
    private boolean hasNorth = false;
    private boolean hasEast = false;
    private boolean hasWest = false;
    private int dist;
    private boolean visited = false;
    
    public Square(int rw, int cl)
    {
        row = rw;
        col = cl;
       
    }
    
    //Constructor for routing square
    public Square()
    {}
    
    public void setWalls(String wlls)
    {
        char[] walls = wlls.toUpperCase().toCharArray();
        for(char c : walls)
        {
            switch(c)
            {
                case 'N':   hasNorth = true;
                            break;
                case 'S':   hasSouth = true;
                            break;
                case 'E':   hasEast = true;
                            break;
                case 'W':   hasWest = true;
                            break;
            }
        }
    }
    
    public int getRow()
    {
        return row;
    }
    
    public int getCol()
    {
        return col;
    }
    
    public void setDistance(int d)
    {
        dist = d;
    }

    public int getDistance()
    {
        return dist;
    }

    //check adjacent square if in bounds and has wall
    public boolean hasWall(Square adj)
    {
        int adjRow = adj.getRow();
        int adjCol = adj.getCol();
        
        if(hasNorth && (adjRow == (row-1)) && (adjCol == col))
        {
            return true;
        }
        if(hasSouth && (adjRow == (row+1)) && (adjCol == col))
        {
            return true;
        }
        if(hasEast && (adjRow == row) && (adjCol == (col + 1)))
        {
            return true;
        }
        if(hasWest && (adjRow == row) && (adjCol == (col - 1)))
        {
            return true;
        }
        
        return false;
    }

    public boolean isAlreadyDone()
    {
        return visited; 
    }

    public void setAlreadyDone()
    {
        visited = true;
    }

    public void clearAlreadyDone()
    {
        visited = false;
    }
}
