package com.pa3.cloudkon.local;

import com.pa3.cloudkon.Constants;

/**
 * This class is used for local workers. It will execute the task assigned by client
 * @author priyanka
 *
 */
public class WorkerThread extends Thread {
	
	private boolean notFinished = true;

	public WorkerThread() {
		super();
	}

	
	@Override
	public void run() {
		String task = null, taskId = null;
		String[] strs = null;
		TaskResponse taskResponse = null;
		
		//check for task queue is empty or finish is not set
		while (notFinished || !(LocalTaskQueue.getLocalTaskQueue().isEmpty())) {
			task = LocalTaskQueue.getLocalTaskQueue().poll();
			if (task != null) {
				taskResponse = new TaskResponse();
				taskResponse.setWorkerId(Thread.currentThread().getName());
				strs = task.split(Constants.SEPERATOR);
				if (strs != null && strs.length == 2) {
					taskId = strs[0];
					task = strs[1];
					if (taskId != null) {
						taskResponse.setTaskId(Integer.parseInt(taskId.trim()));
						if (task != null && task.length() > 5) {
							try {
								//execute the sleep task
								Thread.sleep(Integer.parseInt(task.replaceAll("[^0-9]+","").trim()));
								//generate the response for successful execution of task 
								taskResponse.setResponse(0);
							} catch (NumberFormatException | InterruptedException e) {
								System.out.println("Exception in local worker thread for task with id " + taskId);
								//generate the response for failed task
								taskResponse.setResponse(1);
							}
						}
					}
				}
				//System.out.println("worker running "+taskResponse.getTaskId()+" "+taskResponse.getResponse()+" "+taskResponse.getWorkerId());
				//add response into response queue
				LocalTaskReponseQueue.getLocalTaskReponseQueue().offer(taskResponse);
			}
		}
	}

	public boolean isNotFinished() {
		return notFinished;
	}

	public void setNotFinished(boolean notFinished) {
		this.notFinished = notFinished;
	}

}