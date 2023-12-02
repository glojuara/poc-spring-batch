package br.com.glojuara.pocspringbatch.steps;

import br.com.glojuara.pocspringbatch.domain.Proposal;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LeituraArquivoDelimitadoStepConfig {
	private JobRepository jobRepository;
	private PlatformTransactionManager transactionManager;

	public LeituraArquivoDelimitadoStepConfig(JobRepository jobRepository,
                                              PlatformTransactionManager transactionManager) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	@Bean
	Step leituraArquivoDelimitadoStep(FlatFileItemReader<Proposal> itemReader,
									  ItemProcessor<Proposal, Proposal> itemProcessor,
									  ItemWriter<Proposal> itemWriter) {
		return new StepBuilder("leituraArquivoDelimitadoStep", jobRepository)
				.<Proposal, Proposal>chunk(1, transactionManager)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
	}
}
