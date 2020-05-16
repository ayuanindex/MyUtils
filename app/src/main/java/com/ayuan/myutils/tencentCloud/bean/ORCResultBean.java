package com.ayuan.myutils.tencentCloud.bean;

import com.ayuan.myutils.okhttputil.HttpUtil;

/**
 * @author ayuan
 */
@HttpUtil.POST("https://ocr.tencentcloudapi.com/")
public class ORCResultBean {

    /**
     * Response : {"Number":"京A88888","Confidence":99,"RequestId":"d0f3cfb2-4be0-4804-8698-501a023dfe41","Error":{"Code":"AuthFailure.SignatureFailure","Message":"The provided credentials could not be validated. Please check your signature is correct."}}
     */

    private ResponseBean Response;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class ResponseBean {
        /**
         * Number : 京A88888
         * Confidence : 99
         * RequestId : d0f3cfb2-4be0-4804-8698-501a023dfe41
         * Error : {"Code":"AuthFailure.SignatureFailure","Message":"The provided credentials could not be validated. Please check your signature is correct."}
         */

        private String Number;
        private int Confidence;
        private String RequestId;
        private ErrorBean Error;

        public String getNumber() {
            return Number;
        }

        public void setNumber(String Number) {
            this.Number = Number;
        }

        public int getConfidence() {
            return Confidence;
        }

        public void setConfidence(int Confidence) {
            this.Confidence = Confidence;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public ErrorBean getError() {
            return Error;
        }

        public void setError(ErrorBean Error) {
            this.Error = Error;
        }

        public static class ErrorBean {
            /**
             * Code : AuthFailure.SignatureFailure
             * Message : The provided credentials could not be validated. Please check your signature is correct.
             */

            private String Code;
            private String Message;

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            @Override
            public String toString() {
                return "ErrorBean{" +
                        "Code='" + Code + '\'' +
                        ", Message='" + Message + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "Number='" + Number + '\'' +
                    ", Confidence=" + Confidence +
                    ", RequestId='" + RequestId + '\'' +
                    ", Error=" + Error +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "Response=" + Response +
                '}';
    }
}
