package edu.yuferov.site.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public abstract class ControllerBase {

    @NoArgsConstructor
    @AllArgsConstructor
    public class Response implements Serializable {
        public boolean success;
        public String error;
    }

    protected interface Command <Body> {
        void run(Body body);
    }

    protected <Body extends Response> ResponseEntity<Body> exec(Body body, Command<Body> cmd) {
        try {
            cmd.run(body);
            body.success = true;
            body.error = null;
            return ResponseEntity.ok(body);
        }
        catch (IllegalArgumentException e) {
            body.success = false;
            body.error = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }
        catch (RuntimeException e) {
            body.success = false;
            body.error = "Something went wrong. We can not serve your request. Try again later.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }

}
