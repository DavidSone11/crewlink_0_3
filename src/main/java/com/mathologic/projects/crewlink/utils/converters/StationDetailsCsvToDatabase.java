package com.mathologic.projects.crewlink.utils.converters;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mathologic.projects.crewlink.models.Station;
import com.mathologic.projects.crewlink.repositories.StationRepository;

/**
 * Created by vivek on 31/10/15.
 */
@Service("StationDetails")
public class StationDetailsCsvToDatabase implements CsvToDatabase{

	@Autowired
	private StationRepository stationRepo;

   /* @Autowired
    private TrainRepository trainRepo;
    
    @Autowired
    private TrainTypeRepository trainTypeRepo;*/
    

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

	@Override
	public boolean processRecord(String[] columns) {
		 Station station = null;
		
		try
		{
			station = stationRepo.findByCode(columns[0]);
			if(station==null)
			{
				station = new Station();
				station.setCode(columns[0]);
			}
			station.setName(columns[1]);
			stationRepo.save(station);
		}
		catch(Exception e)
		{
			System.out.println("Error in Station Details : " +e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}

	

    /*public boolean processRecord(String[] columns){
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
			System.out.println("Error : in sourceStation : " + exe.getMessage());
		}

		try {
			destinationStation = stationRepo.findByCode(columns[8]);
			if (destinationStation == null) {
				destinationStation = new Station();
				destinationStation.setCode(columns[8]);
			}
			stationRepo.save(destinationStation);
		} catch (Exception exe) {
			System.out.println("Error : in destinationStation : " + exe.getMessage());
		}
        
    	try{
            try{
                trainType = trainTypeRepo.findByName(columns[2]);
                if(trainType == null){
                	trainType = new TrainType();
                	trainType.setName(columns[2]);
                	trainTypeRepo.save(trainType);
                }
            }catch (Exception exe){
                System.out.println("Error : in TrainType : "+exe.getMessage());
            }
            
            try{
            	List<Train> trains = new LinkedList<Train>();
            	for(int i=12;i<19;i++){
            		if(!columns[i].isEmpty()){
            			Integer trainNo = Integer.parseInt(columns[0]);
            			Day startDay = Day.values()[i-12];
            			Train train = trainRepo.findByTrainNoAndStartDay(trainNo, startDay);
            			if(train==null){
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
                
            }catch (Exception exe){
                System.out.println("Error : in Train : "+exe.getMessage());
            }
        }
        catch (Exception ex){
            System.out.println("Error : "+ex.getMessage());
        }
        return false;
    }
     
    

	@Override
	public boolean processRecordSartDay(String[] columns) {
		try{
        	List<Train> trains = new LinkedList<Train>();
        			Integer trainNo = Integer.parseInt(columns[0]);
        			Day startDay = Day.valueOf(columns[2]);
        			System.out.println("trainNo "+trainNo);
        			Train train = trainRepo.findByTrainNoAndStartDay(trainNo, startDay);
        			if(train==null){
        				train = new Train();
        				train.setTrainNo(trainNo);
        				train.setStartDay(startDay);
        			}
        			trains.add(train);
        			trainRepo.save(trains);
            
        }catch (Exception exe){
            System.out.println("Error : in Train : "+exe.getMessage());
        }
		return false;
	}

	@Override
	public boolean processRecordTrainDetails(String[] columns) {
		 TrainType trainType = null;
	        Station sourceStation = null;
	        Station destinationStation = null;
	        
		try{
        	List<Train> trains = new LinkedList<Train>();
        			Integer trainNo = Integer.parseInt(columns[3]);
        			//System.out.println("trainNo "+trainNo);
        			
        			Train train[] = trainRepo.findByTrainNumber(trainNo);
        			for(int i=0;i<train.length;i++){
        				//System.out.println(train[i].getTrainNo());
        				
        				 try {
        						sourceStation = stationRepo.findByCode(columns[11]);
        						if (sourceStation == null) {
        							sourceStation = new Station();
        							sourceStation.setCode(columns[11]);
        						}
        						stationRepo.save(sourceStation);
        					} catch (Exception exe) {
        						System.out.println("Error : in sourceStation : " + exe.getMessage());
        					}

        					try {
        						destinationStation = stationRepo.findByCode(columns[12]);
        						if (destinationStation == null) {
        							destinationStation = new Station();
        							destinationStation.setCode(columns[12]);
        						}
        						stationRepo.save(destinationStation);
        					} catch (Exception exe) {
        						System.out.println("Error : in destinationStation : " + exe.getMessage());
        					}
        					try{
        		                trainType = trainTypeRepo.findByName(columns[6]);
        		                if(trainType == null){
        		                	trainType = new TrainType();
        		                	trainType.setName(columns[6]);
        		                	trainTypeRepo.save(trainType);
        		                }
        		            }catch (Exception exe){
        		                System.out.println("Error : in TrainType : "+exe.getMessage());
        		            }
        				train[i].setName(columns[4]);
        				train[i].setFromStation(sourceStation);
        				train[i].setToStation(destinationStation);
        				train[i].setTrainType(trainType);
        				trainRepo.save(train[i]);
        			}
        			
        			if(train==null){
        				train = new Train();
        			}
        			trains.add(train);
        			trainRepo.save(trains);
        			
        			
            
        }catch (Exception exe){
            System.out.println("Error : in Train : "+exe.getMessage());
        }
		
		
		return false;
	}*/
    }
