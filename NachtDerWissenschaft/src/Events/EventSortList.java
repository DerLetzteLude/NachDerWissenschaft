package Events;

public class EventSortList {
	public static final int SORT_NAME = 0;
	public static final int SORT_INSTITUT = 1;
	public static final int SORT_TYPE = 2;
	public static final int SORT_STOP = 3;

	boolean mFilterBarrierefrei = false;
	boolean mFilterBisDreizehn = false;
	boolean mFilterFamilienfreundlich = false;
	boolean mFilterJugendliche = false;
	boolean mFilterWissenschaftsjahr = false;

	public int sorting = 0;

	public EventSortList() {	

	}
	
	public EventSortList(int sorting) {
		this.sorting = sorting;

	}
	
	public EventSortList(boolean boolFilterBarrierefrei,boolean boolFilterBisDreizehn, boolean boolFilterFamilienfreundlich, boolean boolFilterJugendliche, boolean boolFilterWissenschaftsjahr ) {
		this.mFilterBarrierefrei = boolFilterBarrierefrei;
		this.mFilterBisDreizehn = boolFilterBisDreizehn;
		this.mFilterFamilienfreundlich = boolFilterFamilienfreundlich;
		this.mFilterJugendliche = boolFilterJugendliche;
		this.mFilterWissenschaftsjahr = boolFilterWissenschaftsjahr;
	}
	
	public EventSortList(int sorting, boolean boolFilterBarrierefrei,boolean boolFilterBisDreizehn, boolean boolFilterFamilienfreundlich, boolean boolFilterJugendliche, boolean boolFilterWissenschaftsjahr ) {
		this.sorting = sorting;
		this.mFilterBarrierefrei = boolFilterBarrierefrei;
		this.mFilterBisDreizehn = boolFilterBisDreizehn;
		this.mFilterFamilienfreundlich = boolFilterFamilienfreundlich;
		this.mFilterJugendliche = boolFilterJugendliche;
		this.mFilterWissenschaftsjahr = boolFilterWissenschaftsjahr;
	}


	public int getSorting() {
		return sorting;
	}

	public void setSorting(int sorting) {
		this.sorting = sorting;
	}

	public boolean isBoolFilterBarrierefrei() {
		return mFilterBarrierefrei;
	}

	public void setBoolFilterBarrierefrei(boolean boolFilterBarrierefrei) {
		this.mFilterBarrierefrei = boolFilterBarrierefrei;
	}

	public boolean isBoolFilterBisDreizehn() {
		return mFilterBisDreizehn;
	}

	public void setBoolFilterBisDreizehn(boolean boolFilterBisDreizehn) {
		this.mFilterBisDreizehn = boolFilterBisDreizehn;
	}

	public boolean isBoolFilterFamilienfreundlich() {
		return mFilterFamilienfreundlich;
	}

	public void setBoolFilterFamilienfreundlich(boolean boolFilterFamilienfreundlich) {
		this.mFilterFamilienfreundlich = boolFilterFamilienfreundlich;
	}

	public boolean isBoolFilterJugendliche() {
		return mFilterJugendliche;
	}

	public void setBoolFilterJugendliche(boolean boolFilterJugendliche) {
		this.mFilterJugendliche = boolFilterJugendliche;
	}

	public boolean isBoolFilterWissenschaftsjahr() {
		return mFilterWissenschaftsjahr;
	}

	public void setBoolFilterWissenschaftsjahr(boolean boolFilterWissenschaftsjahr) {
		this.mFilterWissenschaftsjahr = boolFilterWissenschaftsjahr;
	}

}
