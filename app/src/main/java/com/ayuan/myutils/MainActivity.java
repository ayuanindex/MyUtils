package com.ayuan.myutils;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ayuan.myutils.baiduIot.CustomerMQTTCallback;
import com.ayuan.myutils.baiduIot.CustomerMQTTClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomerMQTTClient customerMQTTClient = new CustomerMQTTClient("ahh", "tcl://klajksdlfjkl.com", "ayuan/text", "alksdj", new CustomerMQTTCallback() {
            @Override
            public void setOptions(MqttConnectOptions mqttConnectOptions) {

            }

            @Override
            public void connectedCallback(boolean isConnected) {

            }

            @Override
            public void disconnectedCallback(boolean isDisConnecetd) {

            }

            @Override
            public void subscribe(boolean isSuccess, String topic) {

            }

            @Override
            public void tips(String msg) {

            }
        });

        customerMQTTClient.initMQTT(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        customerMQTTClient.connect();
    }
}
