package com.mathologic.projects.crewlink.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.mathologic.projects.crewlink.custom.models.DateTimeModel;
import com.mathologic.projects.crewlink.custom.models.OrderedRoundTrips;
import com.mathologic.projects.crewlink.custom.models.RoundTripSM;
import com.mathologic.projects.crewlink.custom.models.RoundTripVM;
import com.mathologic.projects.crewlink.utils.converters.TimeUtils;

public class CrewlinkSolverV3 {
	private TimeUtils timeUtils = new TimeUtils();
	
	private int hqr8less = 12 * 60;
	private int hqr8more = 16 * 60;
	private int pr = 30 * 60;
	private int minPRin = 6 * 24 * 60;
	private int maxPRin = 8 * 24 * 60;

	private int hqr8lesshr = 12;
	private int hqr8morehr = 16;
	private int prhr = 30;
	private int minPRind = 6;
	private int maxPRind = 8;

	private List<RoundTripSM> roundTrips;

	public CrewlinkSolverV3(List<RoundTripVM> roundTrips) {
		this.roundTrips = new ArrayList<RoundTripSM>();
		for(RoundTripVM i : roundTrips) {
			int startMins = new DateTimeModel(i.getDepartureDay().ordinal(),i.getDepartureTime()).number;
			int endMins = new DateTimeModel(i.getArrivalDay().ordinal(),i.getArrivalTime()).number;
			RoundTripSM it = new RoundTripSM(i.getId(),startMins,endMins,i.getLastDrivingDutyDuration());
//			System.out.println(it);
			this.roundTrips.add(it);
		}
	}

	public CrewlinkSolverV3(List<RoundTripVM> roundTrips, int hqr8less,
			int hqr8more, int pr, int minPRin, int maxPRin) {
		this(roundTrips);
		this.hqr8lesshr = hqr8less;
		this.hqr8morehr = hqr8more;
		this.prhr = pr;
		this.minPRind = minPRin;
		this.maxPRind = maxPRin;
		this.hqr8less = this.hqr8lesshr * 60;
		this.hqr8more = this.hqr8morehr * 60;
		this.pr = this.prhr * 60;
		this.minPRin = this.minPRind * 24 * 60;
		this.maxPRin = this.maxPRind * 24 * 60;
	}
	
	public OrderedRoundTrips execute(){
//		System.out.println(roundTrips);
		Collections.sort(roundTrips,new RoundTripComp());
//		System.out.println(roundTrips);
		OrderedRoundTrips minOrder = new OrderedRoundTrips();
		minOrder.duration = Long.MAX_VALUE;
		int i=0;
		for(i=0;i<roundTrips.size();i++) {
			List<RoundTripSM> oList = new ArrayList<RoundTripSM>();
			List<RoundTripSM> items = new ArrayList<RoundTripSM>();
			for(RoundTripSM rt : roundTrips) {
				items.add(rt.clone());
			}
			int start = i;
			int index = i;
			int count = 0;
			Long duration =0L;
			Long totalDuration = 0L;
			OrderedRoundTrips order = getMinOrder(start, index, count, duration, totalDuration, items, oList, roundTrips.size());
			if(order.duration < minOrder.duration) {
				minOrder = order;
			}
		}
//		System.out.println("====== Minimized Round Trips for Crewlink ======");
//		System.out.println(minOrder.roundTrips);
//		System.out.println(minOrder.duration);
		return minOrder;
	}
	
	private OrderedRoundTrips getMinOrder(int start,int index, int count,Long duration,Long totalDuration,List<RoundTripSM> items, List<RoundTripSM> oList, int totalCount) {
		RoundTripSM a = items.get(index);
		items.remove(index); 	// remove from source list
		oList.add(a);			// add in oList
		RoundTripSM s = oList.get(0);
		Long dur = new Long(s.startMins - a.startMins);
		if(dur<0) {
			dur = 10080 + dur;
		}
		int itemsLeft = totalCount - oList.size(); 
		if(count == totalCount-1 || (totalDuration > 10*10080 && dur < 1*24*60 && itemsLeft >4 ) || totalDuration > 15*10080) 
		{
			// end condition with complete result
			Long newTDur = totalDuration + dur;
			OrderedRoundTrips result = new OrderedRoundTrips();
			result.roundTrips = oList;
			result.duration = newTDur;
//			System.out.println("---- Result ----");
//			System.out.println(result);
			return result;
		}
		
		int nextAafter = 0;
		if(a.lastDutyDuration<8*60) {
			nextAafter = a.endMins + hqr8less;
		}else {
			nextAafter = a.endMins + hqr8more;
		}
		int nid = getClosestK(items,nextAafter); 
		RoundTripSM b = items.get(nid);
		
		Long nextDuration = new Long(b.startMins - a.startMins);
		if(nextDuration < 0) {
			nextDuration = 10080 + nextDuration;
		}
		
		Long actualRest = new Long(b.startMins - a.endMins);
		if(actualRest < 0) {
			actualRest = 10080 + actualRest;
		}
		
		Long totalNextDuration = duration + nextDuration;
		if(totalNextDuration > minPRin && totalNextDuration < maxPRin) {
			OrderedRoundTrips prOrder = null;
			OrderedRoundTrips hqrOrder = null;
			{
				// Add PR 
				List<RoundTripSM> prItems = new ArrayList<RoundTripSM>();
				for(RoundTripSM i : items) {
					prItems.add(i.clone());
				}
				List<RoundTripSM> prOList = new ArrayList<RoundTripSM>();
				for(RoundTripSM i : oList) {
					prOList.add(i.clone());
				}
				int nextAAferPR = a.endMins + pr;
				int nidpr = getClosestK(items,nextAAferPR);
				Long newDuration = 0L;
				RoundTripSM prb = items.get(nidpr);
				Long nextDurationPR = new Long(prb.startMins - a.startMins);
				if(nextDurationPR < 0) {
					nextDurationPR = 10080 + nextDurationPR;
				}
//				Long actualRestPR = new Long(b.startMins - a.endMins);
//				if(actualRestPR < 0) {
//					actualRestPR = 10080 + actualRestPR;
//				}
//				if(actualRestPR<pr) {
//					OrderedRoundTrips invalid = new OrderedRoundTrips();
//					invalid.duration = Long.MAX_VALUE;
//					return invalid;
//				}
				
				Long newTotalDuration = totalDuration + nextDurationPR; 
				prOrder = getMinOrder(start,nidpr,count+1,newDuration,newTotalDuration,prItems,prOList,totalCount);
			}
			{
				// Add HQR
				List<RoundTripSM> hqrItems = new ArrayList<RoundTripSM>();
				for(RoundTripSM i : items) {
					hqrItems.add(i.clone());
				}
				List<RoundTripSM> hqrOList = new ArrayList<RoundTripSM>();
				for(RoundTripSM i : oList) {
					hqrOList.add(i.clone());
				}
				Long newTotalDuration = totalDuration + nextDuration;
				if(actualRest > pr) {
					totalNextDuration = 0L;
				}
//				else if (actualRest < hqr8less){
//					OrderedRoundTrips invalid = new OrderedRoundTrips();
//					invalid.duration = Long.MAX_VALUE;
//					return invalid;
//				}
				hqrOrder = getMinOrder(start,nid,count+1,totalNextDuration,newTotalDuration,hqrItems,hqrOList,totalCount);
			}
			if(prOrder.duration<=hqrOrder.duration) {
				return prOrder;
			}else {
				return hqrOrder;
			}
		}else if(totalNextDuration > minPRin && totalNextDuration >= maxPRin) {
			OrderedRoundTrips prOrder;
			// Add PR 
			List<RoundTripSM> prItems = new ArrayList<RoundTripSM>();
			for(RoundTripSM i : items) {
				prItems.add(i.clone());
			}
			List<RoundTripSM> prOList = new ArrayList<RoundTripSM>();
			for(RoundTripSM i : oList) {
				prOList.add(i.clone());
			}
			int nextAAferPR = a.endMins + pr;
			int nidpr = getClosestK(items,nextAAferPR);
			Long newDuration = 0L;
			RoundTripSM prb = items.get(nidpr);
			Long nextDurationPR = new Long(prb.startMins - a.startMins);
			if(nextDurationPR < 0) {
				nextDurationPR = 10080 + nextDurationPR;
			}
//			Long actualRestPR = new Long(b.startMins - a.endMins);
//			if(actualRestPR < 0) {
//				actualRestPR = 10080 + actualRestPR;
//			}
//			if(actualRestPR<pr) {
//				OrderedRoundTrips invalid = new OrderedRoundTrips();
//				invalid.duration = Long.MAX_VALUE;
//				return invalid;
//			}
			Long newTotalDuration = totalDuration + nextDurationPR; 
			prOrder = getMinOrder(start,nidpr,count+1,newDuration,newTotalDuration,prItems,prOList,totalCount);
			return prOrder;
		}else {
			OrderedRoundTrips hqrOrder;
			// Add HQR
			List<RoundTripSM> hqrItems = new ArrayList<RoundTripSM>();
			for(RoundTripSM i : items) {
				hqrItems.add(i.clone());
			}
			List<RoundTripSM> hqrOList = new ArrayList<RoundTripSM>();
			for(RoundTripSM i : oList) {
				hqrOList.add(i.clone());
			}
			Long newTotalDuration = totalDuration + nextDuration; 
			if(actualRest > pr) {
				totalNextDuration = 0L;
			}
//			else if (actualRest < hqr8less){
//				OrderedRoundTrips invalid = new OrderedRoundTrips();
//				invalid.duration = Long.MAX_VALUE;
//				return invalid;
//			}
			hqrOrder = getMinOrder(start,nid,count+1,totalNextDuration,newTotalDuration,hqrItems,hqrOList,totalCount);
			return hqrOrder;
		}
	}

	private int getClosestK(List<RoundTripSM> roundTrips, int x) {
	    int low = 0;
	    int high = roundTrips.size() - 1;

	    if ( high < 0 )
	        throw new IllegalArgumentException("The array cannot be empty");

	    OUTER:
	    while ( low < high ) {
	        int mid = (low + high) / 2;
	        int d1 = Math.abs(roundTrips.get(mid).startMins - x);
	        int d2 = Math.abs(roundTrips.get(mid+1).startMins - x);
	        if ( d2 < d1 )
	            low = mid + 1;
	        else if ( d2 > d1 )
	            high = mid;
	        else { // --- handling "d1 == d2" ---
	            for ( int right=mid+2; right<=high; right++ ) {
	                d2 = Math.abs(roundTrips.get(right).startMins - x);
	                if ( d2 < d1 ) {
	                    low = right;
	                    continue OUTER;
	                } else if ( d2 > d1 ) {
	                    high = mid;
	                    continue OUTER;
	                }
	            }
	            high = mid;
	        }
	    }
	    if(roundTrips.get(high).startMins < x) {
	    	boolean rev = false;
		    for(int i=high,c=0;i!=high||c<roundTrips.size();i=(i+1)%roundTrips.size(),c++) {
		    	int d = roundTrips.get(i).startMins-x;
		    	if(d<0&&rev) {
		    		return i;
		    	}if(d>=0) {
		    		return i;
		    	}
		    	if(i==roundTrips.size()-1) {
		    		rev = true;
		    	}
		    }
	    }
	    return high;
	}
}

class RoundTripComp implements Comparator<RoundTripSM>{

	@Override
	public int compare(RoundTripSM item1, RoundTripSM item2) {
		int d;
	
		d = item1.startMins - item2.startMins;
		if(d==0) {
			d = (item2.startMins - item2.endMins)-(item1.startMins - item1.endMins);
		}
		
		return d;
	}
	
}
