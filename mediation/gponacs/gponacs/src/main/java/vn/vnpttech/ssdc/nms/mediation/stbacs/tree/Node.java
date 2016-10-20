package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

import java.util.ArrayList;

public class Node extends TreeNode{

	private ArrayList<TreeNode> nextNode = new ArrayList<TreeNode>();
	
	TreeNode parentNode = new TreeNode();
	
	public Node(String nameID) {
		super(nameID);
		this.type = TreeNode.NODE;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<TreeNode> getNextNode() {
		return nextNode;
	}

	public void setNextNode(ArrayList<TreeNode> next) {
		this.nextNode = next;
	}

	public TreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode parent) {
		this.parentNode = parent;
	}

	public TreeNode getChildNode(String idNode){
		for(int i=0;i<nextNode.size();i++){
			if(nextNode.get(i).getNameID().equals(idNode))return nextNode.get(i);			
		}		
		return null;
	}
	
}
