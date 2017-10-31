package com.mathologic.projects.crewlink.controllers;


import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mathologic.projects.crewlink.models.Role;
import com.mathologic.projects.crewlink.models.Station;
import com.mathologic.projects.crewlink.models.User;
import com.mathologic.projects.crewlink.repositories.UserRepository;

/**
 * Created by vivek on 31/10/15.
 */
@Controller
@RequestMapping("/api/custom/user")
public class CurrentUserDetials {
    @Autowired
    UserRepository userRepository;
    
    @RequestMapping(value="/time",method=RequestMethod.GET)
    public @ResponseBody LocalTime getTime(@RequestParam("time")@DateTimeFormat(pattern="HH:mm")LocalTime time){
    	return time;
    }

    @RequestMapping(value="/myDetails", method= RequestMethod.GET)
    public @ResponseBody User myDetails() {
    	UserDetails userDetails = null;
    	User user = null;
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if (!(auth instanceof AnonymousAuthenticationToken)) {
    		userDetails = (UserDetails)auth.getPrincipal();
    	}
    	if(userDetails!=null) {
    		User dbUser = userRepository.findByUsernameAndIsActive(userDetails.getUsername(),true);
    		if(dbUser!=null) {
    			user = new User();
    			user.setId(dbUser.getId());
	    		user.setUsername(dbUser.getUsername());
	    		user.setEmail(dbUser.getEmail());
	    		user.setEmployeeNo(dbUser.getEmployeeNo());
	    		user.setExtension(dbUser.getExtension());
	    		user.setFirstName(dbUser.getFirstName());
	    				//Station station = new Station();
	    				//station.setCode(dbUser.getStation().getCode());
	    				//station.setName(dbUser.getStation().getName());
	    		//user.setStation(station);
	    		user.setLastName(dbUser.getLastName());
	    		user.setMobileNo(dbUser.getMobileNo());
	    			Role role = new Role();
	    			role.setName(dbUser.getRole().getName());
	    		user.setRole(role);
	    		user.setTelephoneNo(dbUser.getTelephoneNo());
    		}
    	}
        return user;
    }
    
    @RequestMapping(value="/activate/{activationKey}", method= RequestMethod.GET)
    public @ResponseBody Boolean activateUser(@PathVariable("activationKey") String activationKey) {
    	if(activationKey!=null && activationKey!="") {
    		User user = userRepository.findByActivationKey(activationKey);
    		if(user!=null) {
    			user.setIsActive(true);
    			userRepository.save(user);
    			return true;
    		}
    	}
        return false;
    }

}
