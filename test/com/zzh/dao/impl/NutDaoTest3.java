package com.zzh.dao.impl;

import java.util.Iterator;
import java.util.List;

import com.zzh.Main;
import com.zzh.dao.Dao;
import com.zzh.dao.FieldFilter;
import com.zzh.dao.entity.annotation.*;
import com.zzh.service.IdNameEntityService;
import com.zzh.trans.Atom;

import junit.framework.TestCase;

public class NutDaoTest3 extends TestCase {

	public static class SubStudent extends Student {

		public String getBrief() {
			return String.format("%s (%d)", getName(), getAge());
		}

	}

	@Table("student2")
	public static class Student2 extends Student {
		@Column
		private String brief;

		public String getBrief() {
			return brief;
		}

		public void setBrief(String brief) {
			this.brief = brief;
		}

	}

	@Override
	protected void setUp() throws Exception {
		dao = Main.getDao("com/zzh/dao/impl/test.sqls");
		dao.executeBySqlKey(".student.drop", ".student.create", ".student2.drop",
				".student2.create");
	}

	@Override
	protected void tearDown() throws Exception {
		Main.closeDataSource();
	}

	private Dao dao;

	public void testFieldFilter() {
		dao.clear(SubStudent.class, null);
		dao.insert(Student.make(SubStudent.class, "zzh", 26));
		dao.insert(Student.make(SubStudent.class, "xyz", 80));
		FieldFilter.create(Student.class, "id|name").run(new Atom() {
			public void run() {
				Student zzh = dao.fetch(Student.class, "zzh");
				assertNull(zzh.getAboutMe());
				zzh.setAboutMe("abc");
				dao.update(zzh);
				zzh = dao.fetch(Student.class, "zzh");
				assertNull(zzh.getAboutMe());
				List<Student> stus = dao.query(Student.class, null, null);
				for (Iterator<Student> it = stus.iterator(); it.hasNext();)
					assertNull(it.next().getAboutMe());

			}
		});
	}

	public void testSubStudent() {
		dao.clear(SubStudent.class, null);
		dao.insert(Student.make(SubStudent.class, "xyz", 26));
		assertEquals(1, dao.count(SubStudent.class));
	}

	public void testSubStdentService() {
		IdNameEntityService<Student> srv = new IdNameEntityService<Student>(dao) {};
		srv.setEntityType(SubStudent.class);
		srv.clear(null);
		srv.insert(Student.make(SubStudent.class, "killer", 43));
		Student stu = srv.fetch("killer");
		assertTrue(stu instanceof SubStudent);
		assertEquals("killer (43)", ((SubStudent) stu).getBrief());
	}

	public void testStdent2Service() {
		IdNameEntityService<Student> srv = new IdNameEntityService<Student>(dao) {};
		srv.setEntityType(Student2.class);
		srv.clear(null);
		srv.insert(Student.make(Student2.class, "killer", 43));
		Student stu = srv.fetch("killer");
		assertTrue(stu instanceof Student2);
		assertEquals(1, dao.count("student2"));
	}

	public void testStdentWidthException() {
		IdNameEntityService<Student> srv = new IdNameEntityService<Student>(dao) {};
		srv.setEntityType(Student2.class);
		try {
			srv.insert(Student.make("killer", 43));
			fail();
		} catch (Exception e) {}
	}

}