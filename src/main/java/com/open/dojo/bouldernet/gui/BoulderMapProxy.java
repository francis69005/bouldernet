package com.open.dojo.bouldernet.gui;

import com.open.dojo.bouldernet.DirectionEnum;

public interface BoulderMapProxy {

	void move(DirectionEnum direction);
	
	void addMapListener(MapListener listener);

}
