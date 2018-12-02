package br.com.ev.batch.configurations;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 
 * @author Evelyn
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Tasklet tasklet() {
		return new CountingTasklet();
	}
	
	@Bean
	public Step step4() {
		return stepBuilderFactory.get("step1")
				.tasklet((contribution, chunkContext) -> {
					System.out.println(">> Step 4 from job 2!!!");
					return RepeatStatus.FINISHED;
				}).build();
	}
	
	@Bean
	public Flow flow1() {
		return new FlowBuilder<Flow>("flow1")
				.start(stepBuilderFactory.get("step1")
						.tasklet(tasklet()).build())
				.build();
	}
	
	@Bean
	public Flow flow2() {
		return new FlowBuilder<Flow>("flow2")
				.start(stepBuilderFactory.get("step2")
						.tasklet(tasklet()).build())
				.next(stepBuilderFactory.get("step3")
						.tasklet(tasklet()).build())
				.build();
	}
	
	
	@Bean(name="job1")
	public Job job1() {
		return jobBuilderFactory.get("job1")
				.start(flow1())
				.split(new SimpleAsyncTaskExecutor()).add(flow2())
				.end()
				.build();
	}
	
	@Bean(name="job2")
	public Job job2() {
		return jobBuilderFactory.get("job2")
				.start(step4())
				.build();
	}
	
	private static class CountingTasklet implements Tasklet {
		
		@Override
		public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
			System.out.println(String.format("%s has been executed on thread %s", chunkContext.getStepContext().getStepName(), Thread.currentThread().getName()));
			return RepeatStatus.FINISHED;
		}
	}

}
