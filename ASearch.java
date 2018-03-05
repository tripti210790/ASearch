import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import javax.swing.plaf.synth.SynthOptionPaneUI;


public class ASearch {

	public static final int move_cost=1;
	
	static class Block 
	{
		int h_cost=0; //heuristic cost
		int f_cost =0; //final cost
		int i,j;
		
		Block parent;
		
		Block (int i ,int j)
		{
			this.i=i;
			this.j=j;
		}
		
		public String toString()
		{
			return "["+this.i+","+this.j+"]" ;
		}
		
	}
	
	static Block [][] grid = new Block[11][21];
	static Block [][] grid_path = new Block[11][21];
	static ArrayList<Block> path = new ArrayList<Block>();
	
	static PriorityQueue<Block> openlst;
	
	static boolean closedlst [][];
	
	static int init_i, init_j; 
	static int end_i,end_j;
	
	
	public  static void setWall(int i, int j)
	{
		grid[i][j]=null;
	}
	
	public static void setInitialBlock( int i , int j)
	{
		init_i=i;
		init_j=j;
	}
	
	public static void setDestinationBlock (int i , int j)
	{
		end_i=i;
		end_j=j;
	}
	
	
	static void searchCost (Block present , Block b , int cost )
	{
		if( b==null || closedlst [b.i][b.j]) //Check for wall
		{
			return;
		}
			
		int final_cost_for_b = b.h_cost+cost;
		
		boolean open = openlst.contains(b);
		
		if(!open || final_cost_for_b < b.f_cost)
		{
			b.f_cost=final_cost_for_b;
			b.parent=present;
			
			if(!open)
			{
			   openlst.add(b);	
			}
			
		}
		
	}
	
	
	public static void ASearch()
	{
		openlst.add(grid[init_i][init_j]);
		
		Block present;
		
		while(true)
		{
			 present=openlst.poll();
			 
			 if(present == null)
				 break;
			 
			 closedlst[present.i][present.j]=true;
			 
			 if(present.equals(grid[end_i][end_j]))
			 {
				 return;
			 }
			 
			 Block b;
			 
			 if(present.i-1>=0)
			 {
				 b=grid[present.i-1][present.j];
				 searchCost(present,b,present.f_cost+move_cost);	 
			 }
			 
			 if(present.j-1>=0)
			 {
				 b=grid[present.i][present.j-1];
				 searchCost(present,b,present.f_cost+move_cost); 
			 }
			 
			 if(present.j+1<grid[0].length)
			 {
				 b=grid[present.i][present.j+1];
				 searchCost(present,b,present.f_cost+move_cost);
			 }
			 
			 if(present.i+1<grid.length)
			 {
				 b=grid[present.i+1][present.j];
				 searchCost(present,b,present.f_cost+move_cost);				 
				 
			 }
			  
			 
		 }
			
		}
	
	
	
	
	public  static void run (int x,int y,int init_i,int init_j,int end_i,int end_j,int [][] blocked)
	{
		grid=new Block[x][y];
		closedlst =new boolean [x][y];
		
		openlst=new PriorityQueue(30,new Comparator()
		{
		
		public int compare(Object o1, Object o2) {
			// TODO Auto-generated method stub
			Block b1= (Block)o1;
			Block b2=(Block) o2;	
			return b1.f_cost<b2.f_cost ? -1 : b1.f_cost>b2.f_cost ? 1:0 ;
		}

	});
		
		setInitialBlock(init_i,init_j);
		setDestinationBlock(end_i,end_j);
		
		for(int i=0;i<x;++i)
		{
			for(int j=0;j<y;++j)
			{
				grid[i][j]=new Block(i,j);
				grid[i][j].h_cost=Math.abs(i-init_i)+Math.abs(j-init_j); 
			}
		}
		
		
		grid[init_i][init_j].f_cost = 0;
		
		
		for(int i=0;i<blocked.length;++i)
		{
			setWall(blocked[i][0],blocked[i][1]);	
		}
		
		
		//Display sample map 
		System.out.println("Sample Map:");
		for(int i=0;i<x;++i){
            for(int j=0;j<y;++j){
               if(i==init_i &&j==init_j)System.out.print("S   "); //Source
               else if(i==end_i && j== end_j)System.out.print("E   ");  //Destination
               else if(grid[i][j]!=null)System.out.printf("%-3C ",'.');
               else System.out.print("W   "); 
            }
            System.out.println();
        } 
        System.out.println();
        
        //Search starts here 
        ASearch();
             
        
        if(closedlst [end_i][end_j]){
            //Trace back the path 
             System.out.println("Path: ");
             Block current = grid[end_i][end_j];
             grid_path[end_i][end_j]=current;
             
             System.out.print(current);
         
             while(current.parent!=null){
                 System.out.print(" -> "+current.parent);
                 grid_path[current.parent.i][current.parent.j]=current.parent;
                 current = current.parent;
                 
             } 
             
             System.out.println();
        }else System.out.println("No path");
        
        
        
        
      //Display output map 
      		System.out.println("Output Map:");
      		
      		
      		
      		for(int i=0;i<x;++i){
                  for(int j=0;j<y;++j){
                     if(i==init_i &&j==init_j)System.out.print("S   "); //Source
                     else if(i==end_i && j== end_j)System.out.print("E   ");  //Destination
                     else if(grid_path[i][j]!=null)// && path.size()<counter)
                     {
                    	 System.out.print("\"\"");
                    	 System.out.print("   ");
                     }
                     else if(grid[i][j]!=null)// && path.size()<counter)
                    	 System.out.printf("%-3c ",'.');
                     
                     else System.out.print("W   "); 
                  }
                  System.out.println();
                 
              } 
              System.out.println();
              
              
      		
        
          
		
		
	}
	

	public static void main(String [] args) throws Exception	
	{
		 
		 run( 11, 21, 4, 0, 5, 20, new int[][]{{1,13},{2,13},{3,8},{3,13},{4,1},{4,3},{4,4},{4,8},{4,13},{4,15},{5,4},{5,8},{5,9},{5,10},{5,11},{5,12},{5,13},{5,15},{6,1},{6,3},{6,5},{6,13},{6,15},{7,5},{7,13},{7,15},{8,5},{8,15},{9,6},{9,15},{10,7},{10,15}});
		 
	}
	
	

}// End of class
	

