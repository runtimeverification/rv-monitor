package demo;

public class Example {
	static Object lock = new Object();
	
	static int x=0;
	
	public static void main(String[] args)
	{
			MyThread t = new MyThread();
	
			t.start();
			
			synchronized(lock)
			{
				x=0;//race here
			}
		
	}
	
	static class MyThread extends Thread
	{
		
		public void run()
		{
			
			synchronized(lock)
			{
				x++;
			}
			
			//race here, may throw divide by zero exception
			System.out.println(1/x);

		}
	}
}
