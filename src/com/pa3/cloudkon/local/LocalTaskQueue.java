package com.pa3.cloudkon.local;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * This  class is used create local task queue
 * @author priyanka
 *
 */
public class LocalTaskQueue implements Queue<String>{
	public static LocalTaskQueue localTaskQueue = new LocalTaskQueue();  // object of in memory task queue
	public String name;  // name of queue
	private LocalTaskQueueNode front,rear;   // stores the front and rear
	private int length;     // length of queue


	public LocalTaskQueue(){
		length = 0;
		front = null;
		rear = null;	
	}
	
	
	/**
	 * This function will add the value into queue
	 */
	@Override
	public synchronized boolean offer(String content) {
		//generate node
		LocalTaskQueueNode localTaskQueueNode = new LocalTaskQueueNode(content);
		//if queue is empty then change the value of front to node and otherwise set the next of rear to node
		if(isEmpty())
			front = localTaskQueueNode;
		else 
			rear.setNext(localTaskQueueNode);
		rear = localTaskQueueNode;
		length++;     // increase the length of queue
		
		return true;
	}

	/**
	 * This function will retrieve the value from queue without removing value.
	 */
	@Override
	public String peek() {
		if(!isEmpty()){
			return front.getContent();			
		}		
		return null;
	}

	/*
	 * This function will remove and return the value from queue
	 */
	
	@Override
	public synchronized String poll() {
		if(!isEmpty()){
			String content = front.getContent();
			front = front.getNext();
			length--;
			if(isEmpty()){
				rear = null;
			}
			return content;
			
		}
		return null;
	}

	/**
	 * check whether queue is empty or not
	 */
	@Override
	public synchronized boolean isEmpty() {
		return (length == 0);
	}
	
	/**
	 * initialize the local task queue
	 */
	public static void init()
	{
		localTaskQueue = new LocalTaskQueue();
	}
	@Override
	public boolean addAll(Collection<? extends String> arg0) {
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
	public Iterator<String> iterator() {
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
	public boolean add(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String element() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String remove() {
		// TODO Auto-generated method stub
		return null;
	}


	public static synchronized LocalTaskQueue getLocalTaskQueue() {
		return localTaskQueue;
	}


	public static void setLocalTaskQueue(LocalTaskQueue localTaskQueue) {
		LocalTaskQueue.localTaskQueue = localTaskQueue;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	
}

/**
 * This is class for node of local task queue
 * @author priyanka
 *
 */
class LocalTaskQueueNode{
	private LocalTaskQueueNode next;
	private String content;
	
	public LocalTaskQueueNode(){
		super();
	}
	
	public LocalTaskQueueNode(String content){
		this.content = content;
	}

	public LocalTaskQueueNode getNext() {
		return next;
	}

	public void setNext(LocalTaskQueueNode next) {
		this.next = next;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
