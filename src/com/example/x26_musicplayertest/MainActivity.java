package com.example.x26_musicplayertest;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			int duration = bundle.getInt("duration");
			int currentPosition = bundle.getInt("currentPosition");
			//刷新进度条
			sb.setMax(duration);
			sb.setProgress(currentPosition);
			
		};
	};
	
	MusicInterface mi;
	private Intent intent;
	private MusicServiceConn conn;

	private static SeekBar sb;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sb = (SeekBar) findViewById(R.id.sb);
        //进度条拖动监听
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			//拖动停止（手指抬起）
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//根据拖动位置改变音乐进度
				int progress = seekBar.getProgress();
				mi.seekTo(progress);
			}
			//拖动开始（手指按下）
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			//拖动中（手指滑动）
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        intent = new Intent(this, MusicService.class);
        //混合调用
        //为了把服务所在进程变为服务进程
        startService(intent);
        
        conn = new MusicServiceConn();
        //绑定服务,拿到中间人对象
        bindService(intent, conn, BIND_AUTO_CREATE);
    }
    
    //连接对象
    class MusicServiceConn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mi = (MusicInterface) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
    	
    }

    public void play(View v){
    	mi.play();
    }
    
    public void continueplay(View v){
    	mi.continueplay();
    }
    
    public void pause(View v){
    	mi.pause();
    }
    
    public void stop(View v){
    	unbindService(conn);
    	stopService(intent);
    	finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
