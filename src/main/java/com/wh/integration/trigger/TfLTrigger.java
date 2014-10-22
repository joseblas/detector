package com.wh.integration.trigger;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.util.Assert;

public class TfLTrigger  implements Trigger {

	private volatile long period;

	private volatile TimeUnit timeUnit;

	private volatile long initialDelay = 0;

	private volatile boolean fixedRate = false;
	
	public TfLTrigger(long period, TimeUnit timeUnit) {
		Assert.isTrue(period >= 0, "period must not be negative");
		Assert.notNull(timeUnit, "timeUnit must not be null");
		
		this.timeUnit = timeUnit;
		this.period = this.timeUnit.toMillis(period);
	}
	
	
	public TfLTrigger(long period) {
		this(period, TimeUnit.MILLISECONDS);
	}
	
	
	public void setFixedRate(boolean fixedRate) {
		this.fixedRate = fixedRate;
	}
	
	
	public void setInitialDelay(long initialDelay) {
		Assert.isTrue(initialDelay >= 0, "initialDelay must not be negative");
		this.initialDelay = this.timeUnit.toMillis(initialDelay);
	}
	
	@Override
	public Date nextExecutionTime(TriggerContext triggerContext) {
		
		if (triggerContext.lastScheduledExecutionTime() == null) {
			return new Date(System.currentTimeMillis() + this.initialDelay);
		}
		else if (this.fixedRate) {
			return new Date(triggerContext.lastScheduledExecutionTime().getTime() + this.period);
		}
		return new Date(triggerContext.lastCompletionTime().getTime() + this.period);
	}
	

}
