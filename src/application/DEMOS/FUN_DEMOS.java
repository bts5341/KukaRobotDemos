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
public class FUN_DEMOS extends RoboticsAPIApplication {
	@Inject
	private LBR robot;
	@Inject 
	private ITaskLogger logger;

	
	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		/* Learned Positions Table
		 * Point 1 = First Wave Point
		 * Point 2 = Second Wave Point
		 * Point 3 = Unused
		 * Point 4 = Home
		 * Point 5 = First Fist Bump Point
		 * Point 6 = Second Fist Bump Point
		 */
		
		boolean END=false;//used to mark when the user wants to leave
		MotionBatch wave = new MotionBatch(ptp(getApplicationData().getFrame("/P1")),//sets up a MotionBatch for the waving demo
				ptp(getApplicationData().getFrame("/P2")),//does a point to point motion to position 2
				ptp(getApplicationData().getFrame("/P1")),//does a point to point motion to position 1
				ptp(getApplicationData().getFrame("/P2")),//does a point to point motion to position 2...
				ptp(getApplicationData().getFrame("/P1")),
				ptp(getApplicationData().getFrame("/P4"))).setBlendingRel(0.1).setJointVelocityRel(0.5);//BlendingRel and JointVelocityRel are used to control fluidness of motion and speed respectively
		MotionBatch dance = new MotionBatch(//sets up a MotionBatch for the dancing demo
				ptp(Math.PI/2,Math.PI/2,0,Math.PI/2,-Math.PI/2,0,0),//moves the corisponding joints to the given angles ex: joint 1 to pi/2, joint 2 to pi/2...
				ptp(Math.PI/2,Math.PI/4,-Math.PI/4,Math.PI/4,-Math.PI/2,0,0),
				ptp(Math.PI/2,Math.PI/2,0,Math.PI/2,-Math.PI/2,0,0),
				ptp(Math.PI/2,Math.PI/4,-Math.PI/4,Math.PI/4,-Math.PI/2,0,0),
				ptp(Math.PI/2,Math.PI/2,0,Math.PI/2,-Math.PI/2,0,0),
				ptp(-Math.PI/2,-Math.PI/2,0,-Math.PI/2,Math.PI/2,0,0),
				ptp(-Math.PI/2,-Math.PI/4,-Math.PI/4,-Math.PI/4,Math.PI/2,0,0),
				ptp(-Math.PI/2,-Math.PI/2,0,-Math.PI/2,Math.PI/2,0,0),
				ptp(-Math.PI/2,-Math.PI/4,-Math.PI/4,-Math.PI/4,Math.PI/2,0,0),
				ptp(-Math.PI/2,-Math.PI/2,0,-Math.PI/2,Math.PI/2,0,0)
				).setJointVelocityRel(0.7).setBlendingRel(0.2);
		while(!END){//while the user wants to keep going
		int answer=getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION,"What would you like me to do?", "Wave","Fist Bump","Dance","End");
		//the above line is to ask the user what demo to do. the answers do different demos as seen in the following if statement
		
		if (answer==0){
			logger.info("Waving!");//prints message to Smart Pad
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.4));//moves to point 4
			robot.move(ptp(getApplicationData().getFrame("/P1")).setJointVelocityRel(0.4));//moves to point 1
			robot.move(wave);//does wave routine
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.75));//moves to point 4
		
		}else if (answer==1){
			logger.info("Fist Bump!");//prints message to Smart Pad
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));//moves to point 4
			robot.move(ptp(getApplicationData().getFrame("/P5")).setJointVelocityRel(0.5));//moves to point 5
			ThreadUtil.milliSleep(500);//waits 1/2 second
			robot.move(ptp(getApplicationData().getFrame("/P6")).setJointVelocityRel(0.65));//moves to point 6
			robot.move(ptp(getApplicationData().getFrame("/P5")).setJointVelocityRel(0.5));//moves to point 5
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));//moves to point 4
		}else if (answer==2){
			logger.info("Dance!");//prints to smart pad
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));//moves to point 4
			robot.move(dance);//"dances"
			robot.move(ptp(getApplicationData().getFrame("/P4")).setJointVelocityRel(0.5));//moves to point 4
			
			
			
		}else if(answer==3){
			//ends
			logger.info("Sayonara!");
			END=true;
		}
		}
	}
	}
