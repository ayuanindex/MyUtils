package com.ayuan.myutils.tencentCloud.speechSynthesis;

import android.util.Log;

import com.tencent.qcloudtts.LongTextTTS.LongTextTtsController;
import com.tencent.qcloudtts.callback.QCloudPlayerCallback;
import com.tencent.qcloudtts.callback.TtsExceptionHandler;
import com.tencent.qcloudtts.exception.TtsException;
import com.tencent.qcloudtts.exception.TtsNotInitializedException;

/**
 * @author ayuan
 */
public class Text2Voice {
    private static final String TAG = "Text2Voice";

    /**
     * 语速
     */
    private int voiceSpeed = 0;

    /**
     * 语音
     */
    private int voiceType = 5;

    /**
     * 语言
     */
    private int language = 1;
    private LongTextTtsController longTextTtsController;

    public Text2Voice() {
    }

    public Text2Voice(int voiceSpeed, int voiceType, int language) {
        this.voiceSpeed = voiceSpeed;
        this.voiceType = voiceType;
        this.language = language;
    }

    /**
     * 在使用云API之前，请前往 腾讯云API密钥页面 申请安全凭证。 安全凭证包括 SecretId 和 SecretKey
     *
     * @param appId     appId
     * @param secretId  用于标识 API 调用者身份
     * @param secretKey 用于加密签名字符串和服务器端验证签名字符串的密钥
     * @return 返回已经初始化的LongTextTtsController
     */
    public LongTextTtsController initLongTextTtsController(long appId, String secretId, String secretKey) {
        if (longTextTtsController == null) {
            longTextTtsController = new LongTextTtsController();
        }
        longTextTtsController.init(appId, secretId, secretKey);
        // 设置语速
        longTextTtsController.setVoiceSpeed(voiceSpeed);

        // 设置音色
        longTextTtsController.setVoiceType(voiceType);

        // 设置语言
        longTextTtsController.setVoiceLanguage(language);

        // 设置ProjectId
        longTextTtsController.setProjectId(0);
        return longTextTtsController;
    }

    public void start(String str, ResultData resultData) {
        try {
            if (longTextTtsController != null) {
                longTextTtsController.startTts(str, new TtsExceptionHandler() {
                    @Override
                    public void onRequestException(TtsException e) {

                    }
                }, new QCloudPlayerCallback() {
                    @Override
                    public void onTTSPlayStart() {
                        Log.e(TAG, "在TTS Play开始");
                    }

                    @Override
                    public void onTTSPlayWait() {
                        Log.e(TAG, "在TTS播放等待中");
                    }

                    @Override
                    public void onTTSPlayResume() {
                        Log.e(TAG, "在TTS播放履历上");
                    }

                    @Override
                    public void onTTSPlayNext() {
                        Log.e(TAG, "在TTS播放下一步");
                    }

                    @Override
                    public void onTTSPlayStop() {
                        Log.e(TAG, "在TTS播放停止");
                    }

                    @Override
                    public void onTTSPlayEnd() {
                        Log.e(TAG, "在TTS播放结束");
                    }

                    @Override
                    public void onTTSPlayProgress(String s, int i) {
                        resultData.progress(i);
                        Log.e(TAG, "在TTS播放进度-----------" + s + ":" + i);
                    }
                });
            }
        } catch (TtsNotInitializedException e) {
            e.printStackTrace();
        }
    }

    public interface ResultData {
        void progress(int i);
    }
}
