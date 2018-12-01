package br.com.ev.batch.configurations;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author Evelyn
 *
 */
@Configuration
//Ativar processamento em lote - Faz a al�a de inicializa��o de toda a infraestrutura que o spring batch precisa executar, e fornece alguns construtores necess�rios
@EnableBatchProcessing 
public class FlowConfiguration {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.tasklet((contribution, chunkContext) -> {
					System.out.println(">> Step 1 from inside flow foo");
					return RepeatStatus.FINISHED;
				}).build();
	}
	
	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2")
				.tasklet((contribution, chunkContext) -> {
					System.out.println(">> Step 2 from inside flow foo");
					return RepeatStatus.FINISHED;
				}).build();
	}
	
	@Bean
	public Flow foo() {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("foo");
		
		flowBuilder.start(step1())
		.next(step2())
		.end();
		
		return flowBuilder.build();
	}

}
