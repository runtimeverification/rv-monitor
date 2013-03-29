
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SafeFile_1{

	static FileReader fr = null;

	public static void sub1(){
		System.out.println("begin");

		File file = null;
		try{
			file = File.createTempFile("rvmonitortest1", ".tmp");
			FileWriter fw_1 = new FileWriter(file);
			fw_1.write("testing\n");
			fw_1.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		try{
			System.out.println("open");
			fr = new FileReader(file);
			mop.SafeFileRuntimeMonitor.openEvent(fr, Thread.currentThread());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		try{
			System.out.println("close");
			fr.close();
			mop.SafeFileRuntimeMonitor.closeEvent(fr, Thread.currentThread());
		} catch (Exception e){
		}
		System.out.println("end");
	}

	public static void sub2(){
		System.out.println("begin");

		File file = null;
		try{
			file = File.createTempFile("rvmonitortest1", ".tmp");
			FileWriter fw_1 = new FileWriter(file);
			fw_1.write("testing\n");
			fw_1.close();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}

		try{
			System.out.println("open");
			fr = new FileReader(file);
			mop.SafeFileRuntimeMonitor.openEvent(fr, Thread.currentThread());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("end");
	}

	public static void sub3(){
		System.out.println("begin");
		try{
			System.out.println("close");
			fr.close();
			mop.SafeFileRuntimeMonitor.closeEvent(fr, Thread.currentThread());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		System.out.println("end");
	}

	public static void main(String[] args){
		mop.SafeFileRuntimeMonitor.beginCallEvent(Thread.currentThread());
		mop.SafeFileRuntimeMonitor.beginCallEvent(Thread.currentThread());
		sub1();
		mop.SafeFileRuntimeMonitor.endCallEvent(Thread.currentThread());
		mop.SafeFileRuntimeMonitor.beginCallEvent(Thread.currentThread());
		sub2();
		mop.SafeFileRuntimeMonitor.endCallEvent(Thread.currentThread());
		mop.SafeFileRuntimeMonitor.beginCallEvent(Thread.currentThread());
		sub3();
		mop.SafeFileRuntimeMonitor.endCallEvent(Thread.currentThread());
		mop.SafeFileRuntimeMonitor.endCallEvent(Thread.currentThread());
	}

}
