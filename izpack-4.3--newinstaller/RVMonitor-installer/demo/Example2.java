package demo;

public class Example2 {
	static Object lock = new Object();
	
	static int x=0;
	
	public static void main(String[] args)
	{
		try
		{
			MyThread t1 = new MyThread();
			MyThread t2 = new MyThread();
	
			t1.start();
			t2.start();
		
			synchronized(lock)
			{
				x++;
			}
		
			x=0;//race here
		
			t1.join();
			
			t2.join();
				
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			//sleep for 10ms to let main thread go first 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			
			synchronized(lock)
			{
				x++;
			}
			
			System.out.println(1/x);//may throw divide by zero exception

		}
	}
}
