package Mr.Doom.com;

import java.util.ArrayList;
import java.util.List;

public class BinaryTree {
	private BTreeNode root;

	private List<String> list;

	public BinaryTree(BTreeNode root) {
		this.root = root;
		list = new ArrayList<String>();
	}

	public BTreeNode getRoot() {
		return root;
	}

	public void insert(String data) {
		BTreeNode node = new BTreeNode(data);
		if (this.root == null) {
			this.root = node;
		} else {
			BTreeNode CurrentNode = this.root;
			while (true) {
				if (CurrentNode.LeftTreeNode == null) {
					CurrentNode.LeftTreeNode = node;
					return;
				} else if (CurrentNode.RightTreeNode == null) {
					CurrentNode.RightTreeNode = node;
					return;
				}
				CurrentNode = CurrentNode.RightTreeNode;
			}
		}
	}

	public List<String> order(BTreeNode TreeNode){
		System.out.println("-----------------------");
		if(TreeNode != null){
			if(TreeNode.LeftTreeNode != null){
				System.out.println(TreeNode.LeftTreeNode.Data);
				list.add(TreeNode.LeftTreeNode.Data);
				order(TreeNode.LeftTreeNode);
			}
			if(TreeNode.RightTreeNode != null){
				System.out.println(TreeNode.RightTreeNode.Data);
				list.add(TreeNode.RightTreeNode.Data);
				order(TreeNode.RightTreeNode);
			}
		}
		return list;
	}
}
