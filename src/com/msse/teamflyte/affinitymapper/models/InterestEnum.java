package com.msse.teamflyte.affinitymapper.models;


public enum InterestEnum {

	SOCCER("Soccer"), GUITAR("Guitar"), COFFEE("Coffee");

	private String displayName;

	private InterestEnum(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

//	public static List<String> filterInterestGroups(List<String> interestGroups)
//	{
//		List<String> filteredInterestGroups = new ArrayList<String>();
//		for(String interestGroup : interestGroups)
//		{
//			if(isMember(interestGroup))
//			{
//				filteredInterestGroups.add(interestGroup);
//			}
//		}
//		return filteredInterestGroups;
//	}
//
//	public static boolean isMember(String interestGroup) {
//		InterestEnum[] interests = InterestEnum.values();
//		for (InterestEnum interest : interests)
//			if (interest.displayName.equals(interestGroup)) {
//				return true;
//			}
//		return false;
//	}
}
