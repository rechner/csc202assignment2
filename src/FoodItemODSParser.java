import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *  Parses a properly formatted ODF spreadsheet for constructing a FoodItem.
 *  
 * @author Zachary Sturgeon
 *
 */
public class FoodItemODSParser {
	
	private String name;
	private double price; 
	private int quantity;
	private String description;
	private String size;
	private String specialOrder; // false if empty string
	
	public FoodItemODSParser(String filename) throws IOException, ODSParserException {
		
		File f = new File(filename);
		
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

		//Parse first row:
		this.name = sheet.getImmutableCellAt(columnDictionary.get("name"), 1).getTextValue();		
		String priceText = sheet.getImmutableCellAt(columnDictionary.get("price"), 1).getTextValue();
		this.price = Double.parseDouble(priceText);
		String quantityText = sheet.getImmutableCellAt(columnDictionary.get("quantity"), 1).getTextValue();
		this.quantity = Integer.parseInt(quantityText);
		this.description = sheet.getImmutableCellAt(columnDictionary.get("description"), 1).getTextValue();
		this.size = sheet.getImmutableCellAt(columnDictionary.get("size"), 1).getTextValue();
		this.specialOrder = sheet.getImmutableCellAt(columnDictionary.get("specialOrder"), 1).getTextValue();
		
	}
	
	
	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getDescription() {
		return description;
	}

	public String getSize() {
		return size;
	}

	public String getSpecialOrder() {
		return specialOrder;
	}
}

