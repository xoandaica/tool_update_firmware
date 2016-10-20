package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

import java.util.ArrayList;

public class Leaf extends TreeNode{

	private ArrayList<TreeNode> pre = new ArrayList<TreeNode>();
	
	private String value;
	
	public Leaf(String nameID) {
		super(nameID);
		this.type = TreeNode.LEAF;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<TreeNode> getPre() {
		return pre;
	}

	public void setPre(ArrayList<TreeNode> pre) {
		this.pre = pre;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
}
