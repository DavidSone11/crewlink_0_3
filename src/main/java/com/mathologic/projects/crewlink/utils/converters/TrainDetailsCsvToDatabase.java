package com.mathologic.projects.crewlink.utils.converters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mathologic.projects.crewlink.custom.models.SelectViewModel;
import com.mathologic.projects.crewlink.custom.repositories.TrainVMRepository;
import com.mathologic.projects.crewlink.models.Day;

/**
 * Created by vivek on 31/10/15.
 */
@Service("TrainDetails")
public class TrainDetailsCsvToDatabase implements CsvToDatabase {

	/*@Autowired
	private StationRepository stationRepo;

	@Autowired
	private TrainRepository trainRepo;*/

	/*@Autowired
	private TrainTypeRepository trainTypeRepo;
*/
	
	/*@Autowired 
	private StationVMRepository stationVMRepository;
	
	@Autowired
	private TrainTypeVMRepository trainTypeRepository;
	*/
	@Autowired
	private TrainVMRepository trainVMRepository;
	
	private String seperator = ",";
	private String stringMarker = "'";

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
	
	/**
	 * This method accept all columns of a line and sends to the save train method
	 */
	public boolean processRecord(String[] columns) {
		Integer trainNo = null;
		String startDays=new String();
		Integer day=new Integer(0);
		// format the days in comma separated string format. eg. '1,2,3' by the below logic.
		// column 6 to 12 contains the days columns. 
		// reads each day column and checks whether it contains 'y' or not. If it does means convert that
		// to enum value of that day.
		try {
			trainNo = Integer.parseInt(columns[0]);
			for (int i = 4; i <= 10; i++) {
				if (!columns[i].isEmpty()) {
					day = i-4;
					startDays=startDays.concat(","+day.toString());
				}
				
			}
			startDays = startDays.substring(1, startDays.length());
			// call the save train method
			trainVMRepository.saveTrain(trainNo,  
					columns[1], columns[2], 
					columns[3],  startDays,
					columns[11]);
			
	
		} catch (Exception exe) {
			System.out
					.println("Error : in sourceStation : " + exe.getMessage());
		}
		return false;
	}


	/*public boolean processRecord(String[] columns) {
		TrainType trainType = null;
		Station sourceStation = null;
		Station destinationStation = null;

		try {
			sourceStation = stationRepo.findByCode(columns[6]);
			if (sourceStation == null) {
				sourceStation = new Station();
				sourceStation.setCode(columns[6]);
			}
			stationRepo.save(sourceStation);
		} catch (Exception exe) {
			System.out
					.println("Error : in sourceStation : " + exe.getMessage());
		}

		try {
			destinationStation = stationRepo.findByCode(columns[8]);
			if (destinationStation == null) {
				destinationStation = new Station();
				destinationStation.setCode(columns[8]);
			}
			stationRepo.save(destinationStation);
		} catch (Exception exe) {
			System.out.println("Error : in destinationStation : "
					+ exe.getMessage());
		}

		try {
			try {
				trainType = trainTypeRepo.findByName(columns[2]);
				if (trainType == null) {
					trainType = new TrainType();
					trainType.setName(columns[2]);
					trainTypeRepo.save(trainType);
				}
			} catch (Exception exe) {
				System.out
						.println("Error : in TrainType : " + exe.getMessage());
			}

			try {
				List<Train> trains = new LinkedList<Train>();
				for (int i = 12; i < 19; i++) {
					if (!columns[i].isEmpty()) {
						Integer trainNo = Integer.parseInt(columns[0]);
						Day startDay = Day.values()[i - 12];
						Train train = trainRepo.findByTrainNoAndStartDay(
								trainNo, startDay);
						if (train == null) {
							train = new Train();
							train.setTrainNo(trainNo);
							train.setStartDay(startDay);
						}
						train.setName(columns[1]);
						train.setFromStation(sourceStation);
						train.setToStation(destinationStation);
						train.setTrainType(trainType);
						trains.add(train);
					}
				}
				trainRepo.save(trains);

			} catch (Exception exe) {
				System.out.println("Error : in Train : " + exe.getMessage());
			}
		} catch (Exception ex) {
			System.out.println("Error : " + ex.getMessage());
		}
		return false;
	}
*//*
	@Override
	public boolean processRecordSartDay(String[] columns) {
		try {
			List<Train> trains = new LinkedList<Train>();
			Integer trainNo = Integer.parseInt(columns[0]);
			Day startDay = Day.valueOf(columns[2]);
			System.out.println("trainNo " + trainNo);
			Train train = trainRepo.findByTrainNoAndStartDay(trainNo, startDay);
			if (train == null) {
				train = new Train();
				train.setTrainNo(trainNo);
				train.setStartDay(startDay);
			}
			trains.add(train);
			trainRepo.save(trains);

		} catch (Exception exe) {
			System.out.println("Error : in Train : " + exe.getMessage());
		}
		return false;
	}

	@Override
	public boolean processRecordTrainDetails(String[] columns) {
		TrainType trainType = null;
		Station sourceStation = null;
		Station destinationStation = null;

		try {
			Integer trainNo = Integer.parseInt(columns[3]);
			// System.out.println("trainNo "+trainNo);

			List<Train> trainsByNumber = trainRepo.findByTrainNumber(trainNo);
			for (Train train : trainsByNumber) {
				// System.out.println(train[i].getTrainNo());

				try {
					sourceStation = stationRepo.findByCode(columns[11]);
					if (sourceStation == null) {
						sourceStation = new Station();

						sourceStation.setCode(columns[11]);
					}
					System.out.println("source station : " + sourceStation);
					stationRepo.save(sourceStation);
				} catch (Exception exe) {
					System.out.println("Error : in sourceStation : "
							+ exe.getMessage());
				}

				try {
					destinationStation = stationRepo.findByCode(columns[12]);
					if (destinationStation == null) {
						destinationStation = new Station();
						System.out.println(destinationStation.getCode());
						destinationStation.setCode(columns[12]);
					}
					System.out.println("destinationStation station : "
							+ destinationStation);
					stationRepo.save(destinationStation);
				} catch (Exception exe) {
					System.out.println("Error : in destinationStation : "
							+ exe.getMessage());
				}
				try {
					trainType = trainTypeRepo.findByName(columns[6]);
					if (trainType == null) {
						trainType = new TrainType();
						trainType.setName(columns[6]);
						trainTypeRepo.save(trainType);
					}
				} catch (Exception exe) {
					System.out.println("Error : in TrainType : "
							+ exe.getMessage());
				}
				train.setName(columns[4]);
				train.setFromStation(sourceStation);
				train.setToStation(destinationStation);
				train.setTrainType(trainType);
				trainRepo.save(train);
			}

			
			 * if(train==null){ train = new Train(); } trains.add(train);
			 * trainRepo.save(trains);
			 

		} catch (Exception exe) {
			System.out.println("Error : in Train : " + exe.getMessage());
		}

		return false;
	}*/
}
