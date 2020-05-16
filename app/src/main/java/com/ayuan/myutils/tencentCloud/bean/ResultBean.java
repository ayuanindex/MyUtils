package com.ayuan.myutils.tencentCloud.bean;

public class ResultBean {

    /**
     * control : {"number":2,"power_switch":0,"Temperature":22,"Mode":1,"Wind":2}
     */

    private ControlBean control;

    public ControlBean getControl() {
        return control;
    }

    public void setControl(ControlBean control) {
        this.control = control;
    }

    public static class ControlBean {
        /**
         * number : 2
         * power_switch : 0
         * Temperature : 22
         * Mode : 1
         * Wind : 2
         */

        private int number;
        private int power_switch;
        private int Temperature;
        private int Mode;
        private int Wind;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getPower_switch() {
            return power_switch;
        }

        public void setPower_switch(int power_switch) {
            this.power_switch = power_switch;
        }

        public int getTemperature() {
            return Temperature;
        }

        public void setTemperature(int Temperature) {
            this.Temperature = Temperature;
        }

        public int getMode() {
            return Mode;
        }

        public void setMode(int Mode) {
            this.Mode = Mode;
        }

        public int getWind() {
            return Wind;
        }

        public void setWind(int Wind) {
            this.Wind = Wind;
        }

        @Override
        public String toString() {
            return "ControlBean{" +
                    "number=" + number +
                    ", power_switch=" + power_switch +
                    ", Temperature=" + Temperature +
                    ", Mode=" + Mode +
                    ", Wind=" + Wind +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "control=" + control +
                '}';
    }
}
