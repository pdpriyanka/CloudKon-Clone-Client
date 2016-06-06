package com.pa3.cloudkon;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pa3.cloudkon.local.ClientThread;
import com.pa3.cloudkon.local.LocalTaskQueue;
import com.pa3.cloudkon.local.LocalTaskReponseQueue;
import com.pa3.cloudkon.local.WorkerThread;
import com.pa3.cloudkon.remote.RemoteClientThread;

/**
 * This class is used to launch the client and local workers.
 * 
 * @author priyanka
 *
 */
public class Client {
	volatile boolean allTasksSubmitted = false;

	public static void main(String[] args) {
		if(args!=null && args.length>0 && "client".equalsIgnoreCase(args[0]))
		{

		// command line interface
		new ClientCLI(args).parse();
			
		}
		else{
			System.out.println("Invalid command line argument");
			System.exit(0);
		}
		// If LOCAL is passed as a value then launch local workers and execute
		// the tasks
		if (args[2].trim().equalsIgnoreCase("LOCAL")) {
			localClientWorker(args);
		} else {
			remoteClient(args);
		}
	}

	/**
	 * This function will launch the client and local workers on same VM
	 * 
	 * @param args
	 */
	public static void localClientWorker(String[] args) {
		// create task and response queue
		LocalTaskQueue.init();
		LocalTaskQueue.getLocalTaskQueue().setName(args[2]);
		LocalTaskReponseQueue.init();
		LocalTaskReponseQueue.getLocalTaskReponseQueue().setName(args[2] + "Response");

		// create set the store the task ids of all submitted tasks
		Set<Integer> tasksSet = new HashSet<Integer>();

		// get the number of workers
		int numberOfWorkers = Integer.parseInt(args[4]);
		// get the file path which contains the tasks to be executed
		String filePath = args[6];

		String id = "";
		try {
			id = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.out.println("Network address not found.");
		}
		// creates worker threads
		List<WorkerThread> workerThreads = new ArrayList<WorkerThread>();
		WorkerThread workerThread1 = new WorkerThread();
		for (int i = 0; i < numberOfWorkers; i++) {
			workerThread1 = new WorkerThread();
			workerThread1.setName(id+"_"+(i+1));
			workerThreads.add(workerThread1);
		}

		// start worker threads
		for (WorkerThread workerThread : workerThreads) {
			workerThread.start();
		}

		// create client thread
		ClientThread clientThread = new ClientThread(tasksSet, filePath);

		clientThread.start();

		// check for client thread to alive
		while (clientThread.isAlive()) {
		}

		// check for worker thread to alive
		for (WorkerThread workerThread : workerThreads) {
			workerThread.setNotFinished(false);
		}

		// check for worker threads to alive
		while (isAlive(workerThreads)) {
		}		

	}

	// This function used to check whether any one of threads is alive or not.
	public static boolean isAlive(List<? extends Thread> threads) {
		if (threads != null && threads.size() > 0) {
			for (Thread thread : threads) {
				if (thread.isAlive())
					return true;
			}
		}
		return false;
	}

	public static void remoteClient(String[] args) {

		RemoteClientThread remoteClientThread = new RemoteClientThread(args[2],args[4]);
		// get the start time of program execution
		remoteClientThread.start();
		while(remoteClientThread.isAlive()){}
			
	}
}