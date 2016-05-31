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
			//ˢ�½�����
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
        //�������϶�����
        sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			//�϶�ֹͣ����ָ̧��
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//�����϶�λ�øı����ֽ���
				int progress = seekBar.getProgress();
				mi.seekTo(progress);
			}
			//�϶���ʼ����ָ���£�
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			//�϶��У���ָ������
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        intent = new Intent(this, MusicService.class);
        //��ϵ���
        //Ϊ�˰ѷ������ڽ��̱�Ϊ�������
        startService(intent);
        
        conn = new MusicServiceConn();
        //�󶨷���,�õ��м��˶���
        bindService(intent, conn, BIND_AUTO_CREATE);
    }
    
    //���Ӷ���
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
