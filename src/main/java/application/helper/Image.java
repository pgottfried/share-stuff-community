package application.helper;
/*
 * Only used in the fileuploadController. 
 * jQuery fileupload plugin certain properties..
 */
import java.io.Serializable;


public class Image implements Serializable{
	
		private static final long serialVersionUID = 1L;
		private String url;
	    private String thumbnailUrl;
	    private String deleteUrl;
	    private String deleteType;

	    
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
	    
}