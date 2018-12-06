package com.cssl.pojo;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
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
	public void testDetached(){
		
		//service
		DetachedCriteria cri = DetachedCriteria.forClass(House.class);
		cri.add(Restrictions.idEq(9));
		
		//dao
		cri.add(Restrictions.like("title", "%房"));
		cri.getExecutableCriteria(session).list();
	}
	
	@Test
	public void testQBE(){
		
		Criteria cri = session.createCriteria(House.class,"h")
				.createCriteria("h.street", "s");
		
		Street s = new Street();
		s.setSid(3);
		s.setSname("司马里");
		cri.add(Example.create(s));
		List<House> list = cri.list();
		for (House h : list) {
			System.out.println(h.getStreet().getSname());
		}
		
		/*House hh = new House();
		hh.setId(2);
		hh.setTitle("%房");
		hh.setDescription(null);
		hh.setPrice(2200);
		hh.setFloorage(0);
		
		cri.add(Example.create(hh).excludeZeroes().enableLike());
		
		List<House> list = cri.list();
		for (House h : list) {
			System.out.println(h.getTitle()+"\t"+h.getPrice()+"\t"+h.getDescription());
		}*/
	}
	
	/**
	 * 连表查询
	 */
	@Test
	public void testQBC2(){
		//A->B->C    A->B   A->C
		Criteria cri = session.createCriteria(House.class)
			.createCriteria("street")
			.createCriteria("district");
			//.createCriteria("type")
			//.createCriteria("user");		
		
		List<House> list = cri.list();
	
	}
	
	/**
	 * 连表查询
	 */
	@Test
	public void testQBC(){
				
		Criteria cri = session.createCriteria(House.class,"h")
			.createAlias("h.street", "s", JoinType.LEFT_OUTER_JOIN)
			.createAlias("s.district", "d", JoinType.LEFT_OUTER_JOIN)
			.createAlias("h.type", "t", JoinType.LEFT_OUTER_JOIN)
			.createAlias("h.user", "u", JoinType.LEFT_OUTER_JOIN);
		cri.add(Restrictions.eq("d.id", 2));
		cri.add(Restrictions.eq("s.sid", 3));
		cri.add(Restrictions.like("h.title", "%房%"));
		
		List<House> list = cri.list();
		for (House h : list) {
			System.out.println(h.getId()
					+"\t"+h.getStreet().getSname()
					+"\t"+h.getStreet().getDistrict().getName()
					+"\t"+h.getType().getName()
					+"\t"+h.getUser().getName()
					+"\t"+h.getTitle());
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
