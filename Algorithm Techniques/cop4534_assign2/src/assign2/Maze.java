/*
 * Maze class for creating and storing route
 */
package assign2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Character.isDigit;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 *
 * @author adam
 */
public class Maze
{

    private int numRows;
    private int numCols;
    private Square[][] squares;
    public static final int INFINITY = Integer.MAX_VALUE / 3; //to avoid overflow
    private Square startSquare;
    private int penalty;
    StringBuilder route = new StringBuilder();
    int wallCount = 0;
    
    public Maze(String fileName) throws FileNotFoundException, IOException
    {
        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] dimensions = reader.readLine().split("\\s");
        numRows = Integer.parseInt(dimensions[0]);
        numCols = Integer.parseInt(dimensions[1]);
        squares = new Square[numRows][numCols];
        
        //Create all Squares and initialize
        for(int i = 0; i < numRows; i++)
        {
            for(int j = 0; j <numCols; j++)
            {
                squares[i][j] = new Square(i, j);
                squares[i][j].setWalls("");
            }
        }

        String wholeLine = reader.readLine();
        
        //Read in lines, store in array, and check for input line errors
        while(wholeLine != null)
        { 
            String[] line = wholeLine.split("\\s");
            if(line.length == 3 && checkInt(line[0]) && checkInt(line[1]) && !checkInt(line[2]))
            {             
                int row = Integer.parseInt(line[0]);
                int col = Integer.parseInt(line[1]);
                if(row < numRows && col < numCols)
                {
                    squares[row][col].setWalls(line[2]);

                    if(row == 0 && col == 0)
                    {
                        startSquare = squares[row][col];
                    }
                }
                else
                {
                    System.out.println("Input out of bounds: " + wholeLine + " continuing...");
                }
            }
            else
            {
                System.out.println("Skipping input with improper format: " + wholeLine);
            }
            wholeLine = reader.readLine();
        
        }  
        if(startSquare == null)
        {
            startSquare = squares[0][0];
        }
    }
    
    private boolean checkInt(String x)
    {
        boolean isInt = true;
        for(char c : x.toCharArray())
        {
            if(!isDigit(c))
            {
                isInt = false;
                break;
            }
        }
       
        return isInt;
    }
    
    private class PQEntry implements Comparable<PQEntry>
    {

        private int dist;       //key
        private Square sq;

        public PQEntry(Square s, int d)
        {
            dist = d;
            sq = s;
        }

        public Square getSquare()
        {
            return sq;
        }

        @Override
        public int compareTo(PQEntry other)
        {
            return dist - other.dist;
        }
    }

    public List<Square> getAllSquares()
    {
        List<Square> squareList = new LinkedList<>();
        
        for(int i = 0; i < numRows; i++)
        {
            for(int j = 0; j < numCols; j++)
            {
                squareList.add(squares[i][j]);
            }
        }
        
        return squareList;
    }
    
    //sets all square distances back and clears the route and wallcount
    public void clear()
    {
        for(Square s : getAllSquares())
        {
            s.clearAlreadyDone();
        }
        route.delete(0, route.length());
        wallCount = 0;
    }
    
    //Dijkstra's
    public void computeDistances(int penalty)      
    {
        for (Square s : getAllSquares())
        {
            s.setDistance(INFINITY);
        }
        
        this.penalty = penalty;
        startSquare.setDistance(0);
        PriorityQueue<PQEntry> pq = new PriorityQueue<>();
        boolean foundExit = false;
        pq.add(new PQEntry(startSquare, 0));

        while (!pq.isEmpty())
        {
            PQEntry e = pq.remove();
            Square v = e.getSquare();
            
            if (v.isAlreadyDone())
            {
                continue;
            }

            v.setAlreadyDone();
            if(v.getCol()== (numCols -1)  && v.getRow() == (numRows - 1))
            {
                foundExit = true;
                break;
            }

            for (Square w : getAdjacents(v))
            {
                int cvw = 1;
                if (v.hasWall(w) || w.hasWall(v))       //check both squares
                {
                    cvw += penalty;
                }

                if (v.getDistance() + cvw < w.getDistance())
                {
                    w.setDistance(v.getDistance() + cvw);
                    PQEntry q = new PQEntry(w, v.getDistance() + cvw);
                    pq.add(q);
                }              
            }
        }
        
        if(foundExit)
        {
            getRoute(squares[(numRows -1)][(numCols - 1)]);
            System.out.println("Penalty of " + penalty + " cost " + 
                    squares[(numRows -1)][(numCols - 1)].getDistance() + 
                    " steps with " + wallCount + " wall(s)" +": "+ 
                    route.reverse());
        }
        
        //probably never printed now with wall knocking
        else
        {
            System.out.println("None");
        }
    }
    
    //recursively find previous square and add direction to stringbuilder
    public void getRoute(Square v)
    {
       if(v.getDistance() == 0)
       {
           
       }
       else
       {
            Square nearestSquare = new Square();
            nearestSquare.setDistance(Integer.MAX_VALUE/3);
            String direction = "";
            boolean found = false;
            
            for(Square w : getAdjacents(v))
            {
                if(w.getDistance() == (v.getDistance() - 1) 
                        && (!w.hasWall(v) && !v.hasWall(w)))
                {
                    nearestSquare = w;
                    direction = getDirection(w, v);
                    found = true;
                    break;
                }
            }
            
            //If not found previous then check with penalty for wall
            if(!found)
            {
                for(Square w : getAdjacents(v))
                {
                    if(w.getDistance() == (v.getDistance() - 1 - penalty)
                            && (w.hasWall(v) || v.hasWall(w)))
                    {
                        nearestSquare = w;
                        direction = getDirection(w, v);
                        wallCount++;
                        break;
                    }
                }
            }
            
            route.append(direction);
            getRoute(nearestSquare);
       }      
    }
    
    //compares squares to return direction for route print out
    private String getDirection(Square from, Square to)
    {
        if(to.getCol() > from.getCol() && to.getRow() == from.getRow())
        {
            return "E";
        }
        if(to.getCol() < from.getCol() && to.getRow() == from.getRow())
        {
            return "W";
        }
        if(to.getCol() == from.getCol() && to.getRow() > from.getRow())
        {
            return "S";
        }
        return "N";
    }
    
    public List<Square> getAdjacents(Square s)
    {
        List<Square> adjacentList = new LinkedList<>();
        int upRow = s.getRow() + 1;
        int upCol = s.getCol() + 1;
        int downRow = s.getRow() - 1;
        int downCol = s.getCol() - 1;
        
        if( downRow >= 0)           //North
        {
            adjacentList.add(squares[downRow][s.getCol()]);
        }
        if( upRow < numRows)        //South
        {
            adjacentList.add(squares[upRow][s.getCol()]);
        }
        if( upCol < numCols)        //East
        {
            adjacentList.add(squares[s.getRow()][upCol]);
        }    
        if( downCol >= 0)           //West
        {
            adjacentList.add(squares[s.getRow()][downCol]);
        }
        
        return adjacentList;
    }
   
}
