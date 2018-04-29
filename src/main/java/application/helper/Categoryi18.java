package application.helper;


import application.entity.BaseEntity;

public class Categoryi18 extends BaseEntity {
	
	public Categoryi18(Long id, String name, String localName){
		this.id = id;
		this.name = name;
		this.localName = localName;
	}

	private static final long serialVersionUID = 1L;
		private String name;
		private String localName;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLocalName() {
			return localName;
		}
		public void setLocalName(String localName) {
			this.localName = localName;
		}
}

