import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 *
 * @author Zachary Sturgeon
 */
public class FoodItemGUI extends JFrame implements TreeSelectionListener {

	private static final long serialVersionUID = 1L;
	
	private DefaultMutableTreeNode root = new DefaultMutableTreeNode("Food");
	private DefaultMutableTreeNode selectedNode = null; //holds currently selected item
	
	/* There are two TreeModels, one which holds the accurate data, and a copy
	 * used for showing filtered search results.  When the query is blank,
	 * this.treeModel will be shown. Otherwise results are placed in 
	 * this.filterModel.	*/
	private DefaultTreeModel treeModel = new DefaultTreeModel(root);
	private DefaultTreeModel filterModel = new DefaultTreeModel(root);
	
	/** Creates new form FoodItemGUI */
    public FoodItemGUI() {
        initComponents();
        makeEditable(false); //nothing selected by default
        
        // default to search operation:
        searchTextbox.requestFocus();
        
        /*// test data structure
        DefaultMutableTreeNode category = new DefaultMutableTreeNode("Beverages");
        DefaultMutableTreeNode food = new 
        		DefaultMutableTreeNode(new FoodItem("Coffee", 1.25, 100, 
				"Brewed caffienated beverage", "Medium", ""));
        category.add(food);
        
        food = new DefaultMutableTreeNode(new FoodItem("Water", 0.0, 1000, 
				"Complimentary ice water", "Medium", ""));
        category.add(food);
        
        food = new DefaultMutableTreeNode(new FoodItem("Tea", 1.00, 1000, 
				"Hot brewed tea", "Medium", ""));
        category.add(food);
        
        food = new DefaultMutableTreeNode(new FoodItem("Juice", 1.00, 1000, 
				"Fruit juice", "Medium", ""));
        category.add(food);
        this.root.add(category);
        
        category = new DefaultMutableTreeNode("Sides");
        food = new DefaultMutableTreeNode(new FoodItem("Chips", 2.00, 400, 
				"(American: French Fries) - fried potato wedges", "Medium", ""));
        category.add(food);
        food = new DefaultMutableTreeNode(new FoodItem("Onion Rings", 2.00, 400, 
				"Rings of onion", "Medium", ""));
        category.add(food);
        this.root.add(category);
        
        categoryCombobox.removeAllItems();
        categoryCombobox.addItem("Beverages");
        categoryCombobox.addItem("Sides");*/
        
        this.root.add(new DefaultMutableTreeNode("Unsorted"));
        
        ResultTree.setModel(this.treeModel);
        
        // expand out all categories so we can see everything
        this.expandTree(ResultTree);
    }
    
    private void initComponents() {
    	
    	// Make the UI rendering not look like vomit
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // Can't handle this, it'll just have to look like crap.
	    }
	    catch (ClassNotFoundException e) {
	       // And here
	    }
	    catch (InstantiationException e) {
	       // *sigh*...
	    }
	    catch (IllegalAccessException e) {
	       // please make it stop
	    }

    	ResultTree = new JTree();
    	
        searchTextbox = new JTextField();
        searchButton = new JButton();
        
        jSeparator1 = new JSeparator();
        nameLabel = new JLabel();
        nameTextbox = new JTextField();
        categoryLabel = new JLabel();
        categoryCombobox = new JComboBox();
        addCategoryButton = new JButton();
        
        priceLabel = new JLabel();
        NumberFormat fformat = NumberFormat.getNumberInstance();
        fformat.setMinimumFractionDigits(2);
        fformat.setMaximumFractionDigits(2);
        priceTextbox = new JFormattedTextField(fformat);
        
        quantityLabel = new JLabel();
        NumberFormat nformat = NumberFormat.getNumberInstance();
        nformat.setMaximumFractionDigits(0);
        quantityTextbox = new JFormattedTextField(nformat);
        
        descriptionLabel = new JLabel();
        descriptionTextbox = new JTextField();
        sizeLabel = new JLabel();
        sizeCombobox = new JComboBox();
        specialOrderCheckbox = new JCheckBox();
        specialOrderTextbox = new JTextField();
        newButton = new JButton();
        saveButton = new JButton();
        deleteButton = new JButton();
        ScrollPane = new JScrollPane();
        
        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        optionMenu = new JMenu();
        openMenuItem = new JMenuItem();
        importODFMenuItem = new JMenuItem();
        saveMenuItem = new JMenuItem();
        quitMenuItem = new JMenuItem();
        menuSeparator1 = new JPopupMenu.Separator();
        instantSearchMenuItem = new JCheckBoxMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Bind action listeners and events:
        
        // Bind enter key (TextBox throws actionPerformed when enter is pressed)
        searchTextbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            	searchButtonMouseClicked(null);
            }
        });
        // If text is blank, remove the search filter:
        searchTextbox.getDocument().addDocumentListener(new DocumentListener() {
        	  public void changedUpdate(DocumentEvent e) {
        	    clear();
        	  }
        	  public void removeUpdate(DocumentEvent e) {
        	    clear();
        	  }
        	  public void insertUpdate(DocumentEvent e) {
        	    clear();
        	  }
        	  
        	  private void clear() {
        		  if (searchTextbox.getText().isEmpty() || instantSearchMenuItem.isSelected()) 
        			  searchButtonMouseClicked(null);
        	  }
        });
        
        
        searchButton.setText("Search");
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchButtonMouseClicked(evt);
            }
        });

        nameLabel.setText("Name");

        nameTextbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextboxActionPerformed(evt);
            }
        });

        categoryLabel.setText("Category");

        categoryCombobox.setModel(new DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        categoryCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryComboboxActionPerformed(evt);
            }
        });

        addCategoryButton.setText("Add");
        addCategoryButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addCategoryButtonMouseClicked(evt);
            }
        });

        priceLabel.setText("Price");
        priceTextbox.addFocusListener(new FocusListener() {
			private String oldval;
			@Override
			public void focusLost(FocusEvent e) {
				// put it back when we're done
				if (priceTextbox.getText().equals(""))
					priceTextbox.setText(oldval);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// clear the input when focused to keep from wrestling with formatter
				oldval = priceTextbox.getText();
				priceTextbox.setText("");
			}
		});       

        quantityLabel.setText("Quantity");
        quantityTextbox.addFocusListener(new FocusListener() {
        	private String oldval;
			@Override
			public void focusLost(FocusEvent e) {
				if (quantityTextbox.getText().equals(""))
					quantityTextbox.setText(oldval);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				oldval = quantityTextbox.getText();
				quantityTextbox.setText("");
			}
		});

        descriptionLabel.setText("Description");

        sizeLabel.setText("Size");

        sizeCombobox.setModel(new DefaultComboBoxModel(new String[] { "Small", "Medium", "Large" }));

        specialOrderCheckbox.setText("Special Order");

        specialOrderCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specialOrderCheckboxActionPerformed(evt);
            }
        });

        newButton.setIcon(new ImageIcon("pixmaps/add.png")); // NOI18N
        newButton.setText("New");
        newButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newButtonMouseClicked(evt);
            }
        });

        saveButton.setIcon(new ImageIcon("pixmaps/stock_save.png")); // NOI18N
        saveButton.setText("Save");
        saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveButtonMouseClicked(evt);
            }
        });

        deleteButton.setIcon(new ImageIcon("pixmaps/edit-delete.png")); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteButtonMouseClicked(evt);
            }
        });
        

        ResultTree.setEditable(false);
        ResultTree.setRootVisible(false);
        ResultTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        ResultTree.addTreeSelectionListener(this); // catch events in this frame
        ScrollPane.setViewportView(ResultTree);

        jMenu1.setText("File");
        optionMenu.setText("Options");

        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		openMenuItemMouseClicked(evt);
        	}
        });
        jMenu1.add(openMenuItem);

        importODFMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        importODFMenuItem.setText("Import ODF");
        importODFMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		importMenuItemMouseClicked(evt);
        	}
        });
        jMenu1.add(importODFMenuItem);

        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		saveMenuItemMouseClicked(evt);
        	}
        });
        jMenu1.add(saveMenuItem);

        jMenu1.add(menuSeparator1);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		quitMenuItemMouseClicked(evt);
        	}
        });
        jMenu1.add(quitMenuItem);
        
        jMenuBar1.add(jMenu1);
        
        instantSearchMenuItem.setText("Enable Instant Search");
        optionMenu.add(instantSearchMenuItem);
        jMenuBar1.add(optionMenu);

        setJMenuBar(jMenuBar1);
        
        
        // Bind misc events
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ResultTree.addMouseListener(new PopClickListener());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ScrollPane, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(searchTextbox, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(searchButton))
                    .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(categoryLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoryCombobox, 0, 182, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addCategoryButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(priceLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(priceTextbox, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(quantityLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(quantityTextbox, GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                            .addComponent(nameTextbox, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descriptionLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descriptionTextbox, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sizeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sizeCombobox, 0, 270, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(specialOrderCheckbox)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(specialOrderTextbox, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newButton, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(deleteButton, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(categoryCombobox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryLabel)
                    .addComponent(addCategoryButton))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(priceTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceLabel)
                    .addComponent(quantityLabel)
                    .addComponent(quantityTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(sizeCombobox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeLabel))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(specialOrderTextbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(specialOrderCheckbox))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton)
                    .addComponent(deleteButton)
                    .addComponent(saveButton))
                .addGap(8, 8, 8))
            .addComponent(ScrollPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );

        pack();
    }
    
    
    
    private void makeEditable(boolean editable) {
    	// Disable editing context
		nameLabel.setEnabled(editable);
        nameTextbox.setEnabled(editable);
        categoryLabel.setEnabled(editable);
        categoryCombobox.setEnabled(editable);
        priceLabel.setEnabled(editable);
        priceTextbox.setEnabled(editable);
        quantityLabel.setEnabled(editable);
        quantityTextbox.setEnabled(editable);
        descriptionLabel.setEnabled(editable);
        descriptionTextbox.setEnabled(editable);
        sizeLabel.setEnabled(editable);
        sizeCombobox.setEnabled(editable);
        specialOrderCheckbox.setEnabled(editable);
        specialOrderTextbox.setEnabled(editable);
        deleteButton.setEnabled(editable);
        saveButton.setEnabled(editable);
    }
    
    private void setFoodItem(FoodItem food, String category) {
    	if (category != null) {
    		categoryCombobox.setSelectedItem(category);
    	}
    	
    	if (food != null) {
	    	nameTextbox.setText(food.getName());
	    	priceTextbox.setValue(food.getPrice());
	    	quantityTextbox.setText(""+food.getQuantity());
	    	descriptionTextbox.setText(food.getDescription());
	    	sizeCombobox.setSelectedItem(food.getSize());
	    	if (food.getSpecialOrder().equals("")) {
	    		specialOrderCheckbox.setSelected(false);
	    		specialOrderTextbox.setEnabled(false);
	    		specialOrderTextbox.setText("");
	    	} else {
	    		specialOrderCheckbox.setSelected(true);
	    		specialOrderTextbox.setEnabled(true);
	    		specialOrderTextbox.setText(food.getSpecialOrder());
	    	}
    	} else {
	    	nameTextbox.setText("");
	    	categoryCombobox.setSelectedItem(null);
	    	priceTextbox.setValue(null);   // must use null, otherwise
	    	quantityTextbox.setText(null); // number formatter throws a fit.
	    	descriptionTextbox.setText("");
	    	sizeCombobox.setSelectedItem(null);
	    	specialOrderCheckbox.setSelected(false);
	    	specialOrderTextbox.setEnabled(false);
    		specialOrderTextbox.setText("");
	    }
    	
    }
    
    /** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent evt) {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) ResultTree.getLastSelectedPathComponent();
    	
    	if (node == null)
    	    //Nothing is selected.     
    	    return;
    	
    	this.selectedNode = node;
    	
    	Object nodeInfo = node.getUserObject();
    	if (node.isLeaf() && (nodeInfo instanceof FoodItem)) {
    		//TODO: Get parent node and set it as the category
    		FoodItem selected = (FoodItem) nodeInfo;
    		
    		//System.out.print(selected.toString() + " @ ");
    		//System.out.println(node.getParent() + "/" + node.getParent().getIndex(node));
    		this.makeEditable(true);
    		this.setFoodItem(selected, node.getParent().toString());
    	} else {
    		this.makeEditable(false);
    		this.setFoodItem(null, null);
    	}
    }                               

    private void nameTextboxActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void categoryComboboxActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                
    

    private void specialOrderCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
    	
    	if (specialOrderCheckbox.isSelected())
    		specialOrderTextbox.setText("");
		specialOrderTextbox.setEnabled(specialOrderCheckbox.isSelected());
    	
    }           
    
    private DefaultTreeModel copyTree(DefaultTreeModel original) {
    	DefaultMutableTreeNode parent = this.copyNode((DefaultMutableTreeNode) original.getRoot());
    	return new DefaultTreeModel(parent);
    }
    
    /**
     * Creates a deep copy of a node.
     * @param original
     * @return DefaultMutableTreeNode
     */
    private DefaultMutableTreeNode copyNode(DefaultMutableTreeNode original) {
    	DefaultMutableTreeNode copy = new DefaultMutableTreeNode(original.getUserObject());
    	
    	Enumeration<DefaultMutableTreeNode> categories = original.children();
    	while (categories.hasMoreElements()) {
    		DefaultMutableTreeNode category = categories.nextElement();
    		DefaultMutableTreeNode categoryCopy = null;
    		
    		if ((!category.isLeaf()) && (category.getUserObject() instanceof String)) {
    			categoryCopy = new DefaultMutableTreeNode(category.getUserObject());
    			Enumeration<DefaultMutableTreeNode> leaves = category.children();
    			while (leaves.hasMoreElements()) {
    				DefaultMutableTreeNode leaf = leaves.nextElement();
    				categoryCopy.add(new DefaultMutableTreeNode(leaf.getUserObject()));
    				
    			}
    		}
    		
    		if ((category.getUserObject() instanceof String) && (category != null))
    			copy.add(categoryCopy);
    	}
    	
    	return copy;
    }

    private void searchButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	//get search query:
    	String query = searchTextbox.getText();
    	
    	if (!query.isEmpty()) {
    		//make copy of tree and root:
    		this.filterModel = this.copyTree(this.treeModel);
    		DefaultMutableTreeNode rootCopy = (DefaultMutableTreeNode) this.filterModel.getRoot();
    		
    		List<DefaultMutableTreeNode> removeQueue = new ArrayList<DefaultMutableTreeNode>();
    		
    		@SuppressWarnings("unchecked")
			Enumeration<DefaultMutableTreeNode> e = rootCopy.breadthFirstEnumeration(); 		
    		while (e.hasMoreElements()) {
    			
    	        DefaultMutableTreeNode node = e.nextElement();
    	        
    	      //this might be a FoodItem or a string (category).
    	        Object nodeInfo = node.getUserObject();	
    	        		
	    		if (node.isLeaf() && (nodeInfo instanceof FoodItem)) {
		    		FoodItem item = (FoodItem) node.getUserObject();
		    		
		    		//queue for deletion everything that doesn't match the query:
	    	        if (!(item.getName().matches("(?i)"+query+"(.*)"))) {
	    	        	//if we try to remove the node here, it'll invalidate the enumeration
	    	        	//so we'll queue it instead.
	    	        	removeQueue.add(node);
	    	            
	    	        }
		    	}	
    		}
    		
    		//Remove unmatched results outside of enumeration:
    		for (int i = 0; i < removeQueue.size(); i++) {
    			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(removeQueue.get(i).getParent());
    			if (parent != null)
    				this.filterModel.removeNodeFromParent(removeQueue.get(i));
    		}
    		
    		ResultTree.setModel(this.filterModel);
    		// expand out all categories so we can see everything
            this.expandTree(ResultTree);
    		
    	} else {
    		
    		ResultTree.setModel(this.treeModel);
    		// expand out all categories so we can see everything
            this.expandTree(ResultTree);
    	
    	}
    }                                         

    private void addCategoryButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	String category = JOptionPane.showInputDialog("Enter name of new category");
    	if (category != null) {
    		 // TODO handle LinkedList
    		this.root.add(new DefaultMutableTreeNode(category));
    		treeModel.reload();
    		// expand out all categories so we can see everything
            this.expandTree(ResultTree);
            updateCategoryCombobox();
    	}
    }
    
    private void updateCategoryCombobox() {
    	categoryCombobox.removeAllItems();
    	
    	Enumeration<DefaultMutableTreeNode> categories = root.children();
    	while(categories.hasMoreElements()) {
    		
    		DefaultMutableTreeNode category = categories.nextElement();
    		if (category != null) 
    			categoryCombobox.addItem(category.getUserObject().toString());	
    	}
    }

    private void newButtonMouseClicked(java.awt.event.MouseEvent evt) {
        // Add a new FoodItem with some default values
    	DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
    			new FoodItem("New Food", 0.0, 0, "", "Medium", ""));
    	
    	DefaultMutableTreeNode parent = null; 
    	if (this.selectedNode != null) {
    		parent = this.getParentByName(this.selectedNode.getParent().toString());
    		if (parent.getParent() == null) 
    			parent = (DefaultMutableTreeNode) this.selectedNode;
    	} else {
    		//pick the first node:
    		parent = this.root.getFirstLeaf();
    	}
    	
    	parent.add(newNode);
    	updateCategoryCombobox();
    	setAutoCompleteList(searchTextbox);
		
		this.selectedNode = newNode;
		// notify listening parties that the tree has changed.
    	treeModel.reload();
    	this.expandTree(ResultTree); 	// keep everything expanded
    	// set selection back to the same object.
    	ResultTree.getSelectionModel().setSelectionPath(new TreePath(treeModel.getPathToRoot(selectedNode)));
    }
    
    
    /**
     * Returns the corresponding parent node for a string category LinkedList node. 
     * 
     * @param name
     * @return DefaultMutableTreeNode
     */
    private DefaultMutableTreeNode getParentByName(String name) {
    	@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = this.root.breadthFirstEnumeration(); 		
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = e.nextElement();
			if (node.getUserObject().equals(name))
				return node;
		}
		
		return null;
    }

    private void saveButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	//Read values:
    	String name = nameTextbox.getText();
    	String category = (String) categoryCombobox.getSelectedItem();
    	String price = priceTextbox.getText();
    	String quantity = quantityTextbox.getText();
    	String description = descriptionTextbox.getText();
    	String size = (String) sizeCombobox.getSelectedItem();    	
    	String specialOrder = "";
    	if (specialOrderCheckbox.isSelected())
    		specialOrder = specialOrderTextbox.getText();
    	
    	selectedNode.setUserObject(new FoodItem(name, Double.parseDouble(price), 
    			Integer.parseInt(quantity), description, size, specialOrder));
    	
    	if (!selectedNode.getParent().equals(category)) {
    		// category has changed, move to the appropriate parent:
    		DefaultMutableTreeNode parent = this.getParentByName(category);
    		if (parent != null) {
    			// delete node and add to the new parent
    			this.treeModel.removeNodeFromParent(this.selectedNode);
    			this.selectedNode = new DefaultMutableTreeNode(new 
    				FoodItem(name, Double.parseDouble(price), 
    				Integer.parseInt(quantity), description, size, specialOrder));
    			parent.add(this.selectedNode);
    		}
    			
    	}
    	
    	// notify listening parties that the tree has changed.
    	treeModel.reload();
    	this.expandTree(ResultTree); 	// keep everything expanded
    	// set selection back to the same object.
    	ResultTree.getSelectionModel().setSelectionPath(new TreePath(treeModel.getPathToRoot(selectedNode)));
    	
    }
    
    private void deleteSelectedNode(DefaultMutableTreeNode node) {
    	
    	int n = JOptionPane.showConfirmDialog(this,
    		    "Are you sure you want to delete the selected item?\n"
    		    +((!node.isLeaf()) ? "The entire '" + node + 
    		    		"' category will be removed.\n\n" : 
    		    	node + " will be removed.\n\n")
    		    + "This action cannot be undone."
    		    ,
    		    "Confirm Delete",
    		    JOptionPane.YES_NO_OPTION);
    	
    	//yes
    	if (n == 0) {
    		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(this.selectedNode.getParent());
            if (parent != null) {
                this.treeModel.removeNodeFromParent(this.selectedNode);
            }
            
            if (!this.selectedNode.isLeaf()) {
            	updateCategoryCombobox();
            }
            
            setAutoCompleteList(searchTextbox);
    	}
    }

    private void deleteButtonMouseClicked(java.awt.event.MouseEvent evt) {
    	deleteSelectedNode(this.selectedNode);    	
    }
    
    private void deleteMenuActionPerformed(ActionEvent evt) {
    	deleteSelectedNode(this.selectedNode);  
    }
    
    private void expandTree(JTree jTree) {
    	for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
    	}
    }
    
    private void importMenuItemMouseClicked(ActionEvent evt) {
		
    	// open file dialogue and parse ODF file:
    	JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "ODF spreadsheet", "ods");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           
           try {
        	   	ODSParserFactory parser = new ODSParserFactory(chooser.getSelectedFile());
        	   	
        	   	this.treeModel = parser.getTreeModel();
        	   	this.root = (DefaultMutableTreeNode) treeModel.getRoot();
        	   	
        	   	ResultTree.setModel(this.treeModel);
        		// expand out all categories so we can see everything
                this.expandTree(ResultTree);
                // update the category combobox:
                updateCategoryCombobox();
                setAutoCompleteList(searchTextbox);
        	   	
			} catch (ODSParserException e) {
				// missing column exception:
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"Parse Error", JOptionPane.ERROR_MESSAGE);
				
			} catch (IOException e) {
				// error reading file.
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"Import Error", JOptionPane.ERROR_MESSAGE);
				
			}
        }
		
	}
    
    private ArrayList<String> getSuggestionList() {
    	
    	ArrayList<String> ret = new ArrayList<String>();
    	
    	@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = this.root.breadthFirstEnumeration(); 		
		while (e.hasMoreElements()) {
			
	        DefaultMutableTreeNode node = e.nextElement();
	        
	      //this might be a FoodItem or a string (category).
	        Object nodeInfo = node.getUserObject();	
	        		
    		if (node.isLeaf() && (nodeInfo instanceof FoodItem)) {
	    		FoodItem item = (FoodItem) node.getUserObject();
	    		ret.add(item.getName());
    		}
		}
		
		return ret;
    }
    
    private void setAutoCompleteList(JTextField textfield) {
    	AutoCompleteDecorator.decorate(searchTextbox, getSuggestionList(), false);
    }
    
    private void openMenuItemMouseClicked(ActionEvent evt) {
    	
    	// open file dialogue and parse binary file:
    	JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        	"FoodItem binary file", "bin", "food");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	try {
				FileInputStream fileIn = new FileInputStream(chooser.getSelectedFile());
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				
				this.treeModel = (DefaultTreeModel) objectIn.readObject(); 
				objectIn.close();
				fileIn.close();
				
				this.root = (DefaultMutableTreeNode) this.treeModel.getRoot();
				ResultTree.setModel(this.treeModel);
        		// expand out all categories so we can see everything
                this.expandTree(ResultTree);
                // update the category combobox:
                updateCategoryCombobox();
                setAutoCompleteList(searchTextbox);
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"File I/O Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"Parse Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"Casting Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
        }
		
	}
    
    private void saveMenuItemMouseClicked(ActionEvent evt) {
    	JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "FoodItem binary file", "bin", "food");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if ((returnVal == JFileChooser.APPROVE_OPTION) && 
            (chooser.getSelectedFile() != null)) {
        	
        	try {
				FileOutputStream fileOut = new FileOutputStream(chooser.getSelectedFile());
				ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
				outStream.writeObject(ResultTree.getModel());
				outStream.close();
				fileOut.close();

			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage(), 
						"Error While Saving", JOptionPane.ERROR_MESSAGE);
			}
        	
        }
    }
    
    private void quitMenuItemMouseClicked(ActionEvent evt) {
    	System.exit(0);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FoodItemGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JTree ResultTree;
    private JButton addCategoryButton;
    private JComboBox categoryCombobox;
    private JLabel categoryLabel;
    private JButton deleteButton;
    private JLabel descriptionLabel;
    private JTextField descriptionTextbox;
    private JMenuItem importODFMenuItem;
    private JMenu jMenu1;
    private JMenu optionMenu;
    private JCheckBoxMenuItem instantSearchMenuItem;
    private JMenuBar jMenuBar1;
    private JScrollPane ScrollPane;
    private JSeparator jSeparator1;
    private JPopupMenu.Separator menuSeparator1;
    private JLabel nameLabel;
    private JTextField nameTextbox;
    private JButton newButton;
    private JMenuItem openMenuItem;
    private JLabel priceLabel;
    private JFormattedTextField priceTextbox;
    private JLabel quantityLabel;
    private JTextField quantityTextbox;
    private JMenuItem quitMenuItem;
    private JButton saveButton;
    private JMenuItem saveMenuItem;
    private JButton searchButton;
    private JTextField searchTextbox;
    private JComboBox sizeCombobox;
    private JLabel sizeLabel;
    private JCheckBox specialOrderCheckbox;
    private JTextField specialOrderTextbox;
    // End of variables declaration
    
    class PopUpDemo extends JPopupMenu {
		private static final long serialVersionUID = 1L;
		JMenuItem anItem;
        public PopUpDemo(){
            anItem = new JMenuItem("Delete Selected");
            anItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					deleteMenuActionPerformed(arg0);
				}
			});
            add(anItem);
        }
    }
    
    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e){
            PopUpDemo menu = new PopUpDemo();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

}

