package com.pa3.cloudkon.local;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pa3.cloudkon.Constants;

/**
 * This class is used to launch client and it will do polling for
 * response of tasks executed by workers.
 * @author priyanka
 *
 */
public class ClientThread extends Thread {
	private Set<Integer> tasksSet;    // contain the task ids of all submitted tasks
	private String filePath;    // contain path of file which has list of tasks to be executed.

	public ClientThread(Set<Integer> tasksSet, String filePath) {
		super();
		this.tasksSet = tasksSet;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		List<String>reponsesList = new ArrayList<String>();
		// get the start time of program execution
		long startTime = System.currentTimeMillis();

		//read the given file line by line. Each task is seperated by new line.
		//generate unique task id for each task and put  taskid:task in local queue.
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String task = null;
			int taskCounter = 0;
			while ((task = bufferedReader.readLine()) != null) {
				task = taskCounter + Constants.SEPERATOR + task.trim();
				LocalTaskQueue.getLocalTaskQueue().offer(task);   // add task into in memory local task queue
				tasksSet.add(taskCounter);   // add task into set
				taskCounter++;    // task id generator
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		} catch (IOException e) {
			System.out.println("Io exception");
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				System.out.println("Io exception");

			}
		}

		//Polling for response
		TaskResponse taskResponse = null;
		int taskid = -1;

		while (true) {
			
			//check whether response queue contain any response
			if (LocalTaskReponseQueue.getLocalTaskReponseQueue().size() > 0) {
				taskResponse = LocalTaskReponseQueue.getLocalTaskReponseQueue().poll();
				if (taskResponse != null) {
					taskid = taskResponse.getTaskId();
					
					//check for failed task response. 
					if (taskResponse.getResponse() == 1) {
						System.out.println("Warning : Task with id " + taskid + " is failed");
						tasksSet.remove(taskid);
						reponsesList.add(taskid+" "+taskResponse.getResponse()+" "+taskResponse.getWorkerId());
					} else {
						//If taskid from response queue is present in set then that means it is executed only once 
						//and if its response is 0 then it is successfully executed.
						// if task id is not present in set and response is 0 then that means the task response is
						//added more than once in response queue
						if (tasksSet.contains(taskid)) {
							tasksSet.remove(taskid);
							reponsesList.add(taskid+" "+taskResponse.getResponse()+" "+taskResponse.getWorkerId());
						} else {
							System.out.println("Warning: got multiple responses for task with id " + taskid);
						}
					}
				}
			}
			
			// if task set is empty that means responses for all the tasks are got hence terminate the clientThread. 
			if (tasksSet.isEmpty()) {
				break;
			}
		}	
		long endTime = System.currentTimeMillis();
		// calculate the total time required to execute the program
				System.out.println("For local workers : Total time to execute all tasks is "
						+ ((((double)endTime-startTime)) / 1000) + " seconds.");
				
				
		// writing responses to client
		File file = null;
		BufferedWriter bufferedWriter = null;				
		try {
			file = new File("./output.txt");
			file.createNewFile();
			bufferedWriter =new BufferedWriter(new FileWriter(file));
			int i = 0;
			for (String reponse : reponsesList) {			
				bufferedWriter.write(reponse);
				i++;
				if(i<reponsesList.size())
					bufferedWriter.newLine();				
			}

		} catch (IOException e) {
			System.out.println("IO exception.");
		}
		finally{
			if(bufferedWriter!=null){
				try {
					bufferedWriter.flush();
					bufferedWriter.close();
				} catch (IOException e) {
					System.out.println("IOEXception in bufferwriter.");
				}
			}
		}
		
	}

}
