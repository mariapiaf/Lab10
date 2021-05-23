package it.polito.tdp.rivers.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.rivers.model.Flow;
import it.polito.tdp.rivers.model.River;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RiversDAO {

	public void getAllRivers(Map<Integer, River> idMap) {
		
		final String sql = "SELECT id, name FROM river";


		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!idMap.containsKey(res.getObject("id"))) {
					River r = new River(res.getInt("id"), res.getString("name"));
					idMap.put(r.getId(), r);
				}
			}

			conn.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

	}
	
	public River aggiungiInfoFiume(int fiume, Map<Integer, River> idMap){
		String sql = "SELECT f.id, f.day, f.flow, f.river, r.id, r.name, COUNT(*) AS totMis, MIN(f.day) AS min, MAX(f.day) AS max, AVG(f.flow) AS avg "
				+ "FROM flow f,river r "
				+ "WHERE f.river = ? AND r.id=f.river "
				+ "ORDER BY day";
		
		
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fiume);
			ResultSet res = st.executeQuery();
			
			River r=idMap.get(fiume);
			if(res.next()) {
				r.setTotMisurazioni(res.getInt("totMis"));
				r.setFlowAvg(res.getDouble("avg"));
				r.setPrimaMisurazione(res.getTimestamp("min").toLocalDateTime().toLocalDate());
				r.setUltimaMisurazione(res.getTimestamp("max").toLocalDateTime().toLocalDate());
			}
			conn.close();
			return r;
			
		} catch (SQLException e) {
			throw new RuntimeException("SQL Error");
		}

	}
	
	public List<Flow> getFlows(River fiume, Map<Integer, River> idMap){
		String sql = "SELECT * "
				+ "FROM flow "
				+ "WHERE river = ?";
		
		List<Flow> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fiume.getId());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				River river = idMap.get(res.getInt("river"));
				result.add(new Flow(res.getTimestamp("day").toLocalDateTime().toLocalDate(), res.getFloat("flow"), river));
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}
	}
}
