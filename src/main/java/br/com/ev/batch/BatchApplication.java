package br.com.ev.batch;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("job1")
	private Job job1;
	
	@Autowired
	@Qualifier("job2")
	private Job job2;
	
	private int x = 2;

	@Override
	public void run(String... args) throws Exception {
		
		if(x == 1) {
			
			Map<String, JobParameter> confMap = new HashMap<>();
		    confMap.put("time", new JobParameter(System.currentTimeMillis()));
		    JobParameters jobParameters = new JobParameters(confMap);
			
			jobLauncher.run(job1, jobParameters);
			
		}else {
			
			Map<String, JobParameter> confMap = new HashMap<>();
		    confMap.put("time", new JobParameter(System.currentTimeMillis()));
		    JobParameters jobParameters = new JobParameters(confMap);
			
			jobLauncher.run(job2, jobParameters);
			
		}
		
		
		
		
	}
}
