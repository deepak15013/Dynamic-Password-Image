/* ** Thread for Registration Activity** */

/**
 *
 */
package edu.iiitd.dynamikpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.SurfaceHolder;
import edu.iiitd.dynamikpass.model.Image;
import edu.iiitd.dynamikpass.utils.Constants;
import edu.iiitd.dynamikpass.utils.DatabaseHelper;


/**
 * @author impaler
 *
 * The Main thread which contains the game loop. The thread must have access to 
 * the surface view and holder to trigger events every game tick.
 */
public class MainThread extends Thread {

	private static final String TAG = MainThread.class.getSimpleName();

//	private static final String MyPREFERENCES = null;

	// Surface holder that can access the physical surface
	private SurfaceHolder surfaceHolder;

	//SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	// The actual view that handles inputs
	// and draws to the surface
	private RegistrationPanel gamePanel;
//	static MainThread thread;
	// Context context=MainGamePanel.getContext();

	private Context mContext;

	private static  AtomicBoolean running;

	//private Droid mdroid;
	public static void setRunning(boolean running) {
		boolean result = MainThread.running.getAndSet(running);
		Log.d(TAG, "Setting thread running value to "+ result);
	}

	public MainThread(SurfaceHolder surfaceHolder, RegistrationPanel gamePanel, Context mContext) {
		super();
		this.surfaceHolder = surfaceHolder;
		this.gamePanel = gamePanel;
		this.mContext = mContext;
		this.running = new AtomicBoolean(true);

	}

	@SuppressLint("WrongCall")
	@Override
	public void run() {
		Canvas canvas;
		Log.d(TAG, "Starting registration loop");
		while (running.get()) {
			if(!(running.get())){
				Log.d(TAG, "Exiting the thread");
				return;
			}
			canvas = null;
			// try locking the canvas for exclusive pixel editing
			// in the surface
			try {
				canvas = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					// update game state 
					// render state to the screen
					// draws the canvas on the panel
					this.gamePanel.onDraw(canvas);
				}
			}catch(NullPointerException e){
				running.getAndSet(false);
				return;
			}finally {
				// in case of an exception the surface is not left in 
				// an inconsistent state
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}	// end finally
		}
	}

	public void doSubmit() {


//		DatabaseHelper db = new DatabaseHelper(mContext);
//		db.clearTableDroid();
//		for(Image img: RegistrationPanel.imglist){
//
//
//			db.saveOrUpdateImage(img);
//			RegistrationPanel.findCell();
//		}
//		RegistrationActivity.user.setImgPassword(RegistrationPanel.findCell());
		RegistrationActivity.user.setImgPassword(RegistrationPanel.findCell());
		Intent intent = new Intent(mContext, TableLayoutExampleActivity.class);
		intent.putExtra("checkuser", RegistrationActivity.checkuser);
		intent.putExtra(Constants.USER, RegistrationActivity.user);
		Bundle bundleObject = new Bundle();
		bundleObject.putSerializable("imglist", RegistrationPanel.imglist);
		intent.putExtras(bundleObject);
		mContext.startActivity(intent);


		//}
	}

	public void doVerify() {
		System.out.println("right!!");
	}

	public void doConfirm() {
		// TODO Auto-generated method stub

	}



}

