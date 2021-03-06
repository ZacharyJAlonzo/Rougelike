/**
 * @(#)MovingObject.java
 *
 *
 * @author 
 * @version 1.00 2020/2/25
 */


abstract class MovingObject extends RObject implements killable
{
	protected Tile playMap;
	protected int currentTile;
	protected int ax, ay;
	protected int totalHP, currentHP;
	
	protected enum moveState
	{
		NO_MOVE, MOVE, ATTACK, FRIENDLY;
	}
		   
    public MovingObject(Tile t) 
    {
    	super(true);
    	isMoving = true;
    	playMap = t;
    	currentTile = 0;
    	
    	ax = playMap.getSize()/2-1;
    	ay = playMap.getSize()/2-1;

		totalHP = 20;
		currentHP = 20;
		
    	playMap.spawn(this, currentTile, ax, ay);
    }
    
    public MovingObject(Tile t, int x, int y)
    {
    	super(true);
    	isMoving = true;
    	playMap = t;
    	currentTile = 0;
    	
    	ax = x;
    	ay = y;
   
    	playMap.spawn(this, currentTile, ax, ay);
    }
    
    abstract public void Tick();
    
    
    //killable interface implementation//
    public int getCurrentHP() { return currentHP; }
    public int getTotalHP() { return totalHP; }
    public boolean isDead()
    {
    	if(currentHP <= 0)
    	{
    		die();
    		return true;
    	}
    	else return false;
    }
    
    public void die()
    {
    	//despawn, drop inventory
    	System.out.println("died");
    	playMap.getCells(currentTile)[ay][ax].removeObject();
    	System.out.println(playMap.getCells(currentTile)[ay][ax].getObject().getDisplay()+".");
    }
    public void takeDamage(int damage)
    {
    	currentHP -= damage;
    	isDead();
    }
    /////////////////////////////////////
	    
    abstract void attack(MovingObject target);
    
    public moveState moveRight()
    {
    	//the moving entity has attempted to step into a new tile
    	if(ax+1 == playMap.getCells(currentTile).length)
    	{
    		//is there a tile to the right?
    		/* 0 1 2  no right for 2
    		 * 3 4 5  no right for 5
    		 * 6 7 8  no right for 8
    		 */
    		switch(currentTile)
    		{
    			case 2:
    			{
    				return moveState.NO_MOVE;
    			}
    			
    			case 5:
    			{
    				return moveState.NO_MOVE;   				
    			}
    			case 8:
    			{
    				return moveState.NO_MOVE;    			
    			}
    			
    			default:
    			{
    				break;
    			}
    		}
    		
    		//if there is a tile, is it blocked by a wall?
    		//ax is 0 because we are checking the first row
    		switch(playMap.getCells(currentTile+1)[ay][0].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				playMap.getCells(currentTile)[ay][ax].removeObject();			
    		 			
    				//since we ae moving to a new tile, update the current tile
    				currentTile += 1;
    				ax = 0;
    				//ay remains the same
    			
    			 	playMap.getCells(currentTile)[ay][ax].addObject(this);
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    	}
    	//we are only moving within the tile's bounds
    	else
    	{
    		switch(playMap.getCells(currentTile)[ay][ax+1].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				//update ax 
				 	playMap.getCells(currentTile)[ay][ax].removeObject();			
    				ax += 1;
    				 playMap.getCells(currentTile)[ay][ax].addObject(this);
    				//ay remains the same
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
	
    	}

    }
    

    public moveState moveLeft()
    {
    	//the moving entity has attempted to step into a new tile
    	if(ax-1 == -1)
    	{
    		//is there a tile to the left?
    		/* 0 1 2  no right for 0
    		 * 3 4 5  no right for 3
    		 * 6 7 8  no right for 6
    		 */
    		switch(currentTile)
    		{
    			case 0:
    			{
    				return moveState.NO_MOVE;
    			}
    			
    			case 3:
    			{
    				return moveState.NO_MOVE;   				
    			}
    			case 6:
    			{
    				return moveState.NO_MOVE;    			
    			}
    			
    			default:
    			{
    				break;
    			}
    		}
    		
    		switch(playMap.getCells(currentTile-1)[ay][playMap.getCells(currentTile).length-1].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				playMap.getCells(currentTile)[ay][ax].removeObject();			

    				//since we ae moving to a new tile, update the current tile
    				currentTile -= 1;
    				ax = playMap.getCells(currentTile).length-1;
    				//ay remains the same
    			
    			 	playMap.getCells(currentTile)[ay][ax].addObject(this);
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    	}
    	//we are only moving within the tile's bounds
    	else
    	{
    		
    		switch(playMap.getCells(currentTile)[ay][ax-1].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				//update ax 
				 	playMap.getCells(currentTile)[ay][ax].removeObject();			
    				ax -= 1;
    			 	playMap.getCells(currentTile)[ay][ax].addObject(this);
    				//ay remains the same
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    		
    	}

    }
    

    public moveState moveUp()
    {
    	//the moving entity has attempted to step into a new tile
    	if(ay-1 == -1)
    	{
    		//is there a tile to the north?
    		/* 0 1 2  no right for 0
    		 * 3 4 5  no right for 1
    		 * 6 7 8  no right for 2
    		 */
    		switch(currentTile)
    		{
    			case 0:
    			{
    				return moveState.NO_MOVE;
    			}
    			
    			case 1:
    			{
    				return moveState.NO_MOVE;   				
    			}
    			case 2:
    			{
    				return moveState.NO_MOVE;    			
    			}
    			
    			default:
    			{
    				break;
    			}
    		}
    		
    		switch(playMap.getCells(currentTile-3)[playMap.getCells(currentTile).length-1][ax].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				//since we ae moving to a new tile, update the current tile
    				playMap.getCells(currentTile)[ay][ax].removeObject();
    			
    				currentTile -= 3;
    				ay = playMap.getCells(currentTile).length-1;
    				//ax remains the same
    			
    				playMap.getCells(currentTile)[ay][ax].addObject(this);
    								
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    	}
    	//we are only moving within the tile's bounds
    	else
    	{
    		switch(playMap.getCells(currentTile)[ay-1][ax].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				//update ax 
				 	playMap.getCells(currentTile)[ay][ax].removeObject();			
    				ay -= 1;
    			 	playMap.getCells(currentTile)[ay][ax].addObject(this);
    				//ay remains the same
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}

    	}

    }


     public moveState moveDown()
    {
    	//the moving entity has attempted to step into a new tile
    	if(ay+1 == playMap.getCells(currentTile).length)
    	{
    		//is there a tile to the south?
    		/* 0 1 2  no right for 6
    		 * 3 4 5  no right for 7
    		 * 6 7 8  no right for 8
    		 */
    		switch(currentTile)
    		{
    			case 6:
    			{
    				return moveState.NO_MOVE;
    			}
    			
    			case 7:
    			{
    				return moveState.NO_MOVE;   				
    			}
    			case 8:
    			{
    				return moveState.NO_MOVE;    			
    			}
    			
    			default:
    			{
    				break;
    			}
    		}
    		
    		
    		switch(playMap.getCells(currentTile+3)[0][ax].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				System.out.println("here");
    				//since we ae moving to a new tile, update the current tile
    				playMap.getCells(currentTile)[ay][ax].removeObject();			
    			
    				currentTile += 3;
    				ay = 0;
    			  			
    				playMap.getCells(currentTile)[ay][ax].addObject(this);
    				//ax remains the same
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    	}
    	//we are only moving within the tile's bounds
    	else
    	{
    		switch(playMap.getCells(currentTile)[ay+1][ax].getObject().getDisplay())
    		{
    			case '#':
    				return moveState.NO_MOVE;
    			case ' ':
    			{
    				//update ax 
					 playMap.getCells(currentTile)[ay][ax].removeObject();			
    				ay += 1;
    			 	playMap.getCells(currentTile)[ay][ax].addObject(this);
    				//ay remains the same
    				return moveState.MOVE;
    			}
    			default:
    				return moveState.ATTACK;
    		}
    		
    	}

    }
    
    
    public int getAX()
    {
    	return ax;
    }
    public int getAY()
    {
    	return ay;
    }
    
    
    	
}
