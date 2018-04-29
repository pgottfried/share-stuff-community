package application.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;



@Entity
@Table(name="image")
public class Image extends BaseEntity {
	

	private static final long serialVersionUID = 1L;
	@Column(name="name", nullable = false, length = 32)
	private String name;
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(length=1024000) // in 1024 byte
	private byte[] image;
	@Column(name="coverPic")
	private boolean coverPic;
	
    @Transient //means dont save to database
    private String url;
    @Transient
    private String thumbnailUrl;
    @Transient
    private String deleteUrl;
    @Transient
    private String deleteType;
    
	@ManyToOne
	@JoinColumn(name="offer_id")
	private Offer offer = new Offer();
	
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getDeleteUrl() {
		return deleteUrl;
	}
	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}
	public String getDeleteType() {
		return deleteType;
	}
	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}
	public Offer getOffer() {
		return offer;
	}
	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public boolean isCoverPic() {
		return coverPic;
	}
	public void setCoverPic(boolean coverPic) {
		this.coverPic = coverPic;
	}

	
}
