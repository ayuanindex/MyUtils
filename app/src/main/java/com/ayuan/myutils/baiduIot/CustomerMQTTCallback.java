package com.ayuan.myutils.baiduIot;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public interface CustomerMQTTCallback {
    /**
     * 默认已配置，修改配置时使用
     *
     * @param mqttConnectOptions
     */
    void setOptions(MqttConnectOptions mqttConnectOptions);

    /**
     * 建立连接成功或者失败的回调
     *
     * @param isConnected true表示成功，false表示失败
     */
    void connectedCallback(boolean isConnected);

    /**
     * 手动断开连接时的成功或失败的回调
     *
     * @param isDisConnecetd true表示成功，false表示失败
     */
    void disconnectedCallback(boolean isDisConnecetd);

    /**
     * 主题订阅成功时的回调
     *
     * @param isSuccess
     */
    void subscribe(boolean isSuccess, String topic);

    /**
     * 提示消息
     *
     * @param msg 提示消息
     */
    void tips(String msg);
}
