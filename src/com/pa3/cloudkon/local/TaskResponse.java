package com.pa3.cloudkon.local;

/**
 * This class is used to store task id and its correponsing response
 * @author priyanka
 *
 */
public class TaskResponse {
	private int taskId;
	private int response;
	private String workerId;

	
	public TaskResponse(){
		super();
		response = 1;
	}
	
	public TaskResponse(int taskId, int response) {
		super();
		this.taskId = taskId;
		this.response = response;
	}
	
	public int getTaskId() {
		return taskId;
	}
	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getResponse() {
		return response;
	}
	public void setResponse(int response) {
		this.response = response;
	}
	
	

}
