package de.derpeterson.webapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import de.derpeterson.webapp.exception.UserPasswordIncorrectlyException;
import de.derpeterson.webapp.helper.PasswordHelper;

@Entity
@Table(name="user")
@NamedQueries({
	@NamedQuery(name="User.findByUsername", query="SELECT u FROM User u WHERE u.username = :username"),
	@NamedQuery(name="User.findByEMail", query="SELECT u FROM User u WHERE u.email = :email"),
	@NamedQuery(name="User.findByUsernameOrEMail", query="SELECT u FROM User u WHERE u.email = :email OR u.username = :username"),
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", length = 255)
    private Long id;

    @Column(name = "email", length = 255, unique = true)
	private String email;
	
	@Column(name = "username", length = 255, unique = true)
    private String username;
	
	@Column(name = "password", length = 255)
    private String password;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private PasswordResetToken passwordResetToken;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private VerificationToken verificationToken;

	/** Zeitpunkt zu dem dieser Entity erstellt wurde */	
    @Column(name="created")
    private Timestamp created = null;

    public User() {
		super();
	}

	public User(String email, String username, String password) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = false;
	}

	/** Wann wurde der Eintrag zuletzt geändert. */
    @Column(name="last_modified")
    private Timestamp lastModified = null;

	/** Vor dem ersten Speichern in die DB */
    @PrePersist
    protected void onCreate() {
        this.created = new Timestamp(System.currentTimeMillis());
        this.lastModified = new Timestamp(System.currentTimeMillis());
    }
    
    @PostPersist
    protected void afterCreate() {}

    /** Aktionen vor jedem Speichern in der DB */
    @PreUpdate
    protected void onUpdate() {
        this.lastModified = new Timestamp(System.currentTimeMillis());
    }
    
    public Long getId() {
		return id;
	}
    
	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getUsername() {
		return username;
	}
	
	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}
	
	public Boolean isEnabled() {
		return this.enabled;
	}

	public User setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}
	
	public PasswordResetToken getPasswordResetToken() {
		return passwordResetToken;
	}

	public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}

	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

    public static User findByPrimaryKey(EntityManager entityManager, long id) {
        return entityManager.find(User.class, id);
    }
	
	public static User findByUsername(EntityManager entityManager, String username) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsername", User.class);
        
        query.setParameter("username", username);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);
        
        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		return user;
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        
        return null;
    }
	
	public static User findByUsernameWithPassword(EntityManager entityManager, String username, String password) throws UserPasswordIncorrectlyException {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsername", User.class);
        
        query.setParameter("username", username);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);

        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		if(PasswordHelper.checkPassword(password, user.getPassword())) {
        			return user;
        		} else {
        			throw new UserPasswordIncorrectlyException("user password not correct");
        		}
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        return null;
    }
	
	public static User findByUsernameOrEMailWithPassword(EntityManager entityManager, String usernameOrEmail, String password) throws UserPasswordIncorrectlyException {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsernameOrEMail", User.class);
        
        query.setParameter("email", usernameOrEmail);
        query.setParameter("username", usernameOrEmail);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);

        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		if(PasswordHelper.checkPassword(password, user.getPassword())) {
        			return user;
        		} else {
        			throw new UserPasswordIncorrectlyException("user password not correct");
        		}
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        return null;
    }
	
	public static User findByUsernameOrEMail(EntityManager entityManager, String usernameOrEmail) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByUsernameOrEMail", User.class);
        
        query.setParameter("email", usernameOrEmail);
        query.setParameter("username", usernameOrEmail);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);

        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		return user;
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        return null;
    }
	
	public static User findByEMail(EntityManager entityManager, String email) {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEMail", User.class);
        
        query.setParameter("email", email);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);

        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		return user;
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        
        return null;
    }
	
	public static User findByEMailWithPassword(EntityManager entityManager, String email, String password) throws UserPasswordIncorrectlyException {
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEMail", User.class);
        
        query.setParameter("email", email);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);

        try {
        	User user = query.getSingleResult();
        	if(Objects.nonNull(user)) {
        		if(PasswordHelper.checkPassword(password, user.getPassword())) {
        			return user;
        		} else {
        			throw new UserPasswordIncorrectlyException("user password not correct");
        		}
        	}
        } catch(NoResultException ex) {
//        	throw new UserNotFoundException("User not found");
        	/* Kein Fehler in dem Sinne */
        }
        return null;
    }

	public Timestamp getCreated() {
		return created;
	}

    public Timestamp getLastModified() {
		return lastModified;
	}
}