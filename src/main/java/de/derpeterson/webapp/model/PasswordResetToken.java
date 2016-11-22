package de.derpeterson.webapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

@Entity
@Table(name="password_reset_token")
@NamedQueries({
	@NamedQuery(name="PasswordResetToken.findByToken", query="SELECT p FROM PasswordResetToken p WHERE p.token = :token"),
	@NamedQuery(name="PasswordResetToken.findByUser", query="SELECT p FROM PasswordResetToken p WHERE p.user = :user"),
	@NamedQuery(name="PasswordResetToken.deleteAllOldToken", query="DELETE FROM PasswordResetToken p WHERE p.expiry < :olderThan")
})
public class PasswordResetToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final int EXPIRY_TIME_IN_HOURS = 60 * 24;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", length = 255)
	private Long id;

	@Column(name = "token", length = 255, nullable = false)
	private String token;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "user_id", unique = true)
	private User user;
	
    @Column(name="expiry")
    private Timestamp expiry = null;

	/** Zeitpunkt zu dem dieser Entity erstellt wurde */	
    @Column(name="created")
    private Timestamp created = null;

	/** Wann wurde der Eintrag zuletzt geändert. */
    @Column(name="last_modified")
    private Timestamp lastModified = null;

	public PasswordResetToken() {
		super();
	}

	public PasswordResetToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
	}

	/** Vor dem ersten Speichern in die DB */
    @PrePersist
    protected void onCreate() {
        this.created = new Timestamp(System.currentTimeMillis());
        this.lastModified = new Timestamp(System.currentTimeMillis());
        
        calculateExpiryDate(EXPIRY_TIME_IN_HOURS);
    }
    
    @PostPersist
    protected void afterCreate() {}

    /** Aktionen vor jedem Speichern in der DB */
    @PreUpdate
    protected void onUpdate() {
        this.lastModified = new Timestamp(System.currentTimeMillis());
    }
    
    public static PasswordResetToken findByPrimaryKey(EntityManager entityManager, long id) {
        return entityManager.find(PasswordResetToken.class, id);
    }
	
	public static PasswordResetToken findByToken(EntityManager entityManager, String token) {
        TypedQuery<PasswordResetToken> query = entityManager.createNamedQuery("PasswordResetToken.findByToken", PasswordResetToken.class);
        
        query.setParameter("token", token);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);
        
        try {
        	PasswordResetToken passwordResetToken = query.getSingleResult();
        	if(Objects.nonNull(passwordResetToken)) {
        		return passwordResetToken;
        	}
        } catch(NoResultException ex) {
        	/* Kein Fehler */
        }
        
        return null;
    }
	
	public static PasswordResetToken findByUser(EntityManager entityManager, User user) {
        TypedQuery<PasswordResetToken> query = entityManager.createNamedQuery("PasswordResetToken.findByUser", PasswordResetToken.class);
        
        query.setParameter("user", user);

        // Uns interressiert nur ein Element aus der Abfrage
        query.setMaxResults(1);
        
        try {
        	PasswordResetToken passwordResetToken = query.getSingleResult();
        	if(Objects.nonNull(passwordResetToken)) {
        		return passwordResetToken;
        	}
        } catch(NoResultException ex) {
        	/* Kein Fehler */
        }
        
        return null;
    }
	
	public static int deleteAllOldToken(EntityManager entityManager, long olderThan) throws Exception {
		Query query = entityManager.createNamedQuery("PasswordResetToken.deleteAllOldToken");
		query.setParameter("olderThan", new Date(olderThan));

		int affected = 0;
		try {
			entityManager.getTransaction().begin();
			affected = query.executeUpdate();
			entityManager.getTransaction().commit();
		} catch(Exception e) {
			entityManager.getTransaction().rollback();
			throw e;
		}
		return affected;
    }

	public Long getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public PasswordResetToken setToken(String token) {
		this.token = token;
		return this;
	}

	public User getUser() {
		return user;
	}

	public PasswordResetToken setUser(User user) {
		this.user = user;
		return this;
	}
	
    public Timestamp getCreated() {
		return created;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}
	
	public Timestamp getExpiration() {
		return expiry;
	}
	
    public PasswordResetToken calculateExpirationDate() {
    	return calculateExpiryDate(EXPIRY_TIME_IN_HOURS);
    }
	
    public PasswordResetToken calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        this.expiry = new Timestamp(cal.getTime().getTime());
        return this;
    }
    
    public boolean isExpired(long timestamp) {
    	if(Objects.nonNull(this.expiry)
    			&& (this.expiry.getTime() - timestamp) <= 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean isExpired() {
    	return isExpired(new Date().getTime());
    }
}
