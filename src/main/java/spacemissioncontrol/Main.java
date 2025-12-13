package spacemissioncontrol;

import org.hibernate.SessionFactory;
import spacemissioncontrol.util.HibernateConfig;

public class Main {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateConfig.getSessionFactory()) {
            System.out.println("Schema created successfully");
        }
    }
}