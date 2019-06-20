package ace.models;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleDAOImpl implements ArticleDAO {

	private static final ArticleDAO articleDAO = new ArticleDAOImpl();
	private static final Logger logger = LoggerFactory.getLogger(ArticleDAOImpl.class);
	
	private String url; 
	private String driver; 
	private String username; 
	private String password; 
	
	private ArticleDAOImpl() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(this.getClass().getResource("").getPath() + "/database.properties"));
			url 	 = prop.getProperty("url").trim();
			driver 	 = prop.getProperty("driver").trim();
			username = prop.getProperty("username").trim();
			password = prop.getProperty("password").trim();
		
			Class.forName(driver);
			
		} catch (Exception e) {
			logger.info(e.toString());
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, username, password);
	}
	
	public void dbClose(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		if(rs 		!= null) try {rs.close();} 		catch(Exception e) {}
		if(pstmt 	!= null) try {pstmt.close();} 	catch(Exception e) {}
		if(conn 	!= null) try {conn.close();} 	catch(Exception e) {}
	}
	
	public void dbClose(PreparedStatement pstmt, Connection conn) {
		if(pstmt 	!= null) try {pstmt.close();} 	catch(Exception e) {}
		if(conn 	!= null) try {conn.close();} 	catch(Exception e) {}
	}
		
	public static ArticleDAO getInstance() {
		return articleDAO;
	}

	@Override
	public void insertArticle(ArticleDTO articleDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into t_article (no, title, password, name, content)");
		sql.append(" values(seq_article.nextval,?,?,?,?)");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, articleDTO.getTitle());
			pstmt.setString(2, articleDTO.getPassword());
			pstmt.setString(3, articleDTO.getName());
			pstmt.setString(4, articleDTO.getContent());
			
			pstmt.executeUpdate();			
		} finally {
			dbClose(pstmt, conn);
		}
	}

	@Override
	public List<ArticleDTO> getArticleList() throws SQLException {
		//JDBC 삼총사
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//return할 list
		List<ArticleDTO> list = new ArrayList<ArticleDTO>();
		
		// 쿼리
		StringBuffer sql = new StringBuffer();
		sql.append(" select no, title, name, regdate, readcount");
		sql.append(" from   t_article");
		sql.append(" order  by no desc");
		

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				ArticleDTO articleDTO = new ArticleDTO();
				articleDTO.setNo(rs.getLong("no"));
				articleDTO.setTitle(rs.getString("title"));
				articleDTO.setName(rs.getString("name"));
				articleDTO.setRegdate(rs.getDate("regdate"));
				articleDTO.setReadcount(rs.getInt("readcount"));
				list.add(articleDTO);
			}
			
		} finally {
			dbClose(rs, pstmt, conn);
		}
		
		return list;
	}

	@Override
	public List<ArticleDTO> getArticleList(PageDTO pageDTO) throws SQLException {
		
		//리스트를 담을 콜렉션 프레임 워크 선언
		List<ArticleDTO> list = new ArrayList();
		
		StringBuffer sql = new StringBuffer();
	/* 	sql.append(" select no, title, name ");
		sql.append(" 	   ,to_char(regdate,'YYYY-MM-DD') as regdate, readcount ");
		sql.append(" from t_article");
		sql.append(" order by no desc"); */
		
		sql.append(" SELECT B.* ");
		sql.append(" FROM	(SELECT rownum as rnum, A.*");
		sql.append(" 		FROM (select no, title, name  ");
		sql.append(" 					   ,regdate, readcount");
		sql.append(" 				from t_article   ");
		sql.append(" 				order by no desc) A ) B  ");
		sql.append(" WHERE rnum between ? AND ?");
		
		Connection 			conn 	= null;
		PreparedStatement 	pstmt 	= null;
		ResultSet			rs 		= null;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pageDTO.getStartNum());
			pstmt.setLong(2, pageDTO.getEndNum());

			rs = pstmt.executeQuery();
			
			//rs 안에 다음 레코드가 존재하면 계속해서 반복함
			while(rs.next()){
				ArticleDTO dto = new ArticleDTO();
				
				dto.setNo(rs.getLong("no"));
				dto.setTitle(rs.getString("title"));
				dto.setName(rs.getString("name"));
				dto.setRegdate(rs.getDate("regdate"));
				dto.setReadcount(rs.getInt("readcount"));
				
				list.add(dto);
			}
			
		} finally {
			dbClose(rs, pstmt, conn);		
		}
		return list;
	}

	@Override
	public int updateArticle(ArticleDTO articleDTO) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		int result = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" update t_article set");
		sql.append(" title = ?,");
		sql.append(" name = ?,");
		sql.append(" content = ?");
		sql.append(" where no = ? and password = ?");
		
		try {
			
		conn = getConnection();
		pstmt = conn.prepareStatement(sql.toString());
		
		pstmt.setString(1, articleDTO.getTitle());
		pstmt.setString(2, articleDTO.getName());
		pstmt.setString(3, articleDTO.getContent());
		pstmt.setLong(4, articleDTO.getNo());
		pstmt.setString(5, articleDTO.getPassword());
		
		result = pstmt.executeUpdate();
		
		} finally {
			dbClose(pstmt, conn);
		}
		
		return result;
	}

	@Override
	public int deleteArticle(ArticleDTO articleDTO) throws SQLException {
		int result = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from T_ARTICLE where no=? and password=?");
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		   
		try{
		   conn = getConnection();
		   pstmt = conn.prepareStatement(sql.toString());
		
		   pstmt.setLong(1, articleDTO.getNo());
		   pstmt.setString(2, articleDTO.getPassword());
				   
		   if(pstmt.executeUpdate() ==1){    			  
			   result = 1;
		   }
		   
		} finally{
			dbClose(pstmt, conn);
		}
	
		return result;
	}

	@Override
	public ArticleDTO getArticle(long no) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		ArticleDTO articleDTO = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" select no, title, name, regdate, readcount,content");
		sql.append(" from   t_article");
		sql.append(" where  no=?");
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, no);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				articleDTO = new ArticleDTO();
				articleDTO.setNo(rs.getLong("no"));
				articleDTO.setTitle(rs.getString("title"));
				articleDTO.setName(rs.getString("name"));
				articleDTO.setRegdate(rs.getDate("regdate"));
				articleDTO.setReadcount(rs.getInt("readcount"));
				articleDTO.setContent(rs.getString("content"));
			}
		} finally {
			dbClose(rs, pstmt, conn);
		}		
		return articleDTO;
	}	
	
	
	@Override
	public long getTotalCount() throws SQLException {
		StringBuffer sql_totalCount = new StringBuffer();		
		sql_totalCount.append(" select count(*) as cnt from hotels");
		
		long result = 0; //return할 변수
		
		Connection 			conn 	= null;
		PreparedStatement 	pstmt 	= null;
		ResultSet			rs 		= null;
		
		try {
			conn = getConnection();
						
			pstmt = conn.prepareStatement(sql_totalCount.toString());
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				result = rs.getLong("cnt");		//전체 레코드수
			}			
		} finally {
			dbClose(rs, pstmt, conn);	
			
		}
		return result;
	}
}
