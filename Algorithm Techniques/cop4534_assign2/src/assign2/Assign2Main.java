/*
 * COP4534 - Assignment 2 Maze Traversal
 * Weiss
 *
 * @author Adam Merille
 */
package assign2;


public class Assign2Main
{

    static int penaltiesSize = 3;
    static int fieldsSize = 3;
    static int[] penalties = new int[penaltiesSize];
    static String[] fields = new String[fieldsSize];
    static int numFields = 0;
    static int numPenalties = 0;
    static boolean inputError = false;
    
    public static void main(String[] args)
    {
        getLineArgs(args);
        if(!inputError)
        {
            try
            {
                for (int i = 0 ; i < numFields; i++)
                {
                    System.out.println("MAZE " + fields[i]);
                    
                        String field = fields[i];
                        Maze mz = new Maze(field);
                    for (int j = 0; j < numPenalties; j++)
                    {
                        int penalty = penalties[j];
                        mz.computeDistances(penalty);
                        mz.clear();
                    }
                    System.out.println("\n");
                }
            } catch (Exception e)
            {
                System.err.println("Error processing: " + e);
            }
        }
    }

    public static void getLineArgs(String[] args)
    {
        int minArgs = 4;
        int i = 0;          //walkthrough variable
        int p = 0;
        int f = 0;
        String arg = args[i];
        if (arg.equals("-p") && args.length >= minArgs)
        {
            arg = args[++i];            //get first penalty
            
            try{
                Integer.parseInt(arg);
                while (!arg.equals("-f"))
                {
                    if (p < penaltiesSize)
                    {
                        penalties[p] = Integer.parseInt(arg);
                        arg = args[++i];
                        p++;
                        numPenalties++;
                    }
                    
                    //Case for greater number of penalties that size
                    else
                    {
                        int[] oldpenalties = penalties;
                        penalties = new int[oldpenalties.length * 2];
                        penaltiesSize = penalties.length;
                        System.arraycopy(oldpenalties, 0, penalties, 0, oldpenalties.length);
                        
                        penalties[p] = Integer.parseInt(arg);
                        arg = args[++i];
                        p++;
                        numPenalties++;
                    }
                }
            }
            catch(NumberFormatException e)
            {
                inputError = true;
                System.err.println("Input error: Make sure to have spaced number(s) following -p flag.");        
            }
            arg = args[++i];
            while (i < args.length)
            {
                if (f < fieldsSize)
                {
                    fields[f] = args[i];
                    i++;
                    f++;
                    numFields++;
                }
                //case for large amount of fields?
                else
                {
                    String[] oldfields = fields;
                    fields =new String[oldfields.length * 2];
                    fieldsSize = oldfields.length;
                    System.arraycopy(oldfields, 0, fields, 0, oldfields.length);
                    
                    fields[f] = args[i];
                    i++;
                    f++;
                    numFields++;
                }
            }

        } else
        {
            System.err.println("Error reading command arguments\nProper format: -p integer* -f filename* (* more than one separated by space)");
        }
    }
}
