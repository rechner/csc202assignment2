package edu.nvcc.util;
import java.io.Serializable;
import java.text.NumberFormat;

/**
 * @author Zachary Sturgeon
 * 
 */
public class FoodItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double price; 
	private int quantity;
	private String description;
	private String size;
	private String specialOrder; // false if empty string
	
	private NumberFormat money = NumberFormat.getCurrencyInstance();
	
	public FoodItem(String name, double price, int quantity, 
				String description, String size, String specialOrder) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.description = description;
		this.size = size;
		this.specialOrder = specialOrder;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	
	public String getPriceString() {
		return money.format(this.price);
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSpecialOrder() {
		return specialOrder;
	}

	public void setSpecialOrder(String specialOrder) {
		this.specialOrder = specialOrder;
	}

	@Override
	public String toString() {
		return name	 + " (" +  getPriceString() + " × " + quantity + ")";
	}
	
	

}
