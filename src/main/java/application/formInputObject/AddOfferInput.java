package application.formInputObject;

import application.entity.Offer;

public class AddOfferInput {
	private String title;
	private String description;
	private String category;
//	private String[] selectedShippingOptions;
//	private String[] selectedPaymentOptions;
	
	public AddOfferInput(){super();}
	public AddOfferInput(Offer offer){
		this.title = offer.getTitle();
		this.description = offer.getDescription();
		this.category = offer.getCategory().getName();
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
//	public String[] getSelectedShippingOptions() {
//		return selectedShippingOptions;
//	}
//	public void setSelectedShippingOptions(String[] selectedShippingOptions) {
//		this.selectedShippingOptions = selectedShippingOptions;
//	}
//	public String[] getSelectedPaymentOptions() {
//		return selectedPaymentOptions;
//	}
//	public void setSelectedPaymentOptions(String[] selectedPaymentOptions) {
//		this.selectedPaymentOptions = selectedPaymentOptions;
//	}
}
