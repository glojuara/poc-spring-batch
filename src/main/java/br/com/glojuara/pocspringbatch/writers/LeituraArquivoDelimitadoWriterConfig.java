package br.com.glojuara.pocspringbatch.writers;

import br.com.glojuara.pocspringbatch.domain.Proposal;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeituraArquivoDelimitadoWriterConfig {
	@Bean
	ItemWriter<Proposal> leituraArquivoDelimitadoWriter() {
		return items -> {
			items.forEach(System.out::println);
		};
	}
}
