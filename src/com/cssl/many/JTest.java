package com.cssl.many;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cssl.util.HibernateUtil;

public class JTest {
	
	private static SessionFactory factory;
	private Session session;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass");
		factory = HibernateUtil.getSessionFactory();		
	}
	
	@Before
	public void setUp() throws Exception {		
		session = factory.getCurrentSession();
		session.beginTransaction();
	}
	
	@Test
	public void testSave2(){
		//无状态session
		StatelessSession session = factory.openStatelessSession();
		Husband h = (Husband)session.get(Husband.class, 1);
		h.setHname("aaa");
		
		/*for (int i = 0; i < 100000; i++) {
			Husband h = new Husband();
			h.setHname("");
			session.insert(h);
		}*/
		
	}
	
	@Test
	public void testUpdate(){
		
		for (int i = 0; i < 100000; i++) {
			Husband h = new Husband();
			h.setHname("");
			session.save(h);
			if(i%1000==0){
				session.flush();
				session.clear();
			}			
		}
		
	}
	
	/*
	 * list
	 */
	@Test
	public void testCacheTwo4(){
		
		String hql = "from Husband where hid<:hid";
		Query query = session.createQuery(hql);
		query.setInteger("hid", 8);
		List<Husband> list = query.setCacheable(true).list();
		session.getTransaction().commit();
		
		System.out.println("==========2==========");
		
		session = factory.getCurrentSession();
		session.beginTransaction();		
		hql = "from Husband where hid<:id";
		query = session.createQuery(hql);
		query.setInteger("id", 8);
		list = query.setCacheable(true).list();
		
	}
	
	/**
	 * get|load|iterate
	 */
	@Test
	public void testCacheTwo3(){
		session.setCacheMode(CacheMode.NORMAL);
		Husband h = (Husband)session.get(Husband.class, 1);
		System.out.println(h.getHname()+"\t"+h.getWifes().size());
		session.getTransaction().commit();		
		
		System.out.println("==========2==========");
		
		//factory.evict(Husband.class);
		session = factory.getCurrentSession();		
		session.beginTransaction();	
		session.setCacheMode(CacheMode.GET);
		h = (Husband)session.get(Husband.class, 1);
		System.out.println(h.getHname()+"\t"+h.getWifes().size());
				
		
	}
	
	/**
	 * get|load|iterate
	 */
	@Test
	public void testCacheTwo2(){
		
		Wife w = (Wife)session.get(Wife.class, 1);
		System.out.println(w.getWname()+"\t"+w.getHusband().getHname());
		System.out.println("==========1==========");
		Wife w2 = (Wife)session.get(Wife.class, 1);	
		System.out.println(w2.getWname()+"\t"+w2.getHusband().getHname());
		session.getTransaction().commit();
		
		System.out.println("==========2==========");
		
		session = factory.getCurrentSession();
		session.beginTransaction();		
		Wife w3 = (Wife)session.get(Wife.class, 1);
		System.out.println(w3.getWname()+"\t"+w3.getHusband().getHname());
		
	}
	
	/**
	 * get|load|iterate
	 */
	@Test
	public void testCacheTwo(){
		
		Wife w = (Wife)session.get(Wife.class, 1);
		System.out.println(w.getWname());
		System.out.println("==========1==========");
		Wife w2 = (Wife)session.get(Wife.class, 1);
		System.out.println(w==w2);
		System.out.println(w2.getWname());
		session.getTransaction().commit();
		
		System.out.println("==========2==========");
		
		session = factory.getCurrentSession();
		session.beginTransaction();		
		Wife w3 = (Wife)session.get(Wife.class, 1);
		System.out.println(w==w3);
		System.out.println(w3.getWname());
		w3.setWname(w3.getWname()+"0");
	}
	
	/**
	 * get|load|iterate
	 */
	@Test
	public void testCacheOne(){
		
		Wife w = (Wife)session.get(Wife.class, 1);
		w = (Wife)session.get(Wife.class, 1);
		
		/*String hql = "from Wife";
		Query query = session.createQuery(hql);
	
		List<Wife> list = query.list();
		for (Wife w : list) {
			System.out.println(w.getWid()+"\t"+w.getWname());		
		}
		
		System.out.println("=================");
		
		list = query.list();
		for (Wife w : list) {
			System.out.println(w.getWid()+"\t"+w.getWname());		
		}*/
	}
	
	/**
	 * 1+N
	 */
	@Test
	public void testOneN(){
		//1、不用关联对象，使用懒加载
		//select wid,wname,w_hid from wife
		//2、一定用到关联对象，使用迫切左外连接
		//String hql = "from Wife w left join fetch w.husband";	
		//3、BatchSize：用到一部分关联对象(1+N/size)
		String hql = "from Wife";
		Query query = session.createQuery(hql);
		//Criteria query = session.createCriteria(Wife.class);
	
		List<Wife> list = query.list();
		for (Wife w : list) {
			System.out.println(w.getWid()+"\t"+w.getWname());
			//if(w.getWid()<5)
				System.out.println(w.getHusband().getHname());
		}
	}
	
	@Test
	public void testSave(){
		
		for (int i = 0; i < 10; i++) {
			Husband h = new Husband();
			h.setHname("鑫哥"+i);
			Wife w1 = new Wife();
			w1.setWname("凤姐"+i);
			Wife w2 = new Wife();
			w2.setWname("芙蓉姐"+i);
			//设置关联关系
			w1.setHusband(h);
			w2.setHusband(h);
		
			session.save(h);
			session.save(w1);
			session.save(w2);			
		}
		
	}
	
	@After
	public void tearDown() throws Exception {
		session.getTransaction().commit();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass");
		factory.close();
		factory = null;
	}



}
