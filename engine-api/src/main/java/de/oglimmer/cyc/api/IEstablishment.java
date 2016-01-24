package de.oglimmer.cyc.api;

import java.util.List;

import de.oglimmer.cyc.collections.JavaScriptList;

public interface IEstablishment {

	ICompany getParent();

	JavaScriptList<IEmployee> getEmployees();

	String getAddress();

	List<InteriorAccessory> getInteriorAccessories();

	int getLocationSize();

}
