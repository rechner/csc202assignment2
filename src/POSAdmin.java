import edu.nvcc.util.CategoryList;
import edu.nvcc.util.LinkedListError;

//addCategory/FoodItem and deleteCategory/FoodItem
public class POSAdmin {
	
	private CategoryList root = null;
	
	public void addCategory(String category) {
		root.add(category, category);
	}
	
	public void removeCategory(String category) {
		try {
			root.delete(category);
		} catch (LinkedListError e) {
			System.err.println("Invalid category '"+category+"'.  Unable to remove.");
			e.printStackTrace();
		}
	}
	
	public void addFoodItem(String category, FoodItem food) {
		root.add(food, category);
		
	}
	
	public void removeFoodItem(FoodItem food) {
		try {
			root.delete(food);
		} catch (LinkedListError e) {
			System.err.println("Invalid FoodItem '"+food+"'.  Unable to remove.");
			e.printStackTrace();
		}
	}
	
	public CategoryList search(String keyword) {
		return root.search(keyword);
	};

}
