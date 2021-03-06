package com.angelPods.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.angelPods.dto.FbDto;

public class FbDao {
	
	DataSource dataSource;
	
	private static FbDao instance = new FbDao();
	
	private FbDao() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static FbDao getInstance() {
		return instance;
	}
	
	public int write(String userId, int cNum, int cdNum,
			String sido, String gue, String dong , String addrDetail, String title, String contents, String sn, String lat, String lng) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int pk = -1;
		
		/*
		 * //TODO 쭌 : select와 insert를 함께 사용해서 트랜잭션으로 문제 생길 수 있고 해답은 to 트랜잭션 공부할 것 int
		 * fbNum = -1; try { con = dataSource.getConnection(); String sqlIdentifier =
		 * "SELECT AUTO_INCREMENT " + "FROM information_schema.tables " +
		 * "WHERE table_name = 'FIND_BOARD' " + "AND table_schema = DATABASE( ) ;";
		 * pstmt = con.prepareStatement(sqlIdentifier); ResultSet rsNum =
		 * pstmt.executeQuery(); if (rsNum.next()) { fbNum = rsNum.getInt(1); } }
		 * catch(Exception e) { e.printStackTrace();
		 * 
		 * //TODO 0905 추가함 테스트필요.================ } finally { try { if(pstmt != null)
		 * pstmt.close(); if(con != null) con.close(); } catch (Exception e2) {
		 * e2.printStackTrace(); } } //===============================
		 */		
		try {
			con = dataSource.getConnection();
			String query = "insert into find_board "
					+ "(userid, c_num, cd_num, sido, gue, dong, addrdetail, title, contents, sn, lat, lng) values"
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
//			pstmt.setInt(1, fbNum);
			pstmt.setString(1, userId);
			pstmt.setInt(2, cNum);
			pstmt.setInt(3, cdNum);
			pstmt.setString(4, sido);
			pstmt.setString(5, gue);
			pstmt.setString(6, dong);
			pstmt.setString(7, addrDetail);
			pstmt.setString(8, title);
			pstmt.setString(9, contents);
			pstmt.setString(10, sn);
			pstmt.setString(11, lat);
			pstmt.setString(12, lng);
			pstmt.executeUpdate();
			
			
			try(ResultSet rs = pstmt.getGeneratedKeys()){
				if(rs.next()) {
					pk = rs.getInt(1);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return pk;
	}
	
	public ArrayList<FbDto> list() {
		ArrayList<FbDto> dtos = new ArrayList <FbDto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			//TODO 행정동을 기준으로 리스트를 불러오는 것 구현 -> JSON으로 주소정보를 받아서 저장할 FIND_BOARD_ADDR 테이블 만들고, 두 테이블로 뷰를 만드는 방법 학습
			//TODO 자신의 위치를 기준으로 행정동을 찾아내는 것 구현 "성동구 응봉동의 <습득물>을 찾습니다" 왼쪽 상단 표시, 행정동으로 정렬
			String query = "select " + 
					"    F.fb_num, F.userId, C.name C_NAME , CD.name CD_NAME, " + 
					"    F.timestamp, F.sido, F.gue, F.dong , F.addrDetail, F.title, F.contents, F.sn, F.lat, F.lng, F.chat_hit, " + 
					"    F.hit, F.f_state, F.c_num, F.cd_num, fImg.imagesystemname thumbnailImage " + 
					"from find_board F " + 
					"    left outer join CATEGORY C on C.c_Num = F.c_num " + 
					"    left outer join category_detail CD on cd.cd_num = f.cd_num " + 
					"    left outer join fimages FImg on fimg.fb_num = F.fb_num AND fimg.idx = 1 " + 
					"order by F.fb_num desc";
					
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				int fbNum = rs.getInt("fb_num");
				String userId = rs.getString("userId");
				int cNum = rs.getInt("c_num");
				int cdNum = rs.getInt("cd_num");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				String sido = rs.getString("sido");
				String gue = rs.getString("gue");
				String dong = rs.getString("dong");
				String addrDetail = rs.getString("addrDetail");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				String sn = rs.getString("sn");
				String lat = rs.getString("lat");
				String lng = rs.getString("lng");
				int chatHit = rs.getInt("hit");
				int hit = rs.getInt("chat_hit");
				int fState = rs.getInt("f_state");
				String cName = rs.getString("c_name");
				String cdName = rs.getString("cd_name");
				String thumbnailImage = rs.getString("thumbnailImage");
				
				FbDto dto = new FbDto(
						 fbNum,  userId,  cNum,  cdNum, timestamp,
						 sido, gue, dong,  addrDetail,  title,  contents,  sn,  lat,  lng,
						 chatHit,  hit,  fState, cName, cdName, thumbnailImage);
				dtos.add(dto);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return dtos;
	}

	public FbDto getContent(int fbNum) {
		FbDto dto = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = dataSource.getConnection();
			
			String query = "select " + 
					"    F.fb_num, F.userId, C.name C_NAME , CD.name CD_NAME, " + 
					"    F.timestamp, F.sido, F.gue, F.dong , F.addrDetail, F.title, F.contents, F.sn, F.lat, F.lng, F.chat_hit, " + 
					"    F.hit, F.f_state, F.c_num, F.cd_num, fImg.imagesystemname thumbnailImage " + 
					"from find_board F " + 
					"    left outer join CATEGORY C on C.c_Num = F.c_num " + 
					"    left outer join category_detail CD on cd.cd_num = f.cd_num " + 
					"    left outer join fimages FImg on fimg.fb_num = F.fb_num AND fimg.idx = 1 " + 
					"where F.fb_num = ?";
//			String query = "select * from find_board where fb_num = ?";
			ps = con.prepareStatement(query);
			ps.setInt(1, fbNum);
			rs = ps.executeQuery();
			if (rs.next()) {
				String userId = rs.getString("userId");
				int cNum = rs.getInt("c_num");
				int cdNum = rs.getInt("cd_num");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				String sido = rs.getString("sido");
				String gue = rs.getString("gue");
				String dong = rs.getString("dong");
				String addrDetail = rs.getString("addrDetail");
				String title = rs.getString("title");
				String contents = rs.getString("contents");
				String sn = rs.getString("sn");
				String lat = rs.getString("lat");
				String lng = rs.getString("lng");
				int chatHit = rs.getInt("hit");
				int hit = rs.getInt("chat_hit");
				int fState = rs.getInt("f_state");
				String cName = rs.getString("c_name");
				String cdName = rs.getString("cd_name");
				String thumbnailImage = rs.getString("thumbnailImage");
				
				dto = new FbDto(
						 fbNum,  userId,  cNum,  cdNum, timestamp,
						 sido, gue, dong ,  addrDetail,  title,  contents,  sn,  lat,  lng,
						 chatHit,  hit,  fState, cName, cdName, thumbnailImage);
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(ps != null) ps.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return dto;
	}
	
	
	
	public int delete (int fbNum) {
		int ri = -1;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			
			con = dataSource.getConnection();
			String query = "delete from find_board where fb_num = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, fbNum);
			pstmt.executeUpdate();
			ri = 1;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		
		return ri;
	}

	public void modify(int fbNum, int cNum, int cdNum, String sido, String gue, String dong, String addrDetail,
			String title, String contents, String sn, String lat, String lng) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = dataSource.getConnection();
			String query = "update find_board set c_num=?, cd_num=?, sido=?, gue=?, dong=?, addrdetail=?, title=?, contents=?, sn=?, lat=?, lng=? where fb_num = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, cNum);
			pstmt.setInt(2, cdNum);
			pstmt.setString(3, sido);
			pstmt.setString(4, gue);
			pstmt.setString(5, dong);
			pstmt.setString(6, addrDetail);
			pstmt.setString(7, title);
			pstmt.setString(8, contents);
			pstmt.setString(9, sn);
			pstmt.setString(10, lat);
			pstmt.setString(11, lng);
			pstmt.setInt(12, fbNum);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
