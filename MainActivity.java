package com.example.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.QuickContactBadge;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 스테이지 구별용 변수이다.
    /////////////////////////////////////////////

    public enum stage {
        MainMenu(1),
        Tutorial(2),
        Story(3),
        game_1(4);

        private final int value;
        stage(int a){
            this.value = a;
        }
        int getValue(){
            return value;
        }
    }

    // 현재 스테이지와 기본값
    public int nowStage = stage.MainMenu.getValue();

    /////////////////////////////////////////////
    // 스테이지 구별용 변수이다.


    // 메인메뉴 버튼 삭제함수이다.
    /////////////////////////////////////////////

    void removeMainMenuBtn(){
        ImageView storyBtn = (ImageView) findViewById(R.id.storyButton);
        ImageView startBtn = (ImageView) findViewById(R.id.startButton);
        ImageView tutorialBtn = (ImageView) findViewById(R.id.tutorialButton);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.remove_mainmenu_btn);
        startBtn.startAnimation(animation);
        storyBtn.startAnimation(animation);
        tutorialBtn.startAnimation(animation);

        storyBtn.setVisibility(View.GONE);
        startBtn.setVisibility(View.GONE);
        tutorialBtn.setVisibility(View.GONE);
    }

    /////////////////////////////////////////////
    // 메인메뉴 버튼 삭제함수이다.

    // 메인메뉴 버튼 불러오기함수이다.
    /////////////////////////////////////////////

    void reLoadMainMenuBtn(){
        ImageView storyBtn = (ImageView) findViewById(R.id.storyButton);
        ImageView startBtn = (ImageView) findViewById(R.id.startButton);
        ImageView tutorialBtn = (ImageView) findViewById(R.id.tutorialButton);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.place_mainmenu_btn);
        startBtn.startAnimation(animation);
        storyBtn.startAnimation(animation);
        tutorialBtn.startAnimation(animation);

        storyBtn.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.VISIBLE);
        tutorialBtn.setVisibility(View.VISIBLE);
    }

    /////////////////////////////////////////////
    // 메인메뉴 버튼 불러오기함수이다.

    private int storyIndex = 0;
    public void setStoryIndex(int set){ storyIndex = set; }
    public int getStoryIndex() { return storyIndex; }

    private int storyText = 0;
    public void setStoryText(int set){ storyText = set; }
    public int getStoryText() { return storyText; }

    private boolean gameStoryThreadShow = false;
    public void setGameStoryThreadShow(boolean set){ gameStoryThreadShow = set; }
    public boolean getGameStoryThreadShow() { return gameStoryThreadShow; }

    private boolean gameTutorialThreadShow = false;
    public void setGameTutorialThreadShow(boolean set){ gameTutorialThreadShow = set; }
    public boolean getGameTutorialThreadShow() { return gameTutorialThreadShow; }

    private boolean gamePlayThreadShow = false;
    public void setGamePlayThreadShow(boolean set){ gamePlayThreadShow = set; }
    public boolean getGamePlayThreadShow() { return gamePlayThreadShow; }

    private int L_R_Click = 0;
    public void setL_R_Click(int set){ L_R_Click = set; }
    public int getL_R_Click() { return L_R_Click; }

    Display dpDisplay;
    DisplayMetrics metrics;

    public float pxToDp_w;
    public float pxToDp_h;

    public String[] gameStory = {
            "",
            "test1",
            "test2",
            "test3"
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dpDisplay = getWindowManager().getDefaultDisplay();
        metrics = new DisplayMetrics();
        dpDisplay.getMetrics(metrics);
        pxToDp_w = metrics.xdpi/160;
        pxToDp_h = metrics.ydpi/160;

        Handler gameHandler = new Handler();
        ImageView playerIMG = findViewById(R.id.tutorialPlayerIMG_t);
        ImageView nomalAtkView = findViewById(R.id.tutorialNomal_ATK_IMG_t);
        Player gamePlayer = new Player(playerIMG, MainActivity.super.getApplicationContext(), gameHandler, pxToDp_w, pxToDp_h, nomalAtkView);

        // 메인 메뉴의 배경이다.
        /////////////////////////////////////////////

        // 실제 화면크기 구하기
        Display display = getWindowManager().getDefaultDisplay();
        Point scale = new Point();
        display.getRealSize(scale);

        // 화면크기를 토대로 캔버스뷰 생성 (배경용)
        Bitmap backgroundBitmap = Bitmap.createBitmap(scale.x, scale.y, Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(backgroundBitmap);

        // 캔버스뷰 연결
        ImageView background = (ImageView) findViewById(R.id.mainMenuView);
        background.setImageBitmap(backgroundBitmap);

        // 초록색 붓 준비
        Paint paint = new Paint();
        paint.setColor(Color.rgb(50, 160, 85));
        paint.setStrokeWidth(10f);

        // 배경 그리기
        for(int a = 50 ; a < scale.x ; a += 130){
            backgroundCanvas.drawLine(a, 0, a, scale.y, paint);
        }
        for(int b = 50 ; b < scale.x ; b += 100){
            backgroundCanvas.drawLine(0, b, scale.x, b, paint);
        }

        paint.setTextSize(400f);
        paint.setTypeface(Typeface.createFromAsset(getAssets(), "font/ramche.ttf"));
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(30f);
        paint.setStyle(Paint.Style.STROKE);
        backgroundCanvas.drawText("android!", scale.x/100f, (scale.y/7f)*5f, paint);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        backgroundCanvas.drawText("android!", scale.x/100f, (scale.y/7f)*5f, paint);

        /////////////////////////////////////////////
        // 메인 메뉴의 배경이다.


        // 메인 메뉴의 3가지 스레드이다.
        /////////////////////////////////////////////


        @SuppressLint("ClickableViewAccessibility")
        ImageView leftBtn_t = findViewById(R.id.moveControlLeft_t);
        ImageView rightBtn_t = findViewById(R.id.moveControlRight_t);
        ImageView jumpBtn_t = findViewById(R.id.moveControlJump_t);
        ImageView nomalAttackBtn_t = findViewById(R.id.attackControlNomal_t);

        leftBtn_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.Tutorial.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        gamePlayer.filp = true;
                        leftBtn_t.setImageResource(R.drawable.move_control_left_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.run.getValue());
                        }
                        if(getL_R_Click() < 2){
                            setL_R_Click(getL_R_Click()+1);
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        leftBtn_t.setImageResource(R.drawable.move_control_left);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.idle.getValue());
                        }
                        if(getL_R_Click()>0){
                            setL_R_Click(getL_R_Click() - 1);
                        }
                    }
                }
                return true;
            }
        });
        rightBtn_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.Tutorial.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        gamePlayer.filp = false;
                        rightBtn_t.setImageResource(R.drawable.move_control_right_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.run.getValue());
                        }
                        if(getL_R_Click() < 2){
                            setL_R_Click(getL_R_Click()+1);
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        rightBtn_t.setImageResource(R.drawable.move_control_right);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.idle.getValue());
                        }
                        if (getL_R_Click() > 0){
                            setL_R_Click(getL_R_Click() - 1);
                        }
                    }
                }
                return true;
            }
        });
        jumpBtn_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.Tutorial.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        jumpBtn_t.setImageResource(R.drawable.move_control_jump_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue() && !gamePlayer.onSky){
                            gamePlayer.nowJumpIndex = gamePlayer.jumpPower;
                            gamePlayer.settingMotion(Player.motion.jump.getValue());
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        jumpBtn_t.setImageResource(R.drawable.move_control_jump);

                        if(!gamePlayer.onSky){
                            if(getL_R_Click() > 0){
                                gamePlayer.settingMotion(Player.motion.run.getValue());
                            }else{
                                gamePlayer.settingMotion(Player.motion.idle.getValue());
                            }
                        }
                    }
                }
                return true;
            }
        });
        nomalAttackBtn_t.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.Tutorial.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        nomalAttackBtn_t.setImageResource(R.drawable.attack_pressed);
                        if(!gamePlayer.onSky && !gamePlayer.activeNomalAttack){
                            gamePlayer.activeNomalAttack = true;
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        nomalAttackBtn_t.setImageResource(R.drawable.attack);
                    }
                }
                return true;
            }
        });

        ImageView leftBtn_g = findViewById(R.id.moveControlLeft_g);
        ImageView rightBtn_g = findViewById(R.id.moveControlRight_g);
        ImageView jumpBtn_g = findViewById(R.id.moveControlJump_g);
        ImageView nomalAttackBtn_g = findViewById(R.id.attackControlNomal_g);

        leftBtn_g.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.game_1.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        gamePlayer.filp = true;
                        leftBtn_g.setImageResource(R.drawable.move_control_left_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.run.getValue());
                        }
                        if(getL_R_Click() < 2){
                            setL_R_Click(getL_R_Click()+1);
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        leftBtn_g.setImageResource(R.drawable.move_control_left);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.idle.getValue());
                        }
                        if(getL_R_Click()>0){
                            setL_R_Click(getL_R_Click() - 1);
                        }
                    }
                }
                return true;
            }
        });
        rightBtn_g.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.game_1.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        gamePlayer.filp = false;
                        rightBtn_g.setImageResource(R.drawable.move_control_right_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.run.getValue());
                        }
                        if(getL_R_Click() < 2){
                            setL_R_Click(getL_R_Click()+1);
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        rightBtn_g.setImageResource(R.drawable.move_control_right);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                            gamePlayer.settingMotion(Player.motion.idle.getValue());
                        }
                        if (getL_R_Click() > 0){
                            setL_R_Click(getL_R_Click() - 1);
                        }
                    }
                }
                return true;
            }
        });
        jumpBtn_g.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.game_1.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        jumpBtn_g.setImageResource(R.drawable.move_control_jump_pressed);
                        if(gamePlayer.nowMotion != Player.motion.jump.getValue() && !gamePlayer.onSky){
                            gamePlayer.nowJumpIndex = gamePlayer.jumpPower;
                            gamePlayer.settingMotion(Player.motion.jump.getValue());
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        jumpBtn_g.setImageResource(R.drawable.move_control_jump);

                        if(!gamePlayer.onSky){
                            if(getL_R_Click() > 0){
                                gamePlayer.settingMotion(Player.motion.run.getValue());
                            }else{
                                gamePlayer.settingMotion(Player.motion.idle.getValue());
                            }
                        }
                    }
                }
                return true;
            }
        });
        nomalAttackBtn_g.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(nowStage == stage.game_1.getValue()){
                    int act = motionEvent.getAction();
                    if(act == MotionEvent.ACTION_DOWN){
                        nomalAttackBtn_g.setImageResource(R.drawable.attack_pressed);
                        if(!gamePlayer.onSky && !gamePlayer.activeNomalAttack){
                            gamePlayer.activeNomalAttack = true;
                        }
                    }
                    if(act == MotionEvent.ACTION_UP){
                        nomalAttackBtn_g.setImageResource(R.drawable.attack);
                    }
                }
                return true;
            }
        });

        Thread gameStoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(gameStoryThreadShow){
                        gameHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                ImageView storyImageView = (ImageView) findViewById(R.id.storyImgView);
                                Bitmap storyBitmap = Bitmap.createBitmap(scale.x, scale.y, Bitmap.Config.ARGB_8888);
                                Canvas storyCanvas = new Canvas(storyBitmap);
                                storyImageView.setVisibility(View.VISIBLE);
                                storyImageView.setImageBitmap(storyBitmap);

                                Paint storyPaint = new Paint();
                                storyPaint.setColor(Color.rgb(0, 255, 130));
                                storyPaint.setStrokeWidth(10.0f);

                                storyCanvas.drawLine(scale.x-300.0f, 150.0f, scale.x-150.0f, 150.0f, storyPaint);
                                storyCanvas.drawLine(scale.x-300.0f, 150.0f, scale.x-300.0f, scale.y-450.0f, storyPaint);
                                storyCanvas.drawLine(scale.x-150.0f, 150.0f, scale.x-300.0f, scale.y-450.0f, storyPaint);

                                storyCanvas.drawLine(scale.x-300.0f, scale.y-200.0f, scale.x-150.0f, 400.0f, storyPaint);
                                storyCanvas.drawLine(scale.x-300.0f, scale.y-200.0f, scale.x-150.0f, scale.y-200.0f, storyPaint);
                                storyCanvas.drawLine(scale.x-150.0f, 400.0f, scale.x-150.0f, scale.y-200.0f, storyPaint);

                                storyCanvas.drawLine(200.0f, scale.y-400.0f, scale.x-400.0f, scale.y-400.0f, storyPaint);
                                storyCanvas.drawLine(200.0f, scale.y-400.0f, 200.0f, scale.y-150.0f, storyPaint);
                                storyCanvas.drawLine(200.0f, scale.y-150.0f, scale.x-400.0f, scale.y-150.0f, storyPaint);
                                storyCanvas.drawLine(scale.x-400.0f, scale.y-150.0f, scale.x-400.0f, scale.y-400.0f, storyPaint);

                                storyCanvas.drawLine(scale.x-300.0f, scale.y-150.0f, scale.x-150.0f, scale.y-150.0f, storyPaint);

                            }
                        });
                        try {
                            while (gameStoryThreadShow){
                                Thread.sleep(100);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Thread gamePlayThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    if (gamePlayThreadShow) {
                        if(nowStage == stage.game_1.getValue()){
                            int mapGravity = 5;
                            HitBox[] flatformHitBoxs = new HitBox[7];
                            Point[] flatformPoints = new Point[7];
                            ImageView[] flatformIMGs = new ImageView[7];
                            FrameLayout.LayoutParams[] flatformsParams = new FrameLayout.LayoutParams[7];

                            flatformPoints[0] = new Point(50, 700);
                            flatformHitBoxs[0] = new HitBox(flatformPoints[0].x, flatformPoints[0].y, 700, 100, pxToDp_w, pxToDp_h);
                            flatformsParams[0] = new FrameLayout.LayoutParams((int)(700*pxToDp_w), (int)(100*pxToDp_h));

                            flatformPoints[1] = new Point(800, 600);
                            flatformHitBoxs[1] = new HitBox(flatformPoints[1].x, flatformPoints[1].y, 500, 50, pxToDp_w, pxToDp_h);
                            flatformsParams[1] = new FrameLayout.LayoutParams((int)(500*pxToDp_w), (int)(50*pxToDp_h));

                            flatformPoints[2] = new Point(1400, 700);
                            flatformHitBoxs[2] = new HitBox(flatformPoints[2].x, flatformPoints[2].y, 600, 70,pxToDp_w,pxToDp_h);
                            flatformsParams[2] = new FrameLayout.LayoutParams((int)(600*pxToDp_w),(int)(70*pxToDp_h));

                            flatformPoints[3] = new Point(2200, 800);
                            flatformHitBoxs[3] = new HitBox(flatformPoints[3].x, flatformPoints[3].y, 600, 60,pxToDp_w,pxToDp_h);
                            flatformsParams[3] = new FrameLayout.LayoutParams((int)(600*pxToDp_w),(int)(60*pxToDp_h));

                            flatformPoints[4] = new Point(2900, 100);
                            flatformHitBoxs[4] = new HitBox(flatformPoints[4].x, flatformPoints[4].y, 500, 1000,pxToDp_w,pxToDp_h);
                            flatformsParams[4] = new FrameLayout.LayoutParams((int)(500*pxToDp_w),(int)(1000*pxToDp_h));

                            flatformPoints[5] = new Point(2500, 300);
                            flatformHitBoxs[5] = new HitBox(flatformPoints[5].x, flatformPoints[5].y, 300, 100,pxToDp_w,pxToDp_h);
                            flatformsParams[5] = new FrameLayout.LayoutParams((int)(300*pxToDp_w),(int)(100*pxToDp_h));

                            flatformPoints[6] = new Point(3500, 500);
                            flatformHitBoxs[6] = new HitBox(flatformPoints[6].x, flatformPoints[6].y, 200, 100,pxToDp_w,pxToDp_h);
                            flatformsParams[6] = new FrameLayout.LayoutParams((int)(200*pxToDp_w),(int)(100*pxToDp_h));


                            HitBox[] bpple_1_hitbox = new HitBox[1];
                            Point[] bpple_1_point = new Point[1];
                            ImageView[] bpple_1_imgs = new ImageView[1];
                            FrameLayout.LayoutParams[] bpple_1_params = new FrameLayout.LayoutParams[1];
                            int[] bpple_1_onFlatform = new int[1];
                            int[] bpple_1_hp = new int[1];
                            int[] bpple_1_atkcool = new int[1];
                            Point[] bpple_1_randomPoint = new Point[1];

                            bpple_1_onFlatform[0] = 3;
                            bpple_1_point[0] = new Point(2500,700);
                            bpple_1_hitbox[0] = new HitBox(bpple_1_point[0].x, bpple_1_point[0].y, 70, 70, pxToDp_w, pxToDp_h);
                            bpple_1_params[0] = new FrameLayout.LayoutParams((int)(70*pxToDp_w), (int)(70*pxToDp_h));
                            bpple_1_hp[0] = 300;
                            bpple_1_randomPoint[0] = new Point(2500, 700);
                            bpple_1_atkcool[0] = 30;


                            for(int a = 0 ; a<flatformIMGs.length ; a++){
                                int b = a;
                                FrameLayout layout = findViewById(R.id.playLayout);
                                gameHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        flatformsParams[b].setMargins((int)(flatformPoints[b].x * pxToDp_w), (int)(flatformPoints[b].y * pxToDp_h), 0, 0);
                                        flatformIMGs[b] = new ImageView(getApplicationContext());
                                        flatformIMGs[b].setScaleType(ImageView.ScaleType.FIT_XY);
                                        flatformIMGs[b].setImageResource(R.drawable.flatform);
                                        flatformIMGs[b].setLayoutParams(flatformsParams[b]);
                                        layout.addView(flatformIMGs[b], 1);
                                    }
                                });
                            }
                            for(int a = 0 ; a<bpple_1_imgs.length ; a++){
                                int b = a;
                                FrameLayout layout = findViewById(R.id.playLayout);
                                gameHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        bpple_1_params[b].setMargins((int)(bpple_1_point[b].x * pxToDp_w), (int)(bpple_1_point[b].y * pxToDp_h), 0, 0);
                                        bpple_1_imgs[b] = new ImageView(getApplicationContext());
                                        bpple_1_imgs[b].setScaleType(ImageView.ScaleType.FIT_XY);
                                        bpple_1_imgs[b].setImageResource(R.drawable.bpple);
                                        bpple_1_imgs[b].setLayoutParams(bpple_1_params[b]);
                                        layout.addView(bpple_1_imgs[b], flatformHitBoxs.length);
                                    }
                                });
                            }

                            try {
                                while (gamePlayThreadShow) {
                                    if(gamePlayer.nowMotion != Player.motion.jump.getValue()){
                                        boolean drop = true;
                                        for (HitBox flatformHitBox : flatformHitBoxs) {
                                            if (flatformHitBox.IsHit(gamePlayer.playerHitbox)) {
                                                drop = false;
                                                break;
                                            }
                                        }
                                        if (drop) {
                                            gamePlayer.settingMotion(Player.motion.jump.getValue());
                                        }
                                    }

                                    if(gamePlayer.nowMotion == Player.motion.jump.getValue()){
                                        gamePlayer.onSky = true;
                                        if(gamePlayer.nowJumpIndex == gamePlayer.jumpPower){
                                            boolean hit = true;
                                            while (hit) {
                                                gamePlayer.Y +=1;
                                                for(int a = 0 ; a< flatformHitBoxs.length ; a++){
                                                    flatformHitBoxs[a].reLocate(flatformPoints[a].x + gamePlayer.X, flatformPoints[a].y + gamePlayer.Y);
                                                }
                                                int z = 0;
                                                for (HitBox flatformHitBox : flatformHitBoxs) {
                                                    if (!flatformHitBox.IsHit(gamePlayer.playerHitbox)) {
                                                        z++;
                                                    }
                                                }
                                                if (z >= flatformHitBoxs.length) {
                                                    hit = false;
                                                }
                                            }
                                        }
                                        int jumpMoveLoop = gamePlayer.nowJumpIndex<0?gamePlayer.nowJumpIndex*-1:gamePlayer.nowJumpIndex;
                                        while (jumpMoveLoop > 0){
                                            if(gamePlayer.nowJumpIndex<0){
                                                gamePlayer.Y -= 1;
                                            }else{
                                                gamePlayer.Y += 1;
                                            }
                                            for(int a = 0 ; a< flatformHitBoxs.length ; a++){
                                                flatformHitBoxs[a].reLocate(flatformPoints[a].x + gamePlayer.X, flatformPoints[a].y + gamePlayer.Y);
                                            }
                                            jumpMoveLoop -= 1;
                                            boolean breaking = false;
                                            for (HitBox flatformHitBox : flatformHitBoxs) {
                                                if (flatformHitBox.IsHit(gamePlayer.playerHitbox)) {
                                                    gamePlayer.onSky = false;
                                                    if(L_R_Click > 0){
                                                        gamePlayer.settingMotion(Player.motion.run.getValue());
                                                    }else{
                                                        gamePlayer.settingMotion(Player.motion.idle.getValue());
                                                    }
                                                    gamePlayer.nowJumpIndex = 0;
                                                    breaking = true;
                                                    break;
                                                }
                                            }
                                            if (breaking) {
                                                break;
                                            }
                                        }
                                        gamePlayer.nowJumpIndex -= mapGravity;
                                    } else {
                                        for(HitBox hitbox:flatformHitBoxs){
                                            if(hitbox.IsHit(gamePlayer.playerHitbox)){
                                                gamePlayer.onSky = false;
                                                if(L_R_Click > 0){
                                                    gamePlayer.settingMotion(Player.motion.run.getValue());
                                                }else{
                                                    gamePlayer.settingMotion(Player.motion.idle.getValue());
                                                }
                                                gamePlayer.nowJumpIndex = 0;
                                                break;
                                            }
                                        }
                                    }

                                    if(getL_R_Click() > 0){
                                        if(gamePlayer.filp){
                                            gamePlayer.X += gamePlayer.speed;
                                        }else{
                                            gamePlayer.X -= gamePlayer.speed;
                                        }
                                    }

                                    for(int a = 0 ; a<flatformHitBoxs.length ; a++){
                                        int b = a;
                                        gameHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                flatformsParams[b].setMargins((int)((flatformPoints[b].x + gamePlayer.X)*pxToDp_w),(int)((flatformPoints[b].y + gamePlayer.Y)*pxToDp_h) , 0, 0);
                                                flatformIMGs[b].setLayoutParams(flatformsParams[b]);
                                                flatformHitBoxs[b].reLocate(flatformPoints[b].x + gamePlayer.X, flatformPoints[b].y + gamePlayer.Y);
                                            }
                                        });
                                    }
                                    gameHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            gamePlayer.settingImg();
                                        }
                                    });

//                                    for(int a = 0; a<bpple_1_hitbox.length ; a++){
//                                        if(gamePlayer.nomalAttackHitbox.IsHit(bpple_1_hitbox[a])){
//                                            bpple_1_hp[a] -= gamePlayer.nomalAttackDamage;
//                                        }
//                                        if(bpple_1_hp[a] > 0){
//                                            if(bpple_1_hitbox[a].point1.x-200<gamePlayer.playerHitbox.point1.x && bpple_1_hitbox[a].point2.x > bpple_1_hitbox[a].point2.x+200){
//                                                if (bpple_1_hitbox[a].IsHit(gamePlayer.playerHitbox)){
//                                                    if(bpple_1_atkcool[a] == 0){
//                                                        bpple_1_atkcool[a] = 30;
//                                                        gamePlayer.HP -= 10;
//                                                    }else{
//                                                        bpple_1_atkcool[a]--;
//                                                    }
//                                                }else{
//                                                    if(bpple_1_params[a].leftMargin < (int)(250*pxToDp_w)){
//                                                        bpple_1_imgs[a].setScaleX(-1.0f);
//                                                        for(int b = 0;b<10;b++){
//                                                            bpple_1_point[a].x += 1;
//                                                            bpple_1_params[a].setMargins(bpple_1_point[a].x, bpple_1_params[a].topMargin, 0, 0);
//                                                            bpple_1_hitbox[a].reLocate(bpple_1_point[a].x, bpple_1_point[a].y);
//                                                            if(bpple_1_hitbox[a].IsHit(gamePlayer.playerHitbox)){
//                                                                break;
//                                                            }
//                                                        }
//                                                    }else{
//                                                        bpple_1_imgs[a].setScaleX(1.0f);
//                                                        for(int b = 0;b<10;b++){
//                                                            bpple_1_point[a].x -= 1;
//                                                            bpple_1_params[a].setMargins(bpple_1_point[a].x, bpple_1_params[a].topMargin, 0, 0);
//                                                            bpple_1_hitbox[a].reLocate(bpple_1_point[a].x, bpple_1_point[a].y);
//                                                            if(bpple_1_hitbox[a].IsHit(gamePlayer.playerHitbox)){
//                                                                break;
//                                                            }
//                                                        }
//                                                    }
//                                                }
//                                            }else{
//                                                if(bpple_1_randomPoint[a].x == bpple_1_point[a].x){
//                                                    bpple_1_randomPoint[a].x = (int)(Math.random()*(bpple_1_hitbox[0].point2.x/pxToDp_w));
//                                                }
//                                                if(bpple_1_point[a].x < bpple_1_randomPoint[a].x){
//                                                    bpple_1_imgs[a].setScaleX(-1.0f);
//                                                    for(int b = 0;b<5;b++){
//                                                        bpple_1_point[a].x += 1;
//                                                        bpple_1_params[a].setMargins(bpple_1_point[a].x, bpple_1_params[a].topMargin, 0, 0);
//                                                    }
//                                                }else{
//                                                    bpple_1_imgs[a].setScaleX(1.0f);
//                                                    for(int b = 0;b<5;b++){
//                                                        bpple_1_point[a].x -= 1;
//                                                        bpple_1_params[a].setMargins(bpple_1_point[a].x, bpple_1_params[a].topMargin, 0, 0);
//                                                    }
//                                                }
//                                            }
//                                        }else{
//                                            bpple_1_imgs[a].setVisibility(View.GONE);
//                                        }
//                                    }
//                                    for(int a = 0 ; a<bpple_1_imgs.length ; a++){
//                                        int b = a;
//                                        gameHandler.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                bpple_1_params[b].setMargins((int)((bpple_1_point[b].x + gamePlayer.X)*pxToDp_w),(int)((bpple_1_point[b].y + gamePlayer.Y)*pxToDp_h) , 0, 0);
//                                                bpple_1_imgs[b].setLayoutParams(bpple_1_params[b]);
//                                                bpple_1_hitbox[b].reLocate(bpple_1_point[b].x + gamePlayer.X, bpple_1_point[b].y + gamePlayer.Y);
//                                            }
//                                        });
//                                    }
                                    Thread.sleep(100);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            FrameLayout layout = findViewById(R.id.playLayout);
                            for(int a = 0 ; a<flatformPoints.length ; a++){
                                int b = a;
                                gameHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layout.removeView(flatformIMGs[b]);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        Thread tutorialThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(gameTutorialThreadShow){
                        int mapGravity = 5;

                        int[] sandbag = new int[7];
                        int sandbagIndex = 0;

                        for (int a = 0 ; a<4 ; a++){
                            String name = "@drawable/sandbag_"+a;
                            sandbag[a] = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
                        }
                        for (int a = 3 ; a>0; a--){
                            String name = "@drawable/sandbag_"+a;
                            sandbag[7-a] = getApplicationContext().getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
                        }
                        Point sandbagPoint = new Point(700, scale.y/2-90);
                        HitBox sandbagHitBox = new HitBox(sandbagPoint.x, sandbagPoint.y, 70, 140, pxToDp_w, pxToDp_h);

                        ImageView sandbagIMG = new ImageView(MainActivity.super.getApplicationContext());
                        FrameLayout.LayoutParams sandbagParams = new FrameLayout.LayoutParams((int)(70*pxToDp_w), (int)(140*pxToDp_h));
                        sandbagParams.setMargins((int)(sandbagPoint.x * pxToDp_w), (int)(sandbagPoint.y * pxToDp_h), 0, 0);

                        Point flatformPoint = new Point(50, scale.y/2+50);
                        HitBox flatformHitBox = new HitBox(flatformPoint.x, flatformPoint.y, 1000, 100, pxToDp_w, pxToDp_h);

                        ImageView flatformIMG = new ImageView(MainActivity.super.getApplicationContext());
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)(1000*pxToDp_w), (int)(100*pxToDp_h));
                        params.setMargins((int)(flatformPoint.x*pxToDp_w), (int)(flatformPoint.y*pxToDp_h), 0,0 );
                        gameHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                flatformIMG.setLayoutParams(params);
                                flatformIMG.setImageResource(R.drawable.flatform);
                                flatformIMG.setScaleType(ImageView.ScaleType.FIT_XY);

                                sandbagIMG.setLayoutParams(sandbagParams);
                                sandbagIMG.setImageResource(R.drawable.sandbag_0);
                                sandbagIMG.setScaleType(ImageView.ScaleType.FIT_XY);

                                FrameLayout tutorialLayout = findViewById(R.id.tutorialLayout);
                                tutorialLayout.addView(flatformIMG,1);
                                tutorialLayout.addView(sandbagIMG, 2);
                            }
                        });

                        try {
                            while (gameTutorialThreadShow) {
                                if(!gamePlayer.playerHitbox.IsHit(flatformHitBox) && gamePlayer.nowMotion != Player.motion.jump.getValue()){
                                    gamePlayer.settingMotion(Player.motion.jump.getValue());
                                }

                                if(sandbagParams.leftMargin < (int)(250*pxToDp_w)){
                                    sandbagIMG.setScaleX(-1.0f);
                                }else{
                                    sandbagIMG.setScaleX(1.0f);
                                }

                                if(sandbagHitBox.IsHit(gamePlayer.nomalAttackHitbox)||sandbagIndex != 0){
                                    if(sandbagIndex<sandbag.length){
                                        sandbagIMG.setImageResource(sandbag[sandbagIndex]);
                                        sandbagIndex+=1;
                                    }else{
                                        sandbagIMG.setImageResource(sandbag[0]);
                                        sandbagIndex = 0;
                                    }
                                }

                                if(gamePlayer.nowMotion == Player.motion.jump.getValue()){
                                    gamePlayer.onSky = true;
                                    if(gamePlayer.nowJumpIndex == gamePlayer.jumpPower){
                                        while (gamePlayer.playerHitbox.IsHit(flatformHitBox)) {
                                            gamePlayer.Y +=1;
                                            flatformHitBox.reLocate(flatformPoint.x + gamePlayer.X, flatformPoint.y + gamePlayer.Y);
                                        }
                                    }
                                    int jumpMoveLoop = gamePlayer.nowJumpIndex<0?gamePlayer.nowJumpIndex*-1:gamePlayer.nowJumpIndex;
                                    while (jumpMoveLoop > 0){
                                        if(gamePlayer.nowJumpIndex<0){
                                            gamePlayer.Y -= 1;
                                        }else{
                                            gamePlayer.Y += 1;
                                        }
                                        flatformHitBox.reLocate(flatformPoint.x + gamePlayer.X, flatformPoint.y + gamePlayer.Y);
                                        jumpMoveLoop -= 1;
                                        if(gamePlayer.playerHitbox.IsHit((flatformHitBox))){
                                            gamePlayer.onSky = false;
                                            if(L_R_Click > 0){
                                                gamePlayer.settingMotion(Player.motion.run.getValue());
                                            }else{
                                                gamePlayer.settingMotion(Player.motion.idle.getValue());
                                            }
                                            gamePlayer.nowJumpIndex = 0;
                                            break;
                                        }
                                    }
                                    gamePlayer.nowJumpIndex -= mapGravity;
                                } else if (gamePlayer.playerHitbox.IsHit(flatformHitBox)) {
                                    gamePlayer.onSky = false;
                                    if(L_R_Click > 0){
                                        gamePlayer.settingMotion(Player.motion.run.getValue());
                                    }else{
                                        gamePlayer.settingMotion(Player.motion.idle.getValue());
                                    }
                                    gamePlayer.nowJumpIndex = 0;
                                }

                                if(getL_R_Click() > 0){
                                    if(gamePlayer.filp){
                                        gamePlayer.X += gamePlayer.speed;
                                    }else{
                                        gamePlayer.X -= gamePlayer.speed;
                                    }
                                }

                                gameHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        flatformHitBox.reLocate(flatformPoint.x + gamePlayer.X, flatformPoint.y + gamePlayer.Y);
                                        params.setMargins((int)((flatformPoint.x + gamePlayer.X)*pxToDp_w), (int)((flatformPoint.y + gamePlayer.Y)*pxToDp_h), 0,0);
                                        flatformIMG.setLayoutParams(params);

                                        sandbagHitBox.reLocate(sandbagPoint.x + gamePlayer.X, sandbagPoint.y + gamePlayer.Y);
                                        sandbagParams.setMargins((int)((sandbagPoint.x + gamePlayer.X)*pxToDp_w), (int)((sandbagPoint.y + gamePlayer.Y)*pxToDp_h), 0, 0);
                                        sandbagIMG.setLayoutParams(sandbagParams);

                                        gamePlayer.settingImg();
                                    }
                                });

                                Thread.sleep(100);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        gameHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                flatformIMG.setLayoutParams(params);
                                flatformIMG.setImageResource(R.drawable.flatform);
                                flatformIMG.setScaleType(ImageView.ScaleType.FIT_XY);

                                FrameLayout tutorialLayout = findViewById(R.id.tutorialLayout);
                                tutorialLayout.removeView(flatformIMG);
                                tutorialLayout.removeView(sandbagIMG);
                            }
                        });
                    }
                }
            }
        });

        gameStoryThread.start();
        gamePlayThread.start();
        tutorialThread.start();

        /////////////////////////////////////////////
        // 메인 메뉴의 3가지 스레드이다.


        // 메인 메뉴의 버튼들(선택지)이다.
        /////////////////////////////////////////////

        ImageView storyBtn = (ImageView) findViewById(R.id.storyButton);
        ImageView startBtn = (ImageView) findViewById(R.id.startButton);
        ImageView tutorialBtn = (ImageView) findViewById(R.id.tutorialButton);
        Animation alpha_0_to_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_zero_to_one);

        ImageView toHomeBtn_s = (ImageView) findViewById(R.id.toHomeButton_s);
        toHomeBtn_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(nowStage == stage.Story.getValue()){
                        FrameLayout storyLayout = (FrameLayout) findViewById(R.id.storyLayout);
                        Animation letsGone = AnimationUtils.loadAnimation(getApplication(), R.anim.alpha_one_to_zero);
                        storyLayout.startAnimation(letsGone);
                        nowStage = stage.MainMenu.getValue();
                        reLoadMainMenuBtn();
                        storyLayout.setVisibility(View.GONE);
                        toHomeBtn_s.setVisibility(View.GONE);
                        setGameStoryThreadShow(false);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        ImageView toHomeBtn_t = (ImageView) findViewById(R.id.toHomeButton_t);
        toHomeBtn_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(nowStage == stage.Tutorial.getValue()){
                        FrameLayout tutorialLayout = (FrameLayout) findViewById(R.id.tutorialLayout);
                        Animation letsGone = AnimationUtils.loadAnimation(getApplication(), R.anim.alpha_one_to_zero);
                        tutorialLayout.startAnimation(letsGone);
                        nowStage = stage.MainMenu.getValue();
                        reLoadMainMenuBtn();
                        tutorialLayout.setVisibility(View.GONE);
                        toHomeBtn_t.setVisibility(View.GONE);
                        setGameTutorialThreadShow(false);
                        leftBtn_t.setVisibility(View.GONE);
                        rightBtn_t.setVisibility(View.GONE);
                        jumpBtn_t.setVisibility(View.GONE);
                        nomalAttackBtn_t.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        storyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowStage == stage.MainMenu.getValue()){
                    try {
                        removeMainMenuBtn();
                        FrameLayout storyLayout = (FrameLayout) findViewById(R.id.storyLayout);
                        storyLayout.setVisibility(View.VISIBLE);
                        storyLayout.startAnimation(alpha_0_to_1);
                        nowStage = stage.Story.getValue();
                        toHomeBtn_s.setVisibility(View.VISIBLE);
                        setGameStoryThreadShow(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nowStage == stage.MainMenu.getValue()){
                    ImageView playerimg = findViewById(R.id.tutorialPlayerIMG_g);
                    ImageView nomalatkimg = findViewById(R.id.tutorialNomal_ATK_IMG_g);
                    playerimg.setVisibility(View.VISIBLE);
                    nomalatkimg.setVisibility(View.VISIBLE);
                    leftBtn_g.setVisibility(View.VISIBLE);
                    rightBtn_g.setVisibility(View.VISIBLE);
                    jumpBtn_g.setVisibility(View.VISIBLE);
                    nomalAttackBtn_g.setVisibility(View.VISIBLE);
                    removeMainMenuBtn();
                    FrameLayout gameLayout = (FrameLayout) findViewById(R.id.playLayout);
                    gameLayout.setVisibility(View.VISIBLE);
                    gameLayout.startAnimation(alpha_0_to_1);
                    nowStage = stage.game_1.getValue();
                    gamePlayer.resetPlayer(playerimg, nomalatkimg);
                    setGamePlayThreadShow(true);
                }
            }
        });

        tutorialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowStage == stage.MainMenu.getValue()){
                    try {
                        ImageView playerimg = findViewById(R.id.tutorialPlayerIMG_t);
                        ImageView nomalatkimg = findViewById(R.id.tutorialNomal_ATK_IMG_t);
                        playerimg.setVisibility(View.VISIBLE);
                        nomalatkimg.setVisibility(View.VISIBLE);
                        leftBtn_t.setVisibility(View.VISIBLE);
                        rightBtn_t.setVisibility(View.VISIBLE);
                        jumpBtn_t.setVisibility(View.VISIBLE);
                        nomalAttackBtn_t.setVisibility(View.VISIBLE);
                        removeMainMenuBtn();
                        FrameLayout tutorialLayout = (FrameLayout) findViewById(R.id.tutorialLayout);
                        tutorialLayout.setVisibility(View.VISIBLE);
                        tutorialLayout.startAnimation(alpha_0_to_1);
                        nowStage = stage.Tutorial.getValue();
                        toHomeBtn_t.setVisibility(View.VISIBLE);
                        gamePlayer.resetPlayer(playerimg, nomalatkimg);
                        setGameTutorialThreadShow(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        /////////////////////////////////////////////
        // 메인 메뉴의 버튼들(선택지)이다.
    }

    // 배경음악 자동재생기
    /////////////////////////////////////////////

    // 출처
    // Music by <a href="https://pixabay.com/ko/users/geoffreyburch-5739114/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=22045">Geoffrey Burch</a> from <a href="https://pixabay.com/music//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=22045">Pixabay</a>
    // Music by <a href="https://pixabay.com/ko/users/spencer_yk-36670691/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=151967">Spencer Y.K.</a> from <a href="https://pixabay.com/music//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=151967">Pixabay</a>
    // Music by <a href="https://pixabay.com/ko/users/harumachimusic-13470593/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=182043">Noru</a> from <a href="https://pixabay.com/music//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=182043">Pixabay</a>
    // Music by <a href="https://pixabay.com/ko/users/dream-protocol-9556087/?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=116846">Dream-Protocol</a> from <a href="https://pixabay.com//?utm_source=link-attribution&utm_medium=referral&utm_campaign=music&utm_content=116846">Pixabay</a>

    Thread backMusic = new Thread(new Runnable() {
        @Override
        public void run() {
            int playingNumber = 1;
            MediaPlayer mediaPlayer = null;
            while (true){
                try {
                    if(mediaPlayer != null && mediaPlayer.isPlaying()){
                        if(playingNumber != nowStage){
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            playingNumber = nowStage;
                            mediaPlayer = null;
                            Thread.sleep(1000);
                        }
                    }else{
                        // switch case를 쓰면 더 빠르겠지만, 상수-변수 혼용문제로 쓸수가 없었다.

                        if(playingNumber == stage.MainMenu.getValue()){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.manishemegaglbml);
                            mediaPlayer.start();
                        }
                        if(playingNumber == stage.Story.getValue()){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.fairytaledream);
                            mediaPlayer.start();
                        }
                        if(playingNumber == stage.Tutorial.getValue()){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.herscissorsaregood);
                            mediaPlayer.start();
                        }
                        if(playingNumber == stage.game_1.getValue()){
                            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.feedthemachineclassicarcadegame);
                            mediaPlayer.start();
                        }
                    }
                }catch (Exception e){
                    System.out.print(e.toString());
                }
            }
        }
    });


    /////////////////////////////////////////////
    // 배경음악 자동재생기

    @Override
    protected void onResume() {
        // ... 기억하자.. 미디어 플래이어는 resume쪽에 써야 작동된다. (이거땜에 거의 일주일을 날림)
        super.onResume();

        if(!backMusic.isAlive()){
            backMusic.start();
        }
    }
}

// 객체 충돌을 감지할 히트박스(콜라이션)이다.
/////////////////////////////////////////////

class HitBox{
    float pxToDp_w, pxToDp_h;
    int x, y, width, height;
    Point point1, point2, point3, point4;

    HitBox(int x, int y, int width, int height, float pxToDp_w, float pxToDp_h){
        this.pxToDp_w = pxToDp_w;
        this.pxToDp_h = pxToDp_h;
        this.x = (int)(x*pxToDp_w);
        this.y = (int)(y*pxToDp_h);
        this.width = width;
        this.height = height;
        point1 = new Point(x,y);
        point2 = new Point(x + width, y);
        point3 = new Point(x + width, y + height);
        point4 = new Point(x, y + height);
    }
//    HitBox(Point p1, Point p2, Point p3, Point p4, int pxToDp_w, int pxToDp_h){
//        point1 = p1;
//        point2 = p2;
//        point3 = p3;
//        point4 = p4;
//        this.x = p1.x;
//        this.y = p1.y;
//        this.width = p2.x - p1.x;
//        this.height = p4.y - p1.y;
//    }


    boolean IsXIn(HitBox box){
        if(this.point1.x <= box.point2.x && this.point1.x >= box.point1.x){
            return true;
        } else if (this.point2.x <= box.point2.x && this.point2.x >= box.point1.x) {
            return true;
        } else{
            return false;
        }
    }

    boolean IsYIn(HitBox box){
        if(this.point1.y <= box.point4.y && this.point1.y >= box.point1.y){
            return true;
        } else if (this.point4.y <= box.point4.y && this.point4.y >= box.point1.y){
            return true;
        } else{
            return false;
        }
    }

    boolean IsHit(HitBox box){
        if(this.IsXIn(box)){
            if(this.IsYIn(box)){
                return true;
            } else if (box.IsYIn(this)) {
                return true;
            }

        }
        else if(box.IsXIn(this)){
            if(this.IsYIn(box)){
                return true;
            } else if (box.IsYIn(this)) {
                return true;
            }
        }
        return false;
    }

    void reLocate(int x, int y){
        this.x = (int)(x*pxToDp_w);
        this.y = (int)(y*pxToDp_h);
        point1 = new Point(x,y);
        point2 = new Point(x + width, y);
        point3 = new Point(x + width, y + height);
        point4 = new Point(x, y + height);
    }
//    void reLocate(Point XY){
//        this.x = XY.x;
//        this.y = XY.y;
//        point1 = new Point(x,y);
//        point2 = new Point(x + width, y);
//        point3 = new Point(x + width, y + height);
//        point4 = new Point(x, y + height);
//    }
}

/////////////////////////////////////////////
// 객체 충돌을 감지할 히트박스(콜라이션)이다.

// 플래이어 이미지 컨트롤러이다.
/////////////////////////////////////////////

class Player {
    int HP = 200;
    ImageView PlayerImg;
    ImageView NomalAtkImg;
    Context context;
    boolean filp = false;
    boolean back = false;
    boolean changed = false;
    boolean onSky = false;
    int nowMotionNum = 0;
    int nowMotion = motion.idle.getValue();
    int X = 0;
    int Y = 0;
    int jumpPower = 40;
    int speed = 30;
    int nowJumpIndex = 0;
    float pxToDp_w, pxToDp_h;
    HitBox playerHitbox;
    Handler gameHandler;

    int[] idle = new int[3];
    int[] run0 = new int[4];
    int[] run1 = new int[2];
    int[] run2 = new int[2];
    int[] jump = new int[5];

    int[] nomalAttackIMG = new int[5];
    int nowAttackIndex = 0;
    int nomalAttackDamage = 2;
    boolean activeNomalAttack = false;
    HitBox nomalAttackHitbox = new HitBox(0, 0, 0, 0, 0, 0);


    enum motion {
        run(1),
        idle(2),
        jump(3);

        private final int value;
        motion(int a){
            this.value = a;
        }
        int getValue(){
            return value;
        }
    }

    Player(ImageView imageView, Context con, Handler handler, float pxToDp_w, float pxToDp_h, ImageView nomalAtkView){
        context = con;
        PlayerImg = imageView;
        NomalAtkImg = nomalAtkView;
        gameHandler = handler;
        this.pxToDp_w = pxToDp_w;
        this.pxToDp_h = pxToDp_h;

        playerHitbox = new HitBox(250,  100, 90, 120, pxToDp_w, pxToDp_h);

        for (int a = 0 ; a<idle.length ; a++){
            String name = "@drawable/android_idle"+(a+1);
            idle[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
        for (int a = 0 ; a<run0.length ; a++){
            String name = "@drawable/android_run"+(a+1);
            run0[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
        for (int a = 0 ; a<run1.length ; a++){
            String name = "@drawable/android_run4_1_"+(a+1);
            run1[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
        for (int a = 0 ; a<run2.length ; a++){
            String name = "@drawable/android_run4_2_"+(a+1);
            run2[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
        for (int a = 0 ; a<jump.length ; a++){
            String name = "@drawable/android_jump"+(a+1);
            jump[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
        for (int a = 0 ; a<nomalAttackIMG.length ; a++){
            String name = "@drawable/nomal_attack"+(a+1);
            nomalAttackIMG[a] = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        }
    }

    void resetPlayer(ImageView imageView, ImageView nomalAtkView){
        this.nowAttackIndex = 0;
        NomalAtkImg.setVisibility(View.GONE);
        this.PlayerImg = imageView;
        this.NomalAtkImg = nomalAtkView;
        this.X = 0;
        this.Y = 0;
        this.filp = false;
        this.back = false;
        this.changed = false;
        this.onSky = false;
        this.speed = 30;
        this.jumpPower = 40;
        this.nowJumpIndex = 0;
        this.nowMotion = motion.idle.getValue();
        this.nowMotionNum = 0;
    }

    void settingMotion(int changeMotion){
        if(changeMotion != nowMotion){
            nowMotion = changeMotion;
            nowMotionNum = 0;
            changed = true;
            back = false;
        }
    }

    void settingImg(){
        gameHandler.post(new Runnable() {
            @Override
            public void run() {
                if(changed){
                    nowMotionNum = 1;
                    changed = false;
                }

                if(filp){
                    PlayerImg.setScaleX(-1.0f);
                }else{
                    PlayerImg.setScaleX(1.0f);
                }

                if(activeNomalAttack){
                    NomalAtkImg.setVisibility(View.VISIBLE);
                    if(nowAttackIndex < nomalAttackIMG.length){
                        FrameLayout.LayoutParams nomalAtk = new FrameLayout.LayoutParams((int)(60*pxToDp_w),(int)(120*pxToDp_h));
                        NomalAtkImg.setImageResource(nomalAttackIMG[nowAttackIndex]);
                        if (filp) {
                            NomalAtkImg.setScaleX(-1.0f);
                            nomalAttackHitbox = new HitBox(170, 100, 60, 120, pxToDp_w, pxToDp_h);
                            nomalAtk.setMargins((int)(170*pxToDp_w), (int)(100*pxToDp_h), 0, 0);
                            NomalAtkImg.setLayoutParams(nomalAtk);
                        }else {
                            NomalAtkImg.setScaleX(1.0f);
                            nomalAttackHitbox = new HitBox(350, 100, 60, 120, pxToDp_w, pxToDp_h);
                            nomalAtk.setMargins((int)(350*pxToDp_w), (int)(100*pxToDp_h), 0, 0);
                            NomalAtkImg.setLayoutParams(nomalAtk);
                        }
                        nowAttackIndex += 1;
                    }else{
                        NomalAtkImg.setVisibility(View.GONE);
                        nomalAttackHitbox = new HitBox(0,0,0,0,pxToDp_w,pxToDp_h);
                        nowAttackIndex = 0;
                        activeNomalAttack = false;
                    }
                }

                if(nowMotion == motion.idle.getValue()){
                    PlayerImg.setImageResource(idle[nowMotionNum]);
                    if(nowMotionNum+1 == idle.length){
                        back = true;
                    }else if(nowMotionNum == 0){
                        back = false;
                    }

                    if(back){
                        nowMotionNum--;
                    }else{
                        nowMotionNum++;
                    }
                }

                if(nowMotion == motion.jump.getValue()){
                    PlayerImg.setImageResource(jump[nowMotionNum]);
                    if(nowMotionNum+1 < jump.length){
                        nowMotionNum++;
                    }
                }

                if(nowMotion == motion.run.getValue()){
                    if(nowMotionNum<4){
                        PlayerImg.setImageResource(run0[nowMotionNum]);
                        nowMotionNum++;
                    } else if (nowMotionNum<7) {
                        PlayerImg.setImageResource(run1[(nowMotionNum-4)%2]);
                        nowMotionNum++;
                    } else if (nowMotionNum==7) {
                        PlayerImg.setImageResource(run0[3]);
                        nowMotionNum++;
                    } else if (nowMotionNum<11) {
                        PlayerImg.setImageResource(run2[(nowMotionNum-8)%2]);
                        nowMotionNum++;
                    } else {
                        nowMotionNum = 5;
                        PlayerImg.setImageResource(run0[3]);
                    }
                }
            }
        });
    }
}

/////////////////////////////////////////////
// 플래이어 이미지 컨트롤러이다.