package com.jkm.hsy.user.constant;

import com.jkm.hsy.user.util.ReflectUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.plugin.*;


@Intercepts({ @Signature(method = "prepare", type = StatementHandler.class, args = { Connection.class }) })
public class PageInterceptor implements Interceptor {

	private String dataBaseType;

	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
		BoundSql boundSql = delegate.getBoundSql();
		Object obj = boundSql.getParameterObject();
		if (obj instanceof Page<?>) {
			Page<?> page = (Page<?>) obj;
			MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
			Connection connection = (Connection) invocation.getArgs()[0];
			String sql = boundSql.getSql();
			this.setTotalRecord(page, mappedStatement, connection);
			String pageSql = this.getPageSql(page, sql);
			ReflectUtil.setFieldValue(boundSql, "sql", pageSql);
		}
		return invocation.proceed();
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties properties) {
		this.dataBaseType = properties.getProperty("dataBaseType");
	}

	private String getPageSql(Page<?> page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		if ("mysql".equalsIgnoreCase(this.dataBaseType)) {
			return getMysqlPageSql(page, sqlBuffer);
		} else if ("oracle".equalsIgnoreCase(this.dataBaseType)) {
			return getOraclePageSql(page, sqlBuffer);
		}
		return sqlBuffer.toString();
	}

	private String getMysqlPageSql(Page<?> page, StringBuffer sqlBuffer) {
		int offset = (page.getPage().getCurrentPage() - 1)
				* page.getPage().getPageSize();
		sqlBuffer.append(" limit ").append(offset).append(",")
				.append(page.getPage().getPageSize());
		return sqlBuffer.toString();
	}

	private String getOraclePageSql(Page<?> page, StringBuffer sqlBuffer) {
		int offset = (page.getPage().getCurrentPage() - 1)
				* page.getPage().getPageSize() + 1;
		StringBuffer pageSql = new StringBuffer();
		pageSql.append(" SELECT * FROM (");
		pageSql.append(" SELECT A.*,ROWNUM RN FROM (");
		pageSql.append(sqlBuffer).append(" ) A ");
		pageSql.append(") B WHERE B.RN<"
				+ (offset + page.getPage().getPageSize()) + " AND B.RN >="
				+ offset + "");
		return pageSql.toString();
	}

	private void setTotalRecord(Page<?> page, MappedStatement mappedStatement,
			Connection connection) {
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(
				mappedStatement.getConfiguration(), countSql,
				parameterMappings, page);
		ParameterHandler parameterHandler = new DefaultParameterHandler(
				mappedStatement, page, countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				page.getPage().setTotalRecord(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private String getCountSql(String sql) {
		return "SELECT COUNT(1) from (" +sql+") t";
	}

	public void setDataBaseType(String dataBaseType) {
		this.dataBaseType = dataBaseType;
	}
}
