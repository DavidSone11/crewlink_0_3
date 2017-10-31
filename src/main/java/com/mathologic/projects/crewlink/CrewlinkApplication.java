package com.mathologic.projects.crewlink;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mathologic.projects.crewlink.models.User;
import com.mathologic.projects.crewlink.repositories.UserRepository;

@ComponentScan(basePackages = { "com.mathologic.projects.crewlink" })
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class CrewlinkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CrewlinkApplication.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CrewlinkApplication.class);
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter{
	
	@Autowired
	UserRepository userRepo;
	
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.userDetailsService(userDetailsService());
		
	}
	
	@Bean
	UserDetailsService userDetailsService(){
		
		return new UserDetailsService(){

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				
				User user = userRepo.findByUsernameAndIsActive(username,true);
				if(user!=null){
					
					return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true
								,true,true,AuthorityUtils.createAuthorityList(user.getRole().getName()));
				}else{
					
					throw new UsernameNotFoundException("user not found");
				}	
			}
			
			
		};
	}
}

