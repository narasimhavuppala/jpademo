package jpa;

import java.io.Console;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import domain.Person;

/**
 * Interactive query runner; ideal for testing query syntax once
 * you've dealt with setting JPA up for your project.
 */
public class JpaQuery {

	static EntityManagerFactory entityMgrFactory = null;

	public enum Mode {
		CONSOLE,
		SWING
	}

	public static void main(String[] args) {

		String unitName = args.length == 0 ?  "jpademo" : args[0];

		JpaQuery program = new JpaQuery();

		// Do i/o initializations before JPA to fail fast
		program.init();

		System.out.println("JPAQuery.main() -- starting JPA " + unitName);

		// This would be done for you
		// were you running in an EE App Server
		EntityManager entityManager = null;
		try {
			entityMgrFactory = 
			Persistence.createEntityManagerFactory(unitName);
			System.out.println("Created EntityManagerFactory");
			entityManager = entityMgrFactory.createEntityManager();

			fake_data(entityManager);
			
			// Delegate
			program.run(entityManager);

		} catch (Exception e) {
			System.out.println("JPA failure: " + e);
			e.printStackTrace();
			return;
		} finally {	
			if (entityManager != null)
				entityManager.close();
			if (entityMgrFactory != null)
				entityMgrFactory.close();
		}
	}
	
	/** Ensure there is at least one Person, even if the import.sql
	 * fails to get run.
	 * @param entityManager The JPA Connection wrapper
	 */
	private static void fake_data(EntityManager entityManager) {
		Person p = new Person("Firstname", "Lastname");
		entityManager.getTransaction().begin();
		entityManager.persist(p);
		entityManager.getTransaction().commit();
	}

	Console console;
	Mode mode = Mode.CONSOLE;

	void init() {
        if ((console = System.console()) == null) {
			mode = Mode.SWING;
		}
	}

	@SuppressWarnings("unchecked")
	void run(EntityManager entityManager) throws Exception {

		String queryStr;
		Query query = null;

		while(true) {

			try {
				queryStr = mode == Mode.CONSOLE ? 
					console.readLine("Enter JPA Query: ") :
					JOptionPane.showInputDialog(null, "Enter JPA Query: ");
				if (queryStr == null || "quit".equals(queryStr)) {
					return;
				}
	
				query = entityManager.createQuery(queryStr);
	
				List<Object> list = query.getResultList();
				System.out.println("Found " + list.size() + " results:");
				for (Object o : list) {
					System.out.println(o);
				}
				System.out.println();
			} catch (Exception e) {
				switch (mode) {
				case CONSOLE:
					System.out.println("Error: " + e);
					e.printStackTrace();
					break;
				case SWING:
					String message = e.toString();
					JOptionPane.showMessageDialog(null, message);
					break;
				}
			}
		}
	}
}
