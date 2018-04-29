package application.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BaseEntity type with hold common Id property. To be extended.
 * 
 * @author Paule
 *
 */

//it says hibernate to dont create this class as Table
@MappedSuperclass
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = 568379222048217476L;
	//it says hibernate that this is a auto generated  id attribute 
	@Id 
	@GeneratedValue
	protected Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false,  columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable=false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable=true)
    private Date updated;

    @PrePersist
    protected void onCreate() {
    updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
    updated = new Date();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	 
	
}
