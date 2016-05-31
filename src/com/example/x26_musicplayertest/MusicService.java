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
		//停止音乐
		player.stop();
		//释放player对象资源
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
	
	//中间人对象
	//必须继承Binder对象才能作为中间人返回
	class MusicController extends Binder implements MusicInterface{
		//调用外部类方法
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
		
		//重置
		player.reset();
		try {
//			//加载(本地)音乐资源
			player.setDataSource("sdcard/fk.mp3");
//			player.prepare();
//			player.start();
//			
			//加载网络音乐资源
//			player.setDataSource("http://192.168.200.1:8080/fk.mp3");
			//异步准备
			player.prepareAsync();
			//异步准备的监听
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
		
		System.out.println("音乐开始播放");
	}
	
	public void pause(){
		player.pause();
	}
	
	public void continueplay(){
		player.start();
	}
	
	//使音乐从该进度开始播放
	public void seekTo(int progress){
		player.seekTo(progress);
	}
	
	public void addTimer(){
		if(timer == null){
			//计时器
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//获取歌曲总时长
					int duration = player.getDuration();
					//获取歌曲当前进度
					int currentPosition = player.getCurrentPosition();
					
					Message msg = MainActivity.handler.obtainMessage();
					//把进度封装
					Bundle bundle = new Bundle();
					bundle.putInt("duration", duration);
					bundle.putInt("currentPosition", currentPosition);
					
					msg.setData(bundle);
					MainActivity.handler.sendMessage(msg);
				}
				//开始计时器5毫秒后，开始执行第一次run()方法，之后每500毫秒执行一次
			}, 5, 500);
			
		}
	}
}
