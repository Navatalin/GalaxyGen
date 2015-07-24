public class Star
{
    private double[] Location;
    private double exitAngle;
    private double size;
    private String starClass;
    
    public Star(double[] input)
    {
    	if(input[0] == 1)
    	{
    		starClass = "O";
    	}
    	if(input[0] == 2)
    	{
    		starClass = "B";
    	}
    	if(input[0] == 3)
    	{
    		starClass = "A";
    	}
    	if(input[0] == 4)
    	{
    		starClass = "F";
    	}
    	if(input[0] == 5)
    	{
    		starClass = "G";
    	}
    	if(input[0] == 6)
    	{
    		starClass = "K";
    	}
    	if(input[0] == 7)
    	{
    		starClass = "M";
    	}
    	
    	size = input[1];
    	
        Location = new double[2];
    }
    public double[] getLoc()
    {
        return Location;
    }
    public void setLoc(double X, double Y)
    {
    	Location[0] = X;
    	Location[1] = Y;
    }
    public void setExA(double exit) 
    {
    	exitAngle = exit;
    }
    public double getExA()
    {
    	return exitAngle;
    }
    public void setSize(double size)
    {
    	this.size = size;
    }
    public double getSize()
    {
    	return size;
    }
    public void setStarClass(String sClass)
    {
    	starClass = sClass;
    }
    public String getStarClass()
    {
    	return starClass;
    }
}