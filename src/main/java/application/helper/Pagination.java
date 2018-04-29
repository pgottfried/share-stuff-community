package application.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

/*
 * user for spring data pagination
 */
public class Pagination<T> {
	
	public static final int MAX_PAGE_ITEM_DISPLAY = 5;
	
	private Page<T> page;
	private List<PageItem> items;
	private int currentNumber;
	private String url;
	private String params;
	
	public Pagination(Page<T> page, String url, String params){
		this.page = page;
		this.url = url;
		this.params = params;
		items = new ArrayList<PageItem>();
		
		currentNumber = page.getNumber() + 1; //start from 1 to match page.page
		
		int start, size;
		
		
		if (page.getTotalPages() <= MAX_PAGE_ITEM_DISPLAY){
			start = 1;
			size = page.getTotalPages();
		} else {
			if (currentNumber <= MAX_PAGE_ITEM_DISPLAY - MAX_PAGE_ITEM_DISPLAY/2){
				start = 1;
				size = MAX_PAGE_ITEM_DISPLAY;
			} else if (currentNumber >= page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY/2){
				start = page.getTotalPages() - MAX_PAGE_ITEM_DISPLAY + 1;
				size = MAX_PAGE_ITEM_DISPLAY;
			} else {
				start = currentNumber - MAX_PAGE_ITEM_DISPLAY/2;
				size = MAX_PAGE_ITEM_DISPLAY;
			}
		}
		if (page.getTotalPages() > 1){
			for (int i = 0; i<size; i++){
				items.add(new PageItem(start+i, (start+i)==currentNumber));
			}
		}
		
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	public List<PageItem> getItems() {
		return items;
	}

	public void setItems(List<PageItem> items) {
		this.items = items;
	}

	public int getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean getHasPrevious(){
		return page.hasPrevious();
	}
	
	public boolean getHasNext(){
		return page.hasNext();
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
