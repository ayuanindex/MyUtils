package com.ayuan.myutils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.ayuan.myutils.tencentCloud.speechSynthesis.Text2Voice;
import com.tencent.qcloudtts.LongTextTTS.LongTextTtsController;

/**
 * @author ayuan
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ProgressBar progressBar;
    private Button btnPause;
    private Button btnContinue;
    private LongTextTtsController longTextTtsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        String str = "2013年11月，习近平在山东农科院召开座谈会时表示，手中有粮，心中不慌。保障粮食安全对中国来说是永恒的课题，任何时候都不能放松。2013年7月，在湖北鄂州东港村育种基地，习近平总书记拔起一棵稻苗察看分蘖情况，夸奖“很壮实”，强调粮食安全要靠自己。高屋建瓴，他的论述为保障粮食安全指明方向";

        progressBar.setMax(str.length());

        Text2Voice text2Voice = new Text2Voice();
        longTextTtsController = text2Voice.initLongTextTtsController(1301676932L
                , "AKIDYqrzrcNJHyjEagH3M4WbRWLsCJNBB3D8"
                , "mIXEfKjz0sVstdQ2VjhPqAMSIwgCTSAc");
        text2Voice.start(
                str
                , new Text2Voice.ResultData() {
                    @Override
                    public void progress(int i) {
                        progressBar.setProgress((int) (i * Math.random()), true);
                    }
                });
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnContinue = (Button) findViewById(R.id.btn_continue);

        btnPause.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pause:
                if (longTextTtsController != null) {
                    longTextTtsController.pause();
                }
                break;
            case R.id.btn_continue:
                if (longTextTtsController != null) {
                    longTextTtsController.resume();
                }
                break;
        }
    }
}
