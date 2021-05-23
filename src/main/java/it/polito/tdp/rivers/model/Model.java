package it.polito.tdp.rivers.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	
	RiversDAO dao;
	Map<Integer, River> idMap;
	Simulator simulatore;
	
	public Model() {
		dao = new RiversDAO();
		idMap=new HashMap<>();
		this.dao.getAllRivers(idMap);
		simulatore = new Simulator();
	}
	
	public Collection<River> getFiumi() {
		this.dao.getAllRivers(idMap);
		return idMap.values();
		
	}
	
	public River getInfoFiume(int r) {
		return this.dao.aggiungiInfoFiume(r, idMap);
	}
	
	public List<Flow> getFlows(River fiume){
		return this.dao.getFlows(fiume, idMap);
	}
	
	public void simula(float k, float fmed, River fiume) {
		this.simulatore.init(fmed, k, fiume);
		this.simulatore.run();
	}
	
	public int getGiorniDisservizio() {
		return this.simulatore.getGiorniDisservizio();
	}
	
	public float getCMed() {
		return this.simulatore.getCMed();
	}
	

}
