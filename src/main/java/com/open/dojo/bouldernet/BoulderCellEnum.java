package com.open.dojo.bouldernet;

public enum BoulderCellEnum {

	V("Vide"),
	T("Terre"),
	R("Roche"),
	B("Bord"),
	D("Diamant"),
	P("Personne"),
	Q("Personne"),
	S("Sortie");
	
	private String name;
	
	private BoulderCellEnum(String name) {
		this.name = name;
	}
	
	public static BoulderCellEnum getByName(String name) {
		for (BoulderCellEnum boulderCellEnum : BoulderCellEnum.values()) {
			if (boulderCellEnum.name().equals(name)) {
				return boulderCellEnum;
			}
		}
		return BoulderCellEnum.T;
	}
}
