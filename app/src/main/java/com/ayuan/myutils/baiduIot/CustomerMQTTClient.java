package com.ayuan.myutils.baiduIot;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.UUID;

public class CustomerMQTTClient {
    /**
     * 可订阅主题
     */
    private final ArrayList<String> TOPICSELECTS = new ArrayList<String>();

    /**
     * $baidu/iot/shadow/lightShadow/update
     */
    private final String UPDATE = "$baidu/iot/shadow/lightShadow/update";

    /**
     * $baidu/iot/shadow/lightShadow/get
     */
    private final String GET = "$baidu/iot/shadow/lightShadow/get";

    /**
     * $baidu/iot/shadow/lightShadow/delete
     */
    private final String DELETE = "$baidu/iot/shadow/lightShadow/delete";

    /**
     * 已经订阅的主题
     */
    private ArrayList<String> TOPICSUBSCRIBED = new ArrayList<String>();

    /**
     * 回调
     */
    private CustomerMQTTCallback customerMQTTCallback;

    private MqttClient mqttClient;

    /**
     * 设备名称
     */
    private String DEVICENAME;

    /**
     * 连接地址
     */
    private String PATH;

    private String NAME;

    private char[] KEY;

    private MqttConnectOptions mqttConnectOptions;

    /**
     * @param DEVICENAME           设备名称呢
     * @param PATH                 连接地址
     * @param NAME                 用户名
     * @param KEY                  密码
     * @param customerMQTTCallback 回调
     */
    public CustomerMQTTClient(String DEVICENAME, String PATH, String NAME, String KEY, CustomerMQTTCallback customerMQTTCallback) {
        this.DEVICENAME = DEVICENAME + UUID.randomUUID().toString();
        this.PATH = PATH;
        this.NAME = NAME;
        this.KEY = KEY.toCharArray();
        this.customerMQTTCallback = customerMQTTCallback;
        addSubscribe();
    }

    private void addSubscribe() {
        // 设备向该主题发布消息，可更新物影子。
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/update/accepted");
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/update/rejected");
        // 向该主题发布消息，可获取该设备的物影子。
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/get/accepted");
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/get/rejected");
        // 当物影子中的期望值字段有更新时，该主题获得消息
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/delta");
        // 向该主题发布任意JSON格式消息，可清空该设备的物影子。
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/delete/accepted");
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/delete/rejected");
        // 订阅该主题，会收到物影子reported的变化，变化条件包括增加属性、减少属性、属性值变化。
        // 物接入会将reported字段中发生变化的当前值和更新值发送到该主题。
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/update/documents");
        // 订阅该主题，会收到当物影子的reported字段发生变化时的物影子全部信息。
        // snapshot主题和documents主题都是在物影子的reported字段发生变化时触发，snapshot主题会收到物影子的全部信息，documents主题只会收到reported中发生变化的值。
        TOPICSELECTS.add("$baidu/iot/shadow/lightShadow/update/snapshot");
        // 物接入会为该主题绑定订阅和发布权限，设备可以订阅或发布符合 $baidu/iot/general/# 的主题。
        // 例如，设备A发布消息到主题$baidu/iot/general/a，设备B订阅主题$baidu/iot/general/a，则设备A就能与设备B进行通信。
        TOPICSELECTS.add("$baidu/iot/general/");
    }

    /**
     * 初始化MQTT
     *
     * @param mqttCallback mqtt连接的回调
     */
    public void initMQTT(MqttCallback mqttCallback) {
        try {
            // 参数一：主机地址；
            // 参数二：客户端ID，一般以客户端唯一标识符，不能够和其他客户端重名；
            // 参数三：数据保存在内存
            mqttClient = new MqttClient(PATH, DEVICENAME, new MemoryPersistence());
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            mqttConnectOptions.setUserName(NAME);// 设置连接的用户名(自己的服务器没有设置用户名)
            mqttConnectOptions.setPassword(KEY);// 设置连接的密码(自己的服务器没有设置密码)
            mqttConnectOptions.setConnectionTimeout(10);// 设置连接超时时间 单位为秒
            mqttConnectOptions.setKeepAliveInterval(20);// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            mqttClient.setCallback(mqttCallback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可以后期修改option的配置
     */
    public void setOptions() {
        customerMQTTCallback.setOptions(mqttConnectOptions);
    }

    public void connect() {
        if (mqttClient.isConnected()) {
            customerMQTTCallback.tips("连接已经打开，请勿重新开启");
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        mqttClient.connect(CustomerMQTTClient.this.mqttConnectOptions);
                        //连接建立成功
                        customerMQTTCallback.connectedCallback(true);
                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                        //安全问题连接失败
                        Log.e("安全问题连接失败", e.getMessage() + "");
                        customerMQTTCallback.connectedCallback(false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                        //连接失败原因
                        Log.e("连接失败原因", "" + e.getMessage());
                        customerMQTTCallback.connectedCallback(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        customerMQTTCallback.connectedCallback(false);
                    }
                }
            }.start();
        }
    }

    /**
     * 关闭连接
     */
    public void disConnect() {
        if (!mqttClient.isConnected()) {
            return;
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        mqttClient.disconnect();
                        customerMQTTCallback.disconnectedCallback(true);
                    } catch (MqttException e) {
                        e.printStackTrace();
                        customerMQTTCallback.disconnectedCallback(false);
                    }
                }
            }.start();
        }
    }

    /**
     * 订阅主题
     *
     * @param topic 需要订阅的主题
     */
    public void subscribe(final String topic) {
        if (!mqttClient.isConnected()) {
            customerMQTTCallback.tips("请建立连接后再试！！！");
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    boolean flag;
                    String msg;
                    try {
                        if (!TOPICSUBSCRIBED.contains(topic)) {
                            TOPICSUBSCRIBED.add(topic);
                            mqttClient.subscribe(topic, 0);
                            flag = true;
                            msg = "订阅成功";
                        } else {
                            flag = false;
                            msg = "订阅失败";
                        }
                        customerMQTTCallback.subscribe(flag, topic + msg);
                    } catch (MqttException e) {
                        e.printStackTrace();
                        customerMQTTCallback.subscribe(false, topic + "不存在！");
                    }
                }
            }.start();
        }
    }

    /**
     * 向指定主题发送消息
     *
     * @param topic   目标主题
     * @param message 需要发送的消息
     */
    public void sendMessage(final String topic, final String message) {
        if (!mqttClient.isConnected()) {
            customerMQTTCallback.tips("连接为建立");
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (UPDATE.equals(topic) || GET.equals(topic) || DELETE.equals(topic)) {
                        try {
                            mqttClient.publish(topic, new MqttMessage(message.getBytes()));
                        } catch (MqttException e) {
                            e.printStackTrace();
                            customerMQTTCallback.tips("消息发送失败");
                        }
                    } else {
                        customerMQTTCallback.tips("错误的主题");
                    }
                }
            }.start();
        }
    }

    public ArrayList<String> getTOPICSELECTS() {
        return TOPICSELECTS;
    }

    public ArrayList<String> getTOPICSUBSCRIBED() {
        return TOPICSUBSCRIBED;
    }

    public String getUPDATE() {
        return UPDATE;
    }

    public String getGET() {
        return GET;
    }

    public String getDELETE() {
        return DELETE;
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public String getDEVICENAME() {
        return DEVICENAME;
    }

    public void setDEVICENAME(String DEVICENAME) {
        this.DEVICENAME = DEVICENAME;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String PATH) {
        this.PATH = PATH;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public char[] getKEY() {
        return KEY;
    }

    public void setKEY(char[] KEY) {
        this.KEY = KEY;
    }

    public MqttConnectOptions getMqttConnectOptions() {
        return mqttConnectOptions;
    }
}
