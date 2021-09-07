package application;


import javax.inject.Inject;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.motionModel.MotionBatch;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.task.ITaskLogger;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class RobotApplication extends RoboticsAPIApplication {
	@Inject
	private ITaskLogger logger;
	@Inject
	private LBR robot;

	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		// your application execution starts here
		boolean END=false;
		MotionBatch wave = new MotionBatch(ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P4"))).setBlendingRel(0.1).setJointVelocityRel(0.5);
		MotionBatch dance = new MotionBatch(ptp(Math.PI/2,Math.PI/2,0,Math.PI/2,-Math.PI/2,0,0),ptp(Math.PI/2,Math.PI/4,-Math.PI/4,Math.PI/2,-Math.PI/2,0,0),ptp(Math.PI/2,Math.PI/2,0,Math.PI/2,-Math.PI/2,0,0),ptp(-Math.PI/2,-Math.PI/2,0,-Math.PI/2,Math.PI/2,0,0),ptp(-Math.PI/2,-Math.PI/4,-Math.PI/4,-Math.PI/2,Math.PI/2,0,0),ptp(-Math.PI/2,-Math.PI/2,0,-Math.PI/2,Math.PI/2,0,0)).setJointVelocityRel(0.4).setBlendingRel(0);
		while(!END){
		int answer=getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION,"What would you like me to do?", "Wave","High Five","Dance","End");
		if (answer==0){
			logger.info("Waving!");
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.4));
			robot.move(ptp(getApplicationData().getFrame("/P1")).setJointVelocityRel(0.4));
			robot.move(wave);
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.75));
		
		}else if (answer==1){
			logger.info("High Five!");
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));
			robot.move(ptp(getApplicationData().getFrame("/P5")).setJointVelocityRel(0.5));
			ThreadUtil.milliSleep(1000);
			robot.move(ptp(getApplicationData().getFrame("/P6")).setJointVelocityRel(0.65));
			robot.move(ptp(getApplicationData().getFrame("/P5")).setJointVelocityRel(0.5));
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));
		}else if (answer==2){
			logger.info("Dance!");
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));
			robot.move(dance);
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));
			
			
			
		}else if(answer==3){
			logger.info("Sayonara!");
			END=true;
		}
		}
	}
}