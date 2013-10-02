import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import edu.nvcc.util.CategoryList;
import edu.nvcc.util.CategoryNode;

// constructs tree and linkedlist from a spreadsheet.

public class ODSParserFactory {
	
	// Tree (to display in JTree widget)
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("Food");
	private DefaultTreeModel treeModel = new DefaultTreeModel(root);
	
	// LinkedList (internal data representation)
	private CategoryList listModel = new CategoryList();
	
	public ODSParserFactory(String filename) throws ODSParserException, IOException {
		
		this(new File(filename));
		
	}

	public ODSParserFactory(File f) throws ODSParserException, IOException {
		
		//We'll assume everything we want is on the first sheet:
		final Sheet sheet = SpreadSheet.createFromFile(f).getSheet(0);
		
		//I'd like my first row to be a title, so to match the column index
		//to the title we'll make an hashmap:
		Map<String, Integer> columnDictionary = new TreeMap<String, Integer>();
		for (int col = 0; col < sheet.getColumnCount(); col++) {
			//map the value of each column in row 0 to corresponding column index
			String cellText = sheet.getImmutableCellAt(col, 0).getTextValue();
			columnDictionary.put(cellText.toLowerCase(), col);
		}
		
		//Check that the required column names are set:
		String[] requiredColumns = {"food", "price", "quantity", "description", "size", "special order"};
		for (String key : requiredColumns) {
			if (!columnDictionary.containsKey(key)) {
				System.err.println("Did not find required column name \""+key+"\"");
				throw new ODSParserException("Did not find required column name \""+key+"\"");
			}
		}
		
		// create lists of each column for every row:
		List<String> nameList = ColumnToList(sheet, columnDictionary.get("food")); 
		List<String> categoryList = ColumnToList(sheet, columnDictionary.get("category"));
		List<String> priceList = ColumnToList(sheet, columnDictionary.get("price"));
		List<String> quantityList = ColumnToList(sheet, columnDictionary.get("quantity"));
		List<String> descriptionList = ColumnToList(sheet, columnDictionary.get("description"));
		List<String> sizeList = ColumnToList(sheet, columnDictionary.get("size"));
		List<String> specialOrderList = ColumnToList(sheet, columnDictionary.get("special order"));
		
		//get all unique categories (a set is a list with no duplicates):
		Set<String> uniqueCategoriesList = new HashSet<String>(categoryList);
		
		//iterate and create each category.
		for (String item : uniqueCategoriesList) {
			DefaultMutableTreeNode category = new DefaultMutableTreeNode(item);
			
			CategoryNode linkedCategory = new CategoryNode(item, null);
			LinkedList<FoodItem> linkedFoodItem = new LinkedList<FoodItem>();
			
			for (int row = 1; row < sheet.getRowCount(); row++) {
				int index = row - 1;
				if (categoryList.get(index).equals(item)) {
					String name = nameList.get(index);
					double price = Double.parseDouble(priceList.get(index));
					int quantity = Integer.parseInt(quantityList.get(index));
					String description = descriptionList.get(index);
					String size = sizeList.get(index);
					String specialOrder = specialOrderList.get(index);
					
					//create food item and add it to category
					FoodItem food = new FoodItem(name, price, quantity,
							description, size, specialOrder);
					
					category.add(new DefaultMutableTreeNode(food));
					linkedFoodItem.add(food);
					linkedCategory.setElement(linkedFoodItem);
				}
			}
			
			root.add(category);
			
		}
				
	}
	
	
	/**
	 * Gets an entire column from a sheet as one list.
	 * @param sheet
	 * @param column
	 * @return List<String>
	 */
	private static List<String> ColumnToList(Sheet sheet, int column) {
		List<String> ret = new ArrayList<String>();
		for (int row = 1; row < sheet.getRowCount(); row++) {
			String cellText = sheet.getImmutableCellAt(column, row).getTextValue();
			ret.add(cellText);
		}
		return ret;
	}

	/**
	 * Gets an entire row from one sheet as a list.
	 * @param sheet
	 * @param row
	 * @return List<String>
	 */
	private static List<String> RowToList(Sheet sheet, int row) {
		List<String> ret = new ArrayList<String>();
		for (int col = 0; col < sheet.getColumnCount(); col++)
			ret.add(sheet.getImmutableCellAt(col, row).getTextValue());
		return ret;
	}


	public void setTreeModel(DefaultTreeModel treeModel) {
		this.treeModel = treeModel;
	}


	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}


	public void setListModel(CategoryList listModel) {
		this.listModel = listModel;
	}


	public CategoryList getListModel() {
		return listModel;
	}

}

