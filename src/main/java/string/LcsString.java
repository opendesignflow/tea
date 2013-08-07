/**
 * 
 */
package com.idyria.osi.tea.string;

import java.util.List;

public class LcsString extends LongestCommonSubsequence<Character> {
	private String x;
	private String y;

	public LcsString(String from, String to) {
		this.x = from;
		this.y = to;
	}

	protected int lengthOfY() {
		return y.length();
	}

	protected int lengthOfX() {
		return x.length();
	}

	protected Character valueOfX(int index) {
		return x.charAt(index);
	}

	protected Character valueOfY(int index) {
		return y.charAt(index);
	}

	public String getHtmlDiff() {
		DiffType type = null;
		List<DiffEntry<Character>> diffs = diff();
		StringBuffer buf = new StringBuffer();

		for (DiffEntry<Character> entry : diffs) {
			if (type != entry.getType()) {
				if (type != null) {
					buf.append("</span>");
				}
				buf.append("<span class=\"" + entry.getType().getName() + "\">");
				type = entry.getType();
			}
			buf.append(escapeHtml(entry.getValue()));
		}
		buf.append("</span>");
		return buf.toString();
	}

	private String escapeHtml(Character ch) {
		switch (ch) {
		case '<':
			return "&lt;";
		case '>':
			return "&gt;";
		case '"':
			return "\\&quot;";
		default:
			return ch.toString();
		}
	}
	
	/**
	 * 
	 * @return true if the found LCS is at the start of both string
	 */
	public boolean isLcsStart() {
		
		String lcs = this.getLcs();
		return x.startsWith(lcs) && y.startsWith(lcs);
		
	}

	// EXAMPLE. Here's how you use it.
	public static void main(String[] args) {
//		LcsString seq = new LcsString("<p>the quick brown fox</p>",
//				"<p>the <b>Fast</b> brown dog</p>");
		
		LcsString seq = new LcsString("htoc_bridge_to_app_p_cmd_shift_out",
		"htoc_bridge_to_app_p_cmd_data");
		
		System.out.println("LCS: " + seq.getLcsLength());
		System.out.println("Edit Dist: " + seq.getMinEditDistance());
		System.out.println("Backtrack: " + seq.backtrack());
		System.out.println("HTML Diff: " + seq.getHtmlDiff());
		
		System.out.println("LCS val: " + seq.getLcs());
		
		seq = new LcsString("pcie_bridge_to_vendor_p_data_valid","pcie_bridge_to_vendor_p_sop");
//		System.out.println("LCS: " + seq.getLcsLength());
//		System.out.println("Backtrack: " + seq.backtrack());
		System.out.println("LCS val: " + seq.getLcs());
		
		seq = new LcsString("pcie_bridge_to_vendor_p_sop","pcie_bridge_to_vendor_p_data_valid");
//		System.out.println("LCS: " + seq.getLcsLength());
//		System.out.println("Backtrack: " + seq.backtrack());
		System.out.println("LCS val: " + seq.getLcs());
		
		seq = new LcsString("pcie_bridge_to_vendor_rr_sop","htoc_bridge_to_app_rr_cmd_shift_out");
//		System.out.println("LCS: " + seq.getLcsLength());
//		System.out.println("Backtrack: " + seq.backtrack());
		System.out.println("LCS val: " + seq.getLcs());
		
	
		
	}

}
