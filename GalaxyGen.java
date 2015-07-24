import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.*;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;

public class GalaxyGen
{
    public static String outputDir = "";
    public static void main(String args[])
    {
        ArrayList<Star> stars = new ArrayList<Star>();
        String[] configs = readConfig();
        
        double[] center = new double[2];
        center[0] = 0;
        center[1] = 0;
        
        try
        {
            //double maxRadius = 1500.0;
            double maxRadius = Double.parseDouble(configs[0]);
            //double randPosRatio = 0.75;
            double randPosRatio = Double.parseDouble(configs[1]);
            double maxRandomRadius = maxRadius * randPosRatio;
            //int steps = 1000000;
            int steps = Integer.parseInt(configs[2]);
            //int maxStars = 16000;
            int maxStars = Integer.parseInt(configs[3]);
            //double minDis = 50.0;
            double minDis = Double.parseDouble(configs[4]);
        	int falseCount = 0;
		    //int maxFalse = 200;
		    int maxFalse = Integer.parseInt(configs[5]);
		    outputDir = configs[6];

        
        Star currentStar = new Star(genStarType()); 
        currentStar.setExA(getRand(0.0,360.0));
        currentStar.setLoc(getRand(-1*maxRandomRadius,maxRandomRadius),getRand(-1*maxRandomRadius,maxRandomRadius));
        stars.add(currentStar);
        NormalDistribution n = getdist(maxRadius * 0.2, randPosRatio * maxRadius);

	
        
        for(int i = 0; i < steps; i++)
        {
 
            double dist = n.sample();

            double deg = currentStar.getExA();
            
            Star newStar = new Star(genStarType());
        
            //double[] nLoc = findLoc(currentStar.getLoc(),dist,deg);
            double[] nLoc = randLoc(-1*maxRadius,maxRadius);
        
        	if(getDistance(center,nLoc) > maxRadius || falseCount > maxFalse)
            {
            		double[] randLoc = new double[2];
            		randLoc[0] = getRand(-1*maxRandomRadius,maxRandomRadius);
            		randLoc[1] = getRand(-1*maxRandomRadius,maxRandomRadius);
                	nLoc = findLoc(randLoc,dist,deg);
                	falseCount = 0;
            }
            
        
            boolean create = true;
            
            for(Star s : stars)
            {
                double distfromS = getDistance(s.getLoc(),nLoc);

                double sSize =  s.getSize();
                double sizeNewStar = newStar.getSize();
                
                if((distfromS < (sSize * minDis)) || (distfromS < (sizeNewStar * minDis)))
                {
                    	create = false;
                    	falseCount++;
                }
                
            }
            
            if(create)
            {
                currentStar = newStar;
                currentStar.setLoc(nLoc[0],nLoc[1]);
                currentStar.setExA(getRand(0.0,360.0));
                stars.add(currentStar);
                writeOut(stars);
            }
            if(stars.size() > maxStars)
            {
            	i = steps;
            }
            
        }
        }
        catch(Exception e)
        {
            System.out.println("Error loading config");
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
    }
    public static NormalDistribution getdist(double mean, double std)
    {
        NormalDistribution n = new NormalDistribution(mean,std);
        return n;
    }
    public static double getRand(double low, double high)
    {
        RandomDataGenerator gen = new RandomDataGenerator();
        double res = gen.nextUniform(low,high);
        return res;
    }
    public static double[] findLoc(double start[], double dist, double deg)
    {
        double[] res = new double[2];
        double rads = Math.toRadians(deg);
        
        res[0] = start[0] + dist * Math.cos(rads);
        res[1] = start[1] + dist * Math.sin(rads);
        
        return res;
    }
    public static double[] randLoc(double min, double max)
    {
        double[] res = new double[2];
        
        res[0] = getRand(min,max);
        res[1] = getRand(min,max);
        
        return res;
    }
    public static double getDistance(double[] loc1, double[] loc2)
    {
        double res = Math.sqrt(Math.pow((loc1[0] - loc2[0]),2) + Math.pow((loc1[1] - loc2[1]),2));
        return res;
    }
    public static Star createSystem(double[] location)
    {
        Star s = new Star(location);
        return s;
    }
    public static double[] genStarType()
    {
    	double r = getRand(0.0000000,1.0000000);
    	double res[] = new double[2];
    	
    	if(r < 0.0000003)
    	{
    		res[0] = 1.0;
    		res[1] = 16.0;
    	}
    	else
    	if (r < 0.0013003)
    	{
    		res[0] = 2;
    		res[1] = 2.1;
    	}
    	else
    	if(r < 0.0073)
    	{
    		res[0] = 3;
    		res[1] = 1.4;
    	}
    	else
    	if(r < 0.0373)
    	{
    		res[0] = 4;
    		res[1] = 1.0;
    	}
    	else
    	if(r < 0.1133)
    	{
    		res[0] = 5;
    		res[1] = 0.8;
    	}
    	else
    	if(r < 0.2343)
    	{
    		res[0] = 6;
    		res[1] = 0.45;
    	}
    	else
    	{
    		res[0] = 7;
    		res[1] = 0.08;
    		
    	}
    	
    	return res;
    }
    public static void writeOut(ArrayList<Star> stars)
    {
    	 try
        {
        	double solarmass = 1.98892e30;
            //PrintWriter writer = new PrintWriter("/var/www/html/simoutput/output.csv","UTF-8");
            PrintWriter writer = new PrintWriter(outputDir,"UTF-8");
        	
        	writer.println("X,Y,Mass,Class");
            for(Star s : stars)
            {
                double[] sLoc = s.getLoc();
                writer.println((sLoc[0]*9.46e15) + ", " + (sLoc[1]*9.46e15) + " ," +  (s.getSize()*solarmass) + " ," + s.getStarClass());
                
            }
           
        	writer.println("END");
            writer.close();
            System.out.println("Number of Stars: " + stars.size());
        }
        catch(Exception e)
        {
            System.out.println("error");
        }
    }
    public static String[] readConfig()
	{
	    String[] configs = new String[7];
		String path = "config.cfg";
		BufferedReader br = null;
		String line = "";
		String comment = "#";
		
		try
		{
			br = new BufferedReader(new FileReader(path));
			while((line = br.readLine())!= null)
			{
			    if(!line.contains(comment))
			    {
				    if(line.contains("MaxRadius"))
				    {  
				        String[] t = line.split("=");
				        configs[0] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("RandPOSRatio"))
				    {   
				        String[] t = line.split("=");
				        configs[1] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("Steps"))
				    {   
				        String[] t = line.split("=");
				        configs[2] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("MaxStars"))
				    {   
				        String[] t = line.split("=");
				        configs[3] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("MinDis"))
				    {   
				        String[] t = line.split("=");
				        configs[4] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("MaxFalse"))
				    {   
				        String[] t = line.split("=");
				        configs[5] = t[1].substring(1).trim();
				    }
				    else
				    if(line.contains("OutputDir"))
				    {   
				        String[] t = line.split("=");
				        configs[6] = t[1].substring(1).trim();
				    }
				    
			    }
			}
			for(int i = 0; i < configs.length; i++)
		    {
		        System.out.println(configs[i]);
		    }
			
		}
		catch(Exception e)
		{
		    System.out.println("Error reading Config");
		    System.out.println(e.getMessage());
		}
		
		return configs;
		
	}
}