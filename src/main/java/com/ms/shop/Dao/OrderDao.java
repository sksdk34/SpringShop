package com.ms.shop.Dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ms.shop.Vo.OrderVo;

@Repository("OrderDao")
public class OrderDao {
	
JdbcTemplate template;
	
	@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	@Autowired
	private SqlSession sqlSession;
	
	String Namespace = "com.ms.shop.Dao.OrderDao";
	
	public void insertPurchase(OrderVo info) throws Exception {
		
		sqlSession.insert(Namespace + ".insertPurchase", info);
	}
}
