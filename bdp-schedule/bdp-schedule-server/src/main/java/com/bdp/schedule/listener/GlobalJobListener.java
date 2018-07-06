package com.bdp.schedule.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GlobalJobListener implements JobListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("execute GlobalJobListener.jobToBeExecuted(JobExecutionContext context)");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("execute GlobalJobListener.jobExecutionVetoed(JobExecutionContext context)");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		logger.info(
				"execute GlobalJobListener.jobWasExecuted(JobExecutionContext context, JobExecutionException jobException)");
	}

}
