package com.ayuan.myutils.tencentCloud.mqtt;

import android.content.Context;
import android.util.Log;

import com.qcloud.iot_explorer.common.Status;
import com.qcloud.iot_explorer.data_template.TXDataTemplateClient;
import com.qcloud.iot_explorer.data_template.TXDataTemplateDownStreamCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttActionCallBack;
import com.qcloud.iot_explorer.mqtt.TXMqttConnection;
import com.qcloud.iot_explorer.mqtt.TXMqttRequest;
import com.qcloud.iot_explorer.utils.AsymcSslUtils;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ayuan
 */
public class CustomerMqttConnect {
    private static final String TAG = "CustomerMqttConnect";

    /**
     * 默认测试参数
     */
    private String mBrokerUrl = "ssl://iotcloud-mqtt.gz.tencentdevices.com:8883";

    /**
     * 设备ID
     */
    private String mProductId = "MM54FP07J0";

    /**
     * 设备密匙
     */
    private String mDevName = "qqqq";

    /**
     * 若使用证书验证，设为null
     */
    private String mDevPsk = "WiGlgNPXHaSDxVSArebORg==";

    private final static String BROKER_URL = "broker_url";
    private final static String PRODUCT_ID = "product_id";
    private final static String DEVICE_NAME = "dev_name";
    private final static String DEVICE_PSK = "dev_psk";
    private final static String DEVICE_CERT = "dev_cert";
    private final static String DEVICE_PRIV = "dev_priv";

    private final static String mJsonFileName = "tencent/mqtt/kongtiao.json";

    /**
     * 请求ID
     */
    private static AtomicInteger requestID = new AtomicInteger(0);
    private Context context;
    private TXMqttConnection txMqttConnection;
    private TXDataTemplateClient txDataTemplateClient;

    public CustomerMqttConnect(Context context, String mBrokerUrl, String mProductId, String mDevName, String mDevPsk) {
        this.context = context;
        this.mBrokerUrl = mBrokerUrl;
        this.mProductId = mProductId;
        this.mDevName = mDevName;
        this.mDevPsk = mDevPsk;
    }

    /**
     * 连接设备
     *
     * @param mJsonFileName      设备的Json数据模版
     * @param callBack           回调
     * @param downStreamCallBack 回调
     */
    public void connected(String mJsonFileName, TXMqttActionCallBack callBack, TXDataTemplateDownStreamCallBack downStreamCallBack) {
        txDataTemplateClient = new TXDataTemplateClient(context,
                mBrokerUrl,
                mProductId,
                mDevName,
                mDevPsk,
                null,
                null,
                callBack,
                mJsonFileName,
                downStreamCallBack);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(8);
        options.setKeepAliveInterval(240);
        options.setAutomaticReconnect(true);

        if (mDevPsk != null && mDevPsk.length() != 0) {
            Log.d(TAG, "connect: ----------使用密匙");
            options.setSocketFactory(AsymcSslUtils.getSocketFactory());
        }

        TXMqttRequest mqttRequest = new TXMqttRequest("connect", requestID.getAndIncrement());
        Log.d(TAG, "connect: -------开始连接");
        txDataTemplateClient.connect(options, mqttRequest);

        DisconnectedBufferOptions bufferOptions = new DisconnectedBufferOptions();
        bufferOptions.setBufferEnabled(true);
        bufferOptions.setBufferSize(1024);
        bufferOptions.setDeleteOldestMessages(true);
        txDataTemplateClient.setBufferOpts(bufferOptions);
    }

    /**
     * 订阅相关主题
     */
    public void subscribe() {
        if (txDataTemplateClient != null && txDataTemplateClient.isConnected()) {
            txDataTemplateClient.subscribe("$thing/up/property/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/down/property/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/up/event/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/down/event/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/up/action/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/down/action/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/up/property/" + mProductId + "/" + mDevName, 0, context);
            txDataTemplateClient.subscribe("$thing/up/property/" + mProductId + "/" + mDevName, 0, context);
        }
    }

    public void update() {
        if (txDataTemplateClient != null && txDataTemplateClient.isConnected()) {
            Status control = txDataTemplateClient.propertyGetStatus("control", true);
        }
    }

    public void send(int powerSwitch) {
        if (txDataTemplateClient != null && txDataTemplateClient.isConnected()) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("method", "control");
                jsonObject.put("clientToken", Math.random() * 1000);
                jsonObject.put("timestamp", System.currentTimeMillis() / 1000);

                JSONObject value = new JSONObject();
                value.put("power_switch", powerSwitch);
                txDataTemplateClient.propertyReport(value, null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void disConnected() {
        if (txDataTemplateClient != null && txDataTemplateClient.isConnected()) {
            txDataTemplateClient.disConnect(context);
        }
    }
}
