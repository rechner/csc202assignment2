package edu.nvcc.util;

public class CategoryNode {
	
	private String name;
	private Object element;
	private CategoryNode link;
	
	public CategoryNode(Object element) {
		super();
		this.element = element;
		this.name = "";
		this.link = null;
	}
	
	public CategoryNode(Object element, CategoryNode link) {
		super();
		this.name = "";
		this.element = element;
		this.link = link;
	}
	
	public CategoryNode(Object element, String name, CategoryNode link) {
		super();
		this.name = name;
		this.element = element;
		this.link = link;
	}
	
	public Object getElement() {
		return element;
	}
	public void setElement(Object element) {
		this.element = element;
	}
	public CategoryNode getLink() {
		return link;
	}
	public void setLink(CategoryNode link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
