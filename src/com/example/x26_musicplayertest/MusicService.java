package com.example.x26_musicplayertest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

public class MusicService extends Service {

	MediaPlayer player;
	private Timer timer;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = new MediaPlayer();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ֹͣ����
		player.stop();
		//�ͷ�player������Դ
		player.release();
		player = null;
		
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MusicController();
	}
	
	//�м��˶���
	//����̳�Binder���������Ϊ�м��˷���
	class MusicController extends Binder implements MusicInterface{
		//�����ⲿ�෽��
		public void play(){
			MusicService.this.play();
		}
		
		
		public void pause(){
			MusicService.this.pause();
		}

		public void continueplay() {
			MusicService.this.continueplay();
			
		}


		@Override
		public void seekTo(int progress) {
			// TODO Auto-generated method stub
			MusicService.this.seekTo(progress);
		}

	
	}

	public void play(){
		
		//����
		player.reset();
		try {
//			//����(����)������Դ
			player.setDataSource("sdcard/fk.mp3");
//			player.prepare();
//			player.start();
//			
			//��������������Դ
//			player.setDataSource("http://192.168.200.1:8080/fk.mp3");
			//�첽׼��
			player.prepareAsync();
			//�첽׼���ļ���
			player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					player.start();
					addTimer();
				}
			});
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("���ֿ�ʼ����");
	}
	
	public void pause(){
		player.pause();
	}
	
	public void continueplay(){
		player.start();
	}
	
	//ʹ���ִӸý��ȿ�ʼ����
	public void seekTo(int progress){
		player.seekTo(progress);
	}
	
	public void addTimer(){
		if(timer == null){
			//��ʱ��
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//��ȡ������ʱ��
					int duration = player.getDuration();
					//��ȡ������ǰ����
					int currentPosition = player.getCurrentPosition();
					
					Message msg = MainActivity.handler.obtainMessage();
					//�ѽ��ȷ�װ
					Bundle bundle = new Bundle();
					bundle.putInt("duration", duration);
					bundle.putInt("currentPosition", currentPosition);
					
					msg.setData(bundle);
					MainActivity.handler.sendMessage(msg);
				}
				//��ʼ��ʱ��5����󣬿�ʼִ�е�һ��run()������֮��ÿ500����ִ��һ��
			}, 5, 500);
			
		}
	}
}
