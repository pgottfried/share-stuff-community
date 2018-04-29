package application.helper;
/* 
 * Needed for theamleaf view addOffer (category select)
 * */
public class CheckboxItem {
	
	public CheckboxItem(String label){
		this.label = label;
		this.value = false;
	}
	
	private String label;
	private boolean value = false;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	
	
	
}
