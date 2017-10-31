package com.mathologic.projects.crewlink.custom.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CircularList<T> extends ArrayList<T> {
	

	public CircularList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CircularList(Collection<? extends T> c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
    public T get(int index) {
		if(size()>0)
			return super.get(index % size());
		else
			return null;
    }
}
