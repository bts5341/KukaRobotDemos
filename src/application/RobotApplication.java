package application;


import javax.inject.Inject;
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
	private LBR lBR_iiwa_14_R820_1;

	@Override
	public void initialize() {
		// initialize your application here
	}

	@Override
	public void run() {
		// your application execution starts here
		MotionBatch test = new MotionBatch(ptp(getApplicationData().getFrame("/P4")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P1")),ptp(getApplicationData().getFrame("/P2")),ptp(getApplicationData().getFrame("/P4"))).setBlendingRel(0.5).setJointVelocityRel(0.1);
		for (int i=0;i<2;i++){
		int answer=getApplicationUI().displayModalDialog(ApplicationDialogType.QUESTION,"What would you like me to do?", "Wave","no");
		if (answer==0){
			logger.info("Waving!");
			lBR_iiwa_14_R820_1.move(test);
		
		}else{
			logger.info("bye!");
		}
		}
	}
}