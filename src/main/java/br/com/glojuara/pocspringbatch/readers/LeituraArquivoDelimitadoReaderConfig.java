package br.com.glojuara.pocspringbatch.readers;

import br.com.glojuara.pocspringbatch.domain.Proposal;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.aws.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;


@Configuration
public class LeituraArquivoDelimitadoReaderConfig {

	@Bean
	@StepScope
	public MultiResourceItemReader<Proposal> multiResourceItemReader(
			@Value("#{jobParameters['proposalsFile']}") Resource[] resources,
			FlatFileItemReader<Proposal> flatFileItemReader) throws IOException {

		return new MultiResourceItemReaderBuilder<Proposal>()
				.name("multiplosArquivosClienteTransacaoReader")
				.resources(resources)
				.delegate(flatFileItemReader)
				.build();

	}

	@Bean
	@StepScope
	@ConditionalOnProperty(name = "s3.enabled", havingValue = "true")
	public FlatFileItemReader<Proposal> s3Reader(@Value("#{jobParameters['proposalsFile']}") Resource resource,
												 AmazonS3 amazonS3) {

		var bucketName = "glojuara-batch";
		S3Object s3object = amazonS3.getObject(bucketName, resource.getFilename());

		return new FlatFileItemReaderBuilder<Proposal>()
				.name("leituraArquivoDelimitadoReader")
				.resource(new InputStreamResource(s3object.getObjectContent()))
				.strict(true)
				.delimited()
				.names("document", "name", "email")
				.targetType(Proposal.class)
				.build();
	}


	@Bean
	@StepScope
	@ConditionalOnProperty(name = "s3.enabled", havingValue = "false", matchIfMissing = true)
	public FlatFileItemReader<Proposal> localReader(@Value("#{jobParameters['proposalsFile']}") Resource resource) {

		return new FlatFileItemReaderBuilder<Proposal>()
				.name("leituraArquivoDelimitadoReader")
				.resource(resource)
				.strict(true)
				.delimited()
				.names("document", "name", "email")
				.targetType(Proposal.class)
				.build();
	}

}
