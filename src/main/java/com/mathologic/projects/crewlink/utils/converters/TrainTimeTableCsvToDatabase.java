package com.mathologic.projects.crewlink.utils.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mathologic.projects.crewlink.custom.repositories.TrainStationVMRepository;
import com.mathologic.projects.crewlink.custom.repositories.TrainVMRepository;
import com.mathologic.projects.crewlink.repositories.StationRepository;
import com.mathologic.projects.crewlink.repositories.TrainRepository;
import com.mathologic.projects.crewlink.repositories.TrainStationRepository;

/**
 * Created by vivek on 31/10/15.
 */
@Service("TrainTimeTable")
public class TrainTimeTableCsvToDatabase implements CsvToDatabase {

	@Autowired
	private StationRepository stationRepo;

	@Autowired
	private TrainRepository trainRepo;

	@Autowired
	private TrainStationRepository trainStationRepo;

	@Autowired
	private TrainVMRepository trainVMRepository;

	private String seperator = ",";
	private String stringMarker = "'";

	private String lastTrainNo = "";
	private List<String[]> trainStationList = new ArrayList<String[]>();

	@Autowired
	private TrainStationVMRepository trainStationVMRepository;

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public String getStringMarker() {
		return stringMarker;
	}

	public void setStringMarker(String stringMarker) {
		this.stringMarker = stringMarker;
	}
	/*
	 * public boolean processRecord(String[] columns) { Station station = null,
	 * destinationStation = null, sourceStation = null; try { try { station =
	 * stationRepo.findByCode(columns[3]); if (station == null) { station = new
	 * Station(); station.setCode(columns[3]); } station.setName(columns[4]);
	 * stationRepo.save(station); } catch (Exception exe) { System.out.println(
	 * "Error : in Station : " + exe.getMessage()); }
	 * 
	 * try { sourceStation = stationRepo.findByCode(columns[8]); if
	 * (sourceStation == null) { sourceStation = new Station();
	 * sourceStation.setCode(columns[8]); } sourceStation.setName(columns[9]);
	 * stationRepo.save(sourceStation); } catch (Exception exe) {
	 * System.out.println("Error : in sourceStation : " + exe.getMessage()); }
	 * 
	 * try { destinationStation = stationRepo.findByCode(columns[10]); if
	 * (destinationStation == null) { destinationStation = new Station();
	 * destinationStation.setCode(columns[10]); }
	 * destinationStation.setName(columns[11]);
	 * stationRepo.save(destinationStation); } catch (Exception exe) {
	 * System.out.println("Error : in destinationStation : " +
	 * exe.getMessage()); }
	 * 
	 * // SAVE Train Time Table try { Integer trainNo =
	 * Integer.parseInt(columns[0]); Page<Train> trains =
	 * trainRepo.findByTrainNo(trainNo,new PageRequest(0,20)); for (Train train
	 * : trains) { train.setName(columns[1]);
	 * train.setFromStation(sourceStation);
	 * train.setToStation(destinationStation); TrainStation trainStation =
	 * trainStationRepo.findByTrain_TrainNoAndTrain_StartDayAndStopNumber(
	 * train.getTrainNo(), train.getStartDay(), Integer.parseInt(columns[2]));
	 * if (trainStation == null) { trainStation = new TrainStation();
	 * trainStation.setTrain(train);
	 * trainStation.setStopNumber(Integer.parseInt(columns[2])); }
	 * trainStation.setStation(station);
	 * trainStation.setArrival(LocalTime.parse(columns[5],
	 * DateTimeFormatter.ISO_LOCAL_TIME));
	 * trainStation.setDeparture(LocalTime.parse(columns[6],
	 * DateTimeFormatter.ISO_LOCAL_TIME));
	 * trainStation.setDistance(Long.parseLong(columns[7])); TrainStation
	 * earlierStationTime =
	 * trainStationRepo.findByTrain_TrainNoAndTrain_StartDayAndStopNumber(
	 * Integer.parseInt(columns[0]), train.getStartDay(),
	 * Integer.parseInt(columns[2]) - 1); if (earlierStationTime != null) {
	 * LocalTime tempTime = LocalTime.from(earlierStationTime.getDeparture());
	 * Long minutes = ChronoUnit.MINUTES.between(tempTime,
	 * trainStation.getArrival()); if (minutes > 0) { // It is the same day
	 * trainStation.setJourneyDuration(earlierStationTime.getJourneyDuration() +
	 * minutes);
	 * trainStation.setDayOfJourney(earlierStationTime.getDayOfJourney()); }
	 * else { // These means it is next day
	 * trainStation.setJourneyDuration(earlierStationTime.getJourneyDuration() +
	 * (1440 + minutes));
	 * trainStation.setDayOfJourney(earlierStationTime.getDayOfJourney() + 1); }
	 * } else { trainStation.setDayOfJourney(1);
	 * trainStation.setJourneyDuration(0L); }
	 * trainStation.setDay(Day.values()[(train.getStartDay().ordinal()+
	 * trainStation.getDayOfJourney()-1)%7]);
	 * trainStationRepo.save(trainStation); } } catch (Exception exe) {
	 * System.out.println("Error : in TrainTimeTable :" + exe.getMessage()); }
	 * 
	 * } catch (Exception ex) { System.out.println("Error : " +
	 * ex.getMessage()); } return false; }
	 */

	/*
	 * public boolean processRecord(String[] columns) { Station station = null;
	 * try { try { station = stationRepo.findByCode(columns[4]); if (station ==
	 * null) { station = new Station(); station.setCode(columns[4]); }
	 * //station.setName(columns[4]); stationRepo.save(station); } catch
	 * (Exception exe) { System.out.println("Error : in Station : " +
	 * exe.getMessage()); }
	 * 
	 * // SAVE Train Time Table try { Integer trainNo =
	 * Integer.parseInt(columns[1]); List<Train> trains =
	 * trainRepo.findByTrainNumber(trainNo); for(Train train : trains) {
	 * //System.out.println(train.getTrainNo()); TrainStation trainStation =
	 * trainStationRepo.findByTrain_TrainNoAndTrain_StartDayAndStopNumber(
	 * train.getTrainNo(), train.getStartDay(), Integer.parseInt(columns[3]));
	 * if (trainStation == null) { trainStation = new TrainStation();
	 * trainStation.setTrain(train); } trainStation.setStation(station);
	 * LocalTime time0 = LocalTime.from(LocalTime.parse("00:00",
	 * DateTimeFormatter.ISO_LOCAL_TIME));
	 * trainStation.setArrival(time0.plusMinutes(Long.parseLong(columns[9])));
	 * trainStation.setDeparture(time0.plusMinutes(Long.parseLong(columns[8])));
	 * 
	 * trainStation.setDistance(Long.parseLong(columns[7]));
	 * 
	 * if (trainStation.getDistance()>0) { TrainStation earlierStation =
	 * trainStationRepo.
	 * findTopByTrain_TrainNoAndTrain_StartDayOrderByStopNumberDesc(
	 * Integer.parseInt(columns[1]), train.getStartDay()); Long minutes = 0L;
	 * if(earlierStation.getJourneyDuration()==0L){ LocalTime tempTime =
	 * LocalTime.from(earlierStation.getDeparture()); minutes =
	 * ChronoUnit.MINUTES.between(tempTime, trainStation.getArrival()); } else{
	 * LocalTime tempTime = LocalTime.from(earlierStation.getArrival()); minutes
	 * = ChronoUnit.MINUTES.between(tempTime, trainStation.getArrival());
	 * System.out.println("EArrival   | NArrival  |  minutes");
	 * System.out.println(""+earlierStation.getArrival()+"  | "
	 * +trainStation.getArrival()+ "  | "+minutes);
	 * 
	 * }// this adds time spent at the station up to earlier stations
	 * 
	 * if (minutes > 0) { // It is the same day
	 * trainStation.setJourneyDuration(earlierStation.getJourneyDuration() +
	 * minutes); trainStation.setDayOfJourney(earlierStation.getDayOfJourney());
	 * trainStation.setStopNumber(earlierStation.getStopNumber()+1); } else { //
	 * These means it is next day minutes = (1440 + minutes);
	 * trainStation.setJourneyDuration(earlierStation.getJourneyDuration() +
	 * minutes); trainStation.setDayOfJourney(earlierStation.getDayOfJourney() +
	 * 1); trainStation.setStopNumber(earlierStation.getStopNumber()+1); } }
	 * else { trainStation.setDayOfJourney(1);
	 * trainStation.setJourneyDuration(0L); trainStation.setStopNumber(1); }
	 * trainStation.setDay(Day.values()[(train.getStartDay().ordinal()+
	 * trainStation.getDayOfJourney()-1)%7]);
	 * trainStationRepo.save(trainStation); }
	 * 
	 * } catch (Exception exe) { System.out.println(
	 * "Error : in TrainTimeTable :" + exe.getMessage()); exe.printStackTrace();
	 * }
	 * 
	 * } catch (Exception ex) { System.out.println("Error : " +
	 * ex.getMessage()); } return false; }
	 */
	
	/**
	 * This is used to process records of train running details
	 */

	public boolean processRecord(String[] columns) {
		if (lastTrainNo.isEmpty()) {
			lastTrainNo = columns[0];
		}
		if (!columns[0].equals(lastTrainNo)) {
			processTrainStationList();
			lastTrainNo = "";
			trainStationList = new ArrayList<String[]>();
			if (columns[0].equals("END")) {
				return true;
			}
		}
		trainStationList.add(columns);

		return true;
	}

	private void processTrainStationList() {
		if (trainStationList.size() < 2) {
			return;
		}
		int trainNo = 0;

		for (String[] line : trainStationList) {
			trainNo 				= Integer.parseInt(line[0]);
			int stopNumber 			= Integer.parseInt(line[1]);
			String stationCode 		= line[2].replaceAll("\\s", "");
			int dayOfJourney 		= Integer.parseInt(line[3]);
			String arrivalTime 		= line[4];
			String departuretime 	= line[5];
			long distance 			= Long.parseLong(line[6]);
			trainStationVMRepository.createTrainStations(trainNo,stopNumber, stationCode, dayOfJourney,
					arrivalTime, departuretime, distance);

		}

		/// Delete train_Running_details if Not Exists in CSV but exists in DB
		
//		SelectViewModel trainRaw = trainVMRepository.listTrainsWithOutUserPlan(trainNo, null, null, null, null, null, null, null, "",
//				0L,10L);
//		List<TrainVM> trainList = ResultListToObject.convertAll(trainRaw.getData(), TrainVM.class);
//		for (TrainVM t : trainList) {
//			SelectViewModel trainStationRaw = trainStationVMRepository.listTrainStations(t.getTrainNo(),
//					t.getStartDay(), null, "", 0L, 500L);
//			List<TrainStationVM> dbTrainStationList = ResultListToObject.convertAll(trainStationRaw.getData(),
//					TrainStationVM.class);
//			for (TrainStationVM ts : dbTrainStationList) {
//				boolean isFound = false;
//				for (String[] lines : trainStationList) {
//					String stationCode = lines[3].replaceAll("\\s", "");
//					if (stationCode.equals(ts.getStationCode())) {
//						isFound = true;
//						break;
//					}
//				}
//				if(!isFound){
//					// Delete trainStation record from database 
//					trainStationVMRepository.deleteTrainStations(ts.getId());
//				}
//			}
//		}

	}

}
