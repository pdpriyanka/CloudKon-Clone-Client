package com.pa3.cloudkon.local;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
/**
 * This class is used to create local response queue.
 * @author priyanka
 *
 */
public class LocalTaskReponseQueue implements Queue<TaskResponse>{
	public static LocalTaskReponseQueue localTaskReponseQueue = new LocalTaskReponseQueue();
	private LocalTaskResponseQueueNode front,rear; 
	public String name;    // name of queue
	private int length;    // length of queue

	public LocalTaskReponseQueue(){
		length = 0;
		front = null;
		rear = null;	
	}

	
	//check for empty condition
	@Override
	public synchronized boolean isEmpty() {
		return (length == 0);
	}

	public static void init()
	{
		localTaskReponseQueue = new LocalTaskReponseQueue();
	}
	
	/**
	 * This method is used to add value into queue
	 */
	@Override
	public synchronized boolean offer(TaskResponse taskResponse) {
		LocalTaskResponseQueueNode localTaskResponseQueueNode = new LocalTaskResponseQueueNode(taskResponse);
		if(isEmpty())
			front = localTaskResponseQueueNode;
		else 
			rear.setNext(localTaskResponseQueueNode);
		rear = localTaskResponseQueueNode;
		length= length + 1;    // increase the length
		return true;
		
	}

	/**
	 * This method retreieve the value without removing it
	 */
	@Override
	public TaskResponse peek() {
		if(!isEmpty()){
			return front.getContent();			
		}		
		return null;
	}

	/**
	 * Thsi method will remove and return the value from queue
	 */
	@Override
	public synchronized TaskResponse poll() {
		if(!isEmpty()){
			TaskResponse content = front.getContent();
			front = front.getNext();
			length--;
			if(isEmpty()){
				rear = null;
			}
			return content;
			
		}
		return null;
	}
	
	@Override
	public boolean addAll(Collection<? extends TaskResponse> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public Iterator<TaskResponse> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		return length;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean add(TaskResponse arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TaskResponse element() {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public TaskResponse remove() {
		// TODO Auto-generated method stub
		return null;
	}

	public static synchronized LocalTaskReponseQueue getLocalTaskReponseQueue() {
		return localTaskReponseQueue;
	}

	public static void setLocalTaskReponseQueue(LocalTaskReponseQueue localTaskReponseQueue) {
		LocalTaskReponseQueue.localTaskReponseQueue = localTaskReponseQueue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/**
 * This class is used to create node for response queue
 * @author priyanka
 *
 */
class LocalTaskResponseQueueNode{
	private LocalTaskResponseQueueNode next;
	private TaskResponse content;
	
	public LocalTaskResponseQueueNode(){
		super();
	}
	
	public LocalTaskResponseQueueNode(TaskResponse content){
		this.content = content;
	}

	public LocalTaskResponseQueueNode getNext() {
		return next;
	}

	public void setNext(LocalTaskResponseQueueNode next) {
		this.next = next;
	}

	public TaskResponse getContent() {
		return content;
	}

	public void setContent(TaskResponse content) {
		this.content = content;
	}


}
