package edu.nvcc.util;

import java.io.Serializable;

public class LinkedList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LinkedListNode list;
		public LinkedList() {
		list = null;
	}
	
	public void add(Object element) {
		// add first element
		LinkedListNode newNode = new LinkedListNode(element,list);
		list = newNode;
	}
	
	public Object remove(Object element) throws LinkedListError{
		// point object link to null
		// point previous node to node after
		LinkedListNode previousNode, currentNode;
		currentNode = list;
		previousNode = null;
		
		while (!element.equals(currentNode.getElement())) {
			previousNode = currentNode;
			currentNode = currentNode.getLink();
		}
		
		if (currentNode == null) 
			throw new LinkedListError("Element does not exist");
		
		// reattach severed links
		previousNode.setLink(currentNode.getLink());
		return element;
	}
	
	public boolean contains(Object element) {
		LinkedListNode currentNode = list;
		boolean retFlag = true;
		
		while (!element.equals(currentNode.getElement())) {
			retFlag = false;
		}
		return retFlag;
	}
	
	public boolean isEmpty() {
		return (list == null);
	}
	
	public String toString() {
		String out="";
		LinkedListNode temp;
		temp = list;
		while(temp!=null) {
			out += temp.getElement() + "\n";
			temp = temp.getLink();
		}
		return out;
	}



}
