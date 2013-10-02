package edu.nvcc.util;
import java.util.Iterator;

import edu.nvcc.util.LinkedList;

public class CategoryList<T> implements Iterable {
	
	private CategoryNode list;
	
	public CategoryList() {
		list = null;
	};
	
	public void add(Object element, String name) {
		// add first element
		CategoryNode newNode = new CategoryNode(element, name, list);
		list = newNode;
	}
	
	public Object delete(T element) throws LinkedListError{
			// point object link to null
			// point previous node to node after
			CategoryNode previousNode, currentNode;
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
	
	public CategoryNode getNode(String node) {
		for (Object objElement : this) {
			CategoryNode element = (CategoryNode) objElement;
			if (element.getName().equals(node))
				return element;
		}
		return null;
	}
	
	public CategoryList search(String keyword) {
		CategoryList results = new CategoryList();
		
		for (Object objElement : this) {
			CategoryNode element = (CategoryNode) objElement;
			FoodItem child = null;
			if (element.getElement() != null)
				child = (FoodItem) element.getElement();
			if ((element.getName().equals(keyword)) || (child.getName().equals(keyword)))
				results.add(element, element.toString());
		}
		
		return results;
	}
	
	
	@Override
	public String toString() {
		return "CategoryList [" + (list != null ? "list=" + list : "") + "]";
	}

	public boolean isEmpty() {
		return (list == null);
	}

	@Override
	public Iterator iterator() {
		return new CategoryListIterator(list);
	}
	
	class CategoryListIterator<T> implements Iterator<T> {

		private CategoryNode head = null;
		
		public CategoryListIterator(CategoryNode h) {
			head = h;
		}
		
		@Override
		public boolean hasNext() {
			if (head.getLink() == null)
				return false;
			else
				return true;
		}

		@Override
		public T next() {
			head = head.getLink();
			return (T) head;
		}

		@Override
		public void remove() {
			
		}
		
	}

}
