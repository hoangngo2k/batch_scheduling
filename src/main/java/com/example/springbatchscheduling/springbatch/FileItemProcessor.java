package com.example.springbatchscheduling.springbatch;

import com.example.springbatchscheduling.entity.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FileItemProcessor implements ItemProcessor<FileEntity, FileEntity> {

    private static final Logger log = LoggerFactory.getLogger(FileItemProcessor.class);

    @Override
    public FileEntity process(final FileEntity FileEntity) throws Exception {
        final String filename = FileEntity.getName().toUpperCase();
        final String content = FileEntity.getContent().toUpperCase();

        final FileEntity transformedFile = new FileEntity(filename, content);

        log.info("Converting (" + FileEntity + ") into (" + transformedFile + ")");

        return transformedFile;
    }
}
