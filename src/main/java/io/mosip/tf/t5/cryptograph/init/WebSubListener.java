package io.mosip.tf.t5.cryptograph.init;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.tf.t5.cryptograph.constant.LoggerFileConstant;
import io.mosip.tf.t5.cryptograph.logger.CryptographLogger;
import io.mosip.tf.t5.cryptograph.util.WebSubSubscriptionHelper;

@Component
public class WebSubListener 
implements ApplicationListener<ApplicationReadyEvent> {

	private static Logger logger = CryptographLogger.getLogger(WebSubListener.class);

	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;
  
	@Value("${mosip.event.delay :120000}")
	private int taskSubsctiptionDelay;

	@Autowired
	private WebSubSubscriptionHelper webSubSubscriptionHelper;
  
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		logger.info(LoggerFileConstant.SESSIONID.toString(), "onApplicationEvent", this.getClass().getSimpleName(),
				"Scheduling event subscriptions after (milliseconds): " + taskSubsctiptionDelay);
		taskScheduler.schedule(this::initSubsriptions, new Date(System.currentTimeMillis() + taskSubsctiptionDelay));
	}

	private void initSubsriptions() {
		logger.info(LoggerFileConstant.SESSIONID.toString(), "initSubsriptions", this.getClass().getSimpleName(),
				"Initializing subscribptions..");
		webSubSubscriptionHelper.initSubsriptions();
	}

}