package ace.models;

import java.sql.SQLException;
import java.util.List;

public interface ArticleDAO {
	void insertArticle(ArticleDTO articleDTO) throws SQLException;
	List<ArticleDTO> getArticleList() throws SQLException;
	List<ArticleDTO> getArticleList(PageDTO pageDTO) throws SQLException;
	int updateArticle(ArticleDTO articleDTO) throws SQLException;
	int deleteArticle(ArticleDTO articleDTO) throws SQLException;
	ArticleDTO getArticle(long no) throws SQLException;
	long getTotalCount() throws SQLException;

	// 전체 article 개수
	// 조회수 증가
}
