package io.server.content.votingRewardInterface;

public enum VotingData {
	CASH_1(995, 1000000, "You receive 100k", 16526),
	FOOD_2(13442, 200, "food", 16526),
	DOUBLE_EXP(10934, 1, "double exp", 16526),
	CRYSTTAL_KEY(989, 1, "key", 16526),
	MBOX(16084, 1, "box", 16526),
	VOTE_POINTS(4067, 1, "points", 16526);
		
	private int ItemID;
	private int amount;
	private String text;
	private int interID;

	public int getItemID() {
		return ItemID;
	}


	public int getInterID() {
		return interID;
	}


	public String getText() {
		return text;
	}


	public int getAmount() {
		return amount;
	}


	VotingData(int ItemID, int amount, String text, int interID) {
		this.ItemID = (ItemID);
		this.amount = (amount);
		this.text = (text);
		this.interID = (interID);
	}

	public int getId() {
		return ItemID;
	}

	
}
