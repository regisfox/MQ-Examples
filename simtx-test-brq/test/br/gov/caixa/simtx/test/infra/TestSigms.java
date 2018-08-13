package br.gov.caixa.simtx.test.infra;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class TestSigms {
	final static Logger logger = Logger.getLogger(TestSigms.class);
	@Rule
	public TestRule watcher = new TestWatcher() {
		private SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		@Override
		protected void starting(Description description) {
			Date agora = new Date();
			logger.info("======== teste: " + description.getMethodName() + " (" + fmt.format(agora)
					+ ") ============");
			logger.info("");
		}

		@Override
		protected void finished(Description description) {
			logger.info("======== - ============");
			logger.info("");
		}
	};

	protected void sleep() {
		sleep(5000L);
	}

	protected void sleep(Long tempo) {
		try {
			Thread.sleep(tempo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setUserMtx() {
		System.setProperty("user.name", "SMTXSD01");
	}
	
	public void setUserGms() {
		System.setProperty("user.name", "SGMSS01D");
	}
	
	public void setUserBrq() {
		System.setProperty("user.name", "brqmq");
	}
}
