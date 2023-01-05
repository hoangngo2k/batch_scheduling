package com.example.springbatchscheduling.springbatch;

import com.example.springbatchscheduling.entity.FileEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<FileEntity> reader() {
        return new FlatFileItemReaderBuilder<FileEntity>()
                .name("fileItemReader")
                .resource(new ClassPathResource("users.csv"))
                .delimited()
                .names("name", "content")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<FileEntity>() {{
                    setTargetType(FileEntity.class);
                }})
                .build();
    }

    @Bean
    public FileItemProcessor processor() {
        return new FileItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<FileEntity> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<FileEntity>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO files (`name`, content) VALUES (:name, :content)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository,
                             JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importFileJob")
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager, JdbcBatchItemWriter<FileEntity> writer) {
        return new StepBuilder("step1")
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<FileEntity, FileEntity>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
