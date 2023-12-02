package br.com.glojuara.pocspringbatch.processors;

import br.com.glojuara.pocspringbatch.domain.Proposal;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ProcessadorValidacaoProcessorConfig {
	private Set<String> emails = new HashSet<>();

	@Bean
	ItemProcessor<Proposal, Proposal> procesadorValidacaoProcessor() throws Exception {
		return new CompositeItemProcessorBuilder<Proposal, Proposal>()
				.delegates(beanValidatingProcessor(), emailValidatingProcessor())
				.build();
	}

	private BeanValidatingItemProcessor<Proposal> beanValidatingProcessor() throws Exception {
		BeanValidatingItemProcessor<Proposal> processor = new BeanValidatingItemProcessor<>();
		processor.setFilter(true);
		processor.afterPropertiesSet();
		return processor;
	}

	private ValidatingItemProcessor<Proposal> emailValidatingProcessor() {
		ValidatingItemProcessor<Proposal> processor = new ValidatingItemProcessor<>();
		processor.setValidator(validator());
		processor.setFilter(true);
		return processor;
	}

	private Validator<Proposal> validator() {
		return proposal -> {
            if (emails.contains(proposal.getEmail()))
                throw new ValidationException(String.format("A proposta %s já foi processada!", proposal.getEmail()));
            emails.add(proposal.getEmail());
        };
	}
}
