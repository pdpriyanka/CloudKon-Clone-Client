package com.pa3.cloudkon.remote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.sqs.model.Message;
import com.pa3.cloudkon.Constants;

public class RemoteClientThread extends Thread{
	private String taskQueueName;
	private String filePath;
	
	public RemoteClientThread(String taskQueueName, String filePath){
		this.taskQueueName = taskQueueName;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		List<String>reponsesList = new ArrayList<String>();
		RemoteSQS taskQueue = new RemoteSQS(taskQueueName);
		RemoteSQS responseQueue = new RemoteSQS(taskQueueName + "Reponse");
		Dynamodb dynamodb = new Dynamodb();
		Dynamodb.init();
		
		if (!taskQueue.isSQSExists()) {
			System.out.println("SQS "+ taskQueueName+" is not present.");
			System.exit(0);
		}
		if(!dynamodb.isTableExists(taskQueueName)){
			System.out.println("Dynamodb "+ taskQueueName+" is not present.");
			System.exit(0);
		}		
		
		taskQueue.initSQSURL();
		responseQueue.initSQSURL();

		// create set the store the task ids of all submitted tasks
		Set<Integer> tasksSet = new HashSet<Integer>();

		// get the file path which contains the tasks to be executed

		long startTime = System.currentTimeMillis();

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String task = null;
			int taskCounter = 0;
			while ((task = bufferedReader.readLine()) != null) {
				task = taskCounter + Constants.SEPERATOR + task.trim();
				taskQueue.sendMessage(task);
				tasksSet.add(taskCounter); // add task into set
				taskCounter++; // task id generator
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

		// Polling for response
		int taskid = -1, responseCode = -1;
		Message message = null;
		String response = null;
		String[] strs = null;
		while (true) {

			// check whether response queue contain any response
			message = responseQueue.ReceivingMessage();
			if (message != null) {
				
				responseQueue.deleteMessage(message);

				response = message.getBody();

				strs = response.split(Constants.SEPERATOR);
				if (strs != null && strs.length == 3) {
					try {
						taskid = Integer.parseInt(strs[0]);
						responseCode = Integer.parseInt(strs[1]);

						// check for failed task response.
						if (responseCode == 1) {
							System.out.println("Warning : Task with id " + taskid + " is failed");
							tasksSet.remove(taskid);
							reponsesList.add(response);
							//System.out.println(response);
						} else {
							// If taskid from response queue is present in set
							// then that means it is executed only once
							// and if its response is 0 then it is successfully
							// executed.
							// if task id is not present in set and response is
							// 0 then that means the task response is
							// added more than once in response queue
							if (tasksSet.contains(taskid)) {
								tasksSet.remove(taskid);
								reponsesList.add(response);
								//System.out.println(response);

							} else {
								System.out.println("Warning: got multiple responses for task with id " + taskid);
							}
						}

					} catch (Exception e) {
						System.out.println("Exception in remote client");
					}
				}
			}

			// if task set is empty that means responses for all the tasks are
			// got hence terminate the clientThread.
			if (tasksSet.isEmpty()) {
				break;
			}
		}
		long endTIme = System.currentTimeMillis();
		// calculate the total time required to execute the program
		System.out.println("For remote workers : Total time to execute all tasks is "
				+ ((((double) endTIme - startTime)) / 1000) + " seconds.");	

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
