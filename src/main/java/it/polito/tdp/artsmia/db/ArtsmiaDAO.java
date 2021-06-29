package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artista;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getRuoli() {
		String sql = "SELECT a.`role` as ruolo "
				+ "FROM `authorship` as a "
				+ "GROUP BY a.`role` "
				+ "ORDER BY a.`role` ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("ruolo"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadIdMap(Map<Integer, Artista> idMap, String ruolo) {
		String sql = "SELECT art.`artist_id` as id , art.`name` as nome "
				+ "FROM `authorship` as a, `artists` as art "
				+ "WHERE a.`artist_id` = art.`artist_id` "
				+ "	AND a.`role` = ? "
				+ "GROUP BY  art.`artist_id`, art.`name` ";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artista a = idMap.get(res.getInt("id"));
				if(a == null) {
					a = new Artista(res.getInt("id"), res.getString("nome"));
					idMap.put(a.getId(), a);
				}
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

	public List<Adiacenza> getAdiacenze(Map<Integer, Artista> idMap, String ruolo) {
		String sql = "SELECT a1.`artist_id` as id1,  a2.`artist_id` as id2 , COUNT(*) as peso "
				+ "FROM `authorship` as a1,  `authorship` as a2, `exhibition_objects` as eo1, `exhibition_objects` as eo2 "
				+ "WHERE a1.`artist_id` >  a2.`artist_id` "
				+ "	AND a1.`role` = ? "
				+ "	AND a1.`role` = a2.`role` "
				+ "	AND a1.`object_id`=eo1.`object_id` "
				+ "	AND a2.`object_id`=eo2.`object_id` "
				+ "	AND eo1.`exhibition_id` = eo2.`exhibition_id` "
				+ "GROUP BY  a1.`artist_id`,  a2.`artist_id` ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artista a1 = idMap.get(res.getInt("id1"));
				Artista a2 = idMap.get(res.getInt("id2"));
				if(a1 != null && a2 != null) {
					result.add(new Adiacenza (a1, a2, res.getDouble("peso")));
				}
			}
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
