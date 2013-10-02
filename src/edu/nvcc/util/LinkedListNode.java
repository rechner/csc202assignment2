package edu.nvcc.util;

public class LinkedListNode {
	Object element;
	LinkedListNode link;
	
	public LinkedListNode(Object element) {
		super();
		this.element = element;
		this.link = null;
	}
	
	public LinkedListNode(Object element, LinkedListNode link) {
		super();
		this.element = element;
		this.link = link;
	}
	public Object getElement() {
		return element;
	}
	public void setElement(Object element) {
		this.element = element;
	}
	public LinkedListNode getLink() {
		return link;
	}
	public void setLink(LinkedListNode link) {
		this.link = link;
	}
}
