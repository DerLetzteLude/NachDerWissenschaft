package Events;

public class EventSetSortItems {
	String[] sorters;
	Boolean filterVisible;

	public EventSetSortItems(String[] sorters, boolean filterVisible) {
		this.sorters = sorters;
		this.filterVisible = filterVisible;
	}

	public Boolean getFilterVisible() {
		return filterVisible;
	}

	public void setFilterVisible(Boolean filterVisible) {
		this.filterVisible = filterVisible;
	}

	public String[] getSorters() {
		return sorters;
	}

	public void setSorters(String[] sorters) {
		this.sorters = sorters;
	}
}
