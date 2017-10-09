package Mr.Doom.com;

public class BTreeNode {
	 BTreeNode LeftTreeNode;
	 BTreeNode RightTreeNode;
	 String Data;
	 
	 int MID_TREE = 0;
	 int LEFT_TREE = 1;
	 int RIGHT_TREE = 2;

	public BTreeNode() {
		super();
	}
	
	public BTreeNode(String str){
		this(null,null,str);
	}

	public BTreeNode(BTreeNode LeftTreeNode, BTreeNode RightTreeNode,
			String Data) {
		this.LeftTreeNode = LeftTreeNode;
		this.RightTreeNode = RightTreeNode;
		this.Data = Data;
	}
}
