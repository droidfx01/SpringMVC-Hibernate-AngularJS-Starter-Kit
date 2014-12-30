package main.pkg.name.config.database;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
// TODO Update package
@ComponentScan("main.pkg.name")
public class HibernateConfig {

	// TODO Set application encryption password
	private static final String ENC_PWD = "test";

	private static final String DATABASE_DRIVER = "db.driverClassName";
	private static final String DATABASE_URL = "db.jdbcUrl";
	private static final String DATABASE_USERNAME = "db.username";
	private static final String DATABASE_PASSWORD = "db.password";
	private static final String DATABASE_ACQUIRE_INCREMENT = "db.acquireIncrement";
	private static final String DATABASE_MIN_POOL_SIZE = "db.minPoolSize";
	private static final String DATABASE_MAX_POOL_SIZE = "db.maxPoolSize";
	private static final String DATABASE_MAX_IDLE_TIME = "db.maxIdleTime";

	// TODO Update package
	private static final String ENTITY_MANAGER_PACKAGES_TO_SCAN = "main.pkg.name";

	/**
	 * @return
	 */
	@Bean(name = "sessionFactory")
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(getDataSource());
		sessionFactory
				.setPackagesToScan(new String[] { ENTITY_MANAGER_PACKAGES_TO_SCAN });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	/**
	 * @return
	 */
	public DataSource getDataSource() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(ENC_PWD);

		Properties properties = new EncryptableProperties(encryptor);
		try {
			properties.load(this.getClass().getClassLoader()
					.getResourceAsStream("/properties/datasource.properties"));
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(properties.getProperty(DATABASE_DRIVER));
			dataSource.setJdbcUrl(properties.getProperty(DATABASE_URL));
			dataSource.setUser(properties.getProperty(DATABASE_USERNAME));
			dataSource.setPassword(properties.getProperty(DATABASE_PASSWORD));
			dataSource.setAcquireIncrement(Integer.parseInt(properties
					.getProperty(DATABASE_ACQUIRE_INCREMENT)));
			dataSource.setMinPoolSize(Integer.parseInt(properties
					.getProperty(DATABASE_MIN_POOL_SIZE)));
			dataSource.setMaxPoolSize(Integer.parseInt(properties
					.getProperty(DATABASE_MAX_POOL_SIZE)));
			dataSource.setMaxIdleTime(Integer.parseInt(properties
					.getProperty(DATABASE_MAX_IDLE_TIME)));
			return dataSource;
		} catch (IOException | PropertyVetoException e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * @param sessionFactory
	 * @return
	 */
	@Bean(name = "transactionManager")
	@Autowired
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				sessionFactory);
		return transactionManager;
	}
	
	/**
	 * @return
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	/**
	 * @return
	 */
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("/properties/hibernate.properties"));
			return properties;
		} catch (IOException e) {
			// TODO: handle exception
		}
		return null;
	}
}
