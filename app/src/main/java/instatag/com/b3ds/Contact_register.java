package instatag.com.b3ds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by This Pc on 2/25/2018.
 */

    public class Contact_register {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("data")
        @Expose
        private Register_data data;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Register_data getData() {
            return data;
        }

        public void setData(Register_data data) {
            this.data = data;
        }

    }

