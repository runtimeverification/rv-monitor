package demo;

public class Authenticate {

	static int x=0,y=0;
	static int z=0;//assume z is a shared resource
	static Object lock = new Object();
	
	public static void main(String[] args)
	{
		
		MyThread t = new MyThread();
		t.start();
		
		synchronized(lock)
		{

			y = 1;
			
			x =1;
		}
		
		try{
			
			t.join();
			//use z
			//may throw divide by zero exception 
			System.out.println("Safe: "+1/z);

		}catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	static class MyThread extends Thread
	{
		public void run()
		{
			int r1,r2;
			
			synchronized(lock)
			{
				r1 = y;
			}
	
			r2 =x;//race here, x is not protected
			
			if(r1+r2>0)
			{
				z=1;//authenticate z here if x or y is positive
			}

		}
	}
}
