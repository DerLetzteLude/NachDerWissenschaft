package Events;

import com.example.dao.Institut;
import com.example.dao.Veranstaltung;

public class EventOpenMap {
	Institut institut;
	Veranstaltung veranstaltung;
	
	public EventOpenMap(Institut inst, Veranstaltung ver){
		institut=inst;
		veranstaltung = ver;
	}
	public Institut getInstitut() {
		return institut;
	}
	public void setInstitut(Institut institut) {
		this.institut = institut;
	}
	public Veranstaltung getVeranstaltung() {
		return veranstaltung;
	}
	public void setVeranstaltung(Veranstaltung veranstaltung) {
		this.veranstaltung = veranstaltung;
	}
	
}
