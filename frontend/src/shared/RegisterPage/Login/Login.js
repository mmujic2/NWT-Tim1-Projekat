import React, { useEffect } from "react";
import Form from "react-bootstrap/Form";
import Container from "react-bootstrap/Container";
import styles from "./login.css";
import { useState } from "react";
import Button from "react-bootstrap/Button";
import { Link, useNavigate } from "react-router-dom";
import auth from "../../../service/auth.service";
import CustomAlert from "../../util/Alert";
import { Col, Row } from "react-bootstrap";
import SockJsClient from "react-stomp";
import restaurantService from "../../../service/restaurant.service";

function Login({ setPage }) {
  const [username, setUsername] = useState();
  const [password, setPassword] = useState();
  const [showError, setShowError] = useState(false);
  const navigate = useNavigate();

  const submit = (e) => {
    e.preventDefault();
    document.body.style.cursor = "wait"
    auth.login(username, password).then((res) => {
      if (res.status == 200) {
          if(res.data.user.role == "RESTAURANT_MANAGER") {
            restaurantService.getManagersRestaurantUUID().then(res => {
              document.body.style.cursor = "default"
                if(res.status==200) {
                  localStorage.setItem("restaurantUUID", res.data)
                  navigate("/");
                } else {
                  console.log(res)
                }
            })

          } else {
            document.body.style.cursor = "default"
            navigate("/");
          } 
      }
      else if (res.status == 403) {
        document.body.style.cursor = "default"
        setShowError(true);}
    });
  };
  return (
    <>
      <Container className={styles.container}>
        <Form>
          <h1>Login</h1>

          <Form.Group className="mb-3" controlId="formGroupUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              required
              type="text"
              name="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formGroupPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              required
              type="password"
              name="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <span
              style={{
                display: showError ? "block" : "none",
                color: "#d32f2f",
                fontFamily: "Yantramanav",
                marginTop: 10,
                fontSize: "16px",
              }}
            >
              Invalid access data.
            </span>
          </Form.Group>

          <Button className={styles.btn} type="submit" onClick={submit}>
            Login
          </Button>
          <hr></hr>
          <div style={{ textAlign: "center" }}>
            Don't have an account yet?
            <Link
              className="px-3 lnk"
              onClick={() => {
                setPage("signup");
              }}
            >
              Sign up.
            </Link>
          </div>
        </Form>
      </Container>
      Test
    </>
  );
}

export default Login;
