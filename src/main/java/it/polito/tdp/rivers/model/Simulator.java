package it.polito.tdp.rivers.model;

import java.time.LocalTime;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.model.Event.EventType;


public class Simulator {

	private Model model;
	
	//EVENTI
	private PriorityQueue<Event> queue;
	
	// PARAMETRI DI SIMULAZIONE
	private float k;
	private List<Flow> fIn;
	private float fMed;

	
	// STATO DEL MONDO
	private float C;
	private float Q;
	private float fOut;
	private float fOutMin;
	
	// PARAMETRI IN USCITA
	private int numeroGiorniDisservizio;
	private float cMed;
	
	// IMPOSTAZIONE PARAMETRI INIZIALI
	void init(float fMed, float k, River fiume) {
		this.model = new Model();
		this.k = k;
		this.fMed = fMed*3600*24;
		fIn = this.model.getFlows(fiume);
		this.Q = this.k*this.fMed*30;
		this.C = this.Q/2;
		this.fOutMin = (float) (0.8*this.fMed);
		
		this.queue = new PriorityQueue<>();
		
		for(Flow f: fIn) {
			this.queue.add(new Event(EventType.INGRESSO, f.getDay(), f));
		}
		
		numeroGiorniDisservizio = 0;
		cMed = 0;
	}

	public void run() {
		// CICLO DI SIMULAZIONE
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}
	
	public void processEvent(Event e) {
		switch(e.getType()) {
		case INGRESSO:
			this.C+=e.getFlow().getFlow();
			if(this.C>this.Q) { // TRACIMAZIONE
				this.queue.add(new Event(EventType.TRACIMAZIONE, e.getDate(), e.getFlow()));
			}
			// IRRIGAZIONE
			int p = (int) (Math.random()*100);
			if(p<5) {
				this.queue.add(new Event(EventType.IRRIGAZIONE, e.getDate(),e.getFlow()));
			}
			else {
				this.queue.add(new Event(EventType.USCITA, e.getDate(), e.getFlow()));
			}
			break;
		case USCITA:
			if(this.fOutMin>C) {
				numeroGiorniDisservizio++;
				this.C = 0;
				this.cMed+=C;
			}
			else {
				this.C = this.C - fOutMin;
				this.cMed+=C;
			}
			break;
			
		case IRRIGAZIONE:
			this.fOut = 10*this.fOutMin;
			if(this.fOut>C) {
				numeroGiorniDisservizio++;
				this.fOut = C;
				this.C = 0;
				this.cMed+=C;
			}
			else {
				this.C = this.C-this.fOut;
				this.cMed+=C;
			}
		case TRACIMAZIONE:
			// DEVO TOGLIERE DA C LA QUANTITA' DI ACQUA PARI ALL'ACQUA CHE NON ENTRA, OSSIA C-Q
			float differenza = this.C-this.Q;
			this.C = this.C-differenza;
			break;
		}
	}
	
	public int getGiorniDisservizio() {
		return this.numeroGiorniDisservizio;
	}
	
	public float getCMed() {
		return this.cMed/fIn.size();
	}
}
