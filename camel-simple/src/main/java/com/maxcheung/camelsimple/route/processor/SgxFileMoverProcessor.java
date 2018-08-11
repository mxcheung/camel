package com.maxcheung.camelsimple.route.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SgxFileMoverProcessor implements Processor {


	private static final Logger LOG = LoggerFactory.getLogger(SgxFileMoverProcessor.class);

	
	public SgxFileMoverProcessor() {
		super();
	}

	public void process(Exchange exchange) throws Exception {
		checkHoldingBay() ;
	}

	public void checkHoldingBay() throws IOException {
		LOG.info("Checking files in holdingBay");
		Files.newDirectoryStream(Paths.get("C:/camel/sgxtest/holdingbay/"), path -> path.toFile().isFile())
				.forEach(f -> moveFile(f.toFile()));
	}

	private void moveFile(File fileToMove) {
		File destDir = new File("C:/sqltest/process/");
		File targetFile = new File(destDir, fileToMove.getName());
		String fileName = fileToMove.getName();
		int idx = fileName.indexOf('_');
		String fileTime = fileName.substring(0, idx);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//		yyyyMMddHHmmssSSS
//		20180803063038958
		LocalDateTime dateTime = LocalDateTime.parse(fileTime, formatter);
		long waitTimeSeconds = ChronoUnit.SECONDS.between(dateTime, LocalDateTime.now());
		LOG.info("Found file fileName : {}, fileTime : {} waitTimeSeconds : {}", fileName, dateTime, waitTimeSeconds);
		if (waitTimeSeconds > 300) {
			try {
				FileUtils.moveFile(fileToMove, targetFile);
			} catch (IOException e) {
				LOG.info("Error moving file exception : {}", e);
			}
		}
	}
}